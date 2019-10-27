package com.pavlmits.grpccource.grpc.greeting.server;

import com.proto.numberdecomposition.PrimeNumberDecomposition;
import com.proto.numberdecomposition.PrimeNumberDecompositionRequest;
import com.proto.numberdecomposition.PrimeNumberDecompositionResponse;
import com.proto.numberdecomposition.PrimeNumberDecompositionServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PrimeDecompositionService extends PrimeNumberDecompositionServiceGrpc.PrimeNumberDecompositionServiceImplBase {

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        PrimeNumberDecomposition primeNumberDecomposition = request.getNumber();

        int k = 2;
        int number = primeNumberDecomposition.getNumber();
        while (number > 1) {
            if (number % k == 0) {
                PrimeNumberDecompositionResponse response = PrimeNumberDecompositionResponse.newBuilder()
                        .setResult(k)
                        .build();
                responseObserver.onNext(response);
                number = number / k;
            } else {
                k = k + 1;
            }
        }
        responseObserver.onCompleted();
    }
}
