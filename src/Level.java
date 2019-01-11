/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*Level.java - Imports and interprets level information from text files. Also implements JavaFX elements.
*/

import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


public class Level extends Application {

	//Holds level information imported from text file
	private ArrayList<String> mapData;
	//Holds level objects once they have been translated & instantiated
	private ArrayList<MapElement> mapObjects;
	//Holds Images of level objects to be displayed
	private ArrayList<ImageView> mapImages;
	//Player character
	private WarehouseKeeper player;
	//Holds all instances of Wall objects
	private ArrayList<Wall> wallBlocks;
	//Holds all instances of Tile objects
	private ArrayList<Tile> floorTiles;
	//Holds all instances of Diamond objects
	private ArrayList<Diamond> diamondTiles;
	//Holds all instances of Crate objects
	private ArrayList<Crate> crates;
	//Tracks number of valid moves the player has made
	private int numberOfMoves;
	//How many objects wide the level is
	private int levelWidth;
	//How many objects tall the level is
	private int levelHeight;
	//Holds image file for Crate objects when they are not on a diamond tile
	private Image crate;
	//Holds image file for Crate objects when they are on a diamond tile
	private Image crateInPlace;
	//Holds image file for Diamond objects
	private Image diamond;
	//Holds image file for Tile objects
	private Image floor;
	//Holds image file for Wall objects
	private Image wall;
	//Holds image file for the WarehouseKeeper object
	private Image sokoban;
	//Freezes game logic once a level has been completed
	private boolean gameRunning;
	//Determines header height within the JavaFX window
	private int headerBuffer = 25;
    
    public Level(){
    	
    	//Constructor - set default values for all variables
    	mapData = new ArrayList<String>();
    	mapObjects = new ArrayList<>();
    	mapImages = new ArrayList<>();
    	wallBlocks = new ArrayList<>();
    	floorTiles = new ArrayList<>();
    	diamondTiles = new ArrayList<>();
    	crates = new ArrayList<>();
    	numberOfMoves = 0;
    	levelWidth = 0;
    	levelHeight = 0;
        crate = new Image("file:img/Crate.png");
        crateInPlace = new Image("file:img/CrateInPlace.png");
        diamond = new Image("file:img/Diamond_Edit.png");
        floor = new Image("file:img/Floor.png");
        wall = new Image("file:img/Wall.png");
        sokoban = new Image("file:img/WarehouseKeeper.png");
        gameRunning = true;
    }
	
