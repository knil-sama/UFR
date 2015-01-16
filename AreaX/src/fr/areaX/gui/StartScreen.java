package fr.areaX.gui;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StartScreen extends StackPane implements XNode {

	private VBox boxContainer = new VBox();

	private final String initialMessage = "Initializing.. Please wait";
	private final String insertMessage = "Insert your card into the reader";
	private final String errorMessage = "Smart Card Terminal not connected";

	private final String className = this.getClass().getSimpleName();

	private Label startMessage = new Label(initialMessage);

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
		boxContainer.setMaxWidth(400);
		boxContainer.setMaxHeight(100);
		boxContainer.setStyle("-fx-border-color: #212a34; -fx-border-width: 1; -fx-padding: 10px;");
		boxContainer.setAlignment(Pos.CENTER);

		startMessage.setFont(new Font(15));

		startMessage.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (!startMessage.getText().equals(insertMessage))
					return;

			}
		});
	}

	@Override
	public void onEvent(String source, int eventType, Object args) {

		switch (eventType) {
		case XNode.SMART_CARD_TERMINAL_ERROR:
			startMessage.setText(errorMessage);
			break;
		case XNode.NO_SMART_CARD:
			startMessage.setText(insertMessage);
			break;
		case XNode.SMART_CARD_VERIFIED:
			startMessage.setText("Welcome, Do not remove the card");
			break;
		case XNode.SMART_CARD_REFUSED:
			startMessage.setText("Card Refused, please remove the card");
			break;
		case XNode.SMART_CARD_UPDATED:
			xstage.onEvent(className, XNode.TO_CAMERA_SCREEN, null);
			break;
		case XNode.INITIALISATION_ERROR:
			startMessage.setText("System initialisation failed, Code: IO-01");
			break;
		case XNode.SMART_CARD_IO_ERROR:
			startMessage.setText("Card Read/Write Failed, Code: IO-02");
			break;
		}
	}


}
