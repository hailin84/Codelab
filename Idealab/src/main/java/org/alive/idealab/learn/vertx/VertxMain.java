package org.alive.idealab.learn.vertx;

import io.vertx.core.Vertx;

public class VertxMain {
    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());

    }
}
