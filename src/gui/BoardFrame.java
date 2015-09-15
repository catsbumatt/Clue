package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.*;

import main.Main;
import cards.*;
import cluedo.*;

/**
 * Represents the frame, this is everything you see on the screen including
 * buttons, menus, the canvas and more.
 *
 * @author catsbumatt
 *
 */
public class BoardFrame extends JFrame implements KeyListener {

	public BoardCanvas canvas;

	// TODO CHANGE WHEN FINISHED TESTING
	private int numPlayers = 1;

	public int numSteps = -1; // number of steps the currentplayer has
	public boolean rolled = false;
	public boolean suggested;

	// contains the name of the player and the character that they chose
	private Map<String, String> players = new LinkedHashMap<String, String>();

	public BoardFrame(String name) {
		super(name);

		setNumPlayers();

		ArrayList<String> characters = new ArrayList<String>();
		characters.add("Miss Scarlet");
		characters.add("Colonel Mustard");
		characters.add("Mrs White");
		characters.add("The Reverend Green");
		characters.add("Mrs Peacock");
		characters.add("Professor Plum");
		setPlayerNames(characters, numPlayers);
		// create the menus
		setMenus();
		// create the canvas
		canvas = new BoardCanvas();

		add(canvas, BorderLayout.CENTER);

		// set the frame
		setSize(850, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);

		addButtons();
		setVisible(true);
		addKeyListener(this);
	}

	public Map<String, String> getPlayers() {
		return players;
	}

	/**
	 * Brings up the JDialog for the user to enter the number of players playing
	 * this game, then sets the field 'numPlayers' accordingly
	 */
	private void setNumPlayers() {

		// ask for the number of players
		Object[] options = { "3", "4", "5", "6" };
		int n = JOptionPane.showOptionDialog(this,
				"How many players are there? ", "Players",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);

		if (n == 0) {
			numPlayers = 3;
		} else if (n == 1) {
			numPlayers = 4;
		} else if (n == 2) {
			numPlayers = 5;
		} else if (n == 3) {
			numPlayers = 6;
		}
		// if no buttons are pressed exit the game
		else {
			System.exit(0);
		}

	}

