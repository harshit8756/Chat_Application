import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    // make constructor
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Client()
    {
       try
       {
           System.out.println("Sending request to server....");
           socket = new Socket("127.0.0.1" , 7777);
           System.out.println("Connection Done...");
           br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(socket.getOutputStream());

           startReading();
           startWriting();
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    public void startReading()
    {
        // thread - data read kr ke deta rahe ga
        Runnable r1 = ()-> // lambda expression
        {
            System.out.println("Reader started....");
            try
            {
            while(true)
            {

                    String message =  br.readLine();
                    if(message.equals("exit"))
                    {
                        System.out.println("Server has stopped");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : " + message);

                }
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                System.out.println("Connection Closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting()
    {
        // thread - data user se lega and the send karega client tk
        Runnable r2 =()->
        {
            System.out.println("Writer Started .....");
            try
            {
            while(true && !socket.isClosed())
            {

                    BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                }
                System.out.println("Connection is Closed");

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is client....");
        new Client();
    }
}
