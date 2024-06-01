package com.se14.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.se14.domain.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentDTO {
    @JsonProperty
    private int commentId;
    @JsonProperty
    private String content;
    @JsonProperty
    private String author;
    @JsonProperty
    private String timeStamp;

    public CommentDTO(Comment comment) {
        this.commentId = (int) comment.getID();
        this.content = comment.getText();
        this.author = comment.getAuthor() == null ? "Unknown" : comment.getAuthor().getUsername();
        this.timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTimestamp());
    }
}
