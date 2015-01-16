package fr.areaX.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

/**
 * Taken from 
 * @author Rakesh Bhatt (rakeshbhatt10)
 */
public class CameraNode extends StackPane {

	private class WebCamInfo {

		private String webCamName;
		private int webCamIndex;

		public String getWebCamName() {
			return webCamName;
		}

		public void setWebCamName(String webCamName) {
			this.webCamName = webCamName;
		}

		public int getWebCamIndex() {
			return webCamIndex;
		}

		public void setWebCamIndex(int webCamIndex) {
			this.webCamIndex = webCamIndex;
		}

		@Override
		public String toString() {
			return webCamName;
		}
	}

	private FlowPane bottomCameraControlPane;
	private FlowPane topPane;
	private BorderPane root;
	private String cameraListPromptText = "Choose Camera";
	private ImageView imgWebCamCapturedImage;
	private Webcam webCam = null;
	private boolean stopCamera = false;
	private boolean snapPhoto = false;
	private BufferedImage grabbedImage;
	private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
	private BorderPane webCamPane;
	private Button btnCamreaStop;
	private Button btnCamreaStart;
	private Button btnCameraDispose;
	private Button btnCameraSnap = new Button("Snap");
	
	private ComboBox<WebCamInfo> cameraOptions;
	private XNode listener;
	
	public CameraNode() {
		super();
		initLayout();
	}
	
	private void deleteImgFile(){
		File img = new File("image_person.jpg");
		if(img.exists()){
			img.delete();
		}
	}

	public void setEventListener(XNode listener) {
		this.listener = listener;
	}
	
	private void initLayout() {

		root = new BorderPane();
		topPane = new FlowPane();
		topPane.setAlignment(Pos.CENTER);
		topPane.setHgap(20);
		topPane.setOrientation(Orientation.HORIZONTAL);
		topPane.setPrefHeight(40);
		root.setTop(topPane);
		webCamPane = new BorderPane();
		webCamPane.setStyle("-fx-background-color: #ccc;");
		imgWebCamCapturedImage = new ImageView();
		webCamPane.setCenter(imgWebCamCapturedImage);
		root.setCenter(webCamPane);
		createTopPanel();
		
		VBox bottomContainer = new VBox();
		
		bottomCameraControlPane = new FlowPane();
		bottomCameraControlPane.setOrientation(Orientation.HORIZONTAL);
		bottomCameraControlPane.setAlignment(Pos.CENTER);
		bottomCameraControlPane.setHgap(20);
		bottomCameraControlPane.setVgap(10);
		bottomCameraControlPane.setPrefHeight(40);
		bottomCameraControlPane.setDisable(true);
		createCameraControls();
		
		bottomContainer.getChildren().addAll(btnCameraSnap, 
				bottomCameraControlPane);
		bottomContainer.setPadding(new Insets(10));
		bottomContainer.setAlignment(Pos.CENTER);
		
		root.setBottom(bottomContainer);

		getChildren().add(root);
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				setImageViewSize();
			}
		});
	}

	public void startDefaultCamera() {
		if (Webcam.getWebcams().size()>0) {
			cameraOptions.getSelectionModel().select(0);
		}
	}
	
	protected void setImageViewSize() {

		double height = webCamPane.getHeight();
		double width = webCamPane.getWidth();

		imgWebCamCapturedImage.setFitHeight(400);
		imgWebCamCapturedImage.setFitWidth(width);
		imgWebCamCapturedImage.prefHeight(400);
		imgWebCamCapturedImage.prefWidth(width);
		imgWebCamCapturedImage.setPreserveRatio(true);

	}

	private void createTopPanel() {

		int webCamCounter = 0;
		Label lbInfoLabel = new Label("Select a different camera");
		ObservableList<WebCamInfo> options = FXCollections.observableArrayList();

		topPane.getChildren().add(lbInfoLabel);

		for (Webcam webcam : Webcam.getWebcams()) {
			WebCamInfo webCamInfo = new WebCamInfo();
			webCamInfo.setWebCamIndex(webCamCounter);
			webCamInfo.setWebCamName(webcam.getName());
			options.add(webCamInfo);
			webCamCounter++;
		}

		cameraOptions = new ComboBox<WebCamInfo>();
		cameraOptions.setItems(options);
		cameraOptions.setPromptText(cameraListPromptText);
		cameraOptions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {

			@Override
			public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
				if (arg2 != null) {
					System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
					initializeWebCam(arg2.getWebCamIndex());
				}
			}
		});


		topPane.getChildren().add(cameraOptions);
	}

	protected void initializeWebCam(final int webCamIndex) {

		Task<Void> webCamTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				if (webCam != null) {
					disposeWebCamCamera();
				}

				webCam = Webcam.getWebcams().get(webCamIndex);
				webCam.setViewSize(new Dimension(320,240));
				webCam.open();
				startWebCamStream();

				return null;
			}
		};

		Thread webCamThread = new Thread(webCamTask);
		webCamThread.setDaemon(true);
		webCamThread.start();

		bottomCameraControlPane.setDisable(false);
		btnCamreaStart.setDisable(true);
	}

	protected void startWebCamStream() {

		stopCamera = false;
		snapPhoto = false;

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				while (!stopCamera) {
					try {
						if ((grabbedImage = webCam.getImage()) != null) {

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									Image mainiamge = SwingFXUtils.toFXImage(grabbedImage, null);
									imageProperty.set(mainiamge);
								}
							});
							if (snapPhoto){
								File output = new File("image_person.jpg");
								ImageIO.write(grabbedImage, "jpg", output);
								snapPhoto = false;
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										stopWebCamCamera();
									}
								});
							}
							
							grabbedImage.flush();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return null;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		imgWebCamCapturedImage.imageProperty().bind(imageProperty);

	}

	private void createCameraControls() {

		btnCamreaStop = new Button();
		btnCamreaStop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				stopWebCamCamera();
			}
		});
		btnCamreaStop.setText("Stop Camera");
		btnCamreaStart = new Button();
		btnCamreaStart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				startWebCamCamera();
			}
		});
		btnCamreaStart.setText("Start Camera");
		btnCameraDispose = new Button();
		btnCameraDispose.setText("Dispose Camera");
		btnCameraDispose.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				disposeWebCamCamera();
			}
		});
		bottomCameraControlPane.getChildren().add(btnCamreaStart);
		bottomCameraControlPane.getChildren().add(btnCamreaStop);
		bottomCameraControlPane.getChildren().add(btnCameraDispose);
		
		btnCameraSnap.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent evnt) {
				snapPhoto = true;
				btnCameraSnap.setText("Verifying..");
				btnCameraSnap.setDisable(true);
			}
		
		});
		
	}

	protected void disposeWebCamCamera() {
		stopCamera = true;
		webCam.close();
		btnCamreaStart.setDisable(true);
		btnCamreaStop.setDisable(true);
	}

	protected void startWebCamCamera() {
		stopCamera = false;
		startWebCamStream();
		btnCamreaStop.setDisable(false);
		btnCamreaStart.setDisable(true);
		btnCameraSnap.setText("Snap");
		btnCameraSnap.setDisable(false);
		deleteImgFile();
		
	}

	protected void stopWebCamCamera() {
		stopCamera = true;
		btnCamreaStart.setDisable(false);
		btnCamreaStop.setDisable(true);
	}

}
