import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import customer.CustomerServiceGrpc;
import customer.Customer.*;

public class CustomerClient {
    public static void main(String[] args) {
        // Create a channel to communicate with the gRPC server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // Disable TLS for local testing
                .build();

        // Create a blocking stub for synchronous communication
        CustomerServiceGrpc.CustomerServiceBlockingStub stub = CustomerServiceGrpc.newBlockingStub(channel);

        // Create a request object
        CustomerRequest request = CustomerRequest.newBuilder()
                .setName("John Doe")
                .setIdNumber("123456789")
                .setAddress("123 Main Street, Cityville")
                .build();

        // Make the RPC call
        CustomerResponse response = stub.addCustomer(request);

        // Print the response
        System.out.println("Response from server: " + response.getMessage());

        // Shutdown the channel
        channel.shutdown();
    }
}
