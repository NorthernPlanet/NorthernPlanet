package com.pyhu.northernplanet.db.repository;

import com.pyhu.northernplanet.db.entity.Participants;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participants, Integer> {

  @Transactional
  List<Participants> findByrooms_roomId(int room_id);

  @Transactional
  List<Participants> findByusers_userId(int user_id);

  @Transactional
  int deleteAllByRooms_RoomId(int roomId);
}
