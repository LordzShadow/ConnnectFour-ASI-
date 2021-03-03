package connnectfour;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class settings extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private int width = Main.getWidth();
	private int height = Main.getHeight();
	private int cell_size = Main.getCell_size();
	private Font titleFont = Main.getTitleFont();
	private Font buttonFont = Main.getButtonFont();

	private static ColorPicker cp1 = new ColorPicker();
	private static ColorPicker cp2 = new ColorPicker();

	@Override
	public void start(Stage primaryStage){
		primaryStage.setTitle("ConnnectFour -> Settings");
		StackPane root = new StackPane();
		Scene s = new Scene(root, width, height);
		s.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
		s.getStylesheets().add(getClass().getResource("/main_stuff.css").toExternalForm());

		primaryStage.setScene(s);


		//System.out.println(props.get("name"));

		//Main layout, milles elemendid
		VBox main_layout = new VBox();
		main_layout.setAlignment(Pos.TOP_CENTER);
		main_layout.setTranslateY(50);

		//Pealkiri
		Label pealkiri = new Label();
		pealkiri.getStyleClass().add("title");
		pealkiri.setText("Settings");
		pealkiri.setFont(titleFont);
		main_layout.getChildren().add(pealkiri);

		//Kasutajanime küsimine
		HBox username = new HBox();
		username.setAlignment(Pos.CENTER);
		username.setSpacing(50);
		Label usernametitle = new Label();
		usernametitle.setText("Username:");
		usernametitle.setFont(buttonFont);
		TextField usernamein = new TextField();
		usernamein.getStyleClass().add("input");
		usernamein.setText(Main.props.getProperty("name", "player1"));
		username.getChildren().addAll(usernametitle, usernamein);

		//Mängijate värvid
        HBox colors1 = new HBox();
        colors1.setAlignment(Pos.CENTER);
        colors1.setSpacing(50);
        Label color1 = new Label("Player1 Color: ");
        color1.setFont(buttonFont);

        HBox colors2 = new HBox();
        colors2.setAlignment(Pos.CENTER);
        colors2.setSpacing(50);
        Label color2 = new Label("Player2 Color: ");
        color2.setFont(buttonFont);

		colors1.getChildren().add(color1);
		colors1.getChildren().add(cp1);
		colors2.getChildren().add(color2);
		colors2.getChildren().add(cp2);

        //board size
		HBox boardSizes = new HBox();
		boardSizes.setAlignment(Pos.CENTER);
		boardSizes.setSpacing(50);

		VBox widthSize = new VBox();
		widthSize.setAlignment(Pos.CENTER);
		widthSize.setSpacing(20);
		Label widthLabel = new Label("Board Width: " + Main.getDefault_width());
		widthLabel.setFont(buttonFont);
		//numbri slider
		Slider widthSlider = new Slider(5, 15, Main.getDefault_width());
		widthSlider.setMinWidth(216);

		widthSlider.setShowTickMarks(true);
		widthSlider.setSnapToTicks(true);
		widthSlider.setShowTickLabels(true);
		widthSlider.setMajorTickUnit(1);
		widthSlider.setMinorTickCount(0);
		widthSlider.setOnMouseReleased(event -> {
			int value = (int) Math.round(widthSlider.getValue());
			widthLabel.setText("Board Width: " + value);
		});

		VBox heightSize = new VBox();
		heightSize.setAlignment(Pos.CENTER);
		heightSize.setSpacing(20);
		Label heightLabel = new Label("Board Height: " + Main.getDefault_height());
		heightLabel.setFont(buttonFont);
		//numbri slider
		Slider heightSlider = new Slider(4, 8, Main.getDefault_height());
		heightSlider.setMinWidth(216);
		heightSlider.setShowTickMarks(true);
		heightSlider.setSnapToTicks(true);
		heightSlider.setShowTickLabels(true);
		heightSlider.setMajorTickUnit(1);
		heightSlider.setMinorTickCount(0);
		heightSlider.setOnMouseReleased(event -> {
			int value = (int) Math.round(heightSlider.getValue());
			heightLabel.setText("Board Height: " + value);
		});


		widthSize.getChildren().add(widthLabel);
		widthSize.getChildren().add(widthSlider);
		heightSize.getChildren().add(heightLabel);
		heightSize.getChildren().add(heightSlider);

		boardSizes.getChildren().add(widthSize);
		boardSizes.getChildren().add(heightSize);



		// Fondi muutmine
//		HBox font_change = new HBox();
//		font_change.setAlignment(Pos.CENTER);
//		font_change.setSpacing(50);
//		Label font_name = new Label();
//		font_name.setText("Font:");
//		font_name.setFont(buttonFont);
//		Tooltip tp = new Tooltip("Requires restart!");
//		tp.setShowDelay(Duration.millis(100));
//		TextField font_name_in = new TextField();
//		font_name_in.getStyleClass().add("input");
//		font_name_in.setTooltip(tp);
//		//font_name_in.setText(Main.getProps().getProperty("font", "Comic Sans MS"));
//		font_name_in.setText(buttonFont.getFamily());
//		font_change.getChildren().addAll(font_name, font_name_in);

		//Nupp tagasi peamenüüsse
		Button back_btn = new Button();
		back_btn.getStyleClass().add("custom_button");
		back_btn.setText("Back to Main (save)");
		back_btn.setFont(buttonFont);
		back_btn.setOnAction(e -> {
			primaryStage.setTitle("ConnnectFour");
            Main.setColor(1, cp1.getValue());
            Main.setColor(2, cp2.getValue());
            cp1.setValue(Main.getColor(1));
			Main.save("name", usernamein.getText());
			Main.save("default_width", Integer.toString((int)widthSlider.getValue()));
			Main.save("default_height", Integer.toString((int)heightSlider.getValue()));
			Main.setDefault_width((int)widthSlider.getValue());
			Main.setDefault_height((int)heightSlider.getValue());
			//Main.save("font", font_name_in.getText());
			primaryStage.setScene(Main.s);
		});

		main_layout.getChildren().addAll(username, colors1, colors2, boardSizes, /*font_change, */back_btn);
		main_layout.setSpacing(height/main_layout.getChildren().size()-50);

		root.getChildren().add(main_layout);
		primaryStage.show();
	}

	public static void setColors(Color color1, Color color2){
		cp1.setValue(color1);
		cp2.setValue(color2);
	}

}
