/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*MapElement.java - Abstract superclass for all map elements. Provides location variables as well as getters/setters to access them.
*/

abstract class MapElement {

	//variables containing object X & Y map coordinates
	private int xCoordinate;
	private int yCoordinate;
	
	//Setter - set xCoordinate value
    public void setX(int newX) {
        
        this.xCoordinate = newX;
        }

    //Setter - set yCoordinate value
    public void setY(int newY) {
        
        this.yCoordinate = newY;
        }
    
    //Getter - return xCoordinate value
    public int getX() {
        
        return this.xCoordinate;
        }

    //Getter - return yCoordinate value
    public int getY() {
    	
    	return this.yCoordinate;
    	}
    }
