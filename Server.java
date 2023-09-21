import java.io.*;
import java.net.*;
public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // make object of the contructor
    public Server()
    {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection ");
            System.out.println("Waiting.......");
            socket = server.accept();

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

    // we use multiple threading to connect both the function , because they run on same time
    public void startReading()
    {
    // thread - data read kr ke deta rahe ga
        Runnable r1 = ()-> // lambda expression
        {
            System.out.println("Reader started....");
            try
            {
                while (true)
                {
                    String message = br.readLine();
                    if (message.equals("exit"))
                    {
                        System.out.println("Client has stopped");
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
                while (true && !socket.isClosed())
                {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                }
            }
            catch (Exception e)
            {
               // e.printStackTrace();
                System.out.println("Connection Closed");

            }
        };
        new Thread(r2).start();

    }
    public static void main(String[] args) {
        System.out.println("this is server .... going to start");
        new Server();
    }
}
