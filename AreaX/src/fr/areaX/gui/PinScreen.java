package fr.areaX.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PinScreen extends StackPane {

	private VBox boxContainer = new VBox();

	private Label pinLabel = new Label("Insert your pass into the reader");
	private TextField codeField = new TextField();
	private Button okBtn = new Button("Enter");

	private XNode xstage = null;

	public void setXstage(XNode xstage) {
		this.xstage = xstage;
	}
	
	public PinScreen(){
		initLayout();
	}
	
	private void initLayout(){

		getChildren().add(boxContainer);
				
		
		boxContainer.setSpacing(10);
		boxContainer.setMaxWidth(300);
		boxContainer.setMaxHeight(100);
		boxContainer.setStyle("-fx-border-color: #212a34; -fx-border-width: 1; -fx-padding: 10px;");
		boxContainer.setAlignment(Pos.CENTER);

		pinLabel.setFont(new Font(15));
		
		pinLabel.setText("Enter your PIN :");

		boxContainer.getChildren().addAll(pinLabel, codeField, okBtn);
		
		
		okBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				if (codeField.getText().equals("1212")){
					xstage.onEvent(this.getClass().getSimpleName(),
							XNode.TO_CAMERA_SCREEN, null);
				}
			}
		});
	}
	
}
