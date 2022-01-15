package Lesson6;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    final static String SERVER_ADDR = "localhost";
    final static int SERVER_PORT = 8189;

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        Scanner sc = new Scanner(System.in);
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            System.out.println("Вы подключились к серверу " + socket.getRemoteSocketAddress());
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
                    System.out.println("Потеряно соединение с сервером");
                    out.writeUTF("/end");
                    break;
                } else {
                    System.out.println("Сервер: " + str);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}