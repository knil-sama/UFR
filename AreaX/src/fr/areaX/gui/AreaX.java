package fr.areaX.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class AreaX extends Application {

	   public static void main(String[] args) {
	        launch(args);
	    }
	    
	    @Override
	    public void start(Stage primaryStage) {
	    	primaryStage = new XStage();
	    	primaryStage.show();
	    }

	
}
