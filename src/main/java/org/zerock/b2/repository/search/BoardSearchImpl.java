package org.zerock.b2.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b2.dto.BoardListReplyCountDTO;
import org.zerock.b2.dto.BoardListWithImageDTO;
import org.zerock.b2.entity.Board;
import org.zerock.b2.entity.QBoard;
import org.zerock.b2.entity.QBoardImage;
import org.zerock.b2.entity.QReply;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

  public BoardSearchImpl() {
    super(Board.class);
  }
  
  @Override
  public void search1(Pageable pageable) {
    log.info("========search1=======");
  
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    JPQLQuery<Board> query = from(board);
    query.leftJoin(reply).on(reply.board.eq(board));
    
    getQuerydsl().applyPagination(pageable, query);
    query.fetchCount();
    query.fetch();
  }
  
  @Override
  public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
    
    QBoard board = QBoard.board;
    JPQLQuery<Board> query = from(board);
    
    if(types != null){
      BooleanBuilder booleanBuilder = new BooleanBuilder();
      
      for (String type: types) {
        if(type.equals("t")){
          booleanBuilder.or(board.title.contains(keyword));
        }else if(type.equals("w")){
          booleanBuilder.or(board.content.contains(keyword));
        }else if(type.equals("c")){
          booleanBuilder.or(board.writer.contains(keyword));
        }
      }
      
      query.where(booleanBuilder);
    }
    query.where(board.bno.gt(0));
  
    //paging
    getQuerydsl().applyPagination(pageable, query);
    
    List<Board> list = query.fetch();
    long count = query.fetchCount();
   
    
    
    return new PageImpl<>(list, pageable, count);
  }
  
  
  @Override
  public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable){
    
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    
    JPQLQuery<Board> query = from(board);
    query.leftJoin(reply).on(reply.board.eq(board));
    query.groupBy(board);
    
    
    JPQLQuery<BoardListReplyCountDTO> selectQuery =
            query.select(Projections.bean(BoardListReplyCountDTO.class,
                            board.bno
                          , board.title
                          , board.writer
                          , board.regDate
                          , reply.count().as("replyCount")
            ));
    
    this.getQuerydsl().applyPagination(pageable, selectQuery);
    List<BoardListReplyCountDTO>  list  = selectQuery.fetch();
    long totalCount = selectQuery.fetchCount();
    
    
    
    return null;
  }
  
  
  @Override
  public Page<BoardListWithImageDTO> searchWithImage(String[] types, String keyword, Pageable pageable){
    log.info("------------");
    QBoard board = QBoard.board;
    QBoardImage boardImage = QBoardImage.boardImage;
    QReply reply = QReply.reply;
    
    JPQLQuery<Board> query = from(board);
    query.leftJoin(board.boardImages,boardImage);
    query.leftJoin(reply).on(reply.board.eq(board));
    
    if(types != null && keyword != null){
      
      BooleanBuilder booleanBuilder = new BooleanBuilder();
      Arrays.stream(types).forEach(t -> {
        if(t.equals("t")){
          booleanBuilder.or(board.title.contains(keyword));
        }else if(t.equals("w")){
          booleanBuilder.or(board.writer.contains(keyword));
        }else if(t.equals("c")){
          booleanBuilder.or(board.content.contains(keyword));
        }
      });
      query.where(booleanBuilder);
    }
    
    //1번 게시물에 속한 대표이미지
//    query.where(boardImage.ord.eq(0));
    query.groupBy(board);
    this.getQuerydsl().applyPagination(pageable,query);
  
    JPQLQuery<BoardListWithImageDTO> tupleJPQLQuery = query.select(
            Projections.bean(
              BoardListWithImageDTO.class
            , board.bno
            , board.title
            , board.writer
            , board.regDate
            , boardImage.fileLink.as("imgPath")
            , reply.countDistinct().as("replyCount")
            )
    );
    
    List<BoardListWithImageDTO> dtoList = tupleJPQLQuery.fetch();
    
    long totalCount  = tupleJPQLQuery.fetchCount();
    
    
    
//    tupleList.forEach(tuple -> {
//      Object[] arr = tuple.toArray();
//
//      log.info(arr[0]);
//      log.info(arr[1]);
//      log.info(arr[2]);
//      log.info(arr[3]);
//      log.info("---------");
//    });
    
//    log.info(tupleJPQLQuery.fetch());
    
    //2번 게시물에 속한 모든 이미지를 다 가져오는 경우
    //별로 권장하고 싶지 않음
//    query.groupBy(board);
//    this.getQuerydsl().applyPagination(pageable, query);
//
//    List<Board> list =   query.fetch();
//    list.forEach(board1 -> {
//      log.info(board1.getBno() + " :" + board1.getTitle());
//      log.info(board1.getBoardImages());
//    });
    return new PageImpl<>(dtoList,pageable,totalCount);
  }
}
