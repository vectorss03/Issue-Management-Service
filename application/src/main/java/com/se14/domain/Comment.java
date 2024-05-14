package com.se14.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data // 빠른 개발을 위해 모든 Domain에 @Data 어노테이션을 추가함
@Builder
public class Comment {
    private long id;
    private String content;
    private LocalDateTime timeStamp;
    private Member author;
}
