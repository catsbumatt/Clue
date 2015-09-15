package cluedo;

import gui.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;

import cards.*;
import guess.*;

/**
 * represents a single player on the board, the user can move this player,
 * suggest with this player, and accuse with this player
 * @author catsbumatt
 *
 */
public class Player {

	public String player; // players name
	public String name; // characters name
	private Image token; // characters token
	private Square position; // current square the player is on
	public Room room = null; // current room the player is in
	public List<Card> cards = new ArrayList<Card>(); // all of the players cards
	public NoteBook notebook = new NoteBook();
	public boolean eliminated = false;



	public Player(String player, String name, Image img) {
		this.player = player;
		this.name = name;
		this.token = img;
	}

	public Square getPos() {
		return position;
	}

	public void setPos(Square pos) {
		this.position = pos;
	}

	public void fillNoteBook(Card c) {
		if (!cards.contains(c)){
			notebook.add(c);
		}
	}

	/**
	 * This is called when the current player presses the suggest button when
	 * inside a room, this method creates a new guess and from that fills a
	 * message string which is returned to the frame so it can be used in a
	 * JDialog. the notebook is then updated
	 * @return
	 */
	public String suggest() {
		Guess s = new Guess();
		while (!s.isValid()) {
			s = new Guess();
		}
		if (s.cancelled()) {
			return null;
		}
		// special case, move player
		Cluedo.playerSuggested(s.suspect, this.room);
		// message for the JDialog
		String message = " ";
		PersonCard person = null;
		WeaponCard weapon = null;
		RoomCard room = null;
		for (Player p : Cluedo.players) {
			if (!p.equals(this)) {
				for (Card c : p.cards) {
					if (c.getName().equals(s.suspect.getName())) {

						message = message + " '" + c.getName() + "' ";
						person = (PersonCard) c;
					} else if (c.getName().equals(s.weapon.getName())) {

						message = message + " '" + c.getName() + "' ";
						weapon = (WeaponCard) c;
					} else if (c.getName().equals(s.room.getName())) {

						message = message + " '" + c.getName() + "' ";
						room = (RoomCard) c;
					}
				}
			}
		}
		if (person != null) {
			notebook.remove(person);
		}
		if (weapon != null) {
			notebook.remove(weapon);
		}
		if (room != null) {
			notebook.remove(room);
		}

		return message;
	}

	/**
	 * The player can make an accusation
	 * @return if cancelled, returns 0. if correct, returns 1. if incorrect returns 2
	 */
	public int accuse() {
		Guess a = new Guess();
		while (!a.isValid()) {
			a = new Guess();
		}
		if (a.cancelled()) {
			return 0;
		}
		// if the guess is correct
		if (Cluedo.secretenvelope.getMurderer().equals(a.suspect)
				&& Cluedo.secretenvelope.getMurderLoc().equals(a.room)
				&& Cluedo.secretenvelope.getMurderWeapon().equals(a.weapon)) {

			return 1;
		} else {
			return 2;
		}

	}



	/**
	 * draws the players token on the board canvas
	 * @param g
	 */
	public void draw(Graphics g) {
		if (token != null){
			g.drawImage(token, position.getX(), position.getY(), null);
		}
	}

	/**
	 * draws players card next to the board canvas
	 * @param g
	 */
	public void drawCards(Graphics g) {
		int pos = (Square.size * 24) + 10; // size of board horizontally plus
											// buffer
		int x = pos;
		int y = 20;
		g.setFont(new Font("SansSerif", Font.ITALIC, 24));
		g.setColor(Color.BLACK);
		g.drawString(player + "'s Cards", x, y);
		y += 20;
		int count = 0;
		for (Card c : cards) {
			c.draw(g, x, y);
			count++;
			x += 110;
			if (count > 1) {
				y += 140;
				x = pos;
				count = 0;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
