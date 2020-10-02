package CA1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application
{
    public static Stage ps;

    //START METHOD
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../CA1/MainMenu.fxml")); //Open Login Page
        primaryStage.setScene(new Scene(root)); //Set the Scene
        primaryStage.show(); //Show Scene
        ps = primaryStage;

    }

    //Main
    public static void main(String[] args)
    {
        launch(args);
    }
}
