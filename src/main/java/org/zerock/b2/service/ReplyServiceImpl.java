package org.zerock.b2.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.zerock.b2.dto.PageRequestDTO;
import org.zerock.b2.dto.PageResponseDTO;
import org.zerock.b2.dto.ReplyDTO;
import org.zerock.b2.entity.Reply;
import org.zerock.b2.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
  
  private final ReplyRepository repository;
  private final ModelMapper modelMapper;
  
  @Override
  public Long register(ReplyDTO replyDTO){
  
    Reply reply = modelMapper.map(replyDTO, Reply.class);
    
    Reply result = repository.save(reply);
    
    return result.getRno();
  }
  
  @Override
  public ReplyDTO read(Long rno){
    Optional<Reply> replyOptional = repository.findById(rno);
  
    Reply reply = replyOptional.orElseThrow();
  
    return modelMapper.map(reply, ReplyDTO.class);
  }
  
  @Override
  public void modify(ReplyDTO replyDTO){
    Optional<Reply> replyOptional = repository.findById(replyDTO.getRno());
  
    Reply reply = replyOptional.orElseThrow();
  
    reply.changeText(replyDTO.getReplyText());
  
    repository.save(reply);
  }
  
  @Override
  public void remove(Long rno){
    repository.deleteById(rno);
  }
  
  @Override
  public PageResponseDTO<ReplyDTO> getListOfBoard(Integer bno, PageRequestDTO pageRequestDTO){
    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
            pageRequestDTO.getSize(),
            Sort.by("rno").ascending());
  
    Page<Reply> result = repository.listOfBoard(bno, pageable);
  
    List<ReplyDTO> dtoList =
            result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
                    .collect(Collectors.toList());
  
    return PageResponseDTO.<ReplyDTO>withAll()
            .pageRequestDTO(pageRequestDTO)
            .dtoList(dtoList)
            .total((int)result.getTotalElements())
            .build();
  }
  
}
