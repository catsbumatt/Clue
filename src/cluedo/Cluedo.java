package cluedo;

import gui.*;
import gui.Square.Type;

import java.io.IOException;
import java.util.*;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import cards.*;

/**
 * This class represents the whole game of cluedo, it controls everthing that
 * has to do with Gameplay and more
 *
 * @author catsbumatt
 *
 */
public class Cluedo {

	public static BoardFrame frame;
	// list of all the rooms, indicated by a name
	public static List<Room> rooms = new ArrayList<Room>();
	// list of all players playing the game
	public static List<Player> players = new ArrayList<Player>();

	// the current players turn
	public static Player currentPlayer;

	// one secret envelope with three cards
	public static SecretEnvelope secretenvelope;

	public Cluedo() {

		// create the rooms
		createRooms();
		// make the frame
		frame = new BoardFrame("Cluedo");
		populatePlayers(frame.getPlayers());
		// deal with the cards (3 in envelope, rest with players)
		dealCards();
		currentPlayer = players.get(0);
		// initialize starting areas for players
		setStartSquares();
		frame.repaint();

	}

	/**
	 * create all the cards in the game of Cluedo and deal them out according to
	 * the number of players that are playing the game the secret envelope will
	 * be made first with one murder card, one room card, and a person card
	 * which represents the murdered
	 */
	public List<Card> dealCards() {
		List<Card> cards = new ArrayList<Card>();
		// player cards
		cards.add(new PersonCard("Miss Scarlet", loadImage("Miss Scarlet.jpg")));
		cards.add(new PersonCard("Colonel Mustard",
				loadImage("Col Mustard.jpg")));
		cards.add(new PersonCard("Mrs White", loadImage("Miss White.jpg")));
		cards.add(new PersonCard("The Reverend Green",
				loadImage("Rev Green.jpg")));
		cards.add(new PersonCard("Mrs Peacock", loadImage("Mrs Peacock.jpg")));
		cards.add(new PersonCard("Professor Plum", loadImage("Prof Plum.jpg")));
		// weapon cards
		cards.add(new WeaponCard("Candlestick", loadImage("CandleStick.jpg")));
		cards.add(new WeaponCard("Dagger", loadImage("Dagger.jpg")));
		cards.add(new WeaponCard("Lead Pipe", loadImage("Lead Piping.jpg")));
		cards.add(new WeaponCard("Revolver", loadImage("Revolver.jpg")));
		cards.add(new WeaponCard("Rope", loadImage("Rope.jpg")));
		cards.add(new WeaponCard("Spanner", loadImage("Spanner.jpg")));
		// room Cards
		cards.add(new RoomCard("Kitchen", loadImage("Kitchen.jpg")));
		cards.add(new RoomCard("Ball Room", loadImage("Ball Room.jpg")));
		cards.add(new RoomCard("Conservatory", loadImage("Conservatory.jpg")));
		cards.add(new RoomCard("Billiard Room", loadImage("Billiard Room.jpg")));
		cards.add(new RoomCard("Library", loadImage("Library.jpg")));
		cards.add(new RoomCard("Study", loadImage("Study.jpg")));
		cards.add(new RoomCard("Hall", loadImage("Hall.jpg")));
		cards.add(new RoomCard("Lounge", loadImage("Lounge.jpg")));
		cards.add(new RoomCard("Dining Room", loadImage("Dining Room.jpg")));
		// fill each notebook
		addNotebooks(cards);
		fillSecretEnvelope(cards);
		Collections.shuffle(cards);

		while (!cards.isEmpty()) {
			for (Player p : players) {
				if (!cards.isEmpty()) {
					p.cards.add(cards.get(cards.size() - 1));
					// also update notebook
					p.notebook.remove(cards.get(cards.size() - 1));
					cards.remove(cards.size() - 1);
				}
			}
		}
		return cards;
	}

