package lt.rg.rpr;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lt.rg.rpr.model.LogicNote;
import lt.rg.rpr.model.RenderLogic;
import lt.rg.rpr.view.RootController;

public class Main extends Application{
	
	private Stage primaryStage;
	private BorderPane root;
	
	private RenderLogic renderLogic;
		
	LogicNote imageLogicNote = new LogicNote();
	LogicNote videoLogicNote = new LogicNote();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		renderLogic = new RenderLogic();
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Random pixel render");

		System.out.println("Start");
		long start = System.currentTimeMillis();

		initRootLayout();
		
		long end = System.currentTimeMillis() - start;
		System.out.println("Time: "+ end/1000f +"s");
		System.out.println("Done");
	}
	
	private void initRootLayout(){
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Root.fxml"));
            root = (BorderPane) loader.load();
			
			Scene screen = new Scene(root);
			primaryStage.setScene(screen);
			
			RootController rootController = loader.getController();
			rootController.setMain(this);
			rootController.setImageData(renderLogic.getImageWidth(), renderLogic.getImageHeight(), renderLogic.getImageColor());
			rootController.setVideoData(renderLogic.getVideoWidth(), renderLogic.getVideoHeight(), renderLogic.getVideoFps(), renderLogic.getVideoLength(), renderLogic.getImageColor());
			rootController.connectGUIElements();
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					System.out.println("EXIT");
					imageLogicNote.setCancel(true);
					videoLogicNote.setCancel(true);
				}
			});
			
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LogicNote getImageLogicNote() {
		return imageLogicNote;
	}

	public LogicNote getVideoLogicNote() {
		return videoLogicNote;
	}

	public void setImageLogicNote(LogicNote imageLogicNote) {
		this.imageLogicNote = imageLogicNote;
	}

	public void setVideoLogicNote(LogicNote videoLogicNote) {
		this.videoLogicNote = videoLogicNote;
	}

	public RenderLogic getRenderLogic() {
		return renderLogic;
	}
	
	public void renderImage(int width, int height, int color) {
		renderLogic.setImageWidth(width);
		renderLogic.setImageHeight(height);
		renderLogic.setImageColor(color);
		
		Thread thread = new Thread(new Runnable() {		
			@Override
			public void run() {
				
				renderLogic.createImage(imageLogicNote); 
			}
		});
		thread.start();
	}

	public void renderVideo(int width, int height, int fps, int length, int color) {
		renderLogic.setVideoWidth(width);
		renderLogic.setVideoHeight(height);
		renderLogic.setVideoFps(fps);
		renderLogic.setVideoLength(length);
		renderLogic.setVideoColor(color);
		
		Thread thread = new Thread(new Runnable() {		
			@Override
			public void run() {				
				renderLogic.createVideo(videoLogicNote);
			}
		});
		thread.start();
	}
}
