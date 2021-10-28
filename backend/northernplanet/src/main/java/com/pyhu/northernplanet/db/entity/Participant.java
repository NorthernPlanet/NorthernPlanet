package com.pyhu.northernplanet.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @ManyToOne
  @JoinColumn(name = "code_id")
  Code code;

  @ManyToOne
  @JoinColumn(name = "user_id")
  User user;

  @ManyToOne
  @JoinColumn(name = "room_id")
  Room room;

}
