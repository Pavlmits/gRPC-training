package com.pavlmits.grpccource.grpc.greeting.server;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        //extract fields we need
        Sum requestSum = request.getSum();
        int number1 = requestSum.getNumber1();
        int number2 = requestSum.getNumber2();

        int result = number1 + number2;

        SumResponse response = SumResponse.newBuilder()
                .setResult(result)
                .build();

        //send the response
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<AverageRequest> averageNumber(StreamObserver<AverageResponse> responseObserver) {
        StreamObserver<AverageRequest> requestStreamObserver = new StreamObserver<AverageRequest>() {
            int sum = 0;
            int count = 0;

            @Override
            public void onNext(AverageRequest value) {
                sum += value.getNumber().getNumber();
                count++;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        AverageResponse.newBuilder()
                                .setResult((double)sum / count)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;
    }
}
