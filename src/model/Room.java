package model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public static int LAST_IP = 1;

    int id;
    String name;
    String address;
    List<String> usernames = new ArrayList<>();

    public Room(String name) {
        this.name = name;
        this.id = LAST_IP;
        address = "228.5.6." + LAST_IP;
        LAST_IP++;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void addUser(String name){
        usernames.add(name);
    }

    public void removeUser(String name){
        usernames.remove(name);
    }
}
