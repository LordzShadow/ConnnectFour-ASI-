package connnectfour;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class AIdif extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private int width = Main.getWidth();
    private int height = Main.getHeight();
    private int cell_size = Main.getCell_size();
    private Font titleFont = Main.getTitleFont();
    private Font buttonFont = Main.getButtonFont();

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("ConnnectFour -> Choosing difficulty");
        StackPane root = new StackPane();
        Scene s = new Scene(root, width, height);
        s.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        s.getStylesheets().add(getClass().getResource("/main_stuff.css").toExternalForm());

        primaryStage.setScene(s);

        //Main layout, milles elemendid
        VBox main_layout = new VBox();
        main_layout.setAlignment(Pos.TOP_CENTER);
        main_layout.setTranslateY(50);

        //Pealkiri
        Label pealkiri = new Label();
        pealkiri.getStyleClass().add("title");
        pealkiri.setText("Choose difficulty");
        pealkiri.setFont(titleFont);
        main_layout.getChildren().add(pealkiri);

        Button easy_btn = new Button();
        easy_btn.getStyleClass().add("custom_button");
        easy_btn.setText("Easy");
        easy_btn.setFont(buttonFont);
        easy_btn.setOnAction(e -> {
            Single single = new Single();
            single.difficulty = 2;
            single.start(primaryStage);
            //primaryStage.setTitle("ConnnectFour -> Singleplayer -> Easy");
            //primaryStage.setScene(Main.s);
        });

        Button medium_btn = new Button();
        medium_btn.getStyleClass().add("custom_button");
        medium_btn.setText("Medium");
        medium_btn.setFont(buttonFont);
        medium_btn.setOnAction(e -> {
            Single single = new Single();
            single.difficulty = 4;
            single.start(primaryStage);
            //primaryStage.setTitle("ConnnectFour -> Singleplayer -> Medium");
            //primaryStage.setScene(Main.s);
        });

        Button hard_btn = new Button();
        hard_btn.getStyleClass().add("custom_button");
        hard_btn.setText("Hard");
        hard_btn.setFont(buttonFont);
        hard_btn.setOnAction(e -> {
            Single single = new Single();
            single.difficulty = 7;
            single.start(primaryStage);
            //primaryStage.setTitle("ConnnectFour -> Singleplayer -> Hard");
            //primaryStage.setScene(Main.s);
        });

        //Nupp tagasi peamenüüsse
        Button back_btn = new Button();
        back_btn.getStyleClass().add("custom_button");
        back_btn.setText("Back to Main");
        back_btn.setFont(buttonFont);
        back_btn.setOnAction(e -> {
            primaryStage.setTitle("ConnnectFour");
            primaryStage.setScene(Main.s);
        });

        main_layout.getChildren().addAll(easy_btn, medium_btn, hard_btn, back_btn);
        main_layout.setSpacing(height/main_layout.getChildren().size()-50);

        root.getChildren().add(main_layout);
        primaryStage.show();
    }

}