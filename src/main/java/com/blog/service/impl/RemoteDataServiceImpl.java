package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.RemoteDataRecord;
import com.blog.mapper.RemoteDataMapper;
import com.blog.service.RemoteDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RemoteDataServiceImpl extends ServiceImpl<RemoteDataMapper, RemoteDataRecord> implements RemoteDataService {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    @Transactional
    public RemoteDataRecord push(String source, String dataType, String dataContent) {
        RemoteDataRecord record = new RemoteDataRecord();
        record.setSource(source);
        record.setDataType(dataType);
        record.setDataContent(dataContent);
        record.setStatus("RECEIVED");
        save(record);

        // 通知所有 SSE 订阅者
        notifySubscribers(record);

        return record;
    }

    @Override
    public SseEmitter createSseEmitter() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("SSE connected"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }

        return emitter;
    }

    @Override
    public List<RemoteDataRecord> listLatest(int limit) {
        LambdaQueryWrapper<RemoteDataRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(RemoteDataRecord::getCreatedAt);
        wrapper.last("LIMIT " + limit);
        return list(wrapper);
    }

    private void notifySubscribers(RemoteDataRecord record) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("new-data")
                        .data(record));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
