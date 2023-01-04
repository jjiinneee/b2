package org.zerock.b2.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.JoinColumnOrFormula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name ="tbl_board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "BoardImage")
@Getter
public class Board extends BaseEntity{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer bno;
  
  @Column(length = 200, nullable = false)
  private String title;
  
  private String content;
  
  private String writer;
  
//  @CreationTimestamp
//  private LocalDateTime regDate;
//
//  @CreationTimestamp
//  private LocalDateTime updateDate;
  
  public void changeTitle(String title){
    this.title = title;
  }
  
  public void changeContent(String content){
    this.content = content;
  }
  
//  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
//  @JoinColumn(name="board")
  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  @BatchSize(size = 100)
  private Set<BoardImage> boardImages = new HashSet<>();
  
//  public void addImage(BoardImage boardImage){
//    boardImage.fixOrd(boardImages.size());
//    boardImages.add(boardImage);
//  }
  
  public void addImage(String fileLink){
    
    BoardImage  image = BoardImage.builder().fileLink(fileLink)
            .ord(boardImages.size())
            .build();
    boardImages.add(image);
  }
  
  public void clearImages(){
    boardImages.clear();
  }
  
}
