package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class LibraryServer {
    private final Server server;

    public LibraryServer(int port) throws IOException {
        this.server = ServerBuilder.forPort(port)
                .addService(new LibraryServiceImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started on port " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is shutting down");
            try {
                LibraryServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("Server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        LibraryServer server = new LibraryServer(8080);
        server.start();
        server.blockUntilShutdown();
    }
}
