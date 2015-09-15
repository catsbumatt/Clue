package guess;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import cluedo.Cluedo;
import cluedo.Room;
import cards.Card;
import cards.PersonCard;
import cards.RoomCard;
import cards.WeaponCard;

/**
 * Opens a JDialog which allows the user to enter either a suggestion or an
 * accusation. the user must pick a murderer, a murder weapon, and a murder
 * location or else it will bring up another instance of guess.
 * 
 * @author catsbumatt
 * 
 */
public class Guess {

	private List<Card> cards = new ArrayList<Card>();

	public PersonCard suspect;
	public WeaponCard weapon;
	public RoomCard room;
	boolean inRoom = false;

	boolean validGuess;
	boolean cancelled;

	public Guess() {

		// player cards
		cards.add(new PersonCard("Miss Scarlet"));
		cards.add(new PersonCard("Colonel Mustard"));
		cards.add(new PersonCard("Mrs White"));
		cards.add(new PersonCard("The Reverend Green"));
		cards.add(new PersonCard("Mrs Peacock"));
		cards.add(new PersonCard("Professor Plum"));
		// weapon cards
		cards.add(new WeaponCard("Candlestick"));
		cards.add(new WeaponCard("Dagger"));
		cards.add(new WeaponCard("Lead Pipe"));
		cards.add(new WeaponCard("Revolver"));
		cards.add(new WeaponCard("Rope"));
		cards.add(new WeaponCard("Spanner"));
		// room Cards
		cards.add(new RoomCard("Kitchen"));
		cards.add(new RoomCard("Ball Room"));
		cards.add(new RoomCard("Conservatory"));
		cards.add(new RoomCard("Billiard Room"));
		cards.add(new RoomCard("Library"));
		cards.add(new RoomCard("Study"));
		cards.add(new RoomCard("Hall"));
		cards.add(new RoomCard("Lounge"));
		cards.add(new RoomCard("Dining Room"));
		// ask for the three suggestions
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		ArrayList<JRadioButton> rbuttons = new ArrayList<JRadioButton>();
		for (Card c : cards) {
			rbuttons.add(new JRadioButton(c.getName()));
		}
		// if the person is in a room, select it
		for (Card c : cards) {
			if (c instanceof RoomCard) {
				// for (Room r : Cluedo.rooms) {
				Room r = Cluedo.currentPlayer.room;
				if (r != null) {
					if (r.getName().equals(c.getName())) {
						// get the radiobutton and select it
						for (JRadioButton jrb : rbuttons) {
							if (jrb.getText().equals(r.getName())) {
								jrb.setSelected(true);
								inRoom = true;
								room = (RoomCard) c;
							}
						}
					}
				}
			}
		}
		// remove the rooms from the radioButtons
		if (inRoom) {
			for (Card c : cards) {
				if (c instanceof RoomCard) {
					for (int i = rbuttons.size() - 1; i >= 0; i--) {
						if (rbuttons.get(i).getText().equals(c.getName())
								&& !c.getName().equals(room.getName())) {
							rbuttons.remove(rbuttons.get(i));
						}
					}
				}
			}
		}
		for (JRadioButton jrb : rbuttons) {
			panel.add(jrb);
		}

		int result = JOptionPane.showConfirmDialog(null, panel,
				"please choose a murderer, a weapon, and a murder room",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			validGuess = isEligable(rbuttons);

		} else {
			// cancelled so wants to leave
			validGuess = true;
			cancelled = true;
			return;
		}
	}

	/**
	 * check if what the user has selected is correct, if not; exit
	 * 
	 * @param rbuttons
	 * @param cards
	 */
	public boolean isEligable(List<JRadioButton> rbuttons) {
		int count = 0;
		for (JRadioButton jrb : rbuttons) {
			if (jrb.isSelected()) {
				Card c = getSuspected(jrb.getText());
				count++;
				if (c instanceof PersonCard) {
					suspect = (PersonCard) c;
				} else if (c instanceof WeaponCard) {
					weapon = (WeaponCard) c;
				} else if (c instanceof RoomCard) {
					room = (RoomCard) c;
				}
			}

		}
		// check if three options have been selected and check if are different
		// card types
		if (count != 3) {
			return false;
		}
		if (suspect == null || weapon == null || room == null) {
			return false;
		}
		return true;
	}

	/**
	 * returns a card that matches the given parameter
	 * @param suspect
	 * @return
	 */
	public Card getSuspected(String suspect) {
		for (Card c : cards) {
			if (c.getName().equals(suspect)) {
				return c;
			}
		}
		return null;
	}

	public boolean isValid() {
		return validGuess;
	}

	public boolean cancelled() {
		return cancelled;
	}
}