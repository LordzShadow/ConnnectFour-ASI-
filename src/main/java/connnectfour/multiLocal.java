package connnectfour;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;


public class multiLocal extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	// tühi 0
	// kollane 1
	// punane 2

	Board laud = new Board();

	private int[][] board = laud.getBoard();
	private int cell_size = laud.getCell_size();
	private double width = laud.getWidth();
	private double height = laud.getHeight();


	public void start(Stage primaryStage) {
		primaryStage.setTitle("ConnnectFour -> Multiplayer -> Local");
		laud.setPlayer2_name("Player 2");
		laud.setType("Local");
		StackPane root = new StackPane();
		Scene s = new Scene(root, width, height+cell_size*1.5);
		laud.setback(primaryStage, s, root);
		s.getStylesheets().add(Main.class.getResource("/main_stuff.css").toExternalForm());
		primaryStage.setScene(s);
		HBox buttons = new HBox();
		buttons.setMinHeight(cell_size);
		HBox kord_layout = new HBox();
		kord_layout.setAlignment(Pos.CENTER);
		Label kord = new Label();
		kord_layout.getChildren().add(kord);
		kord_layout.setMinHeight((double) cell_size/2);
		kord_layout.setBackground(new Background(new BackgroundFill(laud.getKaiguvarv(), CornerRadii.EMPTY, Insets.EMPTY)));
		kord.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
		kord.setText(laud.getPlayer1_name() + "'s turn");
		kord.setTextAlignment(TextAlignment.CENTER);

		final Canvas canvas = new Canvas(s.getWidth(), height);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		AtomicBoolean won = new AtomicBoolean(false);

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(i == 0) {
					ImageView btnImage = null;
					ImageView pressed_image = null;
					try {
						btnImage = new ImageView(new Image(getClass().getResourceAsStream("/arrow_button.png")));
						btnImage.setFitWidth(cell_size-20);
						btnImage.setFitHeight(cell_size-20);
						pressed_image = new ImageView(new Image(getClass().getResourceAsStream("/arrow_btn_pressed.png")));
						pressed_image.setFitWidth(cell_size-20);
						pressed_image.setFitHeight(cell_size-20);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Button btn = new Button();
					ImageView finalPressed_image = pressed_image;
					btn.setOnMousePressed(e -> btn.setGraphic(finalPressed_image));
					ImageView finalBtnImage = btnImage;
					btn.setOnMouseReleased(e -> btn.setGraphic(finalBtnImage));
					btn.setGraphic(btnImage);
					btn.setPrefSize(cell_size, cell_size);
					btn.setStyle("-fx-focus-color: transparent;");
					int finalJ = j;
					btn.setOnAction(actionEvent -> {
						if(!won.get()) {
							laud.place(finalJ, gc, kord, kord_layout);

							int voitja = laud.checkWin();
							if (voitja > 0) {
								laud.win(root, won, voitja, primaryStage);
							}else if(laud.getKaidud() >= board.length*board[0].length){
								laud.win(root, won, 3, primaryStage);
							}
						}
					});
					buttons.getChildren().add(btn);
				}

				gc.setFill(Color.BLUE);
				gc.fillRect(j*cell_size, i*cell_size, cell_size, cell_size);
				if (board[i][j] == 0) {
					gc.setFill(Color.WHITE);
					gc.fillOval(j*cell_size+10, i*cell_size+10, cell_size-20, cell_size-20);
				}
			}
		}

		VBox main_layout = new VBox();
		main_layout.getChildren().add(kord_layout);
		main_layout.getChildren().add(buttons);
		main_layout.getChildren().add(canvas);
		root.getChildren().add(main_layout);

		primaryStage.show();
	}
}