import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Floor Class Unit Tests
 * @version Iteration1
 * @date February 3, 2024
 */

public class UTestFloor {
    @Test
    void testRequest_parseReqFile() {

        Request rTest = new Request("14:05:15.0000",1,7,"down");
        assertNotNull(rTest);

    }

}