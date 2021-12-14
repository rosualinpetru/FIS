package upt.ac.cti.vvs.amp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 250);
        stage.setTitle("Amp");
        stage.setScene(scene);

        URL iconURL = getClass().getResource("/upt/ac/cti/vvs/amp/icon.png");
        var image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/upt/ac/cti/vvs/amp/icon.png")));
        stage.getIcons().add(image);
        com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
        if (iconURL != null) {
            application.setDockIconImage(new ImageIcon(iconURL).getImage());
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}