package redes;

import java.io.IOException;
import java.net.*;

public class MulticastPeer {

	private String addressGroup;
	private String username;
	private MulticastSocket mSocket = null;
	private InetAddress groupIp;

	public MulticastPeer(String addressGroup, String username) {
		this.addressGroup = addressGroup;
		this.username = username;
		run();
	}

	public void run() {

		try {
			groupIp = InetAddress.getByName(addressGroup);

			mSocket = new MulticastSocket(6781);
			mSocket.joinGroup(groupIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String content){
		content = username + ": " + content;
		try {
			byte[] message = content.getBytes();
			DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, 6781);
			mSocket.send(messageOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listenMessages(){
		byte[] buffer = new byte[1000];
		try {
			DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
			mSocket.receive(messageIn);
			System.out.println(new String(messageIn.getData()).trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void leaveRoom(){
		try {
			mSocket.leaveGroup(groupIp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
