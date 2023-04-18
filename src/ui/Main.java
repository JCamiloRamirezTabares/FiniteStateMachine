package ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    private FSMGUI controller;
	
	public static void main(String[] team) {
		launch(team);
	}

    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new FSMGUI(primaryStage);
		controller.mainMenu();
    }
}
