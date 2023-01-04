package org.zerock.b2.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends BaseEntity{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rno;
  
  private String replyText;
  
  private String replier;
  
  @ManyToOne(fetch =  FetchType.LAZY)
  private Board board;
  
  public void changeText(String text){
    this.replyText = text;
  }
  
  
}
