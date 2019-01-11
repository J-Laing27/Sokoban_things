/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*Crate.java - Class for creating Crate objects. Extends MapElement -
* - Implements movement & collisions detection methods from MovableMapObject interface.
*/

public class Crate extends MapElement implements MovableMapElement{

	//Constructor - set the objects x & y coordinates
	public Crate(int xLocation, int yLocation) {
		
		this.setX(xLocation);
		this.setY(yLocation);
		}
	
	//Setter - modifies the (x,y) location for movable objects
    public void setLocation(int x, int y) {
        
        int newX = getX() + x;
        int newY = getY() + y;
        setX(newX);
        setY(newY);
    	}
    
    //Checks if the object has collided with another object above it
    public boolean topCollision(MapElement collisionCheck) {
        
    	//Check if the objects potential new (x,y) position is equal to the parameter objects (x,y) position
    	//Return true if so, else return false
    	if(getY()-1 == collisionCheck.getY() && getX() == collisionCheck.getX()) {
    		
    		return true;
    		}
    	return false;
    	}

    //Checks if the object has collided with another object below it
    public boolean bottomCollision(MapElement collisionCheck) {
        
    	//Check if the objects potential new (x,y) position is equal to the parameter objects (x,y) position
    	//Return true if so, else return false
    	if(getY()+1 == collisionCheck.getY() && getX() == collisionCheck.getX()) {
    		
    		return true;
    		}
    	return false;
    	}
    
    //Checks if the object has collided with another object on its left
    public boolean leftCollision(MapElement collisionCheck) {
        
    	//Check if the objects potential new (x,y) position is equal to the parameter objects (x,y) position
    	//Return true if so, else return false
    	if(getX()-1 == collisionCheck.getX() && getY() == collisionCheck.getY()) {
    		
    		return true;
    		}
    	return false;
    	}

    //Checks if the object has collided with another object on its right
    public boolean rightCollision(MapElement collisionCheck) {
        
    	//Check if the objects potential new (x,y) position is equal to the parameter objects (x,y) position
    	//Return true if so, else return false
    	if(getX()+1 == collisionCheck.getX() && getY() == collisionCheck.getY()) {
    		
    		return true;
    		}
    	return false;
    	}
    }