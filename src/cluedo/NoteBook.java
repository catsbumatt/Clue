package cluedo;
import java.util.*;

import cards.*;

/**
 * This class represents the notebook that every single player has. the notebook
 * should be automatically updated but this is done in other classes with the 
 * appropriate information
 * @author CatsbuMatt
 *
 */
public class NoteBook {
	
	// suspects
	private List<PersonCard> suspects = new ArrayList<PersonCard>();
	// weapons
	private List<WeaponCard> weapons = new ArrayList<WeaponCard>();
	// rooms
	private List<RoomCard> rooms = new ArrayList<RoomCard>();
	
	public NoteBook(){		
	}
	
	
	/**
	 * Given an instance of card, checks whether that card is a personCard,
	 * WeaponCard or RoomCard and adds it to the appropriate fields
	 * @param c any card
	 */
	public void add(Card c){
		if (c instanceof PersonCard){
			suspects.add((PersonCard) c);
		}
		else if (c instanceof WeaponCard){
			weapons.add((WeaponCard) c);
		}
		else if (c instanceof RoomCard){
			rooms.add((RoomCard) c);
		}
	}
	
	/**
	 * Given an instance of card, checks whether that card is a personCard,
	 * WeaponCard or RoomCard and removes it from the appropriate fields
	 * @param c any card
	 */
	public void remove(Card c){
		if (c instanceof PersonCard){
			suspects.remove((PersonCard) c);
		}
		else if (c instanceof WeaponCard){
			weapons.remove((WeaponCard) c);
		}
		else if (c instanceof RoomCard){
			rooms.remove((RoomCard) c);
		}
	}
	
	public List<PersonCard> getSuspects() {
		return suspects;
	}
	
	public List<WeaponCard> getWeapons() {
		return weapons;
	}
	
	public List<RoomCard> getRooms() {
		return rooms;
	}	
	
}
