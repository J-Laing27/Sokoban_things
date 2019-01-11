/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*MovableMapElement.java - Abstract interface to hold required methods for movable objects. 
*/

abstract interface MovableMapElement {

	//Setter - modifies the (x,y) location for movable objects
    void setLocation(int x, int y);
    
    //Checks if the object has collided with another object above it
    boolean topCollision(MapElement collisionCheck);
  
    //Checks if the object has collided with another object below it
    boolean bottomCollision(MapElement collisionCheck);
    
    //Checks if the object has collided with another object on its left
    boolean leftCollision(MapElement collisionCheck);

    //Checks if the object has collided with another object on its right
    boolean rightCollision(MapElement collisionCheck);
    }
