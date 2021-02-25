// Network Calculator
// Class MathServer
// Davis Insua

import java.net.*;
import java.io.*;
import java.util.regex.*;

// Class to handle client
 class ClientHandler extends Thread
 {
   Socket client = null;
   static int count = 0;
   
   // Constructor
   ClientHandler(Socket client)
   {
      this.client = client;
   }
   
   public void run()
   {
      try
      {
         // Setup
         System.out.println("Connection from "+client.getInetAddress().toString());
         System.out.println("Port : "+client.getRemoteSocketAddress());
         PrintWriter outgoing;
         BufferedReader incoming;
         // Define input and output
         outgoing = new PrintWriter(client.getOutputStream());
         incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
         String intext, outext;
         
         // Create pattern to parse math strings transferred by the calculator application
         Pattern pattern = Pattern.compile("(\\d+)\\s*(\\+|\\*|\\-|\\/)\\s*(\\d+)");
         
         double answer = 0.0;
         
         while(true)
         {
            intext = incoming.readLine();
            System.out.println("Client input: "+intext);
            if (intext.equals("exit")||intext==null)
            {
             System.out.println("disconection");
             break;
            }
            
            // Driver for calculator
            Matcher matcher = pattern.matcher(intext);
            if (matcher.find())
            {
               // Addition
               if (matcher.group(2).equals("+"))
               {
                  answer = Double.parseDouble(matcher.group(1))+Double.parseDouble(matcher.group(3));
                  System.out.println("Server calculation: "+answer);
                  outgoing.println(answer);
                  outgoing.flush();
               }
               
               // Subtraction
               else if (matcher.group(2).equals("-"))
               {
                  answer = Double.parseDouble(matcher.group(1))-Double.parseDouble(matcher.group(3));
                  System.out.println("Server calculation: "+answer);
                  outgoing.println(answer);
                  outgoing.flush();
               }
               
               // Multiply
               else if (matcher.group(2).equals("*"))
               {
                  answer = Double.parseDouble(matcher.group(1))*Double.parseDouble(matcher.group(3));
                  System.out.println("Server calculation: "+answer);
                  outgoing.println(answer);
                  outgoing.flush();
               }
               
               // Divide
               else if (matcher.group(2).equals("/"))
               {
                  answer = Double.parseDouble(matcher.group(1))/Double.parseDouble(matcher.group(3));
                  System.out.println("Server calculation: "+answer);
                  outgoing.println(answer);
                  outgoing.flush();
               }
            }
         }
         client.close();
      }
        catch (Exception e) 
        {
            System.out.println("Error:  " + e);
            return;
        }
   }
 }
 
public class MathServer 
{   
    public static final int LISTENING_PORT = 6000;

    public static void main(String[] args) {

        ServerSocket listener;
        Socket connection; 
        
        // Create client handler   
        ClientHandler ct;

        try 
        {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            
            while (true) 
            {
                connection = listener.accept(); 
                ct = new ClientHandler(connection);
                ct.start();
            }
        }
        
        catch (Exception e) 
        {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }
    } 
}