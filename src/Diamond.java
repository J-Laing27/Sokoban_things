/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*Diamond.java - Class for creating Diamond objects. Extends MapElement.
*/

public class Diamond extends MapElement  {

	//Variable for 
	private boolean hasCrate;
	
	//Constructor - set the objects x & y coordinates
	public Diamond(int xLocation, int yLocation) {
		
		this.setX(xLocation);
		this.setY(yLocation);
		}
		
	//Setter - Sets new value for hasCrate variable
	public void setHasCrate(boolean newHasCrate) {
		
		hasCrate = newHasCrate; 
		}
	
	//Getter - returns the current state of the hasCrate variable
	public boolean getHasCrate() {
		
		return hasCrate;
		}
	}
 