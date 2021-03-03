package connnectfour;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Board {

    private int width;
    private int height;
    private int cell_size = 100;
    private int cellw;
    private int cellh;
    private int kaik = 1;
    private boolean finished = false;
    private boolean paused = false;
    private Color kaiguvarv = Main.getColor(1);
    private Font titleFont = Main.getTitleFont();
    private Font buttonFont = Main.getButtonFont();

    private int[][] board;
    private int kaidud = 0;
    private String player2_name = "Computer";
    private String player1_name = Main.getProps().getProperty("name", "Player1");
    private String type = "NaN";

    public Board(int x, int y){
        cellw = x;
        cellh = y;
        width = cell_size*cellw;
        height = cell_size*cellh;
        board = new int[cellh][cellw];
    }

    public Board(){
        this(Main.getDefault_width(), Main.getDefault_height());
    }

    public int[][] getBoard() {
        return board;
    }

    public int getCell_size() {
        return cell_size;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Color getKaiguvarv() {
        return kaiguvarv;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public int getKaidud() {
        return kaidud;
    }

    public int getKaik() {
        return kaik;
    }

    public void setPlayer2_name(String player2_name) {
        this.player2_name = player2_name;
    }

    public String getPlayer2_name() {
        return this.player2_name;
    }

    public String getPlayer1_name() {
        return player1_name;
    }

    public void setKaik(int kaik) {
        this.kaik = kaik;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKaiguvarv(Color kaiguvarv) {
        this.kaiguvarv = kaiguvarv;
    }

    public void setback(Stage primary, Scene s, StackPane root) {
        s.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE && !paused && !finished) {
                pausemenu(root, primary);
            } else if (paused) {
                paused = false;
                root.getChildren().remove(root.getChildren().size()-1);
            }
        });
    }

    public void setback(Stage primary, Scene s, StackPane root, DataOutputStream dos) {
        s.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE && !paused && !finished) {
                pausemenu(root, primary, dos);
            } else if (paused) {
                paused = false;
                root.getChildren().remove(root.getChildren().size()-1);
            }
        });
    }

    public int checkWin(){
        int voitja = 0;
        for (int[] ints : board) {
            for (int j = 0; j < board[0].length - 3; j++) {
                for (int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (ints[j + k] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if (praeguneCheck) {
                        voitja = l;
                    }
                }
            }
        }
        for(int i = 0; i < board.length-3; i++){
            for(int j = 0; j < board[0].length; j++){
                for(int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (board[i + k][j] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if(praeguneCheck){
                        voitja = l;
                    }
                }
            }
        }
        for(int i = 0; i < board.length-3; i++){
            for(int j = 0; j < board[0].length-3; j++){
                for(int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (board[i + k][j + k] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if(praeguneCheck){
                        voitja = l;
                    }
                }
            }
        }
        for(int i = 3; i < board.length; i++){
            for(int j = 0; j < board[0].length-3; j++){
                for(int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (board[i - k][j + k] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if(praeguneCheck){
                        voitja = l;
                    }
                }
            }
        }
        return voitja;
    }

    public int checkBoard(int nupp) {
        if (board[0][nupp] != 0) {
            return -1;
        } else if (board[board.length-1][nupp] == 0){
            return board.length-1;
        }else {
            for (int i = 0; i < board.length; i++) {
                if(board[i][nupp] != 0) {
                    return i-1;
                }
            }
            return -1;
        }

    }

    public void place(int finalJ, GraphicsContext gc, Label kord, HBox kord_layout ) {
        int column = checkBoard(finalJ);
        if (column != -1) {
            board[column][finalJ] = kaik;
            gc.setFill(kaiguvarv);
            gc.fillOval(finalJ*cell_size+10, column*cell_size+10, cell_size-20, cell_size-20);
            if (kaik == 1) {
                kaik = 2;
                kaiguvarv = Main.getColor(2);
                kord.setText(player2_name + "'s turn");
            } else {
                kaik = 1;
                kaiguvarv = Main.getColor(1);
                kord.setText(player1_name + "'s turn");
            }
            kaidud++;
            kord_layout.setBackground(new Background(new BackgroundFill(kaiguvarv, CornerRadii.EMPTY, Insets.EMPTY)));
        }

    }

    public void win(StackPane root, AtomicBoolean won, int voitja, Stage primaryStage){
        won.set(true);
        VBox layout = new VBox();
        layout.setBackground(new Background(new BackgroundFill(Color.gray(0.4, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        Label winner = new Label();
        if (voitja == 1) {
            winner.setText(player1_name + " won");
            winner.setTextFill(Color.LIGHTGREEN);
            saveToHistory(player1_name);
        } else if (voitja == 2) {
            winner.setText(player2_name + " won");
            winner.setTextFill(Color.ORANGE);
            saveToHistory(player2_name);
        } else if (voitja == 3) {
            winner.setTextFill(Color.LIGHTBLUE);
            winner.setText("It's a draw");
            saveToHistory("DRAW");
        }
        winner.setFont(titleFont);
        winner.setMaxSize(root.getWidth(), root.getHeight());
        winner.setAlignment(Pos.CENTER);
        finished = true;
        Button exit = exit(primaryStage);
        layout.getChildren().addAll(winner, exit);
        root.getChildren().add(layout);
    }

    public Button exit(Stage primaryStage) {
        Button exit = new Button();
        exit.getStyleClass().add("custom_button");
        exit.setText("Back to main screen");
        exit.setPrefWidth(300);
        exit.setFont(buttonFont);
        exit.setOnAction(e -> {
            primaryStage.setTitle("ConnnectFour");
            primaryStage.setScene(Main.s);
        });
        return exit;
    }

    public void saveToHistory(String winner) {
        String[] game = new String[4];
        game[0] = type;
        game[1] = cellw + "x" + cellh;
        game[2] = Integer.toString(kaidud);
        game[3] = winner;
        Database.saveToXML(game);
    }

    public void connectionLost(StackPane root, Stage primaryStage) {
        Label connection = new Label();
        VBox layout = new VBox();
        layout.setBackground(new Background(new BackgroundFill(Color.gray(0.4, 0.6), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);

        connection.setText("Connection lost with other player");
        connection.setFont(titleFont);
        connection.setWrapText(true);
        connection.setMaxSize(root.getWidth(), root.getHeight());
        connection.setTextAlignment(TextAlignment.CENTER);
        connection.setAlignment(Pos.CENTER);
        connection.setTextFill(Color.RED);
        Button exit = exit(primaryStage);
        //exit.setTranslateY(100);
        layout.getChildren().addAll(connection, exit);
        root.getChildren().add(layout);
    }

    public void pausemenu(StackPane root, Stage primaryStage) {
        paused = true;
        Label text = new Label();
        VBox layout = new VBox();
        layout.setBackground(new Background(new BackgroundFill(Color.gray(0.4, 0.6), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);

        text.setText("PAUSED");
        text.setFont(titleFont);
        text.setWrapText(true);
        text.setMaxSize(root.getWidth(), root.getHeight());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setAlignment(Pos.CENTER);
        text.setTextFill(Color.AQUA);
        HBox btnl = new HBox();
        btnl.setAlignment(Pos.CENTER);
        btnl.setSpacing(20);
        Button exit = exit(primaryStage);
        Button back = new Button();
        back.getStyleClass().add("custom_button");
        back.setText("Back to game");
        back.setPrefWidth(300);
        back.setFont(buttonFont);
        back.setOnAction(e -> {
            paused = false;
            root.getChildren().remove(layout);
        });

        //exit.setTranslateY(100);
        btnl.getChildren().addAll(back, exit);
        layout.getChildren().addAll(text, btnl);
        root.getChildren().add(layout);
    }
    public void pausemenu(StackPane root, Stage primaryStage, DataOutputStream dos) {
        paused = true;
        Label text = new Label();
        VBox layout = new VBox();
        layout.setBackground(new Background(new BackgroundFill(Color.gray(0.4, 0.6), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);

        text.setText("PAUSED");
        text.setFont(titleFont);
        text.setWrapText(true);
        text.setMaxSize(root.getWidth(), root.getHeight());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setAlignment(Pos.CENTER);
        text.setTextFill(Color.AQUA);
        HBox btnl = new HBox();
        btnl.setAlignment(Pos.CENTER);
        btnl.setSpacing(20);
        Button exit = exit(primaryStage);
        exit.setOnAction(e -> {
            try {
                dos.writeUTF("closed");
                dos.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            primaryStage.setTitle("ConnnectFour");
            primaryStage.setScene(Main.s);
        });
        Button back = new Button();
        back.getStyleClass().add("custom_button");
        back.setText("Back to game");
        back.setPrefWidth(300);
        back.setFont(buttonFont);
        back.setOnAction(e -> {
            paused = false;
            root.getChildren().remove(layout);
        });

        //exit.setTranslateY(100);
        btnl.getChildren().addAll(back, exit);
        layout.getChildren().addAll(text, btnl);
        root.getChildren().add(layout);
    }

}
