package org.zerock.b2.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b2.entity.Board;
import org.zerock.b2.entity.Reply;

import java.util.Optional;

@Log4j2
@SpringBootTest
public class ReplyRepositoryTests {
  
  @Autowired
  ReplyRepository repository;
  
  
  @Test
  public void testInsert(){
    Board board = Board.builder().bno(100).build();
    for (int i = 0; i < 100; i++) {
      Reply reply = Reply.builder()
              .board(board)
              .replyText("11111"+i)
              .replier("ghdrlfehd"+i)
              .build();
  
      repository.save(reply);
    }
  }
  
  @Test
  public void testInsert2(){
  
    for (int i = 1; i <= 20; i++) {
      Board board = Board.builder().bno(i).build();
      
      int count = i % 5;
      for (int j = 0; j < count; j++) {
        Reply reply = Reply.builder()
                .board(board)
                .replyText(i+"--" + j + "댓글")
                .replier("user00")
                .build();
        
        repository.save(reply);
      }
    }
  }
  
  
  @Test
  public void testRead(){
    long rno = 99;
    Optional<Reply> result = repository.findById(rno);
  }
  
  @Test
  public void getList(){
    Integer bno = 100;
  
    Pageable pageable = PageRequest.of(0,10, Sort.by("bno").ascending());
    
    Page<Reply> result = repository.listOfBoard(bno,pageable);
  }
}
