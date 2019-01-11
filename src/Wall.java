/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*Wall.java - Class for creating Wall objects. Extends MapElement.
*/

public class Wall extends MapElement  {
	
	//Constructor - set the objects x & y coordinates
	public Wall(int xLocation, int yLocation) {
		
		this.setX(xLocation);
		this.setY(yLocation);
		}
	}
