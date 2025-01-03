package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static final String VERSION = "1.0.0";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("SisCaLivros - Organizador de Biblioteca (Versão " + VERSION + ")");
        primaryStage.setScene(scene);
        // Configurando a aplicação para iniciar em tela cheia
        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
