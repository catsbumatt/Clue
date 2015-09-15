package cards;

import java.awt.Graphics;

/**
 * Card Interface that is implemented by the PersonCard, RoomCard, and
 * WeaponCard classes
 * 
 * @author catsbumatt
 * 
 */

public interface Card {

	/**
	 * returns the name of the card
	 * 
	 * @return
	 */
	public String getName();

	public boolean equals(Card c);

	/**
	 * draws the card on the screen given a graphics object, and an x and y
	 * position on the canvas
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void draw(Graphics g, int x, int y);

}
