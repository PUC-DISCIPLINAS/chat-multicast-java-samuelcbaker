package redes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient {

	private static DatagramSocket aSocket = null;
	private static int serverPort = 6788;
	private static InetAddress aHost;

	public static void main(String args[]) {

		// args fornecem a mensagem e o endere�o do servidor.
		aSocket = null;
		serverPort = 6788;
		String message;
		
		try {

			Scanner in = new Scanner(System.in);
			aSocket = new DatagramSocket();
			aHost = InetAddress.getByName(args[0]);

			while (true){
				System.out.print("Digite o comando desejado: ");
				String command = in.nextLine();

				message = sendToServer(command);

				if(command.contains("join_room")){
					String id = command.split(":")[1];
					String username = command.split(":")[2];
					MulticastPeer multicastPeer = new MulticastPeer(message, username);
					new Connection(multicastPeer);

					String sendMessage = "";
					do {
						sendMessage = in.nextLine();

						if(sendMessage.contains("send_message")) {
							String send = sendMessage.split(":")[1];
							multicastPeer.sendMessage(send);
						} else if(sendMessage.contains("list_members")){
							command = "list_members:" + id + ":" + username;
							sendToServer(command);
						}

					} while (!sendMessage.contains("leave_room"));

					multicastPeer.leaveRoom();

					command = "leave_room:" + id + ":" + username;

					sendToServer(command);

				}

			}


		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

	public static String sendToServer(String command){
		try {
			byte[] m = command.getBytes();
			DatagramPacket request = new DatagramPacket(m, command.length(), aHost, serverPort);
			aSocket.send(request);

			byte[] buffer = new byte[1000];

			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			String message = new String(reply.getData()).trim();

			System.out.println("Resposta: " + message);
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}

class Connection extends Thread {

	private MulticastPeer multicastPeer;

	public Connection(MulticastPeer multicastPeer) {
		this.multicastPeer = multicastPeer;
		this.start();
	}

	public void run() {
		while(true){
			multicastPeer.listenMessages();
		}
	}

	public void close(){
		this.interrupt();
	}
}
