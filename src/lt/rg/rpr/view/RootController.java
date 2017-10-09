package lt.rg.rpr.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lt.rg.rpr.Main;

public class RootController {
	private Main main;
	
	@FXML
	private TextField resTFWidth;
	
	@FXML
	private TextField resTFHeight;
		
	@FXML
	public void buttonRImage() {
		System.out.println("Render Image");
		main.getRenderLogic().setWidth(Integer.parseInt(resTFWidth.getText()));
		main.getRenderLogic().setHeight(Integer.parseInt(resTFHeight.getText()));
		main.getRenderLogic().createImage();
	}
	
	@FXML
	public void buttonRVideo() {
		System.out.println("Render Video");
		main.getRenderLogic().setWidth(Integer.parseInt(resTFWidth.getText()));
		main.getRenderLogic().setHeight(Integer.parseInt(resTFHeight.getText()));
		main.getRenderLogic().createVideo();
	}
	
	@FXML
	public void buttonTest() {
		System.out.println(resTFWidth.getText()+" - "+ resTFHeight.getText());
	}
	
	@FXML
	private void initialize(){		
		resTFWidth.textProperty().addListener(onlyIntFilter(resTFWidth));
		resTFHeight.textProperty().addListener(onlyIntFilter(resTFHeight));
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
							field.textProperty().set(i+"");
						}
					} catch (Exception e) {
						field.textProperty().set(oldValue);
					}
				}
			}
		};
		
		return changeListener;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public void setResolutionWH(int width, int height) {
		resTFWidth.setText(width+"");
		resTFHeight.setText(height+"");
	}
}
