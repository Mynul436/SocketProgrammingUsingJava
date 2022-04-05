import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public  class Server{
    private  ServerSocket serverSocket;
    public Server(int port) throws IOException{
        serverSocket=new ServerSocket(port);
    }
    public void startServer(){
        try{
            while (!serverSocket.isClosed())
            {

                Socket socket= serverSocket.accept();
                System.out.println();
                System.out.println("A New person has come and  connected successfully!!!");
                ClientHandler clienHandler =new ClientHandler(socket);
                Thread thread =new Thread(clienHandler);
                thread.start();
            }
        }catch (IOException e){
            closeServerSocket();
        }
    }
    public void closeServerSocket(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Server is waiting for client!!");
        Server server =new Server(8800);
        server.startServer();
    }

}