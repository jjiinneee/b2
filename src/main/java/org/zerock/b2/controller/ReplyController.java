package org.zerock.b2.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b2.dto.PageRequestDTO;
import org.zerock.b2.dto.PageResponseDTO;
import org.zerock.b2.dto.ReplyDTO;
import org.zerock.b2.service.ReplyService;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {
  
  private final ReplyService replyService;
  
  @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Map<String,Long> register(
          @RequestBody ReplyDTO replyDTO,
          BindingResult bindingResult) throws Exception{
    
    log.info(replyDTO);
    
    if(bindingResult.hasErrors()){
      throw new BindException(bindingResult);
    }
    
    Long rno = replyService.register(replyDTO);
    
    
//    Map<String, Long> resultMap = Map.of("rno", rno);
    
    return Map.of("rno", rno);
  }
  
  
  @ApiOperation(value = "Replies of Board", notes = "GET 방식으로 특정 게시물의 댓글 목록")
  @GetMapping(value = "/list/{bno}")
  public PageResponseDTO<ReplyDTO> getList(
                                           PageRequestDTO pageRequestDTO){
    
    PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
    
    return responseDTO;
  }
  
  
  
  @ApiOperation(value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회")
  @GetMapping("/{rno}")
  public ReplyDTO getReplyDTO( @PathVariable("rno") Long rno ){
    
    ReplyDTO replyDTO = replyService.read(rno);
    
    return replyDTO;
  }
  
  
  @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")
  @DeleteMapping("/{rno}")
  public Map<String,Long> remove( @PathVariable("rno") Long rno ){
    
    replyService.remove(rno);
    
    return Map.of("rno", rno);
  }
  
  @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
  @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
  public Map<String,Long> remove( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){
    
    replyDTO.setRno(rno); //번호를 일치시킴
    
    replyService.modify(replyDTO);
    
    return Map.of("rno", rno);
  }
  
}
