package fr.areaX.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StartScreen extends StackPane {

	private VBox boxContainer = new VBox();
	
	private Label startMessage = new Label("Insert your pass into the reader");
	
	private XNode xstage = null;


	public void setXstage(XNode xstage) {
		this.xstage = xstage;
	}

	public StartScreen() {
		getRootPane();
	}
	
	public void getRootPane(){

		getChildren().add(boxContainer);
				
		boxContainer.getChildren().addAll(startMessage);
		
		boxContainer.setSpacing(10);
		boxContainer.setMaxWidth(300);
		boxContainer.setMaxHeight(100);
		boxContainer.setStyle("-fx-border-color: #212a34; -fx-border-width: 1; -fx-padding: 10px;");
		boxContainer.setAlignment(Pos.CENTER);

		startMessage.setFont(new Font(15));
		
		startMessage.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				xstage.onEvent(this.getClass().getSimpleName(), 
						XNode.TO_PIN_SCREEN, null);
			}
		});
	}

	
}
