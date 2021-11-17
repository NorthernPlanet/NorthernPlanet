package com.pyhu.northernplanet.api.controller;

import com.pyhu.northernplanet.api.request.RoomPutReq;
import com.pyhu.northernplanet.api.response.RoomPutRes;
import com.pyhu.northernplanet.common.dto.ParticipantDto;
import com.pyhu.northernplanet.db.entity.Room;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pyhu.northernplanet.api.request.RoomPostReq;
import com.pyhu.northernplanet.api.response.RoomGetRes;
import com.pyhu.northernplanet.api.service.ParticipantService;
import com.pyhu.northernplanet.api.service.RoomService;
import com.pyhu.northernplanet.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Api(value = "방 관련 API", tags = {"Room"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

  private final RoomService roomService;
  private final UserService userService;
  private final ParticipantService participantService;


  @PostMapping("")
  @ApiOperation(value = "방 생성")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm A",
      timezone = "Asia/Seoul")
  @ApiResponses({@ApiResponse(code = 200, message = "성공"),
      @ApiResponse(code = 401, message = "인증 실패"), @ApiResponse(code = 404, message = "사용자 없음"),
      @ApiResponse(code = 500, message = "서버 오류")})
  public ResponseEntity createRoom(
      @RequestBody @ApiParam(value = "방정보", required = true) RoomPostReq roomInfo) {
    try {
      log.info("[register] room register info: {}", roomInfo);
      roomService.createRoom(roomInfo);

      return new ResponseEntity(HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity(HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/list/{userId}")
  @ApiOperation(value = "사용자 아이디가 참가자로 포함된 전체 방 보기")
  @ApiResponses({@ApiResponse(code = 200, message = "성공"),
      @ApiResponse(code = 401, message = "인증 실패"),
      @ApiResponse(code = 404, message = "사용자 없음"),
      @ApiResponse(code = 500, message = "서버 오류")})
  public ResponseEntity<List<RoomGetRes>> getRoomListByUserId(@PathVariable("userId") Long userId) {
    List<RoomGetRes> rooms = null;
    try {
      log.info("[showRoomsByUserId] userId: {}", userId);
      rooms = roomService.getRoomListByUserId(userId);
      for (RoomGetRes item : rooms) {
        item.setParticipants(participantService.getParticipantByRoomId(item.getRoomId()));
      }
      return new ResponseEntity<>(rooms, HttpStatus.OK);
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }

    return new ResponseEntity<>(rooms, HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/{roomId}")
  @ApiOperation(value = "방 하나의 정보 보기")
  @ApiResponses({@ApiResponse(code = 200, message = "성공"),
      @ApiResponse(code = 401, message = "인증 실패"),
      @ApiResponse(code = 404, message = "방 정보 없음"),
      @ApiResponse(code = 500, message = "서버 오류")})
  public ResponseEntity<RoomGetRes> getRoomByRoomId(@PathVariable("roomId") Long roomId) {
    log.info("roomId로 방 1개 가져오기: {}", roomId);
    RoomGetRes room = null;
    try {
      List<ParticipantDto> participants = participantService.getParticipantByRoomId(roomId);
      room = roomService.getRoom(roomId, participants);
      return new ResponseEntity<>(room, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(room, HttpStatus.BAD_REQUEST);
  }


  @PutMapping("/{roomId}")
  @ApiOperation(value = "방 정보 수정")
  @ApiResponses({@ApiResponse(code = 200, message = "성공"),
      @ApiResponse(code = 401, message = "인증 실패"),
      @ApiResponse(code = 404, message = "방 정보 없음"),
      @ApiResponse(code = 500, message = "서버 오류")})
  public ResponseEntity<?> updateRoom(
      @RequestBody @ApiParam(value = "방업데이트", required = true) RoomPutReq roomInfo) {
    RoomPutRes room = null;
    try {
      log.info("[update] roomUpdateReq: {}", roomInfo);
      room = roomService.updateRoom(roomInfo);
      log.info("[update] room: {}", room);
      return new ResponseEntity<>(room, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(room, HttpStatus.BAD_REQUEST);
  }

  @DeleteMapping("/{roomId}")
  @ApiOperation(value = "방 삭제")
  @ApiResponses({@ApiResponse(code = 200, message = "성공"),
      @ApiResponse(code = 401, message = "인증 실패"),
      @ApiResponse(code = 404, message = "방 없음"),
      @ApiResponse(code = 500, message = "서버 오류")})
  public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId) {
    log.info("[DeleteRoom] room {} is deleted - controller", roomId);
    try {
      Room room = roomService.getRoom(roomId);
      if (room == null) {
        return ResponseEntity.status(404).body(roomId);
      }
      roomService.deleteRoom(roomId);
      return ResponseEntity.status(200).body(roomId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PutMapping("/leave/{roomId}")
  @ApiOperation(value = "방 정보 수정")
  @ApiResponses({@ApiResponse(code = 200, message = "성공"),
      @ApiResponse(code = 401, message = "인증 실패"),
      @ApiResponse(code = 404, message = "방 정보 없음"),
      @ApiResponse(code = 500, message = "서버 오류")})
  public ResponseEntity<?> leaveRoom(@PathVariable("roomId") Long roomId) {
    try {
      log.info("[update] leave room requesst: {}", roomId);
      roomService.leaveRoom(roomId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
  //
  // @GetMapping("/onlive/{roomId}")
  // @ApiOperation(value = "방이 현재 활동중인지 보기")
  // @ApiResponses({@ApiResponse(code = 200, message = "성공"),
  // @ApiResponse(code = 401, message = "인증 실패"),
  // @ApiResponse(code = 404, message = "사용자 없음"),
  // @ApiResponse(code = 409, message = "이미 존재하는 유저"),
  // @ApiResponse(code = 500, message = "서버 오류")})
  // public ResponseEntity<? extends BaseResponseBody> isOnLive(@PathVariable("roomId") int roomId)
  // {
  // try {
  // Rooms room = roomService.getRoom(roomId);
  // if (room.getOnLive()) {
  // return ResponseEntity.status(200).body(BaseResponseBody.of(200, "방이 활동중입니다."));
  // } else {
  // return ResponseEntity.status(404).body(BaseResponseBody.of(404, "방이 활동중이지 않습니다."));
  // }
  // } catch (Exception e) {
  // e.printStackTrace();
  // return ResponseEntity.status(500).body(BaseResponseBody.of(500, "Fail"));
  //
  // }
  // }
  //
  // @PutMapping("/onlive")
  // @ApiOperation(value = "방의 활동 상태를 true/false로 변경하기")
  // @ApiResponses({@ApiResponse(code = 200, message = "성공"),
  // @ApiResponse(code = 401, message = "인증 실패"),
  // @ApiResponse(code = 404, message = "사용자 없음"),
  // @ApiResponse(code = 409, message = "이미 존재하는 유저"),
  // @ApiResponse(code = 500, message = "서버 오류")})
  // public ResponseEntity<? extends BaseResponseBody> setOnLive(
  // @RequestBody @ApiParam(value = "방의 onLive 상태 변경", required = true) RoomOnLiveReq roomInfo) {
  // try {
  // log.info("[set on live] roomInfo: {}", roomInfo);
  // roomService.setRoomOnLive(roomInfo);
  //
  // return ResponseEntity.status(200).body(BaseResponseBody.of(200, "방 onLive 상태가 변경되었습니다."));
  // } catch (Exception e) {
  // e.printStackTrace();
  // return ResponseEntity.status(500).body(BaseResponseBody.of(500, "Fail"));
  //
  // }
  // }
  //

}
