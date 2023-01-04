package org.zerock.b2.service;

import org.springframework.data.domain.Page;
import org.zerock.b2.dto.BoardDTO;
import org.zerock.b2.dto.PageRequestDTO;
import org.zerock.b2.dto.PageResponseDTO;
import org.zerock.b2.entity.Board;

import java.util.stream.Collectors;


public interface BoardService {
  
  Integer register(BoardDTO boardDTO);
  
  BoardDTO readOne(Integer bno);
  
  void modify(BoardDTO boardDTO);
  
  void delete(Integer bno);
  
  PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
  
  default Board dtoToEntity(BoardDTO boardDTO){
    
    Board board = Board.builder()
            .bno(boardDTO.getBno())
            .title(boardDTO.getTitle())
            .content(boardDTO.getContent())
            .writer(boardDTO.getWriter())
            
            .build();
    
    if(boardDTO.getFileList() != null && boardDTO.getFileList().size() > 0) {
      boardDTO.getFileList().forEach(imgLink -> board.addImage(imgLink));
    }
    
    return board;
  }
  
  default BoardDTO entityToDto(Board board) {
    
    BoardDTO dto = BoardDTO.builder()
            .bno(board.getBno())
            .title((board.getTitle()))
            .content(board.getContent())
            .writer(board.getWriter())
            .regDate(board.getRegDate())
            .modDate(board.getModDate())
            .build();
    
    if(board.getBoardImages() != null && board.getBoardImages().size() > 0) {
      dto.setFileList(
              board.getBoardImages().stream().map(boardImage -> boardImage.getFileLink())
                      .collect(Collectors.toList()));
    }
    
    return dto;
  }
  
  
}
