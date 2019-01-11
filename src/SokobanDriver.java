/*
*Jordan Laing - 15009237
*Created - 14/12/2018
*Last Updated - 10/01/2019
*Sokoban v5.0 
*SokobanDriver.java - Contains the main method used to launch the application.
*/

import javafx.application.Application; 
import javafx.stage.Stage;

public class SokobanDriver extends Application {
	   
    public static void main(String args[]) {
	
    	//Activates the JavaFX start method within the Level class
    	//main method serves as a backup when implementing JavaFX in case the start method fails to load
    	launch(Level.class,args);
    	}
    
	public void start(Stage primaryStage) throws Exception {
		
		//Activates the JavaFX start method within the Level class
		launch(Level.class);
		}
	}

