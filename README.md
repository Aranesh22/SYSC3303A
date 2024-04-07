# Elevator Control System - SYSC 3303 A

This project simulates an elevator control system using a real-time multi-threading to manage multiple elevators and floor requests. The system ensures that all elevator cars operate efficiently and respond to user requests. The project is developed iteratively, with each iteration introducing new features and improvements.


## Group Members

🏅 Yehan De Silva, 101185388<br>
🏅 Pathum Danthanarayana, 101181411<br>
🏅 Aranesh Athavan, 101152794<br>
🏅 Lindsay Dickson, 101160876<br>
🏅 Harishan Amutheesan, 101154757<br>

## How to Run Project
1. Compile and build the project.
2. ```data``` folder must contain ```data.csv``` as well as error files for each elevator.<br>
![image](https://github.com/Aranesh22/SYSC3303A/assets/69320325/830db496-aa2b-4b6e-9db0-ada7f472813f)
3. Modify data.csv file for new floor requests. Format for each line must be: <br>
   ```[time(String)],[source floor(int)],[destination floor(int)],[direction(up/down)],[passenger count(int)],``` <br>
   ![image](https://github.com/Aranesh22/SYSC3303A/assets/69320325/cb3b4f34-eeb4-40cf-bf6c-355c8ca07209)
4. Modify error file for each elevator. Name of file must be ```error[id].csv```. <br>
Format for each line must be: <br>
 ```[Floor error occurs on (int)],[Floor travel time seconds (int)],[Unload/load time seconds (int)],``` <br>
 ![image](https://github.com/Aranesh22/SYSC3303A/assets/69320325/e148ed6f-634c-46ea-92df-109f9cd2b099)
5. Create an **Application** run configuration for:
   - Scheduler
   - Floor Subsystem
   - Elevator Subsystem
     - Each elevator subsystem should have its own application run configuration
     - Arguments to the application should be the id of the elevator
     - ![image](https://github.com/Aranesh22/SYSC3303A/assets/69320325/a4d6f7c4-ab2d-48d9-a752-b31049cb11d8)

6. Create a **Compound** run configuration containing all the applications made in the previous step. <br>
![image](https://github.com/Aranesh22/SYSC3303A/assets/69320325/1f37946d-95aa-458b-a128-396ae106ca84)
7. Run compoound run configuration

## Breakdown of Responsibilities (Iteration 5)
* Yehan De Silva
  * Implemented and Tested elevator passanger count/capacity
* Pathum Danthanarayana
  * Implemented the GUI using JFrame
* Aranesh Athavan
  * Implemented timing and timestamp functionality
* Lindsay Dickson
  * updated README file 
  * Implemented timing and timestamp functionality
* Harishan Amutheesan
  * Implemented timing and timestamp functionality 
  
## Breakdown of Responsibilities (Iteration 4)
* Yehan De Silva
  * Corrected bugs with scheduler algorithm
  * Implemented Elevator error injection, detection and handling
  * Updated elevator state machine diagram
  * Updated the README file with new system information and documentation

* Pathum Danthanarayana
  * Corrected bugs with scheduler algorithm
  * Updated scheudler state machine diagram

* Aranesh Athavan
  * Implemented Scheduler/Floor subsystem fault handling
  * Drew timing diagram for elevator/scheduler fault detection/handling

* Lindsay Dickson
  * Refactored and created additional tests to examine networking
  * Addressed compatibility issues with existing tests due to the refactoring

* Harishan Amutheesan
  * Revised the UML class diagram to represent the system's latest architectural changes
  * Implemented Scheduler/Floor subsystem fault handling
 
## Breakdown of Responsibilities (Iteration 3)
* Yehan De Silva
  * Designed new state machine diagram for Elevator subsystem and completed its implementation
  * Implemented new scheduler algorithm

* Pathum Danthanarayana
  * Designed new state machine diagram for Scheduler subsystem and completed its implementation
  * Implemented new scheduler algorithm

* Aranesh Athavan
  * Updated the UML sequence diagram to reflect changes in the system
  * Implemented Elevator Status Class
  * Implemented UDP change for Scheduler


* Lindsay Dickson
  * Refactored and created new tests to accommodate the switch from synchronizer to UDP
  * Addressed compatibility issues with existing tests due to the refactoring

* Harishan Amutheesan
  * Revised the UML class diagram to represent the system's latest architectural changes
  * Updated the README file with new system information and documentation


## Breakdown of Responsibilities (Iteration 2)
* Yehan De Silva
  * Broke down possible states, events and actions
  * Constructed Elevator & Scheduler State Diagrams
  * Updated README file

* Pathum Danthanarayana
  * UML sequence diagram + UML class diagram
  * Developed JUnit tests for SchedulerState classes

* Aranesh Athavan
  * Planned approach on how to implement state design pattern in the project
  * UML sequence diagram + UML class diagram
  * Developed JUnit tests for ElevatorState classes

* Lindsay Dickson
  * Planned approach on how to implement state design pattern in the project
  * Developped ElevatorState interface + inner state classes

* Harishan Amutheesan
  * Planned approach on how to implement state design pattern in the project
  * Developped SchedulerState interface + inner state classes

## Breakdown of Responsibilities (Iteration 1)
* Yehan De Silva
  * Developed Elevator class
  * Code integration
  * UML class diagram

* Pathum Danthanarayana
  * Developed Synchronizer class
  * Developed JUnit tests for Synchronizer class
  * UML class diagram
  * README file

* Aranesh Athavan
  * Developed FloorRequestSimulator class
  * Developed JUnit tests for FloorRequestSimulator class

* Lindsay Dickson
  * Developed Main class and FloorRequest class
  * Developed JUnit tests for FloorRequest class, Elevator class
  * UML Sequence diagram

* Harishan Amutheesan
  * Developed Scheduler class
  * Developed JUnit tests for Scheduler class
  * UML class diagram
  * README file

## Iteration 5

Iteration 5 introduced a GUI, capacity parameter limits to elevators and monitoring of action times and the implementation of timestamps

### GUI 

The GUI is implemented in JFrame and adds a display console showing where each of the elevators is in real time and displaying any faults (if any)
***Breakdown of structure for the GUI***

* The JFrame's content pane contains a JPanel called 'elevatorsPanel', which uses a FlowLayout and contains each individual ElevatorCar
* An ElevatorCar is modelled by another JPanel (called 'mainPanel') which contains three sub panels. The first sub panel contains the elevator's name and passenger count, the second sub panel contains the elevator's control panel (floor number display, and elevator direction triangles), and the elevator doors.
  * Each of these sub panels contain child panels which are positioned correctly using certain layouts
* In order to model the direction triangles, a class called ElevatorDirection class was created which extends JPanel.
  * The JPanel has a set width and height of 18. Its default implementation of the paintComponent() method is overridden, so that when the JPanel is drawn into view, it will instead draw a Polygon with three points (triangle). The coordinates for these three points are determined based on the specified direction (up or down).
  * Since the dimensions of the JPanel is 18 x 18, it turns out that the max height for a particular point is 0, and the x-axis lies on x = 18
    * Therefore, to form a triangle pointing upwards, the following points can be used: (0, 18), (9, 0), (18, 18)

    * To form a triangle pointing downwards, the following points can be used: (0, 0), (9, 18), (18, 0)

***functionality:***
* A console displays system messages and states of the system in real-time
* Successfully registered elevators are displayed and their current floor and direction of travel is shown 
* An elevator opening/closing its doors is displayed 
* For each elevator one of two triangles will turn green to indicate the current direction of travel
* When a fault is detected for a specific elevator, it's floor number is changed to 'ERR'



## Iteration 4 

Iteration 4 introduced error injections and handling


## Iteration 3

Iteration 3 advances the elevator control system with a state machine architecture, enhancing the responsiveness and robustness of the system. This iteration also adds complexity to the interaction between different subsystems, providing a closer simulation to real-world scenarios.

### Scheduler State Machine

The Scheduler functions as the central command, coordinating between the FloorRequests and the Elevators through various states:
- **WaitingForPacket**: Awaits incoming UDP packets containing either FloorRequests or ElevatorStatuses.
- **CheckingPacketType**: Identifies the type of the received packet and routes it for appropriate handling.
- **ProcessingFloorRequest**: Allocates the most suitable elevator to fulfill an incoming FloorRequest.
- **SavingFloorRequest**: Holds FloorRequests when no suitable elevator is immediately available, queuing them for later service.
- **ProcessingElevatorStatus**: Processes updates from elevators, maintains an internal task queue, and communicates status to the Floor subsystem.

### Elevator State Machine

Elevators in the system mirror real-life behavior with distinct states:
- **StationaryDoorsClosed**: The elevator is idle with its doors closed.
- **WaitingForReceiver**: Awaits new requests from the Scheduler.
- **StationaryDoorsOpen**: Opens its doors to allow passengers to board or exit.
- **MovingDoorsClosed**: In motion towards its target floor with doors closed.

### Communication

The system leverages UDP packets for inter-subsystem communication, with `ElevatorMessage` objects facilitating the transfer of target floor information and receiver port numbers.

### Key Components

- **ElevatorReceiver**: Listens for and passes FloorRequests from the Scheduler to the Elevator via the `ElevatorRequestBox`.
- **ElevatorRequestBox**: A thread-safe construct that allows for synchronized message exchange between the Elevator Receiver and Elevator subsystem.
- **ElevatorMessage**: Defines the structure for network messages, which contain essential information for the Scheduler-Elevator interaction.

This iteration's implementation demonstrates an advanced, real-time elevator control system capable of handling concurrent elevator requests efficiently.

### Scheduler Algorithm
#### Storing Elevator Information
Within the Scheduler, an ElevatorTaskQueue is created for each elevator. The task queue itself
stores the IP address of the elevator's receiver, its most recent ElevatorStatus, and an ArrayList
of floors to visit.
#### Received Packet
If the Scheduler receives a packet, it can be either a new FloorRequest (sent from Floor subsystem),
or an ElevatorStatus from an elevator. The Scheduler will distinguish between the two, and process them
accordingly
#### FloorRequest received
If the received packet is a FloorRequest, the Scheduler will first check if there's any elevators that
are suitable to service the request. A suitable elevator is an elevator that is either stationary or
is moving in the same direction as the FloorRequest. If at least a one suitable elevator is found,
the Scheduler will then select the closest elevator from the group of suitable elevators.

Once found, the Scheduler will add the FloorRequest to the elevator's list of floors to visit (adds the start floor followed by the end floor).
Lastly, the Scheduler will send the next floor to visit to the elevator's receiver (in this case, the starting floor of the new FloorRequest)
via UDP. Once the packet is sent, the Scheduler will return to waiting for another packet. If no suitable elevators are found, the FloorRequest is saved in Scheduler to be serviced at a later time.

#### ElevatorStatus received
If the Scheduler received an ElevatorStatus, it will first check if it has received an ElevatorStatus
from the elevator before. If not, it will create a new ElevatorTaskQueue object for the elevator.
Otherwise, it will simply take the new ElevatorStatus and update the elevator's existing ElevatorStatus in its
task queue.

Next, the Scheduler will see if the elevator has reached its target floor (i.e. completed servicing its request).
If so, it will remove the floor that the elevator just reached (target floor) from its list of floors to visit,
and try to get the next floor to visit.

If there is a next floor to visit, the Scheduler will send the next floor to the elevator's receiver via UDP.
If the elevator has no more floors to visit, the Scheduler will look through all the FloorRequests that were saved
to be serviced at a later time, and assign the closest FloorRequest to it.

When assigning the FloorRequest to the elevator, the Scheduler will check if the starting floor of the request
is the same as the current floor that the elevator is currently on. If this is the case, there is no need for the elevator
to 'move' to the current floor by opening/closing its doors again. Therefore, the Scheduler will only add the destination
floor of the FloorRequest to the elevator's list of floors to visit.
(Assumption: If a saved FloorRequest exists, it must have been recently saved. If the FloorRequest
was saved for a long period of time, the elevator would need to stop at the current floor and open/close its doors)

If the elevator is not starting at the same floor it's currently on, then the Scheduler simply adds the starting floor
and destination floor of the FloorRequest to the elevator's list of floors to visit.
Lastly, the Scheduler will send the next floor to the elevator's receiver via UDP (in this case, the starting floor of the FloorRequest).

### Communication Overview
- Elevator's receiver will put an initial ElevatorMessage into the ElevatorRequestBox
- Elevator will get the ElevatorMessage from the ElevatorRequestBox, and sends an ElevatorStatus to the Scheduler via UDP
- Scheduler receives ElevatorStatus from Elevator, processes it, and sends it to the Floor subsystem
- Floor subsystem sends a newly arrived FloorRequest to the Scheduler via UDP
- Scheduler receives the FloorRequest, processes it, and either sends it to the selected elevator's receiver, or saves internally to be serviced later

## Iteration 2

Iteration 2 builds upon iteration 1 by refactoring the project to follow the state design pattern. This iteration introduces the ElevatorState and SchedulerState files.  
[Iteration 2 UML diagrams](https://github.com/Aranesh22/SYSC3303A/tree/main/UML/Iteration%202)


### 🛗Elevator State

* The ElevatorState file consists of inner ElevatorState classes that models each possible Elevator State. Each of the inner classes implements the ElevatorState interface which outlines all possible Elevator Events.

### ⏰ Scheduler State

* The SchedulerState file consists of inner SchedulerState classes that models each possible Scheduler State. Each of the inner classes implements the SchedulerState interface which outlines all possible Scheduler Events.

## Iteration 1

Iteration 1 implements Iteration 0 calculations by introducing the subsystems that form the elevator control system. This iteration includes the development of the FloorRequestSimulator, Elevator, and Scheduler subsystems, along with the classes necessary for their operation.  
[Iteration 1 UML diagrams](https://github.com/Aranesh22/SYSC3303A/tree/main/UML/Iteration%201)

### 🏢 FloorRequestSimulator Subsystem

* The FloorRequestSimulator subsystem is responsible for simulating the floor-level operations of the elevator system. It handles the input from users on each floor, generating requests for elevator service.

### 🛗 Elevator Subsystem

* The Elevator subsystem simulates the behavior of individual elevator cars. It responds to requests from the FloorRequestSimulator subsystem, picking up and transporting users to their desired floors.

### ⏰ Scheduler Subsystem

* The Scheduler subsystem acts as the central decision-making unit. It receives requests from the FloorRequestSimulator subsystem, then process and sends them to the appropriate Elevator subsystem.

### ⌛ Synchronizer Class

* The Synchronizer class is crucial for the correct operation of the system by ensuring thread-safe communication between the subsystems. It manages the flow of information throughout the system.

### 📢 FloorRequest Class

* The FloorRequest class encapsulates the data related to a user's floorRequest for elevator service, including the starting floor, destination floor, and direction.


## Iteration 0

In Iteration 0, we focused on recording the systems basic functions for initial measurements and time calculations for elevator movements.

## Testing 

### Running The Tests
There are 2 directories containing tests: 
1. Testing 
2. testingNetworks 

All files in the 'testing' directory can be ran together

in the 'testingNetworks' directory, tests must be run ***individually***

## FAQ
**Q: The program is not running as expected**<br>
A: Navigate to the menu at the top-left corner of IntelliJ, select `File`, click `Project Structure`, and ensure the settings
are the same as below:
<img width="763" alt="project_structure" src="https://github.com/Aranesh22/SYSC3303A/assets/61627702/8d709f93-0ff3-4413-8851-c97b9189769a">

