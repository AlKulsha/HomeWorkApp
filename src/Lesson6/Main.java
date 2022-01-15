package Lesson6;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        Scanner sc = new Scanner(System.in);
        final int PORT = 8189;
        ServerSocket server = new ServerSocket(PORT);
        try {
            System.out.println("Сервер запущен");
            socket = server.accept();  //создается клиентский сокет и ссылка передается в socket
            System.out.println("Клиент подключился" + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //Поток на чтение
            Thread readThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        try {
                            out.writeUTF(sc.nextLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            readThread.setDaemon(true);
            readThread.start();

            while (true) {
                String str = in.readUTF();
                if (str.equals("/end")) {
                    System.out.println("Клиент отключился");
                    out.writeUTF("/end");
                    break;
                }else{
                    System.out.println("Клиент: " + str);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            }catch (IOException | NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}