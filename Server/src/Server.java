import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static int port = 8081;
    static ArrayList<ClientHandler[]> games = new ArrayList<>();
    static ClientHandler queue_player = null;

    // Used for new lobby system
    //static ArrayList<ClientHandler[]> lobby_players = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        System.out.println("Server Start");
        //games.add(new ClientHandler[]{null, null});
        System.out.println("Avame socketi");
        ServerSocket server_socket = new ServerSocket(port);

        System.out.println("Hakkame kliente otsima");

        while (true) {
            Socket sock = null;

            for(int i = 0; i < games.size(); i++){
                if (games.get(i)[0] == null && games.get(i)[1] == null) {
                    games.remove(i);
                    break;
                }
            }

            try {
                System.out.println("Otsin klienti");
                sock = server_socket.accept();
                System.out.println("Uus klient: " + sock);
                System.out.println(sock.getRemoteSocketAddress());
                System.out.println((InetSocketAddress)sock.getRemoteSocketAddress());

                DataInputStream dis = new DataInputStream(sock.getInputStream());
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

                System.out.println("Assigning new thread for client");

                ClientHandler thread = new ClientHandler(sock, dis, dos);

                thread.start();
                if (queue_player == null) {
                    queue_player = thread;
                } else {
                    games.add(new ClientHandler[]{queue_player, thread});
                    queue_player = null;
                }
            } catch (Exception e) {
                System.out.println("Ei saanud");
                assert sock != null;
                sock.close();
                e.printStackTrace();
            }
        }
    }

}
