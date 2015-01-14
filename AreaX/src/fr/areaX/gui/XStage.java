package fr.areaX.gui;

import fr.areaX.controller.AreaX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class XStage extends Stage implements XNode {

	private String appName = "Area X";
	private Scene scene = null;
	private VBox root = new VBox();
	
	private BorderPane nav = new BorderPane();
	private StackPane panels = new StackPane();

	private HBox titleContainer = new HBox();
	private Label title = new Label(appName);
	
	public static final int START = 1;
	public static final int PIN = 2;
	public static final int CAMERA = 3;
	public static final int INVALID = 4;
	public static final int INSIDE = 5;
	
	private int appState = START;
	StartScreen startScreen = new StartScreen();
	PinScreen pinScreen = new PinScreen();
	CameraScreen cameraScreen = new CameraScreen();

	public XStage() {
		setTitle(appName);
		scene = new Scene(root, 800, 700);
		initRootPane();
		initEvents();
		initPane();
		this.setScene(scene);
		
		Thread th = (new Thread(new Runnable() {
			
			@Override
			public void run() {
				AreaX areaX = AreaX.getInstance();
				areaX.setStartScreenGUI(startScreen);
				areaX.initialise();
			}
		}));
		th.setDaemon(true);
		th.start();
	}
	
	public void displayDialogBox(String msg) {

	}
	
	private void initRootPane() {
		root.getChildren().addAll(nav, panels);
		root.setStyle("-fx-border-color: #212a34; -fx-border-width: 1;");

		
		nav.setLeft(titleContainer);
		nav.setStyle("-fx-background-color: #686d76; -fx-text-fill: #dcdde0;");
		nav.setPadding(new Insets(10, 10, 10, 10));
		nav.setMinHeight(50);
		
		titleContainer.getChildren().addAll(title);
		

		title.setFont(new Font("Impact", 30));
		title.setStyle("-fx-text-fill: #ffffff");
		title.setEffect(new DropShadow());
		
		panels.setMinHeight(500);
		panels.setMinWidth(500);
		//panels.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-base: lightgreen;");
		panels.setAlignment(Pos.CENTER);
		
		startScreen.setXstage(this);
		pinScreen.setXstage(this);
		cameraScreen.setXstage(this);
	}

	private void setPanel(Node node){
		if (panels.getChildren().size()>0)
			panels.getChildren().remove(0);

		panels.getChildren().add(node);
	}
	
	private void initPane() {
		
		switch(appState) {
			case START:
				setPanel(startScreen);
				break;
			case PIN:
				setPanel(pinScreen);
				break;				
			case CAMERA:
				cameraScreen.startDefaultCamera();
				setPanel(cameraScreen);
				break;
		}
		
	}
	
	private void initEvents() {
		
	}

	@Override
	public void onEvent(String source, int eventType, Object args) {
	
		switch(eventType){
		case XNode.TO_PIN_SCREEN:
			appState = PIN;
			initPane();
			break;
		case XNode.TO_CAMERA_SCREEN:
			appState = CAMERA;
			initPane();
			break;		
			
		}
	}

}
