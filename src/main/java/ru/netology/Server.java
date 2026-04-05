package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server {
    private final ExecutorService pool;

    public Server(ExecutorService pool) {
        this.pool = pool;
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();
                pool.submit(() -> {
                    try {
                        // 0. Создаём читатель для сокета
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream())
                        );

                        // 1. Читаем Request Line
                        String requestLine = reader.readLine();
                        String[] parts = requestLine.split(" ");
                        if (parts.length != 3) return;

                        String method = parts[0];   // GET
                        String path = parts[1];     // /index.html

                        // 2. Читаем заголовки
                        String line;
                        while ((line = reader.readLine()) != null && !line.isEmpty()) {
                            // пропускаем
                        }

                        // 3. Здесь можно читать body, если нужно

                        // 4. Формируем ответ
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 12\r\n" +
                                "\r\n" +
                                "Hello, World!";

                        // 5. Пишем ответ в socket
                        BufferedOutputStream out = new BufferedOutputStream(
                                socket.getOutputStream()
                        );
                        out.write(response.getBytes());
                        out.flush();
                        // out.close(); // не нужно, socket.close() в finally закроет всё

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
