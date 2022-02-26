import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler>clientHandlers=new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket=socket;
            this.bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername =bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER :"+clientUsername +" just joined the chat");

        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    @Override
    public void run() {
        String messageForClient;
        while (socket.isConnected())
        {
            try{
                messageForClient =bufferedReader.readLine();
                broadcastMessage(messageForClient);

            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }

   public void broadcastMessage(String messageSend){
        for(ClientHandler clientHandler:clientHandlers){
            try {
                if(!clientHandler.clientUsername.equals(clientUsername))
                {
                    clientHandler.bufferedWriter.write(messageSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                }
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);

            }
        }
   }
   public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER :"+clientUsername+" HAS LEFT THE CHAT");
   }
   public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removeClientHandler();
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
}