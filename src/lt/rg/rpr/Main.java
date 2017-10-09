package lt.rg.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lt.rg.rpr.model.RenderLogic;
import lt.rg.rpr.view.RootController;

public class Main extends Application{
	
	private Stage primaryStage;
	private BorderPane root;
	
	private RenderLogic renderLogic;

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
			
//		renderLogic.createImage();
//		rl.createVideo();
//		rl.testVideoSupport();
		
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
			rootController.setResolutionWH(renderLogic.getWidth(), renderLogic.getHeight());
			
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public RenderLogic getRenderLogic() {
		return renderLogic;
	}
}
