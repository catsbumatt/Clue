package cluedo;

import gui.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 * This class is one of 9 rooms in the game of cluedo, it contains the
 * dimensions of the room, all the doors of the room, every player currently in
 * the room and if it has a secretPassage or not
 * 
 * @author catsbumatt
 * 
 */
public class Room {

	public String name;
	// list of doors and the squares they are on
	public List<Square> doors = new ArrayList<Square>(); // populated in
															// boardCanvas
	// list of players currently in the room
	public List<Player> players = new ArrayList<Player>();
	public List<Square> roomSquares = new ArrayList<Square>(); // populated in
																// boardCanvas
	public Square secretPassage;
	public int minX, minY, maxX, maxY;

	public Room(String name) {
		this.name = name;
	}

	/**
	 * sets the dimension of each room
	 */
	public void setDimensions(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public String getName() {
		return name;
	}

	/**
	 * Checks if the square is within the dimensions of the this room
	 */
	public boolean hasSquare(Square s) {
		if ((s.getX() >= minX && s.getX() <= maxX)
				&& (s.getY() >= minY && s.getY() <= maxY)) {
			return true;
		}
		return false;
	}

	public void addDoor(Square door) {
		doors.add(door);
	}

	public void addRoomSquare(Square roomSquare) {
		roomSquares.add(roomSquare);
	}

	/**
	 * if the room contains a secretPass, the secretPassage field will not be null
	 */
	public boolean hasSecretPass() {
		if (secretPassage == null) {
			return false;
		}
		return true;
	}

}
