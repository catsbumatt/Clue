package cards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class PersonCard implements Card{

	public String name;
	public Image card;

	public PersonCard(String name, Image card){
		this.name = name;
		this.card = card;
	}

	// for the guess class (Should not be drawn)
	public PersonCard(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Card c) {
		if (name.equals(c.getName())){
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		if (card == null){
			g.setColor(Color.RED);
			g.fillRect(x, y, 100, 130);
		}
		g.drawImage(card, x, y, null);
	}




}
