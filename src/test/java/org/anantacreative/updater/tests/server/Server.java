package org.anantacreative.updater.tests.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;


public class Server extends AbstractVerticle {

    private int port;

    public int getPort() {
        return port;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);

        // Serve the static pages
        router.route().handler(StaticHandler.create());
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router::accept).listen(0, res -> {
            if (!res.failed()) {
                port = server.actualPort();
                System.out.println("Server is started");
                startFuture.complete();
            } else startFuture.failed();
        });


    }


}
