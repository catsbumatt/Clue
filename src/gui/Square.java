package gui;
import java.awt.*;

/**
 * Represents a square on the canvas, can be part of a Hallway, Room,
 * Start Position, Door, and Secret Passage. A player has a square as
 * field to represent which square the player is standing on.
 * @author catsbumatt
 *
 */
public class Square {

	private int x;
	private int y;
	public enum Type{
		Hallway, Room, StartPosition, Door, SecretPassage, Block;
	};
	private Type type;

	public static final int size = 25;
	Color col;

	// neighbours
	public Square up, right, down, left;

	public Square(int x, int y, Type t, Color col){
		this.x = x;
		this.y = y;
		this.type = t;
		this.col = col;
	}


	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public Type getType(){
		return type;
	}

	/**
	 * if the x and y value are within the dimensions of this square, returns true
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isIn(int x, int y){
		if ((x >= this.x && y >= this.y) && (x <= this.x + size && y <= this.y + size)){
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		if (type != other.type)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}


	/**
	 * draw the square on the canvas
	 * @param g
	 */
	public void draw(Graphics g){
		g.setColor(col);
		g.fillRect(x, y, size, size);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, size, size);
	}

	public String toString(){
		return x + " " + y + " " + type;
	}

}
