package com.pavlmits.grpccource.grpc.greeting.server;

import com.proto.calculator.Sum;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import com.proto.calculator.SumServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {

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
}
