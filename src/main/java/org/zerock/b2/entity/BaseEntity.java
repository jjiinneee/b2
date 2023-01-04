package org.zerock.b2.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
public class BaseEntity {
  
  @CreatedDate
  @Column(name = "regDate", updatable = false)
  private LocalDateTime regDate;
  
  @LastModifiedDate
  private LocalDateTime modDate;
}