package connnectfour;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;


public class History extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private int width = Main.getWidth();
    private int height = Main.getHeight();
    private ListView<HBox> game_list = new ListView<>();
    private Font titleFont = Main.getTitleFont();
    private Font buttonFont = Main.getButtonFont();


    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("ConnnectFour -> History");
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
        pealkiri.setText("History");
        pealkiri.setFont(titleFont);
        main_layout.getChildren().add(pealkiri);

        // HBox tabeli pealkirjade jaoks + listview.
        String[] labels = new String[]{"Opponent", "Size", "Moves", "Winner"};
        HBox info = new HBox();
        for (String label : labels) {
            Label temp_label = new Label(label);
            temp_label.setFont(buttonFont);
            temp_label.setAlignment(Pos.CENTER);
            temp_label.setMinWidth(width/labels.length - 50/4);
            info.getChildren().add(temp_label);
        }
        info.setAlignment(Pos.CENTER);
        loadGames();

        //Nupp tagasi peamenüüsse
        Button back_btn = new Button();
        back_btn.getStyleClass().add("custom_button");
        back_btn.setText("Back to Main");
        back_btn.setFont(buttonFont);
        back_btn.setOnAction(e -> {
            primaryStage.setTitle("ConnnectFour");
            primaryStage.setScene(Main.s);
        });

        main_layout.getChildren().addAll(info, game_list, back_btn);
        main_layout.setSpacing(15);

        root.getChildren().add(main_layout);
        primaryStage.show();
    }

    public void loadGames() {
        game_list = new ListView<>();
        ArrayList<String[]> games = Database.readXML();
        assert games != null;
        for (int i = games.size()-1; i >= 0; i--) {
            boolean isEmpty = false;
            HBox temp_game = new HBox();
            for (String label : games.get(i)) {
                if (label.equals("--") && i == 0) {
                    isEmpty = true;
                    break;
                }
                Label temp_label = new Label(label);
                temp_label.setFont(buttonFont);
                temp_label.setAlignment(Pos.CENTER);
                temp_label.setMinWidth(width/games.get(i).length - 50/4);
                temp_game.getChildren().add(temp_label);
            }
            if (!isEmpty) {
                temp_game.setAlignment(Pos.CENTER);
                game_list.getItems().add(temp_game);
            }
        }

    }

}
