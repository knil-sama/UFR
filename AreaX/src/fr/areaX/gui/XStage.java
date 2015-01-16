package fr.areaX.gui;

import java.util.Date;

import fr.areaX.controller.AreaX;
import javafx.application.Platform;
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
	public static final int CREATE = 6;
	public static final int ASSOCIATE = 7;

	private int appState = START;
	StartScreen startScreen = new StartScreen();
	PinScreen pinScreen = new PinScreen();
	CameraScreen cameraScreen = new CameraScreen();
	AdminScreen createUserScreen = new AdminScreen();
	
	public XStage() {
		setTitle(appName);
		scene = new Scene(root, 800, 700);
		initRootPane();
		initEvents();
		initPane();
		this.setScene(scene);

		final XNode self = this;
		Thread th = (new Thread(new Runnable() {

			@Override
			public void run() {
				AreaX areaX = AreaX.getInstance();
				areaX.setStartScreenGUI(self);
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
		createUserScreen.setListener(this);
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
		case CREATE:
			createUserScreen.startDefaultCamera();
			setPanel(createUserScreen);
			break;
		}

	}

	private void initEvents() {

	}

	@Override
	public void onEvent(final String source, final int eventType,final Object args) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				switch(eventType){
				case XNode.TO_PIN_SCREEN:
					appState = PIN;
					initPane();
					break;
				case XNode.TO_CAMERA_SCREEN:
					appState = CAMERA;
					initPane();
					break;		
				case XNode.SMART_CARD_TERMINAL_ERROR:
				case XNode.NO_SMART_CARD:
				case XNode.SMART_CARD_VERIFIED:
				case XNode.SMART_CARD_REFUSED:
				case XNode.SMART_CARD_UPDATED:
				case XNode.INITIALISATION_ERROR:
				case XNode.SMART_CARD_IO_ERROR:
					if (appState != START)
						return;
					startScreen.onEvent(source, eventType, args);
					break;
				case XNode.IMAGE_PROCESSING_ERROR:
				case XNode.BIOMETRY_REJECTED:
				case XNode.BIOMETRY_ACCEPTED:
					cameraScreen.onEvent(source, eventType, args);
					cameraScreen.onEvent(source, XNode.SHOW_IMAGE, "image_snap2.jpg");
					break;
					
				case XNode.CREATE_NEW_USER:
					System.out.println("Creating new users with parameters");
					
					Object[] params = (Object[]) args;
					String firstname = (String) params[0];
					String lastname = (String) params[1];
					Date dateofbirth = (Date) params[2];
					String imgUrl = (String) params[3];
					
					AreaX.getInstance().createNewUser(
							firstname, lastname, dateofbirth, imgUrl);
					break;
				case XNode.NEW_USER_CREATED:
					System.out.println("New user created");
					createUserScreen.onEvent(source, eventType, args);
					break;
				}
			}
		});
	}

}
