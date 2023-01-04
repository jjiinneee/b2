package org.zerock.b2.repository;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.b2.dto.*;
import org.zerock.b2.entity.Board;
import org.zerock.b2.entity.BoardImage;
import org.zerock.b2.service.BoardService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
  
  @Autowired
  private BoardRepository boardRepository;
  
  @Autowired
  private BoardService boardService;
  
  
  @Transactional
  @Test
  public void testWithImage(){
    Pageable pageable =  PageRequest.of(0,10,Sort.by("bno").descending());
    Page<BoardListWithImageDTO> result =  boardRepository.searchWithImage(null,null,pageable);
    
    log.info("result", result);
    
    result.getContent().forEach(boardListWithImageDTO -> {
      log.info(boardListWithImageDTO);
    });
  }
  
  
  @Transactional
  @Commit
  @Test
  public void testUpdateImage(){
    Board board = boardRepository.getById(20);
    board.changeTitle("20번 제목 수정");
    board.clearImages();   //image 다 지워짐
    for (int i = 0; i < 3; i++) {
      BoardImage boardImage = BoardImage.builder()
              .fileLink("aaaa"+i+"jpg").build();
      
      board.addImage(boardImage);
    }
    
    boardRepository.save(board);
  }
  
  @Transactional
  @Test
  public void testWithQuery() {
    
    Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
    
    Page<Board> result = boardRepository.getList1(pageable);
    
    result.getContent().forEach(board -> {
      log.info(board);
      log.info(board.getBoardImages());
      log.info("=====================");
    });
    
  }
 
  @Test
  public void searchAll(){
    String[] types = new String[]{"t","c"};
    String keyword = "5";
    Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
    boardRepository.searchAll(types,keyword,pageable);
  }
 
  @Test
  public void search1(){
    Pageable pageable  = PageRequest.of(0,10,Sort.by("bno").descending());
    boardRepository.search1(pageable);
  }
  
  
  @Test
  public void testInsert(){
    log.info("-------------------------");
    log.info("-------------------------");
    log.info(boardRepository);
    log.info("-------------------------");
  
    IntStream.rangeClosed(1,100).forEach(i -> {
      Board board = Board.builder()
              .title("title" + i)
              .content("content" + i)
              .writer("writer :" + i)
              .build();
      
      boardRepository.save(board);
    });
  }
  
  @Test
  public void testInserWithImage(){
    for (int i = 0; i < 20; i++) {
      Board board = Board.builder()
              .title("title" + i)
              .content("content")
              .writer("writer" +(i %1))
              .build();
  
      for (int j = 0; j < 2; j++) {
        BoardImage boardImage = BoardImage.builder()
                .fileLink(i+"aaa.jpg")
                .build();
        board.addImage(boardImage);
      }
      
      boardRepository.save(board);
    }
  }
  
  
  @Test
  public void testRaed(){
    Integer bno = 31;
    
    Optional<Board> result = boardRepository.findById(bno);
    
    
    Board board = result.orElseThrow();
    
    log.info("board " + board);
  }
  
  @Test
  public void testUpdate(){
  
//    Board board = Board.builder()
//            .bno(31)
//            .title("31 title")
//            .content("31 title")
//            .writer("user 31").build();
//
//    boardRepository.save(board);
    Integer bno = 31;

    Optional<Board> result = boardRepository.findById(bno);


    Board board = result.get();
    board.changeTitle("title update");
    board.changeContent("content update..");
    
    boardRepository.save(board);
    log.info("board " + board);
  }
  
  @Test
  public void testDelete(){
    //없는 번호 삭제
    Integer bno  = 100;
    boardRepository.deleteById(bno);
  }
  
  @Test
  public void testPage1(){
    //page 번호 0부터 시작
    Pageable pg = PageRequest.of(1,10, Sort.by("bno").descending());
    Page<Board> result = boardRepository.findAll(pg);
    log.info("totalelements = " + result.getTotalElements());
    log.info("totalPages = " + result.getTotalPages());
    log.info("currentPage = " + result.getNumber());
    log.info("currentPage = " + result.getSize());
    
    result.getContent().forEach( i ->{
      log.info("list : " + i);
    });
    
  }
  
  
  
  @Test
  public void testQueryMethod1(){
    String keyword = "5";
  
    List<Board> list = boardRepository.findByTitleContaining(keyword);
    
    log.info(list);
  }
  
  @Test
  public void testQueryMethod2(){
    String keyword = "5";
    Pageable page = PageRequest.of(0,5, Sort.by("bno").descending());
    
    
    Page<Board> list = boardRepository.findByTitleContaining(keyword,page);
    
    log.info(list);
  }
  
  @Test
  public void testList() {
    
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .type("tcw")
            .keyword("1")
            .page(1)
            .size(10)
            .build();
    
    PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
    
    log.info(responseDTO);
    
  }
  
  @Test
  public void testWithReplyCount(){
    Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
    Page<BoardListReplyCountDTO> result =
            boardRepository.searchWithReplyCount(null,null,pageable);
    
  }
  
  //no session 나오면 transctional 추가하면 됨
  //limit 제대로 니오는지 check
  
  @Test
  public void fileWithImage(){
    Board board = Board.builder()
            .title("ttt")
            .content("contetne")
            .build();
  }
  
}
