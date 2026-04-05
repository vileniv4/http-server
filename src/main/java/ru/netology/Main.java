import ru.netology.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(64);
        Server server = new Server(pool);  // внедрение зависимости
        server.start(9999);
    }
}