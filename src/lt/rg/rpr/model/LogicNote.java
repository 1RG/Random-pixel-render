package lt.rg.rpr.model;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lt.rg.rpr.view.AlertWindow;

public class LogicNote {
	private boolean status = true; // For error
	private Exception exception; // For error info
	private boolean cancel = false; // For cancel event
	
	private Label label;
	private Button buttonRender;
	private Button buttonCancel;
		
	public void setStatus(Exception exception) {
		this.status = false;
		this.exception = exception;
	}
	
	public void setLable(Label label) {
		this.label = label;
	}
	
	public void setButton(Button render, Button cancel) {
		this.buttonRender = render;
		this.buttonCancel = cancel;
	}

	public void runing(boolean run) {	
		if(run) {
			buttonRender.setDisable(true);
			buttonCancel.setDisable(false);
			
			status = true;
		}else {
			buttonRender.setDisable(false);
			buttonCancel.setDisable(true);
		}
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	
	public boolean isCancel() {
		return cancel;
	}
	
	public void display(String text) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				label.setText(text);
			}
		});
	}
	
	public void displayAlert() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				if(status) {		
					AlertWindow.showInfo("Success", "Done", AlertWindow.INFORMATION);
				}else {
					AlertWindow.showInfo("Failure", "Error "+ exception, AlertWindow.ERROR);
				}
			}
		});
	}
}
