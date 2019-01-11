/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*SokobanGame.java - Initialises a Level object and passes the object a parameter for which level number to load.
*/

public class SokobanGame {
    
	//Initialises the Level object
	Level currentLevel = new Level();
	//variable holding the current level number
	private int levelNumber = 1;
	
	//Constructor - sets the current level and calls the levelLoader method
	public SokobanGame(int level) {
		
		setLevel(level);
		levelLoader();
		}
	
	//Getter - returns the current level number
	protected int getLevel() {
		
		return levelNumber;
		}
	
	//Setter - sets the current level number
	protected void setLevel(int newLevel) {
		
		levelNumber = newLevel;
		}
	
	//Load level data 
	protected void levelLoader() {
		
		//Passes the current level number to importLevel to be imported from a text file 
		currentLevel.importLevel(levelNumber);
		//Decode imported level data & turn into objects 
		currentLevel.decode();
		}
	}