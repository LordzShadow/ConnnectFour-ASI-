package connnectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Lobby extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Muutujad serveriga ühenduse jaoks
    Socket sock = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    String ip = "localhost";
    int port = 8081;

    // Ekraani suurus
    int width = Main.getWidth();
    int height = Main.getHeight();

    // Laua objekt, et saaks selle anda MultiOnline klassile
    Board laud = new Board(7, 6);

    // Fondid
    Font title_font = Main.getTitleFont();
    Font loading_font = Font.font(Main.getFont_family(), 30);
    Font buttonFont = Main.getButtonFont();

    volatile boolean connection_closed = false;

    private void connect_to_game(Stage primaryStage, String ip_string) {

        try {
            //10.43.248.42
            InetAddress ip = InetAddress.getByName(ip_string);

            // Loome socketi
            sock = new Socket(ip, port);

            // Input/Output Streamid, et suhelda serveriga
            dis = new DataInputStream(sock.getInputStream());
            dos = new DataOutputStream(sock.getOutputStream());

            // Ootame serverit sõnumit, mis on kas -1(Oled queues) või käigu number
            int message = dis.readInt();
            while (message == -1) {
                if (connection_closed) {
                    dos.writeUTF("closed");
                    sock.close();
                    dis.close();
                    dos.close();
                    return;
                }
                dos.writeUTF("continue");
                message = dis.readInt();
            }
            // Paneb laua objektil enda käigu numbri kirja
            laud.setKaik(message);

            // Saadan serverile oma nime, et see saadaks selle teisele mängijale
            dos.writeUTF(Main.getProps().getProperty("name", "Opponent"));
            // Saan serverilt teise mängija nime
            laud.setPlayer2_name(dis.readUTF());

            // Suunan mängu
            MultiOnline online = new MultiOnline();
            Platform.runLater(() -> {
                online.connectionInfo(laud, sock, dis, dos);
                online.start(primaryStage);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadingScreen(Stage primaryStage, StackPane root, Thread connecting) {
        // Tühjendan ekraani
        root.getChildren().clear();

        // Teen ekraanile Ootamise elemendid(Label, ProgressIndicator)
        VBox layout = new VBox();
        Label label = new Label("In Lobby, Looking for an opponent!");
        label.setFont(loading_font);
        ProgressIndicator loading = new ProgressIndicator();

        Button back_btn = new Button();
        back_btn.getStyleClass().add("custom_button");
        back_btn.setText("Back to Main");
        back_btn.setFont(buttonFont);
        back_btn.setOnAction(e -> {
            connection_closed = true;
            connecting.interrupt();
            primaryStage.setTitle("ConnnectFour");
            primaryStage.setScene(Main.s);
        });

        loading.setPrefSize(100, 100);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, loading, back_btn);
        layout.setSpacing(height/layout.getChildren().size());

        root.getChildren().add(layout);
    }

    public void start(Stage primaryStage) {
        //ip = "localhost";
        primaryStage.setTitle("ConnnectFour -> Multiplayer -> Lobby");
        StackPane root = new StackPane();
        Scene s = new Scene(root,width, height);
        s.getStylesheets().add(Main.class.getResource("/main.css").toExternalForm());
        s.getStylesheets().add(Main.class.getResource("/main_stuff.css").toExternalForm());
        primaryStage.setScene(s);
        // Task, mis pärast jookseb threadil.
        // Üritab connectida ja kui connectitud suunab möngu;
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                connect_to_game(primaryStage, ip);
                return null;
            }
        };

        // lobby elemendid vms
        // IP peaks panema settingutesse ja siis selle siit üldse ära võtma

        Label main_title = new Label();
        main_title.getStyleClass().add("title");
        main_title.setText("Online");
        main_title.setFont(title_font);

        VBox main_layout = new VBox();
        Label ip_address = new Label("Server IP:");
        ip_address.setFont(loading_font);
        TextField ip_address_text = new TextField();
        ip_address_text.setText(ip);
        ip_address_text.setMaxWidth(200);
        Button connect = new Button("Connect to server!");
        connect.getStyleClass().add("custom_button");
        connect.setFont(buttonFont);
        connect.setOnAction(e -> {
            ip = ip_address_text.getText();
            // connectimine
            Thread connecting = new Thread(task);
            loadingScreen(primaryStage, root, connecting);
            connecting.setDaemon(true);
            connecting.start();


        });

        Button back_btn = new Button();
        back_btn.getStyleClass().add("custom_button");
        back_btn.setText("Back to Main");
        back_btn.setFont(buttonFont);
        back_btn.setOnAction(e -> {
            primaryStage.setTitle("ConnnectFour");
            primaryStage.setScene(Main.s);
        });

        main_layout.getChildren().addAll(main_title, ip_address, ip_address_text, connect, back_btn);
        main_layout.setAlignment(Pos.CENTER);
        main_layout.setSpacing(height/main_layout.getChildren().size()-50);
        root.getChildren().add(main_layout);

        primaryStage.show();

    }
}

