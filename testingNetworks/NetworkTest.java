import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic Network Tests
 *
 */
public class NetworkTest {



    @Test
    void testConnectionToLocalhost() {
            // Assuming your local server is running on port 8080
            InetAddress address;
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException e) { throw new RuntimeException(e);}
            try {
                assertTrue(address.isReachable(5000), "Localhost is reachable");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }


    @Test
    void testUDPSend(){

    }
}
