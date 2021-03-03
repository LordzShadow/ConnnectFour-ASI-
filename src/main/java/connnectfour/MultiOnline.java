package connnectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;


public class MultiOnline extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // tühi 0
    // kollane 1
    // punane 2
    Socket sock = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;

    Board laud = new Board(7, 6);


    int width = laud.getWidth();
    int height = laud.getHeight();
    int cell_size = laud.getCell_size();

    int[][] board = laud.getBoard();

    private void connect(GraphicsContext gc, Label kord, HBox kord_layout, StackPane root, Stage primaryStage) {

        try {
            if (laud.getKaik() == 2) {
                laud.setKaiguvarv(Main.getColor(2));
                kord.setText(laud.getPlayer2_name() + "'s turn");
                kord_layout.setBackground(new Background(new BackgroundFill(laud.getKaiguvarv(), CornerRadii.EMPTY, Insets.EMPTY)));
                Task<Void> task = new Task<>() {
                    @Override
                    public Void call() {
                        try {
                            String info = dis.readUTF();
                            if (!info.equals("closed")) {
                                int tulp = Integer.parseInt(info);
                                Platform.runLater(() -> laud.place(tulp, gc, kord, kord_layout));
                            } else {
                                dos.writeUTF("exit");
                                Platform.runLater(() -> laud.connectionLost(root, primaryStage));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

                new Thread(task).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void connectionInfo(Board uuslaud, Socket uussock, DataInputStream uusdis, DataOutputStream uusdos) {
        laud = uuslaud;
        laud.setType("Online " + laud.getPlayer2_name());
        sock = uussock;
        dis = uusdis;
        dos = uusdos;
    }

    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene s = new Scene(root, width, height+cell_size*1.5);
        s.getStylesheets().add(Main.class.getResource("/main_stuff.css").toExternalForm());
        primaryStage.setScene(s);
        laud.setback(primaryStage, s, root, dos);
        HBox buttons = new HBox();
        buttons.setMinHeight(cell_size);
        HBox kord_layout = new HBox();
        kord_layout.setAlignment(Pos.CENTER);
        Label kord = new Label();
        kord_layout.getChildren().add(kord);
        kord_layout.setMinHeight(cell_size/2);
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
                        if(!won.get() && laud.getKaik() == 1) {
                            if (laud.checkBoard(finalJ) != -1) {
                                laud.place(finalJ, gc, kord, kord_layout);
                                try {
                                    dos.writeUTF(String.valueOf(finalJ));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            int voitja = laud.checkWin();
                            if (voitja > 0) {
                                laud.win(root, won, voitja, primaryStage);
                                try {
                                    dos.writeUTF("exit");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //primaryStage.setScene(Main.s);
                            }else if(laud.getKaidud() >= board.length*board[0].length){
                                laud.win(root, won, 3, primaryStage);
                                try {
                                    dos.writeUTF("exit");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if(laud.getKaik() == 2){
                                Task<Void> task = new Task<>() {
                                    @Override
                                    public Void call() {
                                        try {
                                            String info = dis.readUTF();
                                            if (!info.equals("closed")) {
                                                int tulp = Integer.parseInt(info);
                                                Platform.runLater(() -> {
                                                    laud.place(tulp, gc, kord, kord_layout);
                                                    int voitja = laud.checkWin();

                                                    if (voitja > 0) {
                                                        laud.win(root, won, voitja, primaryStage);
                                                        try {
                                                            dos.writeUTF("exit");
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if (laud.getKaidud() >= board.length * board[0].length) {
                                                        laud.win(root, won, 3, primaryStage);
                                                        try {
                                                            dos.writeUTF("exit");
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            } else {
                                                dos.writeUTF("exit");
                                                Platform.runLater(() -> laud.connectionLost(root, primaryStage));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        return null;
                                    }
                                };

                                new Thread(task).start();
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

        connect(gc, kord, kord_layout, root, primaryStage);
    }
}
