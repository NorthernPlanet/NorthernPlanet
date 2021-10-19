package com.ssafy.api.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ssafy.common.util.Room;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RoomManager {

	private final Logger log = LoggerFactory.getLogger(RoomManager.class);

	@Autowired
	private KurentoClient kurento;

	private final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<>();

	public Room getRoom(String roomName, String participantName) {
		log.debug("Searching for room {}", roomName);
		Room room = rooms.get(roomName);

		if (room == null) {
			log.debug("Room {} not existent. Will create now!", roomName);
			room = new Room(roomName, kurento.createMediaPipeline(), participantName);
			rooms.put(roomName, room);
		}
		log.debug("Room {} found!", roomName);
		return room;
	}

	public void removeRoom(Room room) {
		this.rooms.remove(room.getName());
		room.close();
		log.info("Room {} removed and closed", room.getName());
	}
	
	public void setPresenter(String roomName, String presenterName) throws IOException {
		Room room = rooms.get(roomName);
		room.setPresenter(presenterName);
		rooms.put(roomName, room);
	}
}
