package lt.rg.rpr.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lt.rg.rpr.Main;

public class RootController {
	private Main main;
	
	@FXML
	private TextField imgResTFWidth;
	
	@FXML
	private TextField imgResTFHeight;
	
	@FXML
	private Label imgStaL;
	
	@FXML
	private Button imgCancelB;
	
	@FXML
	private Button imgRenderB;
	
	@FXML
	private TextField vidResTFWidth;
		
	@FXML
	private TextField vidResTFHeight;
	
	@FXML
	private TextField vidFpsTF;
	
	@FXML
	private TextField vidLengthTF;
	
	@FXML
	private Label vidStaL;
	
	@FXML
	private Button vidCancelB;
	
	@FXML
	private Button vidRenderB;
	
	@FXML
	public void buttonRImage() {
		System.out.println("Render Image");

		if(imgResTFWidth.getText().isEmpty() || imgResTFHeight.getText().isEmpty()) {
			AlertWindow.showInfo("Try again", "Empty \"Resolution\" form filled", AlertWindow.WARNING);
		}else{
			int w = Integer.parseInt(imgResTFWidth.getText());
			int h = Integer.parseInt(imgResTFHeight.getText());
			if(w == 0 || h == 0) {
				AlertWindow.showInfo("Try again", "Wrong \"Resolution\" form filled", AlertWindow.WARNING);
			}else {
				main.renderImage(w, h);
			}
		}
	}
	
//	FIXME Empty form event 
	@FXML
	public void buttonRVideo() {
		System.out.println("Render Video");

		int w = 0;
		int h = 0;
		int fps = 0;
		int length = 0;
		
		String errorText = "";
		
		if(vidResTFWidth.getText().isEmpty() || vidResTFHeight.getText().isEmpty()) {
			errorText += "Empty \"Resolution\" form filled \n";
		}else{
			w = Integer.parseInt(vidResTFWidth.getText());
			h = Integer.parseInt(vidResTFHeight.getText());
			if(w == 0 || h == 0) {
				errorText += "Wrong \"Resolution\" form filled \n";
			}
		}
		
		if(vidFpsTF.getText().isEmpty()) {
			errorText += "Empty \"Frames/second\" form filled \n";
		}else {
			fps = Integer.parseInt(vidFpsTF.getText());
			if(fps == 0) {
				errorText += "Wrong \"Frames/second\" form filled \n";
			}
		}
		
		if(vidLengthTF.getText().isEmpty()) {
			errorText += "Empty \"Length ms\" form filled \n";
		}else {
			length = Integer.parseInt(vidLengthTF.getText());
			if(length == 0) {
				errorText += "Wrong \"Length ms\" form filled \n";
			}
		}
		
		if(w != 0 && h != 0 && fps != 0 && length != 0) {
			main.renderVideo(w, h, fps, length);
		}else {
			AlertWindow.showInfo("Try again", errorText.substring(0, errorText.length() - 1), AlertWindow.WARNING);
		}
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
	
	@FXML
	private void imageCancel() {
		main.getImageLogicNote().setCancel(true);
	}
	
	@FXML
	private void videoCancel() {
		main.getImageLogicNote().setCancel(true);
	}
	
	private ChangeListener<String> onlyIntFilter(TextField field) {
		ChangeListener<String> changeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.isEmpty()) {
					try {
						int i = Integer.parseInt(newValue);
						if(i < 1 ) {
							if(i < 0) {
								field.textProperty().set(i*(-1)+"");
							}else {
								if(!field.getStyleClass().contains("error_feild")) {
									field.getStyleClass().add("error_feild");
								}
							}
						}else {

// fix first variable zero input
//							if(!newValue.equals(i+"")) {
//								Platform.runLater(() -> {
//									field.textProperty().set(i+""); 
//				            	});
//							}
							
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
	
	public void connectGUIElements() {
		main.getImageLogicNote().setLable(imgStaL);
		main.getImageLogicNote().setButton(imgRenderB, imgCancelB);
		main.getVideoLogicNote().setLable(vidStaL);
		main.getVideoLogicNote().setButton(vidRenderB, vidCancelB);
	}
}
