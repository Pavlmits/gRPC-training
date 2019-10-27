package com.pavlmits.grpccource.grpc.greeting.client;

import com.proto.calculator.AverageNumber;
import com.proto.calculator.AverageRequest;
import com.proto.calculator.AverageResponse;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AverageClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<AverageRequest> requestStreamObserver = asyncClient.averageNumber(new StreamObserver<AverageResponse>() {
            @Override
            public void onNext(AverageResponse value) {
                // we get a response from the server
                System.out.println("Received a response from server");
                System.out.println(value.getResult());
                // onNext will be called only once
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                //the server is done sending us data
                System.out.println("Server has completed sending us something");
                latch.countDown();
                //onCompleted will be called right after onNext()
            }
        });

        System.out.println("Sending message 1");
        requestStreamObserver.onNext(AverageRequest.newBuilder()
                .setNumber(AverageNumber.newBuilder()
                        .setNumber(1)
                        .build())
                .build());

        requestStreamObserver.onNext(AverageRequest.newBuilder()
                .setNumber(AverageNumber.newBuilder()
                        .setNumber(9)
                        .build())
                .build());

        //we inform server that we complete sending data
        requestStreamObserver.onCompleted();

        //waits for some time the server to response
        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Shutting down");
        channel.shutdown();
    }
}
