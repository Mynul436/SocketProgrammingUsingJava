import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.SplittableRandom;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(String host,int port, String username) {
        try{
            socket=new Socket(host,port);
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username=username;

        }catch (IOException e)
        {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner =new Scanner(System.in);
            while (socket.isConnected()){
                String message=scanner.nextLine();
                bufferedWriter.write(username+":"+message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    public void listenForMessage(){
        new Thread(() -> {
            String msg;
            while (socket.isConnected()){
                try {
                    msg=bufferedReader.readLine();
                    System.out.println(msg);
                }catch (IOException e){
                    closeEverything(socket,bufferedReader,bufferedWriter);
                }
            }

        }).start();
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter)
    {
        try {
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner =new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username=scanner.nextLine();
        Client client =new Client("localhost",8800,username);
        client.listenForMessage();
        client.sendMessage();
    }
}