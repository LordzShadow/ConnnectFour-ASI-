package connnectfour;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    public static void main(String[] args) throws IOException {
        try {
            props.load(new FileInputStream(propPath));
        }catch(Exception e){
            props.store(new FileWriter(propPath), null);
        }
        launch(args);
    }

    private static int cell_size = 100;
    private static int width = 700;
    private static int height = 750;

    private static String propPath = "data.properties";
    static Properties props = new Properties();

    private static String font_family;
    private static Font title_font;
    private static Font button_font;

    private static Color color1;
    private static Color color2;

    private static int default_width;
    private static int default_height;

    private static StackPane root = new StackPane();
    static Scene s;

    @Override
    public void start(Stage primaryStage) {
        font_family = props.getProperty("font", "Comic Sans MS");
        title_font = Font.font(font_family, 50);
        button_font = Font.font(font_family, 20);

        try {
            color1 = Color.web(props.getProperty("color1", "#ffff00"));
        }catch (Exception e){
            color1 = Color.YELLOW;
        }
        try {
            color2 = Color.web(props.getProperty("color2", "#ff0000"));
        }catch (Exception e){
            color2 = Color.RED;
        }
        default_width = Integer.parseInt(props.getProperty("default_width", "7"));
        default_height = Integer.parseInt(props.getProperty("default_height", "6"));

        settings settings_obj = new settings();
        Multi multi = new Multi();
        AIdif single = new AIdif();
        History history_obj = new History();

        s  = new Scene(root, width, height);

        primaryStage.setTitle("ConnnectFour");
        primaryStage.setScene(s);
        s.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        s.getStylesheets().add(getClass().getResource("/main_stuff.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height-20);

        VBox main_layout = new VBox();
        main_layout.setAlignment(Pos.TOP_CENTER);
        main_layout.setTranslateY(50);
        main_layout.setSpacing(50);

        Label main_title = new Label();
        main_title.getStyleClass().add("title");
        main_title.setText("ConnnectFour");
        main_title.setFont(title_font);

        // Singleplayer
        Button singeplayer_btn = new Button();
        singeplayer_btn.getStyleClass().add("custom_button");
        singeplayer_btn.setText("Singeplayer");
        singeplayer_btn.setFont(button_font);
        singeplayer_btn.setOnAction(e -> single.start(primaryStage));

        // Multiplayer
        Button multiplayer_btn = new Button();
        multiplayer_btn.getStyleClass().add("custom_button");
        multiplayer_btn.setText("Multiplayer");
        multiplayer_btn.setFont(button_font);
        multiplayer_btn.setOnAction(e -> multi.start(primaryStage));

        // Settings
        Button settings_btn = new Button();
        settings_btn.getStyleClass().add("custom_button");
        settings_btn.setText("Settings");
        settings_btn.setFont(button_font);
        settings_btn.setOnAction(e -> {
            settings.setColors(color1, color2);
            settings_obj.start(primaryStage);
        });

        // History of games.
        Button history_btn = new Button();
        history_btn.getStyleClass().add("custom_button");
        history_btn.setText("History");
        history_btn.setFont(button_font);
        history_btn.setOnAction(e -> history_obj.start(primaryStage));

        main_layout.getChildren().addAll(main_title, singeplayer_btn, multiplayer_btn, settings_btn, history_btn);
        main_layout.setSpacing(height/main_layout.getChildren().size() -50);
        root.getChildren().add(main_layout);
        primaryStage.show();
    }

    public static int getWidth() {return width;}
    public static int getHeight() {return height;}
    public static int getCell_size() {
        return cell_size;
    }
    public static int getDefault_width(){
        return default_width;
    }
    public static int getDefault_height() {
        return default_height;
    }
    public static void setDefault_width(int default_width) {
        Main.default_width = default_width;
    }
    public static void setDefault_height(int default_height) {
        Main.default_height = default_height;
    }
    public static Font getTitleFont(){return title_font;}
    public static Font getButtonFont(){return button_font;}
    public static String getFont_family(){return font_family;}
    public static Properties getProps(){return props;}
    public static void save(String key, String value){
        props.setProperty(key, value);
        try {
            props.store(new FileWriter(propPath), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (key.equals("font")) {
            font_family = props.getProperty("font", "Comic Sans MS");
            title_font = Font.font(font_family, 50);
            button_font = Font.font(font_family, 20);
        }

    }
    public static void setColor(int player, Color color){
        if (player == 1) {
            props.setProperty("color1", color.toString().substring(0, 8));
            color1 = color;
        } else if (player == 2) {
            props.setProperty("color2", color.toString().substring(0, 8));
            color2 = color;
        }
    }
    public static Color getColor(int player){
        if (player == 1) {
            return color1;
        } else if (player == 2) {
            return color2;
        }else{
            return null;
        }
    }
}
