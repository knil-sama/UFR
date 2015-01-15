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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminScreen extends StackPane implements XNode {

	
	VBox rootPane = new VBox();
	
	HBox userform = new HBox();
	private Label fnLabel = new Label("Firstname");
	private TextField fnField = new TextField();
	private Label lnLabel = new Label("Lastname");
	private TextField lnField = new TextField();
	private Label dobLabel = new Label("Date of birth");
	private TextField dobField = new TextField();
	
	
	private CameraNode cameraNode = new CameraNode();

	private Button createUserBtn = new Button("Create User");
	
	private Label status = new Label("Status");
	
	public AdminScreen() {
		initlayout();
		
		cameraNode.setEventListener(this);
		cameraNode.startDefaultCamera();
		deleteImgFile();
	}
	
	private void initlayout() {
		
		userform.getChildren().addAll(fnLabel,fnField, lnLabel, 
				lnField, dobLabel, dobField);
		
		rootPane.getChildren().addAll(userform, cameraNode,
				createUserBtn, status);
		
		getChildren().addAll(rootPane);
		
		
		// style
		rootPane.setSpacing(5);
		userform.setSpacing(5);
		userform.setAlignment(Pos.CENTER);
		rootPane.setAlignment(Pos.CENTER);
		setPadding(new Insets(20));
		
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
		
	}
	
	
}
