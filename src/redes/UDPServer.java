package redes;

import model.Room;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {
	public static List<Room> rooms = new ArrayList<>();

	public static void main(String args[]) {
		
		DatagramSocket aSocket = null;
		
		String message;

		try {

			aSocket = new DatagramSocket(6788);

			System.out.println("Servidor: ouvindo porta UDP/6788.");
			byte[] buffer = new byte[1000];

			while (true) {
				buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				message = new String(request.getData()).trim();

				String response = getResponse(message);

				System.out.println("Servidor: recebido \'" + message  + "\'.");
				DatagramPacket reply = new DatagramPacket(response.getBytes(), response.length(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
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

	public static String getResponse(String message){
		String response = "";

		if(message.contains("create_room")){

			System.out.println("Criando sala...");
			String nameRoom = message.split(":")[1];
			Room room = new Room(nameRoom);
			rooms.add(room);
			response = String.valueOf(room.getId());

		} else if(message.contains("list_rooms")){

			response = "\nSalas:";
			for (Room room : rooms) {
				response += "\n";
				response += room.getName() + " --> " + room.getId();
			}

		} else if(message.contains("join_room")){

			String id = message.split(":")[1];
			String userName = message.split(":")[2];

			for (Room room : rooms) {
				if(Integer.parseInt(id) == room.getId()){
					room.addUser(userName);
					response = room.getAddress();
				}
			}

		} else if(message.contains("leave_room")){
			String id = message.split(":")[1];
			String userName = message.split(":")[2];

			for (Room room : rooms) {
				if(Integer.parseInt(id) == room.getId()){
					room.removeUser(userName);
					response = "Saiu da sala";
				}
			}
		} else if(message.contains("list_members")){
			response = "\nMembros da sala:";
			String id = message.split(":")[1];

			for (Room room : rooms) {
				if(Integer.parseInt(id) == room.getId()){
					for(String member: room.getUsernames()){
						response += "\n";
						response += member
						;
					}
				}
			}
		}

		return response;
	}
}
