package org.zerock.b2.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b2.dto.BoardDTO;
import org.zerock.b2.dto.PageRequestDTO;
import org.zerock.b2.dto.PageResponseDTO;
import org.zerock.b2.entity.Board;
import org.zerock.b2.service.BoardService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
  
  private final BoardService boardService;
  
  @GetMapping("/list")
  public void list(PageRequestDTO pageRequestDTO, Model model){
    PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
    model.addAttribute("responseDTO", responseDTO);
  }
  
  @GetMapping("/register")
  public void registerGET(){
  
  }
  
  @PostMapping("/register")
  public String registerPOST(@Valid BoardDTO boardDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
      log.info("boardDTO", boardDTO);
    
      // ㅇㅓ떤 에러라도 발생하면 처리하겠다
      if(bindingResult.hasErrors()){
        log.info("has error....");
        redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        return "redirect:/board/register";
      }
      
      Integer bno = boardService.register(boardDTO);
      redirectAttributes.addFlashAttribute("result", bno);
    return "redirect:/board/list";
  }
  
  @GetMapping("/read")
  public void read(Integer bno, PageRequestDTO pageRequestDTO, Model model){
    log.info("bno", bno);
    log.info("pageRequestDTO", pageRequestDTO);

    
    BoardDTO boardDTO = boardService.readOne(bno);
    
    log.info("boardDTO", boardDTO);
    model.addAttribute("dto", boardDTO);
  }
}
