import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler extends Thread {


    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket sock;
    String name = "Player";
    int place;
    volatile boolean ready = false;
    volatile boolean sent = false;
    volatile boolean leitud = false;

    public ClientHandler(Socket sock, DataInputStream dis, DataOutputStream dos) {
        this.sock = sock;
        this.dis = dis;
        this.dos = dos;
    }

    public boolean send(String data){
        try {
            if (this.ready) {
                dos.writeUTF(data);
                System.out.println(this.sock.getPort() + " " + data);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void run() {
        String recieved;
        ClientHandler opponent = null;
        System.out.println("ClientHandler olen");

        while (true) {
            //System.out.println(this.sock.getPort() + " Mina olen: " + this);
            try {
                if (!leitud) {
                    for (int i = 0; i < Server.games.size(); i++) {
                        for (int j = 0; j < 2; j++) {
                            if (Server.games.get(i)[j] == this) {
                                leitud = true;
                                System.out.println("Leidsin vastase");
                                opponent = Server.games.get(i)[1-j];
                                dos.writeInt(j+1);
                                place = j;

                                System.out.println(this.sock.getPort() + " Saatsin oma käigu");
                                this.name = dis.readUTF();
                                System.out.println(this.sock.getPort() + " Minu nimi on: " + this.name);
                                this.ready = true;
                                System.out.println(this.sock.getPort() + " proovin saata enda nime vastasele");
                                while (!sent) {
                                    sent = opponent.send(this.name);
                                }
                                if (sent) {
                                    System.out.println(this.sock.getPort() + " Nimi saadetud");
                                } else {
                                    System.out.println(this.sock.getPort() + " Ei ole saadetud");
                                }

                                break;
                            }
                        }
                        if (leitud) {
                            break;
                        } else {
                            dos.writeInt(-1);
                            if (dis.readUTF().equals("closed")) {
                                if (Server.queue_player == this) {
                                    System.out.println("oh no");
                                    Server.queue_player = null;
                                    return;
                                }
                            }
                        }
                    }
                    if (!leitud) {
                        dos.writeInt(-1);
                        if (dis.readUTF().equals("closed")) {
                            System.out.println("oh no2");
                            if (Server.queue_player == this) {
                                Server.queue_player = null;
                                return;
                            }
                        }
                    }
                } else {
                    recieved = dis.readUTF();

                    if (recieved.equals("exit")) {
                        System.out.println("Client " + this.sock + " sends exit...");
                        System.out.println("Closing this connection.");
                        for(int i = 0; i < Server.games.size(); i++){
                            System.out.println(Arrays.toString(Server.games.get(i)));
                            if (Server.games.get(i)[place] == this) {
                                Server.games.get(i)[place] = null;
                                break;
                            }
                        }
                        this.sock.close();
                        System.out.println("Closed connection!");
                        break;
                    }/*else if(recieved.split(" ")[0].equals("send")){
                        opponent.send(recieved.substring(5));
                    }*/else if(!recieved.equals("")){
                        assert opponent != null;
                        opponent.send(recieved);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection lost!");
                if (opponent != null) {
                    opponent.send("closed");
                    for(int i = 0; i < Server.games.size(); i++){
                        System.out.println(Server.games.get(i)[place]);
                        System.out.println(this);
                        if (Server.games.get(i)[place] == this) {
                            Server.games.get(i)[place] = null;
                            break;
                        }
                    }
                } else {
                    System.out.println("Emptying queue");
                    Server.queue_player = null;
                }

                try {
                    this.sock.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }

        try {
            this.dis.close();
            this.dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
