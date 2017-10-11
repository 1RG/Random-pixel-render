package lt.rg.rpr.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lt.rg.rpr.Main;

public class RootController {
	private Main main;
	
	@FXML
	private TextField imgResTFWidth;
	
	@FXML
	private TextField imgResTFHeight;
	
	@FXML
	private TextField vidResTFWidth;
		
	@FXML
	private TextField vidResTFHeight;
	
	@FXML
	private TextField vidFpsTF;
	
	@FXML
	private TextField vidLengthTF;
	
	@FXML
	public void buttonRImage() {
		System.out.println("Render Image");
		main.getRenderLogic().setImageWidth(Integer.parseInt(imgResTFWidth.getText()));
		main.getRenderLogic().setImageHeight(Integer.parseInt(imgResTFHeight.getText()));
		main.getRenderLogic().createImage();
	}
	
	@FXML
	public void buttonRVideo() {
		System.out.println("Render Video");
		main.getRenderLogic().setVideoWidth(Integer.parseInt(vidResTFWidth.getText()));
		main.getRenderLogic().setVideoHeight(Integer.parseInt(vidResTFHeight.getText()));
		main.getRenderLogic().setVideoFps(Integer.parseInt(vidFpsTF.getText()));
		main.getRenderLogic().setVideoLength_ms(Integer.parseInt(vidLengthTF.getText()));
		main.getRenderLogic().createVideo();
	}
	
	@FXML
	private void initialize(){		
		imgResTFWidth.textProperty().addListener(onlyIntFilter(imgResTFWidth));
		imgResTFHeight.textProperty().addListener(onlyIntFilter(imgResTFHeight));
		vidResTFWidth.textProperty().addListener(onlyIntFilter(vidResTFWidth));
		vidResTFHeight.textProperty().addListener(onlyIntFilter(vidResTFHeight));
		vidFpsTF.textProperty().addListener(onlyIntFilter(vidFpsTF));
		vidLengthTF.textProperty().addListener(onlyIntFilter(vidLengthTF));
	}
	
	private ChangeListener<String> onlyIntFilter(TextField field) {
		ChangeListener<String> changeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.length() != 0) {
					try {
						int i = Integer.parseInt(newValue);
						if(i < 1) {
							field.textProperty().set(oldValue);
						}else {

// fix first variable zero input
							if(!newValue.equals(i+"")) {
								Platform.runLater(() -> {
									field.textProperty().set(i+""); 
				            	});
							}
							
							if(field.getStyleClass().contains("error_feild")) {
								field.getStyleClass().remove("error_feild");
							}
						}
					} catch (Exception e) {
						field.textProperty().set(oldValue);
					}
				}else {
					if(!field.getStyleClass().contains("error_feild")) {
						field.getStyleClass().add("error_feild");
					}
				}
			}
		};
		
		return changeListener;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public void setImageData(int width, int height) {
		imgResTFWidth.setText(width+"");
		imgResTFHeight.setText(height+"");
	}
	
	public void setVideoData(int width, int height, int fps, int length) {
		vidResTFWidth.setText(width+"");
		vidResTFHeight.setText(height+"");
		vidFpsTF.setText(fps+"");
		vidLengthTF.setText(length+"");
	}
}