	public void fillSecretEnvelope(List<Card> cards) {

		int murderer = (int) (Math.random() * 6);
		int weapon = (int) (Math.random() * 6) + 6;
		int location = (int) (Math.random() * 9) + 12;
		secretenvelope = new SecretEnvelope(cards.get(murderer),
				cards.get(weapon), cards.get(location));
		// remove those cards from the current set (reversed because removing
		// causes it to shift)
		cards.remove(location);
		cards.remove(weapon);
		cards.remove(murderer);

	}

	/**
	 * Fill everybody's notebook with every single card in the game the cards
	 *
	 * @param cards
	 */
	public void addNotebooks(List<Card> cards) {
		for (Player p : players) {
			for (Card c : cards) {
				p.fillNoteBook(c);
			}
		}
	}

	/**
	 * Create every room which takes a name and 4 ints which represent the area
	 * the room is in
	 */
	public void createRooms() {
		createRoom("Kitchen", 0, 0, 6, 7);
		createRoom("Ball Room", 8, 1, 16, 8);
		createRoom("Conservatory", 18, 0, 24, 6);
		createRoom("Billiard Room", 18, 8, 24, 13);
		createRoom("Library", 17, 14, 24, 19);
		createRoom("Study", 17, 21, 24, 25);
		createRoom("Hall", 9, 18, 15, 25);
		createRoom("Lounge", 0, 19, 7, 25);
		createRoom("Dining Room", 0, 9, 8, 16);
	}

	/**
	 * creates a single room
	 */
	public void createRoom(String name, int minX, int minY, int maxX, int maxY) {
		int s = Square.size;
		Room r = new Room(name);
		r.setDimensions(s * minX, s * minY, s * maxX, s * maxY);
		rooms.add(r);
	}

	/**
	 * Creates every player that was entered in the beginning
	 *
	 * @param map
	 *            contains the player name and the character name
	 */
	public void populatePlayers(Map<String, String> map) {
		ArrayList<String> characters = new ArrayList<String>();
		characters.add("Miss Scarlet");
		characters.add("Colonel Mustard");
		characters.add("Mrs White");
		characters.add("The Reverend Green");
		characters.add("Mrs Peacock");
		characters.add("Professor Plum");
		List<Image> images = new ArrayList<Image>();
		images.add(loadImage("Miss Scarlett token.jpg"));
		images.add(loadImage("Col Mustard token.jpg"));
		images.add(loadImage("Miss White token.jpg"));
		images.add(loadImage("Rev Green token.jpg"));
		images.add(loadImage("Mrs Peacock token.jpg"));
		images.add(loadImage("Prof Plum token.jpg"));

		for (Map.Entry<String, String> entry : map.entrySet()) {
			for (int j = 0; j < characters.size(); j++) {
				if (characters.get(j).equals(entry.getValue())) {
					players.add(new Player(entry.getKey(), entry.getValue(),
							images.get(j)));
				}
			}

		}

	}

	/**
	 * sets all players playing the game on a starting position square the
	 * position given to each character is randomized
	 */
	public void setStartSquares() {
		List<Square> startPositions = new ArrayList<Square>();
		// loop through each square in the canvas to find the starting area
		for (int i = 0; i < BoardCanvas.getBoard().length; i++) {
			for (int j = 0; j < BoardCanvas.getBoard()[0].length; j++) {
				if (BoardCanvas.getBoard()[i][j].getType() == Type.StartPosition) {
					startPositions.add(BoardCanvas.getBoard()[i][j]);
				}
			}
		}
		// shuffle the start positions so players are started randomly
		Collections.shuffle(startPositions);
		for (int i = 0; i < startPositions.size(); i++) {
			for (Player p : players) {
				if (p.getPos() == null) {
					p.setPos(startPositions.get(i));
					break;
				}
			}
		}

	}

	/**
	 * this method should be called when a turn has been ended and the current
	 * player needs to be changed, this method is currently called in the
	 * boardframe where the endturn button is
	 */
	public static void endTurn() {
		currentPlayer = getNext();
		while (currentPlayer.eliminated) {
			currentPlayer = getNext();
		}
		frame.numSteps = -1;
		frame.rolled = false;
		frame.suggested = false;
		frame.canvas.repaint();
	}

