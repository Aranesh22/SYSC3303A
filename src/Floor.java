import java.io.*;


import java.io.IOException;
import java.util.ArrayList;

public class Floor extends Thread{

    public final static float FLOOR_HEIGHT = 3.916f;

    private Synchronizer syncFloor;


    ArrayList<Request> reqList = new ArrayList<>();

    public Floor(Synchronizer sync) {


        this.syncFloor = sync;


    }

    public synchronized void run() {

        for (int i = 0; i < reqList.size(); i++) {

            Request obj = reqList.get(i);
            syncFloor.putRequest(obj);

        }



    }

    public void parseReqFile() throws FileNotFoundException {


        String path = "data/data.csv";
        String line = "";
        try {

            BufferedReader readBuff = new BufferedReader(new FileReader(path));

            int elvId = 0;
            while ((line = readBuff.readLine()) != null) {


                String[] data = line.split(",");
                System.out.println(data[0]);
                System.out.println(data[1]);
                System.out.println(data[2]);
                System.out.println(data[3]);

                String time = data[0];
                int floor = Integer.parseInt(data[1]);
                int destFloor = Integer.parseInt(data[3]);
                String direction = data[2];


                Request req = new Request(time,floor,destFloor,direction,1);

                reqList.add(req);

            }

            for (int i = 0; i < reqList.size(); i++) {
                Request obj = reqList.get(i);
                System.out.println(obj.getTime());
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public float getHeight() {

        return FLOOR_HEIGHT;
    }



}
