package ru.netology;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(64);
        Server server = new Server(pool);

        // хендлеры
        server.addHandler("GET", "/", (request, out) -> {
            try {
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: 11\r\n" +
                        "\r\n" +
                        "Hello, Main!";
                out.write(response.getBytes());
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        server.addHandler("GET", "/messages", (request, out) -> {
            try {
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: 14\r\n" +
                        "\r\n" +
                        "Messages Page!";
                out.write(response.getBytes());
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        server.start(9999);
    }
}