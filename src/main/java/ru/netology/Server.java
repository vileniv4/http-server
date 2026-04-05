package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Server {

    // Поле для хранения хендлеров: метод -> (путь -> хендлер)
    private final Map<String, Map<String, Handler>> handlers = new ConcurrentHashMap<>();

    private final ExecutorService pool;

    public Server(ExecutorService pool) {
        this.pool = pool;
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
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

                        Handler handler = null;
                        Map<String, Handler> innerMap = handlers.get(method);
                        if (innerMap != null) {
                            handler = innerMap.get(path);
                        }

                        Request request = new Request(method, path, null, new HashMap<>());

                        // 2. Читаем заголовки
                        String line;
                        while ((line = reader.readLine()) != null && !line.isEmpty()) {
                            // пропускаем
                        }

                        // 3. Здесь можно читать body, если нужно

                        // 4. Формируем ответ

                        // Создаём out для записи ответа
                        BufferedOutputStream out = new BufferedOutputStream(
                                socket.getOutputStream()
                        );

                        // ... поиск хендлера и создание request ...

                        // 4. Вызываем хендлер или отправляем 404
                        if (handler != null) {
                            handler.handle(request, out);
                        } else {
                            // отправляем 404
                            String response = "HTTP/1.1 404 Not Found\r\n" +
                                    "Content-Length: 9\r\n" +
                                    "\r\n" +
                                    "Not Found";
                            out.write(response.getBytes());
                        }
                        out.flush();

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

    public void addHandler(String method, String path, Handler handler) {

        // Если ключа нет — создаёт новый HashMap, если есть — возвращает существующий
        Map<String, Handler> innerMap = handlers.computeIfAbsent(method, k -> new HashMap<>());
        innerMap.put(path, handler);

    }
}
