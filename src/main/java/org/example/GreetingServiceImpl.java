package org.example;

import io.grpc.stub.StreamObserver;
import org.example.grpc.GreetingServiceGrpc;
import org.example.grpc.GreetingServiceOuterClass;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void greeting(GreetingServiceOuterClass.HelloRequest request,
                         StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {

        System.out.println(request);

        GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass
                .HelloResponse.newBuilder().setGreeting("Hello from server, " + request.getName())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}