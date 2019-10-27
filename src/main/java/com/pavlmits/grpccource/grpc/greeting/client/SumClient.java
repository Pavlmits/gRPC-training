package com.pavlmits.grpccource.grpc.greeting.client;

import com.proto.calculator.Sum;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import com.proto.calculator.SumServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        //create a great service client
        SumServiceGrpc.SumServiceBlockingStub sumClient = SumServiceGrpc.newBlockingStub(channel);

        //create protocol buffer sum
        Sum sum = Sum.newBuilder()
                .setNumber1(10)
                .setNumber2(3)
                .build();

        //do the same for SumRequest
        SumRequest sumRequest = SumRequest.newBuilder()
                .setSum(sum)
                .build();

        SumResponse sumResponse = sumClient.sum(sumRequest);

        System.out.println(sumResponse.getResult());

        System.out.println("Shutting down");
        channel.shutdown();
    }
}
