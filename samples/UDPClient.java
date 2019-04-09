package clientserver;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient 
{
    DatagramSocket Socket;
    
    static Scanner input = new Scanner(System.in);

    public UDPClient() 
    {

    }

    public void createAndListenSocket() 
    {
        try 
        {
            Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");//150.243.201.164
            byte[] incomingData = new byte[1024];
            String sentence = input.nextLine();
            byte[] data = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
            Socket.send(sendPacket);
            System.out.println("Message sent from client");
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            Socket.receive(incomingPacket);
            String response = new String(incomingPacket.getData());
            System.out.println("Response from server:" + response);
            Socket.close();
        }
        catch (UnknownHostException e) 
        {
            e.printStackTrace();
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) 
    {
        UDPClient client = new UDPClient();
        while (true)
        {
        	client.createAndListenSocket();
        }
    }
}

