package connnectfour;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Multi extends Application {

    public static void main(String[] args) {

            launch(args);
    }

    private Font title_font = Main.getTitleFont();
    private Font button_font = Main.getButtonFont();
    private int width = Main.getWidth();
    private int height = Main.getHeight();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ConnnectFour -> Multiplayer");
        StackPane root = new StackPane();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Main.class.getResource("/main.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/main_stuff.css").toExternalForm());

        primaryStage.setScene(scene);

        VBox main_layout = new VBox();
        main_layout.setAlignment(Pos.TOP_CENTER);
        main_layout.setTranslateY(50);

        // tiitel
        Label pealkiri = new Label();
        pealkiri.getStyleClass().add("title");
        pealkiri.setText("Multiplayer");
        pealkiri.setFont(title_font);

        // Local
        Button local_btn = new Button();
        local_btn.getStyleClass().add("custom_button");
        local_btn.setText("Local");
        local_btn.setFont(button_font);
        local_btn.setOnAction(e -> {
            multiLocal local = new multiLocal();
            local.start(primaryStage);
        });

        // Online
        Button online_btn = new Button();
        online_btn.getStyleClass().add("custom_button");
        online_btn.setText("Online");
        online_btn.setFont(button_font);
        online_btn.setOnAction(e -> {
            Lobby lobby = new Lobby();
            lobby.start(primaryStage);
        });
        // Back to main
        Button back_btn = new Button();
        back_btn.setText("Back");
        back_btn.getStyleClass().add("custom_button");
        back_btn.setFont(button_font);
        back_btn.setOnAction(e -> {primaryStage.setScene(Main.s);
            primaryStage.setTitle("ConnnectFour");});

        main_layout.getChildren().add(pealkiri);
        main_layout.getChildren().add(local_btn);
        main_layout.getChildren().add(online_btn);
        main_layout.getChildren().add(back_btn);
        main_layout.setAlignment(Pos.TOP_CENTER);
        main_layout.setSpacing(90);

        root.getChildren().add(main_layout);
        primaryStage.show();
    }

}
