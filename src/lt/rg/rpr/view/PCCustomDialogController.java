package lt.rg.rpr.view;

import java.util.ArrayList;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import lt.rg.rpr.model.CustomPixelColor;

public class PCCustomDialogController {
	private ObservableList<CustomPixelColor> tableList = FXCollections.observableArrayList();
	private ArrayList<CustomPixelColor> arraylist;

	@FXML
	private TableView<CustomPixelColor> tableView;
	
	@FXML
	private TableColumn<CustomPixelColor, Integer> id;
	
	@FXML
	private TableColumn<CustomPixelColor, Integer> color;
	
	@FXML
	private TableColumn<CustomPixelColor, CustomPixelColor> delete;	
	
	@FXML
	private void initialize(){
		id.setCellValueFactory(c -> new ReadOnlyObjectWrapper<Integer>(tableView.getItems().indexOf(c.getValue())+1));
		
		color.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCPColor()).asObject());
		
		color.setCellFactory(new Callback<TableColumn<CustomPixelColor,Integer>, TableCell<CustomPixelColor,Integer>>() {
			
			@Override
			public TableCell<CustomPixelColor, Integer> call(TableColumn<CustomPixelColor, Integer> param) {
				return new ColorTableCell();
			}
		});
		
		delete.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
		
		delete.setCellFactory(new Callback<TableColumn<CustomPixelColor,CustomPixelColor>, TableCell<CustomPixelColor,CustomPixelColor>>() {
			
			@Override
			public TableCell<CustomPixelColor, CustomPixelColor> call(TableColumn<CustomPixelColor, CustomPixelColor> param) {
				
				return new DeleteTableCell(arraylist);
			}
		});
	}
	
	@FXML
	private void addColor() {
		CustomPixelColor cpc = new CustomPixelColor(0);
		
		tableList.add(cpc);
		arraylist.add(cpc);
	}
	
	public void setTableList(ArrayList<CustomPixelColor> arraylist) {
		this.arraylist = arraylist;
		tableList.addAll(arraylist);
		tableView.setItems(tableList);
	}
}

class ColorTableCell extends TableCell<CustomPixelColor, Integer>{
	private ColorPicker colorPicker = new ColorPicker();
	
	@Override
	protected void updateItem(Integer item, boolean empty) {
		super.updateItem(item, empty);  
		if(empty) {
			setGraphic(null);
		} else {
			java.awt.Color color = new java.awt.Color(item);
			
			double r = (double)color.getRed()/255.0;
			double g = (double)color.getGreen()/255.0;
			double b = (double)color.getBlue()/255.0;
			
			this.colorPicker.setValue(new Color(r, g, b, 1));
			this.setGraphic(this.colorPicker);
			
			colorPicker.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					Color color = colorPicker.getValue();
					
					int r = (int) (255.0 * color.getRed());
					int g = (int) (255.0 * color.getGreen());
					int b = (int) (255.0 * color.getBlue());
					
					((CustomPixelColor) getTableView().getItems().get( getTableRow().getIndex()) ).setCPColor(new java.awt.Color(r, g, b).getRGB());
				}
			});
		}
	}
}

class DeleteTableCell extends TableCell<CustomPixelColor, CustomPixelColor>{
	private Button button = new Button("Delete");
	ArrayList<CustomPixelColor> arraylist;
	
	public DeleteTableCell(ArrayList<CustomPixelColor> arraylist) {
		this.arraylist = arraylist;
	}
	
	@Override
	protected void updateItem(CustomPixelColor item, boolean empty) {
		super.updateItem(item, empty);
		if(empty) {
			setGraphic(null);
		} else {
			this.setGraphic(button);
			
			button.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					getTableView().getItems().remove(item);					
					arraylist.remove(item);
				}
			});
		}
	}
}