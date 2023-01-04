package org.zerock.b2.entity;


import lombok.*;

import javax.persistence.*;


//값 객체
@Embeddable
//@Entity
//@Table(name ="tbl_bimage")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class BoardImage {

//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long ino;

//  private String uuid;
//
//  private String fileName;
//
//  private boolean img;
  
  private String fileLink;
  
  private int ord;    //oneToMany쓸고야
  
  public void fixOrd(int ord){
    this.ord = ord;
  }
}
