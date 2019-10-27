package com.pavlmits.grpccource.grpc.greeting.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SquareRootRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SquareRootClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        int number = -1;
        try {
            blockingStub.squareRoot(SquareRootRequest.newBuilder()
                    .setNumber(number)
                    .build());
        } catch (StatusRuntimeException e) {
            System.err.println("Got an exception for square root");
            e.printStackTrace();
        }

        System.out.println("Shutting down");
        channel.shutdown();

    }
}
