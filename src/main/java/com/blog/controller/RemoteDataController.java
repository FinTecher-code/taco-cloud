package com.blog.controller;

import com.blog.dto.ApiResult;
import com.blog.dto.RemoteDataPushRequest;
import com.blog.entity.RemoteDataRecord;
import com.blog.service.RemoteDataService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/remote-data")
public class RemoteDataController {

    private final RemoteDataService remoteDataService;

    public RemoteDataController(RemoteDataService remoteDataService) {
        this.remoteDataService = remoteDataService;
    }

    @PostMapping("/push")
    public ApiResult<RemoteDataRecord> push(@Valid @RequestBody RemoteDataPushRequest request) {
        RemoteDataRecord record = remoteDataService.push(
                request.getSource(),
                request.getDataType(),
                request.getDataContent()
        );
        return ApiResult.success(record);
    }

    @GetMapping
    public ApiResult<List<RemoteDataRecord>> list(
            @RequestParam(defaultValue = "100") int limit) {
        return ApiResult.success(remoteDataService.listLatest(limit));
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        return remoteDataService.createSseEmitter();
    }
}
