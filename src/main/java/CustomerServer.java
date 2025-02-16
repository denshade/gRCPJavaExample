import io.grpc.Server;
import io.grpc.ServerBuilder;
import customer.*;
import customer.Customer.*;

import java.io.IOException;

public class CustomerServer {

    private Server server;

    private void start() throws IOException {
        int port = 50051; // Port number for the gRPC server
        server = ServerBuilder.forPort(port)
                .addService(new CustomerServiceImpl())
                .build()
                .start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            CustomerServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

        @Override
        public void addCustomer(CustomerRequest request, io.grpc.stub.StreamObserver<CustomerResponse> responseObserver) {
            boolean success = true; // Assume operation is successful
            String message = "Customer added successfully";

            if (!success) {
                message = "Failed to add customer";
            }

            CustomerResponse response = CustomerResponse.newBuilder()
                    .setSuccess(success)
                    .setMessage(message)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final CustomerServer server = new CustomerServer();
        server.start();
        server.blockUntilShutdown();
    }
}