package model;

public class Client {
    private final int port;

    public Client(int port) {
        this.port = port;
    }

    public void send(String message) {
        System.out.println("Send:" + message + port);
    }

    public void receive(String message) {
        System.out.println("Receive: " + message + port);
    }

}