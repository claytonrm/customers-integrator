package com.compasso.customersintegrator.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceCreatedResponse {

    private Long id;
    private String uri;
    private LocalDateTime createdAt;

}
