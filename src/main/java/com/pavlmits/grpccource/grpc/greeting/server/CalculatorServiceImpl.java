package com.pavlmits.grpccource.grpc.greeting.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                                .setResult((double) sum / count)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;
    }

    @Override
    public StreamObserver<FindMaximumRequest> findMaximum(StreamObserver<FindMaximumResponse> responseObserver) {
        StreamObserver<FindMaximumRequest> requestObserver = new StreamObserver<FindMaximumRequest>() {
            List<Integer> numbers = new ArrayList<>();

            @Override
            public void onNext(FindMaximumRequest value) {
                numbers.add(value.getNumber());
                FindMaximumResponse findMaximumResponse = FindMaximumResponse.newBuilder()
                        .setMaximum(Collections.max(numbers))
                        .build();
                responseObserver.onNext(findMaximumResponse);
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
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        int number = request.getNumber();

        if (number >= 0) {
            double numberRoot = Math.sqrt(number);
            responseObserver.onNext(
                    SquareRootResponse.newBuilder()
                            .setNumberRoot(numberRoot)
                            .build()
            );
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The number being sent is not positive")
                                .augmentDescription("Number sent: " + number)
                            .asRuntimeException()
            );
        }
    }
}
