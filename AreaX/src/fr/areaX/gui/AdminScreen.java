package fr.areaX.gui;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AdminScreen extends VBox implements XNode {

	private final String className = this.getClass().getSimpleName();
	
	StackPane boss = new StackPane();
	VBox rootPane = new VBox();
	
	HBox userform = new HBox();
	private Label fnLabel = new Label("Firstname");
	private TextField fnField = new TextField();
	private Label lnLabel = new Label("Lastname");
	private TextField lnField = new TextField();
	private Label dobLabel = new Label("Date of birth");
	private TextField dobField = new TextField();
	
	private BorderPane nav = new BorderPane();
	private HBox titleContainer = new HBox();
	private String appName = "Area X - Admin";
	private Label title = new Label(appName);

	
	private CameraNode cameraNode = new CameraNode();

	private Button createUserBtn = new Button("Create User");
	
	private Label status = new Label("Status");
	private XNode listener;
	
	public AdminScreen() {
		initlayout();
		cameraNode.setEventListener(this);
	}
	
	public void startDefaultCamera(){
		cameraNode.startDefaultCamera();
		deleteImgFile();
	}
	
	public void setListener(XNode listener){
		this.listener = listener;
	}
	
	private void initlayout() {
		
		getChildren().addAll(nav, boss);
		
		nav.setLeft(titleContainer);
		nav.setStyle("-fx-background-color: #686d76; -fx-text-fill: #dcdde0;");
		nav.setPadding(new Insets(10, 10, 10, 10));
		nav.setMinHeight(50);
		titleContainer.getChildren().addAll(title);
		
		userform.getChildren().addAll(fnLabel,fnField, lnLabel, 
				lnField, dobLabel, dobField);
		
		title.setFont(new Font("Impact", 30));
		title.setStyle("-fx-text-fill: #ffffff");
		title.setEffect(new DropShadow());
		
		rootPane.getChildren().addAll(userform, cameraNode,
				createUserBtn, status);
		
		boss.getChildren().addAll(rootPane);
		
		
		// style
		rootPane.setSpacing(5);
		userform.setSpacing(5);
		userform.setAlignment(Pos.CENTER);
		rootPane.setAlignment(Pos.CENTER);
		boss.setPadding(new Insets(20));
		
		createUserBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				createUserBtn.setText("Creating ..");
				createUserBtn.setDisable(true);
				status.setText("Checking data ..");
				
				String firstname = fnField.getText();
				String lastname = lnField.getText();
				String dob = dobField.getText();
				String error = "";
				
				SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat ddmmyyyy = new SimpleDateFormat("dd-MM-yyyy");

				Date dateofbirth = null;
				try {
					dateofbirth = yyyymmdd.parse(dob);
				} catch (ParseException e) {
					e.printStackTrace();
					try {
						dateofbirth = ddmmyyyy.parse(dob);
					} catch (ParseException ex) {
						ex.printStackTrace();
					}
				}
				
				if (dateofbirth == null){
					error += "[Date invalid]";
				}
				if (firstname == null || firstname.isEmpty()){
					error += "[Firstname missing]";
				}
				if (lastname ==null || lastname.isEmpty()){
					error += "[Lastname missing]";
				}
				
				File imgFile = new File("image_person.jpg");
				if (!imgFile.exists()){
					error += "[Image not snapped]";
				}
				
				if (!error.isEmpty()) {
					status.setText(error);
					createUserBtn.setDisable(false);
					createUserBtn.setText("Create User");
					return;
				}
				
				Object[] createUserParams = new Object[4];
				createUserParams[0] = firstname;
				createUserParams[1] = lastname;
				createUserParams[2] = dateofbirth;
				createUserParams[3] = "image_person.jpg";

				status.setText("Inserting into database..");
				
				listener.onEvent(className, XNode.CREATE_NEW_USER, createUserParams);
			}
		});
	}
	
	private void deleteImgFile(){
		File img = new File("image_person.jpg");
		if(img.exists()){
			img.delete();
		}
	}

	@Override
	public void onEvent(String source, int eventType, Object args) {
		switch (eventType) {
		case XNode.NEW_USER_CREATED:
			status.setText("New User Created");
			fnField.setText("");
			lnField.setText("");
			dobField.setText("");
			deleteImgFile();
			createUserBtn.setText("Create User");
			createUserBtn.setDisable(false);
			cameraNode.startWebCamCamera();
			break;

		default:
			break;
		}
	}
	
	
}
