package com.pavlmits.grpccource.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        //extract the fields we need
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        //create response
        String result = "Hello " + firstName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        //send the response
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();

        try {
            for (int i = 0; i < 10; i++) {
                String result = "Hello " + firstName + ", response number: " + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();

                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {

        StreamObserver<LongGreetRequest> streamObserverOfRequest = new StreamObserver<LongGreetRequest>() {
            StringBuilder result = new StringBuilder();

            @Override
            public void onNext(LongGreetRequest value) {
                //client sent a message
                result.append("Hello ").append(value.getGreeting().getFirstName()).append(" !\n");
            }

            @Override
            public void onError(Throwable t) {
                //client send an error
            }

            @Override
            public void onCompleted() {
                //client is done
                responseObserver.onNext(
                        LongGreetResponse.newBuilder()
                                .setResult(result.toString())
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
        return streamObserverOfRequest;
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
        StreamObserver<GreetEveryoneRequest> requestObserver = new StreamObserver<GreetEveryoneRequest>() {
            @Override
            public void onNext(GreetEveryoneRequest value) {
                String result = "Hello " + value.getGreeting().getFirstName();
                GreetEveryoneResponse greetEveryoneResponse = GreetEveryoneResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(greetEveryoneResponse);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public void greetWithDeadLine(GreetWithDeadLineRequest request, StreamObserver<GreetWithDeadLineResponse> responseObserver) {

        Context current = Context.current();
        try {
            for (int i = 0; i < 3; i++) {
                if (!current.isCancelled()) {
                    System.out.println("Sleep for 100 ms");
                    Thread.sleep(100);
                } else {
                    return;
                }

            }
            System.out.println("send response");
            responseObserver.onNext(
                    GreetWithDeadLineResponse.newBuilder()
                            .setResult("Hello " + request.getGreeting().getFirstName())
                            .build()
            );

            responseObserver.onCompleted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
