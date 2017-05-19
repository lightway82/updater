package org.anantacreative.updater.tests.server;


import io.vertx.core.Vertx;

public class TestingUpdateServer {
    private static boolean started = false;
    private static Vertx vertx;
    private static Server server;

    /**
     * Запуск сервера если еще не запущен
     */
    synchronized public static void startServer() {
        if (started) return;

        System.out.println("Server is starting...");
        System.setProperty("vertx.disableFileCaching", "true");
        vertx = Vertx.vertx();
        server = new Server();
        vertx.deployVerticle(new Server(), res -> {
            if (!res.failed()) {
                started = true;
            } else throw new RuntimeException("Server not running");

        });

        while (started==false){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getPort() {
        return server.getPort();
    }

}
