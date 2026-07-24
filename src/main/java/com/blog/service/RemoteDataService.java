package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.RemoteDataRecord;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface RemoteDataService extends IService<RemoteDataRecord> {

    RemoteDataRecord push(String source, String dataType, String dataContent);

    SseEmitter createSseEmitter();

    List<RemoteDataRecord> listLatest(int limit);
}
