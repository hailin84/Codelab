package org.alive.idealab.learn.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

/**
 * vertx
 */
public class MainVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        // We need sessions and request bodies
        router.route().handler(BodyHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        // Mount the handler for all incoming requests at every path and HTTP method
        router.route("/sayHello").handler(context -> {
            context.response().end("This is sayHello");
        });
        // 默认规则，匹配所有路径，放到最后
        router.route().handler(context -> {
            // Get the address of the request
            String address = context.request().connection().remoteAddress().toString();

            // Get the query parameter "name"
            MultiMap queryParams = context.queryParams();
            String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
            // Write a json response
            context.json(
                    new JsonObject()
                            .put("name", name)
                            .put("address", address)
                            .put("message", "Hello " + name + " connected from " + address)
            );
        });



        // Create the HTTP server
        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8888)
                // Print the port
                .onSuccess(server ->
                        System.out.println(
                                "HTTP server started on port " + server.actualPort()
                        )
                );
    }
}
