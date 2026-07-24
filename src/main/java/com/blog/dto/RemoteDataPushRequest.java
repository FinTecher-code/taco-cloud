package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RemoteDataPushRequest {
    @NotBlank
    private String source;

    @NotBlank
    private String dataType;

    @NotBlank
    private String dataContent;
}
