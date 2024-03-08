public class ElevatorStatus {

    //Fields
    private final int currentFloor;

    private final int targetFloor;

    private final int recievePortNum;
    private final boolean moving;
    private final String direction;

    public ElevatorStatus() {

        currentFloor = -1;
        targetFloor = -1;
        recievePortNum = -1;
        moving = false;
        direction = null;

    }
    public ElevatorStatus (int currentFloor, int targetFloor, int recievePortNum, boolean moving, String direction) {

        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
        this.recievePortNum = recievePortNum;
        this.moving = moving;
        this.direction = direction;
    }

    public ElevatorStatus(String udpString)  {

        String[] data = udpString.split(",");
        this.currentFloor = Integer.parseInt(data[0]);
        this.targetFloor = Integer.parseInt(data[1]);
        this.recievePortNum = Integer.parseInt(data[2]);
        this.moving = Boolean.parseBoolean(data[3]);
        this.direction = data[4];

    }

    public int getCurrentFloor() {

        return currentFloor;
    }

    public int getTargetFloor() {

        return targetFloor;
    }

    public int getRecievePortNum() {

        return recievePortNum;
    }

    public boolean getMoving() {

        return moving;
    }

    public String getDirection() {

        return direction;
    }

    public byte[] toUdpStringBytes() {

        return (this.currentFloor + "," + this.targetFloor + "," + this.recievePortNum + "," + this.moving + "," +this.direction).getBytes();
    }

    @Override
    public String toString() {

        return "Current Floor:" + this.currentFloor + " | Target Floor:" + this.targetFloor + "| PortNum" + this.recievePortNum + "| Moving" + this.moving + "Direction of Cart -->" + this.direction;
    }



}
