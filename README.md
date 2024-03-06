# Elevator Control System - SYSC 3303 A

This project simulates an elevator control system using a real-time multi-threading to manage multiple elevators and floor requests. The system ensures that all elevator cars operate efficiently and respond to user requests. The project is developed iteratively, with each iteration introducing new features and improvements.


## Group Members

🏅 Yehan De Silva, 101185388<br>
🏅 Pathum Danthanarayana, 101181411<br>
🏅 Aranesh Athavan, 101152794<br>
🏅 Lindsay Dickson, 101160876<br>
🏅 Harishan Amutheesan, 101154757<br>

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
  * Developed Floor class
  * Developed JUnit tests for Floor class

* Lindsay Dickson
  * Developed Main class and FloorRequest class
  * Developed JUnit tests for FloorRequest class, Elevator class
  * UML Sequence diagram
 
* Harishan Amutheesan
  * Developed Scheduler class
  * Developed JUnit tests for Scheduler class
  * UML class diagram
  * README file
 

## Iteration 0

In Iteration 0, we focused on recording the systems basic functions for initial measurements and time calculations for elevator movements.

## Iteration 1

Iteration 1 implements Iteration 0 calculations by introducing the subsystems that form the elevator control system. This iteration includes the development of the Floor, Elevator, and Scheduler subsystems, along with the classes necessary for their operation.  
[Iteration 1 UML diagrams](https://github.com/Aranesh22/SYSC3303A/tree/main/UML/Iteration%201)

### 🏢 Floor Subsystem

* The Floor subsystem is responsible for simulating the floor-level operations of the elevator system. It handles the input from users on each floor, generating requests for elevator service.

### 🛗 Elevator Subsystem

* The Elevator subsystem simulates the behavior of individual elevator cars. It responds to requests from the Floor subsystem, picking up and transporting users to their desired floors.

### ⏰ Scheduler Subsystem

* The Scheduler subsystem acts as the central decision-making unit. It receives requests from the Floor subsystem, then process and sends them to the appropriate Elevator subsystem.

### ⌛ Synchronizer Class

* The Synchronizer class is crucial for the correct operation of the system by ensuring thread-safe communication between the subsystems. It manages the flow of information throughout the system.

### 📢 FloorRequest Class

* The FloorRequest class encapsulates the data related to a user's floorRequest for elevator service, including the starting floor, destination floor, and direction.

## Iteration 2

Iteration 2 builds upon iteration 1 by refactoring the project to follow the state design pattern. This iteration introduces the ElevatorState and SchedulerState files.  
[Iteration 2 UML diagrams](https://github.com/Aranesh22/SYSC3303A/tree/main/UML/Iteration%202)


### 🛗Elevator State

* The ElevatorState file consists of inner ElevatorState classes that models each possible Elevator State. Each of the inner classes implements the ElevatorState interface which outlines all possible Elevator Events.

### ⏰ Scheduler State

* The SchedulerState file consists of inner SchedulerState classes that models each possible Scheduler State. Each of the inner classes implements the SchedulerState interface which outlines all possible Scheduler Events.

## Design
These design decisions form the initial structure of our elevator control system. With each iteration, we will refine our system to fit to specific iteration requirements. Here are the key design decisions we made:

### Concurrency
* We use multi-threading to simulate the real-time operation of elevators and floor requests. Each elevator and floor is managed by its own thread, allowing for concurrent processing of requests and elevator movements. The multi-threaded approach aligns with the real-world scenario where elevators operate independently of one another.

### Communication and Synchronization
* To manage communication between different subsystems, we implemented a `Synchronizer` class that acts as a mediator. This design decision was made to avoid tight coupling between subsystems and to ensure thread-safe interactions. The `Synchronizer` uses `synchronized` methods and `wait/notify` mechanisms to handle multiple threads, ensuring the system will not face concurrency-related issues like race conditions.

### Request Handling
* For Iteration 1, a simple First-In-First-Out (FIFO) strategy handles elevator requests, which was implemented in the `Scheduler` class. This was chosen for its simplicity and fairness, ensuring that all requests are addressed in the order they are received. As we further iterate, we plan to optimize the response time and efficiency.

### Elevator Simulation
* The `Elevator` class was designed to simulate the movement of an elevator car. We decided to implement a time delay to represent the elevator traveling between floors by implementing speed calculations from Iteration 0 which provides a realistic feel to the system's operation.

### Error Handling
* We have put initial error handling mechanisms in place, primarily in the input validation within the `Scheduler`. Invalid floor requests are logged with an error message. As we progress through the iterations, we will develop a comprehensive error handling mechanism to manage exceptions to maintain system stability.

### Testing
* To ensure the reliability of our system, we used JUnit testing to test each class and subsystem.
* We only focused on testing the key functionalities of each subsystem (i.e., whether the subsystem is able to produce/consume, its state is correctly updated), as it is too difficult to test thread-related aspects like synchronization with JUnit tests.

### Code Readability
* We value writing clean, readable code and providing documentation. Each class and method includes JavaDoc comments to explain its purpose, inputs, and outputs. This help show our current understanding of the system and ensures that professors and TAs can easily understand and grade the system.

## How to Run Project
1. Compile and build the project.
2. Modify data.csv file if needed. Format must be 
  ```[time(String)],[source floor(int)],[direction(up/down)],[destination floor(int)]```. data.csv file must end with ```END_REQUEST,-1,END,-1``` to signify end of file.
3. Run the program from the `Main` class.

## FAQ
**Q: The program is not running as expected**<br>
A: Navigate to the menu at the top-left corner of IntelliJ, select `File`, click `Project Structure`, and ensure the settings 
are the same as below:
<img width="763" alt="project_structure" src="https://github.com/Aranesh22/SYSC3303A/assets/61627702/8d709f93-0ff3-4413-8851-c97b9189769a">

