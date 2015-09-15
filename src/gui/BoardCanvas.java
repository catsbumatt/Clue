package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cluedo.*;

/**
 * This class draws the board and cards onto itself, when created, it creates the board
 * and squares
 * @author catsbumatt
 *
 */
public class BoardCanvas extends JPanel implements MouseListener {

	private List<String> str;
	private static Square[][] board = new Square[25][24];

	public BoardCanvas() {
		setBackground(Color.ORANGE);
		str = new ArrayList<String>();

		str.add("OOOOOGBBBPBBBBPBBBBOOOOO");
		str.add("OOOOOBBRRRBOOBRRRRBOOOOO");
		str.add("OOOOOBRRBBBOOBBBRRBOOOOO");
		str.add("OOOOOBRRBOOOOOOBRRBOOOOO");
		str.add("OOOOOBRRBOOOOOOBRRYOOOOO");
		str.add("OOOOOBRRYOOOOOOYRRRBBBGB");
		str.add("BBBBYBRRBOOOOOOBRRRRRRRP");
		str.add("RRRRRRRRBYBBBBYBRRRRRRRB");
		str.add("BRRRRRRRRRRRRRRRRRBBBBBB");
		str.add("BBBBBRRRRRRRRRRRRRYOOOOO");
		str.add("OOOOBBBBRRBBBBBRRRBOOOOO");
		str.add("OOOOOOOBRRBBBBBRRRBOOOOO");
		str.add("OOOOOOOYRRBBBBBRRRBBBBYB");
		str.add("OOOOOOOBRRBBBBBRRRRRRRRB");
		str.add("OOOOOOOBRRBBBBBRRRBBYBBB");
		str.add("BBBBBBYBRRBBBBBRRBOOOOOO");
		str.add("BRRRRRRRRRBBBBBRRYOOOOOO");
		str.add("PRRRRRRRRRRRRRRRRBBOOOOO");
		str.add("BRRRRRRRRBBYYBBRRRBBBBBB");
		str.add("GBBBBBYRRBOOOOBRRRRRRRRP");
		str.add("OOOOOOBRRBOOOOYRRRRRRRRB");
		str.add("OOOOOOBRRBOOOOBRRYBBBBBG");
		str.add("OOOOOOBRRBOOOOBRRBOOOOOO");
		str.add("OOOOOOBRRBOOOOBRRBOOOOOO");
		str.add("OOOOOOBPBBOOOOBBRBOOOOOO");
		// Initialize board
		int x = 0;
		int y = 0;
		int size = Square.size;
		for (int i = 0; i < str.size(); i++) {
			// loop through each character in the string
			for (int j = 0; j < str.get(i).length(); j++) {
				if (str.get(i).charAt(j) == 'O') {
					board[i][j] = new Square(x, y, Square.Type.Room, new Color(
							255, 99, 71)); // tomato colour
					// each room has its room squares
					for (Room r : Cluedo.rooms) {
						if (r.hasSquare(board[i][j])) {
							r.addRoomSquare(board[i][j]);
						}
					}

				} else if (str.get(i).charAt(j) == 'G') {
					board[i][j] = new Square(x, y, Square.Type.SecretPassage,
							Color.GREEN);
					for (Room r : Cluedo.rooms) {
						if (r.hasSquare(board[i][j])) {
							r.secretPassage = board[i][j];
						}
					}
				} else if (str.get(i).charAt(j) == 'B') {
					board[i][j] = new Square(x, y, Square.Type.Block,
							Color.BLACK);
				} else if (str.get(i).charAt(j) == 'P') {
					board[i][j] = new Square(x, y, Square.Type.StartPosition,
							Color.MAGENTA);
				} else if (str.get(i).charAt(j) == 'Y') {
					board[i][j] = new Square(x, y, Square.Type.Door, new Color(
							176, 196, 222)); // light blue steel
					// add each door to the room they are in
					for (Room r : Cluedo.rooms) {
						if (r.hasSquare(board[i][j])) {
							r.addDoor(board[i][j]);
						}
					}
				} else if (str.get(i).charAt(j) == 'R') {
					board[i][j] = new Square(x, y, Square.Type.Hallway,
							new Color(240, 230, 140)); // khaki
				}
				x += size;
			}
			x = 0;
			y += size;
		}

		setNeighbours();
		addMouseListener(this);
	}

	/**
	 * Sets all the neighbours to the squares on the board. null if there is no
	 * neighbour.
	 */
	public void setNeighbours() {

		for (int col = 0; col < board.length; col++) {
			for (int row = 0; row < board[0].length; row++) {
				// no first row square will have a left neighbour
				if (row > 0) {

					board[col][row].left = board[col][row - 1];
				}
				// no right column square will have a down neighbour
				if (col < board.length - 1) {

					board[col][row].down = board[col + 1][row];
				}
				// no left column square will have a right neighbour
				if (row < board[0].length - 1) {

					board[col][row].right = board[col][row + 1];
				}
				// no last row square will have an up neighbour
				if (col > 0) {
					board[col][row].up = board[col - 1][row];
				}
			}
		}

	}

	public static Square[][] getBoard() {
		return board;
	}

	/**
	 * draws all the squares on the board
	 *
	 * @param g
	 */
	public void drawBoard(Graphics g) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j].draw(g);
			}
		}

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBoard(g);
		Cluedo.drawPlayers(g);
		// condition because frame is made before currentplayer is set
		if (Cluedo.currentPlayer != null) {
			Cluedo.currentPlayer.drawCards(g);
		}
		// repaint();

	}

	/**
	 * Listens to the mouse, if the player presses on a door or secretpassage
	 * whil in the room that contains this door and secret passage, move that
	 * player accordingly
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// just incase user tries to click before setup
		if (Cluedo.currentPlayer == null) {
			return;
		}
		// the current player is in a room
		if (Cluedo.currentPlayer.room != null) {
			// if the user clicked on a secret passage
			if (Cluedo.currentPlayer.room.hasSecretPass()) {
				if (Cluedo.currentPlayer.room.secretPassage.isIn(e.getX(),
						e.getY())) {
					Cluedo.takeSecretPass(Cluedo.currentPlayer.room);
					Cluedo.endTurn();
					repaint();
					JOptionPane.showMessageDialog(this,
							"You just used a secret passage, it is now "
									+ Cluedo.currentPlayer.player + "'s turn");
					return;
				}
			}
			// if the user clicked on a door, move the current player to that
			// door
			for (Square door : Cluedo.currentPlayer.room.doors) {
				if (door.isIn(e.getX(), e.getY())) {
					Cluedo.currentPlayer.setPos(door);
					// the player has left the room
					Cluedo.currentPlayer.room = null;
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
