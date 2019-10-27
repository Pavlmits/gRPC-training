package com.pavlmits.grpccource.grpc.greeting.client;

import com.proto.numberdecomposition.PrimeNumberDecomposition;
import com.proto.numberdecomposition.PrimeNumberDecompositionRequest;
import com.proto.numberdecomposition.PrimeNumberDecompositionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PrimeNumberRequest {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();


        PrimeNumberDecompositionServiceGrpc.PrimeNumberDecompositionServiceBlockingStub primeClient =
                PrimeNumberDecompositionServiceGrpc.newBlockingStub(channel);

        PrimeNumberDecompositionRequest primeNumberDecompositionRequest =
                PrimeNumberDecompositionRequest.newBuilder()
                        .setNumber(PrimeNumberDecomposition.newBuilder().setNumber(120))
                        .build();

        primeClient.primeNumberDecomposition(primeNumberDecompositionRequest)
                .forEachRemaining(primeNumberDecompositionResponse -> System.out.println(primeNumberDecompositionResponse.getResult()));


        System.out.println("Shutting down");
        channel.shutdown();
    }
}