	/**
	 * After the number of characters are selected, JDialog is brought up asking
	 * for the users name and the character they want to play as, if they don't
	 * want to pick a character the first one in the list is given
	 *
	 * @param characters
	 * @param count
	 */
	public void setPlayerNames(ArrayList<String> characters, int count) {
		if (count == 0) {
			return;
		}
		JPanel panel = new JPanel();
		JTextField name = new JTextField(10);
		ArrayList<JRadioButton> rbuttons = new ArrayList<JRadioButton>();
		for (int i = 0; i < characters.size(); i++) {
			rbuttons.add(new JRadioButton(characters.get(i)));
		}
		// group the buttons so only one can be clicked
		ButtonGroup buttgroup = new ButtonGroup();
		for (JRadioButton jrb : rbuttons) {
			buttgroup.add(jrb);
		}
		panel.add(name);
		for (JRadioButton jrb : rbuttons) {
			panel.add(jrb);
		}
		int result = JOptionPane.showConfirmDialog(null, panel,
				"Please enter your name and the character you want",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {

			// bring up another dialog to be completed
			if (name.getText().isEmpty()) {
				JOptionPane.showMessageDialog(panel, "You must enter a name");
				setPlayerNames(characters, count);
			}
			if (players.containsKey(name.getText())) {
				JOptionPane
						.showMessageDialog(panel, "That name already exists");
				setPlayerNames(characters, count);
			}
			String character = characters.get(0);
			for (JRadioButton jrb : rbuttons) {
				if (jrb.isSelected()) {
					character = jrb.getText();
					break;				
				}
			}
			players.put(name.getText(), character);
			characters.remove(character);
			setPlayerNames(characters, count - 1);

		} else {
			System.exit(0);
		}
	}

	/**
	 * Sets the JBarmenus which allows you to quit, or create a new game. Also
	 * do other game related stuff
	 */
	public void setMenus() {
		JMenuBar menuBar;
		JMenu menu, submenu;
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		JMenuItem newGame = new JMenuItem(new AbstractAction("New Game") {
			public void actionPerformed(ActionEvent e) {
				Cluedo.newGame();

			}
		});
		JMenuItem quit = new JMenuItem(new AbstractAction("Quit") {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		submenu = new JMenu("Game");
		JMenuItem roll = new JMenuItem(new AbstractAction("Roll Dice") {
			public void actionPerformed(ActionEvent e) {
				rollDice();
			}
		});
		JMenuItem openNotebook = new JMenuItem(new AbstractAction(
				"Open NoteBook") {
			public void actionPerformed(ActionEvent e) {
				showNotebook();
			}
		});
		JMenuItem suggest = new JMenuItem(new AbstractAction("Suggest") {
			public void actionPerformed(ActionEvent e) {
				makeSuggestion();
			}
		});
		JMenuItem accuse = new JMenuItem(new AbstractAction("Accuse") {
			public void actionPerformed(ActionEvent e) {
				makeAccusation();
			}
		});
		JMenuItem endturn = new JMenuItem(new AbstractAction("End Turn") {
			public void actionPerformed(ActionEvent e) {
				endTurn();
				canvas.repaint();
			}
		});
		menu.add(newGame);
		menu.add(quit);
		submenu.add(roll);
		submenu.add(openNotebook);
		submenu.add(suggest);
		submenu.add(accuse);
		submenu.add(endturn);

		menuBar.add(menu);
		menuBar.add(submenu);
		// add to frame
		setJMenuBar(menuBar);
	}

	/**
	 * Adds the buttons which allows you to roll the dice, show the notebook
	 * suggest, accuse and end turn
	 */
	public void addButtons() {
		JPanel buttons = new JPanel();
		// roll dice button
		JButton roll = new JButton("Roll Dice");
		roll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rollDice();
			}
		});
		roll.setFocusable(false);
		buttons.add(roll);
		// open detective notebook button
		JButton noteBook = new JButton("Open NoteBook");
		noteBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showNotebook();

			}
		});
		noteBook.setFocusable(false);
		buttons.add(noteBook);
		// suggest button
		JButton suggest = new JButton("Suggest");
		suggest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeSuggestion();
			}
		});
		suggest.setFocusable(false);
		buttons.add(suggest);
		// accuse
		JButton accuse = new JButton("Accuse");
		accuse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeAccusation();
			}
		});
		accuse.setFocusable(false);
		buttons.add(accuse);
		// End turn button
		JButton endTurn = new JButton("End Turn");
		endTurn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// change Cluedo's current player to the next
				endTurn();
				canvas.repaint();
			}
		});
		endTurn.setFocusable(false);
		buttons.add(endTurn);
		buttons.setBackground(Color.ORANGE);
		add(buttons, BorderLayout.SOUTH);

		buttons.setFocusable(false);
	}

	/**
	 * randomly generates 2 numbers from 1 - 6 representing dice and then adds
	 * them together
	 */
	public void rollDice() {
		// 0 represents that the player has run out of movements
		if (!rolled) {
			// a dice is either 1 - 6 represented by((Math.random() *
			// 6)+1)
			numSteps = ((int) ((Math.random() * 6) + 1) + ((int) (Math.random() * 6 + 1)));
			rolled = true;
			JOptionPane.showMessageDialog(canvas, Cluedo.currentPlayer.player
					+ " just rolled a: " + numSteps);
		} else {
			JOptionPane.showMessageDialog(canvas,
					"you have already rolled the dice!");
		}
	}

	/**
	 * Opens the current players notebook which is a completely new JFrame
	 */
	public void showNotebook() {
		// open new frame or dialogue which contains the notebook
		final JFrame notebook = new JFrame("Detective Notebook");
		notebook.setSize(300, 500);
		notebook.setLocationRelativeTo(null);
		// hide instead of exiting because exiting quits the whole game
		notebook.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// add ok button at bottom to leave hide the notebook
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notebook.setVisible(false);
			}
		});
		ok.setFocusable(false);
		notebook.add(ok, BorderLayout.SOUTH);
		JPanel list = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				int x = 5;
				int y = 20;
				for (Card c : Cluedo.currentPlayer.notebook.getSuspects()) {
					g.setColor(Color.RED);
					g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
					g.drawString(c.getName(), x, y);
					y += 20;
				}
				for (Card c : Cluedo.currentPlayer.notebook.getWeapons()) {
					g.setColor(new Color(0, 100, 0));
					g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
					g.drawString(c.getName(), x, y);
					y += 20;
				}
				for (Card c : Cluedo.currentPlayer.notebook.getRooms()) {
					g.setColor(Color.BLUE);
					g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
					g.drawString(c.getName(), x, y);
					y += 20;
				}
				y += 20;
				g.setColor(Color.BLACK);
				g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
				g.drawString("Red -> Players", x, y);
				y += 20;
				g.drawString("Green -> Weapons", x, y);
				y += 20;
				g.drawString("Blue -> Rooms", x, y);
			}
		};

		notebook.add(list, BorderLayout.CENTER);
		notebook.setVisible(true);
	}

	/**
	 * Called when the suggest button is pressed, if the currentplayer is not in
	 * a room, throws an error Jdialog message, if the player has already
	 * suggested this turn throws an error JDialog message
	 */
	public void makeSuggestion() {
		// player has to be in a room
		if (Cluedo.currentPlayer.room == null) {
			JOptionPane.showMessageDialog(canvas,
					"A player can only suggest inside a room",
					"Suggestion error", JOptionPane.ERROR_MESSAGE);
		} else {
			if (!suggested) {
				String message = Cluedo.currentPlayer.suggest();
				if (message == null) {
					return;
				}
				if (!message.isEmpty()) {
					suggested = true;
					JOptionPane.showMessageDialog(canvas,
							"you have been shown:" + message);
				}
			} else {
				JOptionPane.showMessageDialog(canvas,
						"You have already suggested this turn",
						"Suggestion Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Called when the current player presses the accused button A player can
	 * make an accusation anywhere on the board
	 */
	public void makeAccusation() {
		int bool = Cluedo.currentPlayer.accuse();
		if (bool == 0) {
			// the option has been cancelled
			return;
		} else if (bool == 1) {
			// the player has won
			JOptionPane.showMessageDialog(canvas, ""
					+ Cluedo.currentPlayer.player + " Has Won The GAME!!!");

			System.exit(0);
		} else if (bool == 2) {
			// the player has lost
			JOptionPane.showMessageDialog(canvas, Cluedo.currentPlayer.player
					+ " has incorrectly accused, "
					+ Cluedo.currentPlayer.player + " has been eliminated");
			Cluedo.currentPlayer.eliminated = true;
			if (Cluedo.allEliminated()) {
				JOptionPane.showMessageDialog(canvas,
						"No one has Won this game of Cluedo");
				System.exit(0);
			}
			endTurn();
		}
	}

	/**
	 * Moves the player if a key being pressed is either 'up' 'down' 'left'
	 * 'right'
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// open help message if stuck in room
		if (Cluedo.currentPlayer.room != null) {
			JOptionPane.showMessageDialog(canvas,
					"To leave the room, click on a door or secret passage");
			return;
		}
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
			moveSquare(Cluedo.currentPlayer.getPos().right);

		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
			moveSquare(Cluedo.currentPlayer.getPos().left);

		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_KP_DOWN) {
			moveSquare(Cluedo.currentPlayer.getPos().down);

		} else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_KP_UP) {
			moveSquare(Cluedo.currentPlayer.getPos().up);

		}
		canvas.repaint();
	}

	/**
	 * The player can only move if the Square it is wanting to move to is a
	 * hallway square, if not, then the player cannot move
	 */
	public void moveSquare(Square move) {
		if (move == null) {
			return;
		}
		if (!checkRolled()) {
			return;
		}
		if (walkable(move)) {
			numSteps -= 1;
			Cluedo.currentPlayer.setPos(move);
		} else if (isDoor(move)) {
			// move the player into the room
		}
	}

	/**
	 * Returns true if the square the player is trying to move to is either in
	 * the hall or start position squares
	 *
	 * @param s
	 * @return
	 */
	public boolean walkable(Square s) {
		if (s.getType() == Square.Type.Hallway
				|| s.getType() == Square.Type.StartPosition) {
			return true;
		}
		return false;
	}

	/**
	 * If the square a player has entered is a door, put that player randomly in
	 * the room
	 *
	 * @param s
	 * @return
	 */
	public boolean isDoor(Square s) {
		if (s.getType() == Square.Type.Door) {
			for (Room r : Cluedo.rooms) {
				for (Square door : r.doors) {
					if (s.equals(door)) {
						JOptionPane
								.showMessageDialog(
										canvas,
										"You just entered the "
												+ r.getName()
												+ "\n"
												+ " to leave the room, click on a door \n "
												+ " or secret passage");
						numSteps = 0;
						Cluedo.setRandomlyInRoom(Cluedo.currentPlayer, r);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * if the player is yet to roll the dice, bring up the appropriate JDialog
	 * box if the player has rolled and tries to roll again, bring up the
	 * appropriate JDialog box
	 *
	 * @return
	 */
	public boolean checkRolled() {
		if (numSteps == -1) {
			JOptionPane.showMessageDialog(canvas, "Roll the Dice!");
			return false;
		}
		if (numSteps == 0) {
			JOptionPane.showMessageDialog(canvas,
					"You have ran out of movements");
			return false;
		}
		return true;
	}

	/**
	 * Ends the current players turn
	 */
	public void endTurn() {
		numSteps = -1;
		Cluedo.endTurn();
		JOptionPane.showMessageDialog(canvas, "It is now "
				+ Cluedo.currentPlayer.player + "'s turn");
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
