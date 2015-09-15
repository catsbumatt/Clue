package tests;

import org.junit.Test;

import cluedo.*;
import cards.*;
import gui.*;
import static org.junit.Assert.*;

import java.awt.Color;
import java.util.*;

public class Tests {


	// NoteBook tests
	@Test
	public void testValidNotebookAdd(){
		NoteBook n = new NoteBook();
		PersonCard p = new PersonCard("Miss Scarlet");
		WeaponCard w = new WeaponCard("CandleStick");
		RoomCard r = new RoomCard("Hall");
		n.add(p);
		n.add(w);
		n.add(r);
		assertTrue(n.getSuspects().contains(p));
		assertTrue(n.getWeapons().contains(w));
		assertTrue(n.getRooms().contains(r));

	}


	@Test
	public void testValidNotebookRemove(){
		NoteBook n = new NoteBook();
		PersonCard p = new PersonCard("Miss Scarlet");
		WeaponCard w = new WeaponCard("CandleStick");
		RoomCard r = new RoomCard("Hall");
		n.add(p);
		n.add(w);
		n.add(r);
		n.remove(p);
		n.remove(w);
		n.remove(r);
		assertFalse(n.getSuspects().contains(p));
		assertFalse(n.getWeapons().contains(w));
		assertFalse(n.getRooms().contains(r));
	}

	// Player tests
	@Test
	public void testFillNoteBook(){
		// no image needed
		Player p = new Player("Matt", "Colonel Mustard", null);
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new PersonCard("Miss Scarlet"));
		cards.add(new PersonCard("Colonel Mustard"));
		cards.add(new WeaponCard("Candlestick"));
		cards.add(new WeaponCard("Dagger"));
		cards.add(new RoomCard("Kitchen"));
		cards.add(new RoomCard("Ball Room"));
		for (Card c : cards){
			p.fillNoteBook(c);
		}
		// miss scarlet
		assertTrue(p.notebook.getSuspects().contains(cards.get(0)));
		// candlestick
		assertTrue(p.notebook.getWeapons().contains(cards.get(2)));
		// kitchen
		assertTrue(p.notebook.getRooms().contains(cards.get(4)));

	}

	// test Squares
	@Test
	public void testValidSquareIsin(){
		Square s = new Square(0, 0, null, null);
		// on the edge of the square
		int x = 24;
		int y = 24;
		assertTrue(s.isIn(x, y));
	}

	@Test
	public void testInvalidSquareIsin(){
		Square s = new Square(0, 0, null, null);
		// on the edge of the square
		int x = 26;
		int y = 26;
		assertFalse(s.isIn(x, y));
	}

	// test Rooms
	@Test
	public void testRoom(){
		Room r = new Room("Hall");
		// check if dimensions set correctly
		r.setDimensions(0, 0, 100, 100);
		r.addDoor(new Square(0, 0, Square.Type.Door, null));
		r.players.add(new Player("Matt", "Miss Scarlet", null));
		assertFalse(r.hasSecretPass());
		r.secretPassage = new Square(25, 25, Square.Type.SecretPassage, null);
		assertTrue(r.hasSecretPass());
	}

	@Test
	public void testValidHasSquare(){
		Room r = new Room("Hall");
		r.setDimensions(0, 0, 200, 200);
		Square s = new Square(0, 0, Square.Type.Hallway, null);
		assertTrue(r.hasSquare(s));
	}

	@Test
	public void testInvalidHasSquare(){
		Room r = new Room("Hall");
		r.setDimensions(0, 0, 200, 200);
		Square s = new Square(201, 201, Square.Type.Hallway, null);
		assertFalse(r.hasSquare(s));
	}

	// test Canvas
	@Test
	public void testBoardCanvas(){
		BoardCanvas board = new BoardCanvas();
		// check corners
		assertTrue(board.getBoard()[0][0].left == null);
		assertTrue(board.getBoard()[0][0].up == null);
		assertTrue(board.getBoard()[0][23].right == null);
		assertTrue(board.getBoard()[0][23].up == null);
		assertTrue(board.getBoard()[24][0].down == null);
		assertTrue(board.getBoard()[24][0].left == null);
		assertTrue(board.getBoard()[24][23].down == null);
		assertTrue(board.getBoard()[24][23].right == null);
	}

	// test player
	@Test
	public void testPlayerPosition(){
		Player p = new Player("Matt", "Colonel Mustard", null);
		Square s = new Square(0, 0, Square.Type.Hallway, null);
		p.setPos(s);
		assertTrue(p.getPos().equals(s));
	}

	@Test
	public void testPlayerEquals(){
		Player p1 = new Player("Matt", "Colonel Mustard", null);
		Player p2 = new Player("Matt", "Colonel Mustard", null);
		assertTrue(p1.equals(p2));
	}
}
