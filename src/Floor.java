import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

public class Floor extends Thread{
    public final static float DEFAULT_FLOOR_HEIGHT = 3.916f;
    public final static int DEFAULT_MIN_FLOOR = 1;
    public final static int DEFAULT_MAX_FLOOR = 7;

    private Synchronizer syncFloor;
    ArrayList<Request> reqList = new ArrayList<>();

    public Floor(Synchronizer sync) {
        this.syncFloor = sync;
    }

    public synchronized void run() {
        try {
            parseReqFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < reqList.size(); i++) {

            Request obj = reqList.get(i);
            syncFloor.putRequest(obj);

        }
        while (syncFloor.isRunning()) {
            System.out.println("Floor: Elevator at " + syncFloor.getCurrentFloor());
        }
        System.out.println("Floor: Has exited");
    }

    public void parseReqFile() throws FileNotFoundException {

        String path = "data/data.csv";
        String line;
        try {

            BufferedReader readBuff = new BufferedReader(new FileReader(path));
            while ((line = readBuff.readLine()) != null) {

                String[] data = line.split(",");

                String time = data[0];
                int floor = Integer.parseInt(data[1]);
                int destFloor = Integer.parseInt(data[3]);
                String direction = data[2];

                reqList.add(new Request(time,floor,destFloor,direction));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}