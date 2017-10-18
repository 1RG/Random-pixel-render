package lt.rg.rpr.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertWindow {
	static public String INFORMATION = "i";
	static public String WARNING = "w";
	static public String ERROR = "e";
	
	static public void showInfo(String header, String text, String status) {
		Alert alert = new Alert(null);
		switch (status) {
		case "i":
			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Information");
			break;
		case "w":
			alert.setAlertType(AlertType.WARNING);
			alert.setTitle("Warning");
			break;
		case "e" :
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Error");
			break;
		default:
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("System error");
			break;
		}
	    alert.setHeaderText(header);
	    alert.setContentText(text);
	    
	    alert.showAndWait();
	}
}