	/**
	 * Returns the next player in the players arraylist
	 *
	 * @return
	 */
	public static Player getNext() {
		// get the next player in the players array
		for (int i = 0; i < players.size() - 1; i++) {
			if (currentPlayer.equals(players.get(i))) {
				return players.get(i + 1);

			}
		}
		// final players turn, go back to beginning
		return players.get(0);
	}

	/**
	 * Checks whether all players are eliminated, returns true if they are,
	 * false otherwise
	 */
	public static boolean allEliminated() {
		for (Player p : players) {
			if (!p.eliminated) {
				return false;
			}
		}
		return true;
	}

	/**
	 * sets the character in a random position in the room
	 *
	 * @param r
	 *            the room
	 */
	public static void setRandomlyInRoom(Player player, Room r) {
		int randIndex = (int) (Math.random() * r.roomSquares.size() - 1);
		// check if any players are currently on the square that the current
		// player is trying to move to
		for (Player p : Cluedo.players) {
			while (p.getPos().equals(r.roomSquares.get(randIndex))) {
				randIndex = (int) (Math.random() * r.roomSquares.size() - 1);
			}
		}
		player.setPos(r.roomSquares.get(randIndex));
		player.room = r;
		// finally add the player to the list of players in that room
		r.players.add(player);
	}

	/**
	 * If the player has selected to use a secret Passage, moves the player to
	 * the room that the secret passage leads to.
	 *
	 * @param the
	 *            current room the player is in
	 */
	public static void takeSecretPass(Room r) {
		// if in lounge, set randomly in the conservatory
		if (r.name.equals("Lounge")) {
			for (Room room : rooms) {
				if (room.name.equals("Conservatory")) {
					setRandomlyInRoom(currentPlayer, room);
				}
			}
		} else if (r.name.equals("Conservatory")) {
			for (Room room : rooms) {
				if (room.name.equals("Lounge")) {
					setRandomlyInRoom(currentPlayer, room);
				}
			}
		} else if (r.name.equals("Kitchen")) {
			for (Room room : rooms) {
				if (room.name.equals("Study")) {
					setRandomlyInRoom(currentPlayer, room);
				}
			}
		} else if (r.name.equals("Study")) {
			for (Room room : rooms) {
				if (room.name.equals("Kitchen")) {
					setRandomlyInRoom(currentPlayer, room);
				}
			}
		}
	}

	/**
	 * If a player has been suggested, moves that player to room the suggestion
	 * was mad in
	 *
	 * @param the
	 *            personCard that has the name of the player being moved
	 * @param the
	 *            room that the player is moving to
	 */
	public static void playerSuggested(PersonCard person, Room room) {
		for (Player p : players) {
			if (!p.eliminated && !p.equals(currentPlayer)) {

				if (p.name.equals(person.getName())) {

					JOptionPane.showMessageDialog(frame, p.player
							+ " has been moved to the " + room.name
							+ " because he/she was suggested");

					Cluedo.setRandomlyInRoom(p, room);
					frame.canvas.repaint();
				}
			}
		}
	}

	/**
	 * sets the old frames visibility to false and creates a new instance of
	 * Cluedo
	 */
	public static void newGame() {
		frame.setVisible(false);
		frame = null;
		Cluedo newCluedo = new Cluedo();
	}

	/**
	 * Load an image from the file system, using a given filename.
	 *
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		java.net.URL imageURL = Cluedo.class.getResource("images/" + filename);

		try {
			Image img = ImageIO.read(imageURL);

			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. There's not much we
			// can actually do at this point, except to abort the game.
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	/**
	 * Draws all players in the players arraylist
	 *
	 * @param g
	 */
	public static void drawPlayers(Graphics g) {
		for (Player p : players) {
			if (!p.eliminated) {
				p.draw(g);
			}
		}
	}

}
