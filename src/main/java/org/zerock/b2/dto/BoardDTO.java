package org.zerock.b2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
  private Integer bno;
  
  @NotEmpty
  @Size(min =3, max = 100)
  private String title;
  @NotEmpty
  private String content;
  @NotEmpty
  private String writer;
  private LocalDateTime regDate;
  private LocalDateTime modDate;
  
  private List<String> fileList; //첨부 파일의 경로를 보관하는 리스트
}
