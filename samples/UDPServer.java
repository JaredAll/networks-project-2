package clientserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPServer 
{
    DatagramSocket socket = null;
    static Scanner input = new Scanner(System.in);

    public UDPServer() 
    {

    }
    public void createAndListenSocket() 
    {
        try 
        {
            socket = new DatagramSocket(9876);

            while (true) 
            {
            	byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, 
                		incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                
                System.out.println("Received message from client: " + message);
                System.out.println("Client IP:"+IPAddress.getHostAddress());
                System.out.println("Client port:"+port);
                
                String reply = input.nextLine();
                byte[] data = reply.getBytes();
                
                DatagramPacket replyPacket =
                        new DatagramPacket(data, data.length, IPAddress, port);
                
                socket.send(replyPacket);
                Thread.sleep(2000);
            }
        } 
        catch (SocketException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException i) 
        {
            i.printStackTrace();
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        finally 
        {
        	socket.close();
        }
    }

    public static void main(String[] args) 
    {
        UDPServer server = new UDPServer();
        server.createAndListenSocket();
    }
}

