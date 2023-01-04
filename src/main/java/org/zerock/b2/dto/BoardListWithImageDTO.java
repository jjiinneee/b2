package org.zerock.b2.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BoardListWithImageDTO {
  
  private Integer bno;
  private String title;
  private String writer;
  private LocalDateTime regDate;
  private Long replyCount;
  private String imgPath;
}
