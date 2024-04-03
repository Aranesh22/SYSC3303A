import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * The ElevatorFrame class models the GUI for the project.
 *
 * @author Pathum Danthanarayana, 101181411
 * @version Iteration 5
 */
public class ElevatorFrame extends JFrame {

    /**
     * The ElevatorDirection class models a triangle (arrow) to indicate
     * the direction of a given elevator (either a triangle
     * pointing upwards to indicate the up direction, or a triangle
     * pointing downwards to indicate the down direction).
     */
    private class ElevatorDirection extends JPanel {
        // Fields
        private String direction;
        private Color color;
        private static final int WIDTH = 18;
        private static final int HEIGHT = 18;

        /**
         * Constructor
         * @param direction - the direction to point to (up
         *                  indicates a triangle pointing upwards,
         *                  and down indicates a triangle pointing downwards)
         */
        public ElevatorDirection(String direction) {
            // Call superclass' constructor
            super();
            // Make direction's background transparent
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(18, 18));
            this.direction = direction;
            this.color = ElevatorFrame.DEFAULT_DIRECTION_COLOR;
        }

        /**
         * Override the paintComponent method to draw the triangle to
         * indicate a specific direction (up or down).
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        protected void paintComponent(Graphics g) {
            // Call the superclass' constructor
            super.paintComponent(g);
            // Create a new Graphics2D
            Graphics2D graphics2D = (Graphics2D) g.create();
            // Define new Polygon for the triangle
            Polygon triangle;

            if (direction.equals("up")) {
                // Define coordinates for a triangle pointing downwards

                // (9, 0), (0, 18), (18, 18) ==> Forms a triangle pointing upwards
                // (where p1 and p2 are for base, and p3 is for top)

                // Create the triangle with the x and y coordinates above
                triangle = new Polygon(new int[]{WIDTH / 2, 0, 18}, new int[]{0, HEIGHT, HEIGHT}, 3);
            }
            else if (direction.equals("down")) {
                // Define coordinates for a triangle pointing upwards

                // (0, 0), (18, 0), (9, 18) ==> Forms a triangle pointing downwards
                // (where p1 and p2 are for base, and p3 is for top)

                // Create the triangle with the x and y coordinates above
                triangle = new Polygon(new int[]{0, WIDTH, WIDTH / 2, 0}, new int[]{0, 0, HEIGHT}, 3);
            }
            else {
                // Unknown direction specified for triangle
                System.out.println("Unknown direction specified for triangle");
                return;
            }
            // Set the color of the triangle
            graphics2D.setColor(color);
            // Fill graphics2D with drawn triangle
            graphics2D.fill(triangle);
        }

        /**
         * Sets the color for the direction triangle.
         * @param color - the new color
         */
        public void setColor(Color color) {
            this.color = color;
            // Force the paintComponent() method to be invoked
            // so that the triangle is repainted
            repaint();
        }
    }

    /**
     * The ElevatorCar class models an entire elevator component for
     * the GUI, which contains the elevator's display, elevator doors,
     * elevator name, and number of passengers.
     *
     * @author Pathum Danthanarayana, 101181411
     * @version Iteration 5
     */
    public class ElevatorCar {
        // Fields
        private JPanel mainPanel;
        private JLabel elevatorName;
        private JLabel numPassengers;
        private ElevatorDirection upDirection;
        private ElevatorDirection downDirection;
        private JLabel floorNumber;
        private JPanel elevatorDoorsPanel;
        private JPanel leftDoor;
        private JPanel rightDoor;


        /**
         * Constructor
         */
        public ElevatorCar(JPanel parentPanel, String name) {
            // Main panel containing all other JPanels
            mainPanel = new JPanel(new BorderLayout());

            // Create elevator name and capacity
            createElevatorNameAndNumPassengers(name);
            // Create elevator control panel
            createElevatorControlPanel();
            // Create elevator doors
            createElevatorDoors();

            // Add to main panel to parent panel (contains all elevator panels)
            parentPanel.add(mainPanel);
        }

        /**
         * Creates the elevator name and capacity count.
         */
        private void createElevatorNameAndNumPassengers(String name) {
            // Elevator Name and Capacity
            JPanel spacing = new JPanel();
            spacing.setBackground(Color.WHITE);
            JPanel elevatorDetails = new JPanel(new BorderLayout(0, 5));
            elevatorDetails.setBackground(Color.WHITE);
            elevatorDetails.setPreferredSize(new Dimension(280, 60));

            // Elevator name
            elevatorName = new JLabel(name, SwingConstants.CENTER);
            elevatorName.setFont(regHeaderFont);
            // Number of passengers
            numPassengers = new JLabel("Passengers: ", SwingConstants.CENTER);
            numPassengers.setFont(regSubFont);

            // Add elevator name and number of passengers to JPanel
            elevatorDetails.add(elevatorName, BorderLayout.NORTH);
            elevatorDetails.add(numPassengers, BorderLayout.CENTER);
            elevatorDetails.add(spacing, BorderLayout.SOUTH);

            // Add JPanel to main panel
            mainPanel.add(elevatorDetails, BorderLayout.NORTH);
        }

        /**
         * Creates the elevator control panel, which contains
         * the elevator display (for indicating current floor),
         * and the direction triangles.
         */
        private void createElevatorControlPanel() {
            // Elevator Control Panel: Elevator display, travel directions
            JPanel elevatorControlPanel = new JPanel(new BorderLayout());
            elevatorControlPanel.setPreferredSize(new Dimension(280, 80));

            // Create padding for control panel
            JPanel leftPadding = new JPanel();
            JPanel rightPadding = new JPanel();
            JPanel bottomPadding = new JPanel();
            leftPadding.setPreferredSize(new Dimension(80, 80));
            leftPadding.setBackground(ELEVATOR_DIRECTIONS_PANEL_COLOR);
            rightPadding.setPreferredSize(new Dimension(80, 80));
            rightPadding.setBackground(ELEVATOR_DIRECTIONS_PANEL_COLOR);
            bottomPadding.setPreferredSize(new Dimension(280, 10));
            bottomPadding.setBackground(ELEVATOR_DIRECTIONS_PANEL_COLOR);

            // Elevator direction triangles
            JPanel elevatorDirectionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 5));
            elevatorDirectionsPanel.setPreferredSize(new Dimension(280, 30));
            elevatorDirectionsPanel.setBackground(ELEVATOR_DIRECTIONS_PANEL_COLOR);
            upDirection = new ElevatorDirection("up");
            downDirection = new ElevatorDirection("down");
            elevatorDirectionsPanel.add(upDirection);
            elevatorDirectionsPanel.add(downDirection);

            // Elevator display
            JPanel elevatorDisplay = new JPanel(new BorderLayout());
            elevatorDisplay.setBackground(ElevatorFrame.DEFAULT_DIRECTION_COLOR);
            elevatorDisplay.setPreferredSize(new Dimension(250, 75));
            floorNumber = new JLabel("--", SwingConstants.CENTER);
            floorNumber.setBorder(BorderFactory.createEmptyBorder( -8, 0, 0, 0 ));
            floorNumber.setFont(digitalFont);
            floorNumber.setForeground(FLOOR_NUMBER_COLOR);
            elevatorDisplay.add(floorNumber, BorderLayout.CENTER);

            // Add elevator display and elevator directions to JPanel
            elevatorControlPanel.add(leftPadding, BorderLayout.WEST);
            elevatorControlPanel.add(rightPadding, BorderLayout.EAST);
            elevatorControlPanel.add(bottomPadding, BorderLayout.SOUTH);
            elevatorControlPanel.add(elevatorDirectionsPanel, BorderLayout.NORTH);
            elevatorControlPanel.add(elevatorDisplay, BorderLayout.CENTER);

            // Add JPanel to main JPanel
            mainPanel.add(elevatorControlPanel, BorderLayout.CENTER);
        }

        /**
         * Creates the elevator doors.
         */
        private void createElevatorDoors() {
            // Elevator Doors: Left Door, Right Door, Inside view
            elevatorDoorsPanel = new JPanel(new BorderLayout());
            elevatorDoorsPanel.setBackground(ELEVATOR_DOORS_PANEL_COLOR);
            elevatorDoorsPanel.setPreferredSize(new Dimension(280, 400));
            // Left door
            leftDoor = new JPanel();
            leftDoor.setBackground(ELEVATOR_DOORS_COLOR);
            leftDoor.setPreferredSize(new Dimension(100, 400));
            // Right door
            rightDoor = new JPanel();
            rightDoor.setBackground(ELEVATOR_DOORS_COLOR);
            rightDoor.setPreferredSize(new Dimension(100, 400));

            // Add doors
            elevatorDoorsPanel.add(leftDoor, BorderLayout.WEST);
            elevatorDoorsPanel.add(rightDoor, BorderLayout.EAST);

            // Add elevator doors to main panel
            mainPanel.add(elevatorDoorsPanel, BorderLayout.SOUTH);
        }

        /**
         * Updates the elevator's floor number.
         */
        public void updateFloorNumber(int floorNumber) {
            this.floorNumber.setText(String.valueOf(floorNumber));
        }

        /**
         * Closes the elevator's doors.
         */
        public void updateDoors(boolean doorsOpened) {
            if (doorsOpened) {
                leftDoor.setPreferredSize(new Dimension(100, 400));
                rightDoor.setPreferredSize(new Dimension(100, 400));
            }
            else {
                leftDoor.setPreferredSize(new Dimension(138, 400));
                rightDoor.setPreferredSize(new Dimension(138, 400));
            }
            // Force doors to be revalidated and repainted to see changes in width
            // (simulating the doors opening or closing in real-time)
            leftDoor.revalidate();
            leftDoor.repaint();
            rightDoor.revalidate();
            rightDoor.repaint();
        }

        /**
         * Updates the direction that the elevator is moving in.
         * @param direction - the current direction of the elevator
         */
        public void updateDirection(String direction) {
            // Elevator is moving up
            if (direction.equals("up")) {
                upDirection.setColor(Color.GREEN);
                downDirection.setColor(ElevatorFrame.DEFAULT_DIRECTION_COLOR);
            }
            // Elevator is moving down
            else if (direction.equals("down")) {
                downDirection.setColor(Color.GREEN);
                upDirection.setColor(ElevatorFrame.DEFAULT_DIRECTION_COLOR);
            }
            else {
                System.out.println("Unknown direction specified");
            }
        }

        /**
         * Updates the number of passengers in the elevator.
         */
        public void updateNumPassengers(int numPassengers) {
            this.numPassengers.setText(String.format("Passengers: %d", numPassengers));
        }

        /**
         * Handles visualizing fault in the elevator (shown by
         * showing the floor number as 'ERR', and 'turning off' both
         * elevator direction triangles).
         */
        public void displayFault() {
            floorNumber.setText("ERR");
            upDirection.setColor(ElevatorFrame.DEFAULT_DIRECTION_COLOR);
            downDirection.setColor(ElevatorFrame.DEFAULT_DIRECTION_COLOR);
        }
    }

    // Fields
    Container contentPane;
    JPanel elevatorsPanel;
    JTextArea floorOutputArea;
    HashMap<Integer, ElevatorCar> elevatorCars;
    Font digitalFont;
    Font regHeaderFont;
    Font regSubFont;

    public static final Color DEFAULT_DIRECTION_COLOR = new Color(31, 31, 31);
    public static final Color ELEVATOR_DOORS_COLOR = new Color(217, 217, 217);
    public static final Color ELEVATOR_DOORS_PANEL_COLOR = new Color(48, 48, 48);
    public static final Color ELEVATOR_DIRECTIONS_PANEL_COLOR = new Color(192, 192, 192);
    public static final Color FLOOR_NUMBER_COLOR = new Color(230, 28, 28);

    /**
     * Constructor
     */
    public ElevatorFrame() {
        // Initialize JFrame
        super("Elevator GUI - SYSC 3303A G9");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // Initialize HashMap
        elevatorCars = new HashMap<>();

        // Set up content pane
        contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.WHITE);

        // Set up custom fonts
        setupCustomFonts();

        // Create elevators panel to hold ElevatorCars
        createElevatorsPanel();

        // Create panel for program output
        createFloorOutputArea();

        // Make JFrame visible after all components have been added
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
    }

    /**
     * JPanel that contains all the elevators
     */
    private void createElevatorsPanel() {
        // Display
        elevatorsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        elevatorsPanel.setPreferredSize(new Dimension(1200, 580));
        elevatorsPanel.setBackground(Color.WHITE);
        contentPane.add(elevatorsPanel, BorderLayout.NORTH);
    }

    /**
     * Creates and adds a new ElevatorCar to the elevatorsPanel
     */
    public void addElevatorCar(int elevatorId, String elevatorName) {
        // Create a new ElevatorCar
        ElevatorCar elevatorCar = new ElevatorCar(elevatorsPanel, elevatorName);
        // Add ElevatorCar to HashMap
        elevatorCars.put(elevatorId, elevatorCar);
    }

    /**
     * Sets up custom fonts.
     */
    private void setupCustomFonts() {
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/digital-7.ttf");
            if (inputStream != null) {
                // Font for elevator floor number
                digitalFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(40f);
                regHeaderFont = new Font("Helvetica", Font.PLAIN, 20);
                // Font for elevator name and capacity
                regSubFont = new Font("Helvetica", Font.PLAIN, 16);
            }
            else {
                // Default fonts
                digitalFont = new Font("Roboto", Font.BOLD, 40);
                regHeaderFont = new Font("Roboto", Font.PLAIN, 20);
                regSubFont = new Font("Roboto", Font.PLAIN, 16);
            }
        } catch(IOException | FontFormatException e) {
            System.out.println("Error setting up custom font. Exiting...");
            System.exit(1);
        }
    }

    /**
     * JTextArea that contains all the Floor Subsystem's output
     */
    private void createFloorOutputArea() {
        // Create JTextArea for adding output of Floor subsystem
        floorOutputArea = new JTextArea();
        floorOutputArea.setBackground(ELEVATOR_DOORS_COLOR);
        floorOutputArea.setFont(regSubFont);
        // Add JTextArea to a JScrollPane to allow vertical scrolling
        JScrollPane scrollPane = new JScrollPane(floorOutputArea);
        scrollPane.setPreferredSize(new Dimension(1000, 200));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        contentPane.add(scrollPane, BorderLayout.SOUTH);
    }

    /**
     * Updates the GUI based on the specified ElevatorStatus
     */
    public void updateGUI(ElevatorStatus elevatorStatus) {
        // Check if elevator ID exists in HashMap
        int elevatorId = elevatorStatus.getElevatorId();
        if (!elevatorCars.containsKey(elevatorStatus.getElevatorId())) {
            // If not, create a new ElevatorCar
            addElevatorCar(elevatorId, String.format("Elevator %d", elevatorId));
        }
        else {
            // Update the existing ElevatorCar
            ElevatorCar elevatorCar = elevatorCars.get(elevatorId);
            elevatorCar.updateFloorNumber(elevatorStatus.getCurrentFloor());
            elevatorCar.updateDoors(elevatorStatus.getDoorsOpened());
            elevatorCar.updateDirection(elevatorStatus.getDirection());
            elevatorCar.updateNumPassengers(elevatorStatus.getPassengerCount());
            // Check if elevator has fault
            if (elevatorStatus.getErrorCode() != 0) {
                elevatorCar.displayFault();
            }
        }
    }

    /**
     * Adds specified output message (from Floor subsystem) to the JTextArea of the GUI.
     */
    public void addOutputMessage(String message) {
        floorOutputArea.append(message + "\n");
    }
}
