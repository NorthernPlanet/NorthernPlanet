package com.pyhu.northernplanet.db.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Users {

  @Column(name = "user_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int userId;
  String email;
  String name;
  String image;
  @Column(name = "oauth_id")
  String oauthId;
  String oauth_type;

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Participants> participants = new ArrayList<>();

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Rooms> rooms = new ArrayList<>();

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserPresentation> userPresentations = new ArrayList<>();


}