	//Import map information from text file
 	protected void importLevel(int levelNumber) {
		
 		/*
 		 * fileScanner searches for text file within the level folder.
 		 * If a file is found the scanner will parse through the file &
 		 * print each line into the mapData ArrayList.
 		 * 
 		 * Once the import is complete the levelWidth & levelHeight variables are set
 		 */
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(new FileReader("level/level"+levelNumber+".txt"));
			while (fileScanner.hasNext()){
				mapData.add(fileScanner.nextLine());
			} for(String str:mapData)
				System.out.println(str);
			fileScanner.close();
			} catch (FileNotFoundException e) {
				System.out.println("Sorry! Unable to find file 'level/level"+levelNumber+".txt'");
				System.out.println("Exiting Application...");
				Runtime.getRuntime().exit(0);
				
				}
		levelWidth = mapData.get(0).length();
		levelHeight = mapData.size();
		}
 	
 	//Translates the imported map information into objects
	
 	protected void decode() {
 		
 		/* 'for' loops parse through each character stored in the mapData ArrayList
 		 * and run each character through a switch case to generate an object.
 		 * Objects (except the WarehouseKeeper) are added to ArrayLists for 
 		 * each object type and then all objects are added to the mapObjects
 		 * ArrayList with movable objects being imported last. 
 		 */
 		int gridColumn=0;
 		int gridRow=0;
 		
 		/* As each element stored in the mapData ArrayList is a full text string and not a single character,
 		 * two 'for' loops (one nested inside the other) are used to parse through all the data.
 		 * The first loop tells the application which element of the ArrayList to look at and the 
 		 * second loop tells the application which character within the String to look at.
 		 * 
 		 */
 		for (int height=0; height<levelHeight; height++) {
        	
 			gridRow=height;
        	
        	for (int width=0; width<levelWidth; width++) {
        		
        		gridColumn=width;
        		
        		char key = mapData.get(height).charAt(width);
        		
        		switch (key) {

                case 'X':
                	Wall wallInstance = new Wall(gridColumn, gridRow);
                    wallBlocks.add(wallInstance);
                    break;

                case '*':
                	Crate crateInstance = new Crate(gridColumn, gridRow);
                    crates.add(crateInstance);
                    break;

                case '.':
                	Diamond diamondInstance = new Diamond(gridColumn, gridRow);
                    diamondTiles.add(diamondInstance);
                    break;

                case '@':
                	player = new WarehouseKeeper(gridColumn, gridRow);
                    break;

                case ' ':
                	Tile tileInstance = new Tile(gridColumn, gridRow);
                	floorTiles.add(tileInstance);
                    break;

                default:
                	System.out.println("Unknown symbol detected! Please review level file.");
                	System.out.println("Exiting Application...");
    				Runtime.getRuntime().exit(0);
                    break;
                    }
        		}
        	}
		  
		  mapObjects.addAll(wallBlocks);
		  mapObjects.addAll(floorTiles);
		  mapObjects.addAll(diamondTiles);
		  mapObjects.addAll(crates);
		  mapObjects.add(player);
		  loadMap();
		  }
	
 	//Getter - returns the current number of valid moves the player has made.
 	private int getNumberOfMoves() {
		
		return numberOfMoves;
	}
	
 	//Check how many diamond tiles have crates on them
 	private void checkCompletedTiles() {
	
 		/* Nested 'for' loops parse through all objects in the diamondTiles and
 		 * crates ArrayLists to check if any objects have match (x,y) coordinates.
 		 * If a match is found the current diamondTile object has its 'hasCtare'
 		 * variable set to true. 
 		 */
		for(int diamondI=0; diamondI<diamondTiles.size(); diamondI++) {
			
			Diamond diamondCheck = diamondTiles.get(diamondI);
			diamondCheck.setHasCrate(false);
			
			for(int crateI=0; crateI<crates.size(); crateI++) {
				
				Crate crateCheck = crates.get(crateI);
				
				if(diamondCheck.getX() == crateCheck.getX() && diamondCheck.getY() == crateCheck.getY()) {
				
					diamondCheck.setHasCrate(true);
				}
			}
		}
	}
	
 	//Check if all crates have been moved to a diamond tile
	private boolean checkLevelComplete() {
		
		int completedTiles = 0;
		
		for(int i=0; i<diamondTiles.size(); i++) {
			
    		Diamond tile = diamondTiles.get(i);
    		if (tile.getHasCrate()) {
    			
    			completedTiles++;
    			}
    		}
		if(completedTiles == diamondTiles.size()) {
			
			return true;
		}
		return false;
	}
	
	//Checks if a crate is on a diamond tile to determine which image to load
	private Image crateCheck(MapElement crateCheck) {
		
		/* 
		 * 
		 */
		for(int diamondI=0; diamondI<diamondTiles.size(); diamondI++) {
			
			Diamond diamondCheck = diamondTiles.get(diamondI);
			if(diamondCheck.getX() == crateCheck.getX() && diamondCheck.getY() == crateCheck.getY()) {
				
					return crateInPlace;
					}
			}
		return crate;
		}
	
	private void loadMap() {
		
		for(int i = 0; i<mapObjects.size(); i++) {
			
			MapElement tile = mapObjects.get(i);
			
			if(mapImages.size()<0) {
				
				mapImages.clear();
				}
			if (tile instanceof WarehouseKeeper) {
				
				mapImages.add(new ImageView(sokoban));
				mapImages.get(i).setX(mapObjects.get(i).getX()*32);
				mapImages.get(i).setY(mapObjects.get(i).getY()*32+headerBuffer);
				
			} else if(tile instanceof Wall) {
				
				mapImages.add(new ImageView(wall));
				mapImages.get(i).setX(mapObjects.get(i).getX()*32);
				mapImages.get(i).setY(mapObjects.get(i).getY()*32+headerBuffer);
				
			} else if(tile instanceof Tile) {
				
				mapImages.add(new ImageView(floor));
				mapImages.get(i).setX(mapObjects.get(i).getX()*32);
				mapImages.get(i).setY(mapObjects.get(i).getY()*32+headerBuffer);
				
			} else if(tile instanceof Diamond) {
				
				mapImages.add(new ImageView(diamond));
				mapImages.get(i).setX(mapObjects.get(i).getX()*32);
				mapImages.get(i).setY(mapObjects.get(i).getY()*32+headerBuffer);
				
			} else if(tile instanceof Crate) {
				
				mapImages.add(new ImageView(crate));
				mapImages.get(i).setImage(crateCheck(tile));
				mapImages.get(i).setX(mapObjects.get(i).getX()*32);
				mapImages.get(i).setY(mapObjects.get(i).getY()*32+headerBuffer);
				}
			}
		}
	
    private String checkCollisionType(MovableMapElement movingItem, String direction) {

        switch (direction) {
        
        case "UP":
        	
        	for (int wallI = 0; wallI < wallBlocks.size();wallI++) {
        		
        		Wall wall = wallBlocks.get(wallI);
        		if (movingItem.topCollision(wall)) {
        			
        			return "Blocked";
        			}
        		}
        	
        	if(movingItem instanceof WarehouseKeeper) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.topCollision(crate)) {
        				
        				if(checkCollisionType(crate,direction)=="None") {
        					
        					crate.setLocation(0, -1);
        					checkCompletedTiles();
        					return "Crate";
        					} else if(checkCollisionType(crate,direction)=="Blocked") {
        						
        						return "Blocked";
        						}
        				}
        			}
        		return "None";
        		}
        	
        	if(movingItem instanceof Crate) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.topCollision(crate)) {
        				
        				return "Blocked";
        				}
        			}
        		return "None";
        		}
        	
        case "DOWN":
        	
        	for (int wallI = 0; wallI < wallBlocks.size();wallI++) {
        		
        		Wall wall = wallBlocks.get(wallI);
        		if (movingItem.bottomCollision(wall)) {
        			
        			return "Blocked";
        			}
        		}
        	
        	if(movingItem instanceof WarehouseKeeper) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.bottomCollision(crate)) {
        				
        				if(checkCollisionType(crate,direction)=="None") {
        					
        					crate.setLocation(0, 1);
        					checkCompletedTiles();
        					return "Crate";
        					} else if(checkCollisionType(crate,direction)=="Blocked") {
        						
        						return "Blocked";
        						}
        				}
        			}
        		return "None";
        		}
        	
        	if(movingItem instanceof Crate) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.bottomCollision(crate)) {
        				
        				return "Blocked";
        				}
        			}
        		return "None";
        		}
        	
        case "LEFT":
        	
        	for (int wallI = 0; wallI < wallBlocks.size();wallI++) {
        		
        		Wall wall = wallBlocks.get(wallI);
        		if (movingItem.leftCollision(wall)) {
        			
        			return "Blocked";
        			}
        		}
        	
        	if(movingItem instanceof WarehouseKeeper) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.leftCollision(crate)) {
        				
        				if(checkCollisionType(crate,direction)=="None") {
        					
        					crate.setLocation(-1, 0);
        					checkCompletedTiles();
        					return "Crate";
        					} else if(checkCollisionType(crate,direction)=="Blocked") {
        						
        						return "Blocked";
        						}
        				}
        			}
        		return "None";
        		}
        	
        	if(movingItem instanceof Crate) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.leftCollision(crate)) {
        				
        				return "Blocked";
        				}
        			}
        		return "None";
        		}
        	
        case "RIGHT":                
        	
        	for (int wallI = 0; wallI < wallBlocks.size();wallI++) {
        		
        		Wall wall = wallBlocks.get(wallI);
        		if (movingItem.rightCollision(wall)) {
        			
        			return "Blocked";
        			}
        		}
        	
        	if(movingItem instanceof WarehouseKeeper) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.rightCollision(crate)) {
        				
        				if(checkCollisionType(crate,direction)=="None") {
        					
        					crate.setLocation(1, 0);
        					checkCompletedTiles();
        					return "Crate";
        					} else if(checkCollisionType(crate,direction)=="Blocked") {
        						
        						return "Blocked";
        						}
        				}
        			}
        		return "None";
        		}
        	
        	if(movingItem instanceof Crate) {
        		
        		for (int crateI = 0; crateI < crates.size();crateI++) {
        			
        			Crate crate = crates.get(crateI);
        			if (movingItem.rightCollision(crate)) {
        				
        				return "Blocked";
        				}
        			}
        		return "None";
        		}
        	}
        return "None";
        }
    
    private void checkKeyInput(KeyCode userKey) {
    	
    	String movementDir=userKey.toString();
    	switch (userKey) {
    	
    	case UP:    
    		
    		if(checkCollisionType(player,movementDir)=="Blocked") {
    			
    			break;
    			}
    		player.setLocation(0, -1);
    		numberOfMoves++;
    		break;
        	
        case DOWN:   
        	
        	if(checkCollisionType(player,movementDir)=="Blocked") {
        		
        		break;
        		}
        	player.setLocation(0, 1);
        	numberOfMoves++;
        	break;
        	
        case LEFT:
        	
        	if(checkCollisionType(player,movementDir)=="Blocked") {
        		
        		break;
        	}
        	player.setLocation(-1, 0);
        	numberOfMoves++;
        	break;
        	
        case RIGHT:
        	if(checkCollisionType(player,movementDir)=="Blocked") {
        		
        		break;
        	}
        	player.setLocation(1, 0);
        	numberOfMoves++;
        	break;
        	
        default:
        	break;
        	}
    	checkLevelComplete();
    	System.out.println("number of moves: "+numberOfMoves);
    	}
    
    private void clearLevel() {
    	
    	mapData.clear();
    	mapObjects.clear();
    	mapImages.clear();
    	wallBlocks.clear();
    	floorTiles.clear();
    	diamondTiles.clear();
    	crates.clear();
    	numberOfMoves = 0;
    	levelWidth = 0;
    	levelHeight = 0;
        gameRunning = true;
    }
        
    @Override
     public void start(Stage primaryStage) throws Exception {	
    	    	
    	SokobanGame sokoGame = new SokobanGame(6);
    	Group root = new Group();
    	Scene scene = new Scene(root);
    	MenuBar menuBar = new MenuBar();
    	Text playerMoves = new Text(160, 18, "Moves Taken :"+sokoGame.currentLevel.getNumberOfMoves());
    	
    	Menu levelSelect = new Menu("Level Select");
    	MenuItem level1 = new MenuItem("Level 1");
    	MenuItem level2 = new MenuItem("Level 2");
    	MenuItem level3 = new MenuItem("Level 3");
    	MenuItem level4 = new MenuItem("Level 4");
    	MenuItem level5 = new MenuItem("Level 5");
    	
    	Menu options = new Menu("Options");
    	MenuItem restartLevel = new MenuItem("Restart Level");
    	MenuItem exitGame = new MenuItem("Exit");
    	
    	levelSelect.getItems().add(level1);
    	levelSelect.getItems().add(level2);
    	levelSelect.getItems().add(level3);
    	levelSelect.getItems().add(level4);
    	levelSelect.getItems().add(level5);
    	options.getItems().add(restartLevel);
    	options.getItems().add(exitGame);
    	menuBar.getMenus().add(levelSelect);
    	menuBar.getMenus().add(options);
    	playerMoves.setFont(Font.font("Arial"));;
    	
    	level1.setOnAction(e -> {
    		
    		sokoGame.currentLevel.clearLevel();
    		sokoGame.setLevel(1);
    		sokoGame.levelLoader();
    		root.getChildren().clear();
    		for(int i = 0; i<sokoGame.currentLevel.mapImages.size(); i++) {
    			
    			root.getChildren().add(sokoGame.currentLevel.mapImages.get(i));
    			}
    		root.getChildren().add(menuBar);
    		root.getChildren().add(playerMoves);
    		primaryStage.setHeight(sokoGame.currentLevel.levelHeight*32+32+headerBuffer);
        	primaryStage.setWidth(sokoGame.currentLevel.levelWidth*32+5);
        	}
    	);
    
    	level2.setOnAction(e -> {
    		
    		sokoGame.currentLevel.clearLevel();
    		sokoGame.setLevel(2);
    		sokoGame.levelLoader();
    		root.getChildren().clear();
    		for(int i = 0; i<sokoGame.currentLevel.mapImages.size(); i++) {
    			
    			root.getChildren().add(sokoGame.currentLevel.mapImages.get(i));
    			}
    		root.getChildren().add(menuBar);
    		root.getChildren().add(playerMoves);
    		primaryStage.setHeight(sokoGame.currentLevel.levelHeight*32+32+headerBuffer);
    		primaryStage.setWidth(sokoGame.currentLevel.levelWidth*32+5);
    		}
    	);
    
    	level3.setOnAction(e -> {
    		
    		sokoGame.currentLevel.clearLevel();
    		sokoGame.setLevel(3);
    		sokoGame.levelLoader();
    		root.getChildren().clear();
    		for(int i = 0; i<sokoGame.currentLevel.mapImages.size(); i++) {
        
    			root.getChildren().add(sokoGame.currentLevel.mapImages.get(i));
    			}
    		root.getChildren().add(menuBar);
    		root.getChildren().add(playerMoves);
    		primaryStage.setHeight(sokoGame.currentLevel.levelHeight*32+32+headerBuffer);
    		primaryStage.setWidth(sokoGame.currentLevel.levelWidth*32+5);
    		}
    	);
    
    	level4.setOnAction(e -> {
    		
    		sokoGame.currentLevel.clearLevel();
    		sokoGame.setLevel(4);
    		sokoGame.levelLoader();
    		root.getChildren().clear();
    		for(int i = 0; i<sokoGame.currentLevel.mapImages.size(); i++) {
        
    			root.getChildren().add(sokoGame.currentLevel.mapImages.get(i));
    			}
    		root.getChildren().add(menuBar);
    		root.getChildren().add(playerMoves);
    		primaryStage.setHeight(sokoGame.currentLevel.levelHeight*32+32+headerBuffer);
    		primaryStage.setWidth(sokoGame.currentLevel.levelWidth*32+5);
    		}
    	);
    
    	level5.setOnAction(e -> {
    		
    		sokoGame.currentLevel.clearLevel();
    		sokoGame.setLevel(5);
    		sokoGame.levelLoader();
    		root.getChildren().clear();
    		for(int i = 0; i<sokoGame.currentLevel.mapImages.size(); i++) {
    			
    			root.getChildren().add(sokoGame.currentLevel.mapImages.get(i));
    			}
    		root.getChildren().add(menuBar);
    		root.getChildren().add(playerMoves);
    		primaryStage.setHeight(sokoGame.currentLevel.levelHeight*32+32+headerBuffer);
    		primaryStage.setWidth(sokoGame.currentLevel.levelWidth*32+5);
    		}
    	);

    	restartLevel.setOnAction(e -> {
    		
    		sokoGame.currentLevel.clearLevel();
    		sokoGame.levelLoader();
    		root.getChildren().clear();
    		for(int i = 0; i<sokoGame.currentLevel.mapImages.size(); i++) {
    			
    			root.getChildren().add(sokoGame.currentLevel.mapImages.get(i));
    			}
    		root.getChildren().add(menuBar);
    		root.getChildren().add(playerMoves);
    		}
    	);
    	
    	exitGame.setOnAction(e -> {
    		
    		Platform.exit();
    		}
    	);
    	
    	for(int i = 0; i<sokoGame.currentLevel.mapImages.size(); i++) {
    		
    		root.getChildren().add(sokoGame.currentLevel.mapImages.get(i));
    		}
    	
    	root.getChildren().add(menuBar);
    	root.getChildren().add(playerMoves);
    	
    	primaryStage.setTitle("Sokoban");
    	primaryStage.setScene(scene); 
    	primaryStage.sizeToScene(); 
    	primaryStage.show(); 
    	
    	scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
    		
    		@Override
    		public void handle(KeyEvent event) {
    			
    			if(sokoGame.currentLevel.gameRunning) {
    				
    				sokoGame.currentLevel.checkKeyInput(event.getCode());
    				sokoGame.currentLevel.loadMap();
    				playerMoves.setText("Moves Taken :"+sokoGame.currentLevel.getNumberOfMoves());
    				if(sokoGame.currentLevel.checkLevelComplete()) {
    					
    					sokoGame.currentLevel.gameRunning = false;
    					}
    				}
    			}
    		}
    	);
    	}
    }