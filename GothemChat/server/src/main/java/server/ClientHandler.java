package server;

import service.ServiceMessages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean authenticated;
    private String nickname;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
//                    socket.setSoTimeout(5000);  - через 5 сек выскочит исключение

                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.equals(ServiceMessages.END)) {
                            sendMsg(ServiceMessages.END);
                            break;
                        }
                        if(str.startsWith(ServiceMessages.AUTH)){
                            String[] token = str.split(" ", 3);
                            if(token.length < 3){
                                continue;
                            }
                            String newNick = server.getAuthService()
                                    .getNicknameByLoginPassword(token[1], token[2]);
                            login = token[1];
                            if(newNick != null){
                                if(!server.isLoginAuth(login)){
                                    authenticated = true;
                                    nickname = newNick;
                                    sendMsg(ServiceMessages.AUTH_OK + " " + nickname);
                                    server.subscribe(this);
                                    System.out.println("Client: " + nickname + " authenticated");
                                    //Может быть таймер здесь поставить?
                                    break;

                                } else {
                                    sendMsg("Этот логин уже используется");
                                }
                            }else{
                                sendMsg("Неверный логин или пароль");
                            }
                        }
                        if (str.startsWith(ServiceMessages.REG)){
                            String[] token = str.split(" ", 4);
                            if(token.length < 4){
                                continue;
                            }
                            if(server.getAuthService()
                                    .registration(token[1], token[2], token[3])){
                                sendMsg(ServiceMessages.REG_OK);
                            }else{
                                sendMsg(ServiceMessages.REG_NO);
                            }
                        }
//                        socket.setSoTimeout(0); - таймер отключен
                    }

                    //цикл работы
                    while (authenticated) {
                        String str = in.readUTF();

                        if(str.startsWith("/")) {
                            if (str.equals(ServiceMessages.END)) {
                                sendMsg(ServiceMessages.END);
                                break;
                            }
                            if(str.startsWith("/w")){
                                String[] token = str.split(" ", 3);
                                if(token.length < 3) {
                                    continue;
                                }
                                server.privateMsg(this, token[1], token[2]);
                            }

                        }else {
                            server.broadcastMsg(this, str);
                        }
                    }
                //SocketTimeoutException
                }catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Client disconnect!");
                    server.unsubscribe(this);

                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
