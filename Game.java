import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Math;
/**
 * This is the class to call to start the game
 * Compile with "javac Game.java"
 * Run with "java Game"
 */
public class Game extends JPanel{
    // FOR THE GUI
    // number of green nodes
    static int nodes;
    final double RADIUS = 300;
    static JFrame frame;
    private final int DEFAULT_WIDTH  = 1200;
    private final int DEFAULT_HEIGHT = 800;
    private final Color BACK_COLOR   = Color.WHITE;
    private int x1, y1, x2, y2;
    private MyMouseHandler handler;
    private Graphics g;
    static char whoAmI;
    static boolean gameNotOver = true;
    static boolean auto;

    // number of days in the election campaign
    int daysToElection;
    // the day into the election campaign
    int day;
    // all of the agents
    GreenAgent[] greenTeam;
    GreyAgent[] greyTeam;
    RedAgent redAgent;
    BlueAgent blueAgent;
    // 2D array representing adjecency matrix for green team network
    int[][] network;
    int winner; // 0 for red, 1 for blue, 2 for draw

    /**
     * constructor that creates a random network
     * @param days the number of days the election runs for
     * @param nGreen the number of green agents on the green team
     * @param nGrey the number of grey agents on the grey team
     * @param prob the probability of connection between two green agents
     * @param prop the proportion of grey agents on blue team
     * @param userTeam what team the user is playing on, null if user not playing
     */
    public Game(int days, int nGreen, int nGrey, int prob, int prop, char userTeam){
        // FOR THE GUI
        setBackground( BACK_COLOR );
        setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );

        handler  = new MyMouseHandler();

        this.addMouseListener( handler );

        this.daysToElection = days;
        // initialise array of grey team members taking into account the probability
        this.greyTeam = new GreyAgent[nGrey];
        int numBlueSidedAgents = (int)Math.round((prop / 10) * ((double)nGrey / 10));
        int numSpyAgents = nGrey - numBlueSidedAgents;
        Random rand = new Random();
        boolean sidedWithBlue = rand.nextInt(1,101) <= prop;
        int blueCount = 0;
        int redCount = 0;
        for(int i = 0; i < nGrey; i++) {
            if (blueCount >= numBlueSidedAgents) {
                greyTeam[i] = new GreyAgent(i,1);
                redCount++;
            }
            else if (redCount >= numSpyAgents) {
                greyTeam[i] = new GreyAgent(i, 0);
                blueCount++;
            }
            else if (sidedWithBlue) {
                greyTeam[i] = new GreyAgent(i, 0);
                blueCount++;
            } else {
                greyTeam[i] = new GreyAgent(i, 1);
                redCount++;
            }
        }
        // initialise array of green team members 
        this.greenTeam = new GreenAgent[nGreen];
        for(int i = 0; i < nGreen; i++)
            greenTeam[i] = new GreenAgent(i);
        // initialise the network
        this.network = new int[nGreen][nGreen];
        // array to keep track of which pairs of nodes connections have been decided
        int[][] pairsConnected = new int[nGreen][nGreen];
        // make connections according to the prob
        for(int i = 0; i < nGreen; i++){
            for(int j = 0; j < nGreen; j++){
                // generate a number between 1 and 4 for level of connections
                int level = rand.nextInt(1, 5);
                if(rand.nextInt(1,101) <= prob && pairsConnected[i][j] == 0){
                    network[i][j] = level;
                    network[j][i] = level;
                    pairsConnected[i][j] = 1;
                    pairsConnected[j][i] = 1;
                }
            }
        }
        if (userTeam == 'r' || userTeam == 'R') {
            this.redAgent = new RedAgent(true);
            this.blueAgent = new BlueAgent((days / 2) * 5, false);
            this.whoAmI = 'R';
        } 
        else if (userTeam == 'b' || userTeam == 'B') {
            this.redAgent = new RedAgent(false);
            this.blueAgent = new BlueAgent((days / 2) * 5, true);
            this.whoAmI = 'B';
        }
        else {
            this.redAgent = new RedAgent(false);
            this.blueAgent = new BlueAgent((days / 2) * 5, false);
            this.whoAmI = 'N';
        }
        this.day = 0;
    }

    /**
     * constructor that creates a user defined network
     * @param days the number of days that the election runs for
     * @param nGreen the number of agents on the green team 
     * @param nGrey the number of agents on the grey team
     * @param network the input file containing edges between green nodes
     * @param prop the percentage of grey agents on blue team
     * @param userTeam what team the user is playing on, null if user not playing
     */
     public Game (int days, int nGreen, int nGrey, String edges, int prop, char userTeam){
        // FOR THE GUI
        setBackground( BACK_COLOR );
        setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );

        handler  = new MyMouseHandler();

        this.addMouseListener( handler );

        this.daysToElection = days;
        // initialise array of grey team members
        this.greyTeam = new GreyAgent[nGrey];
        int numBlueSidedAgents = (int)Math.round((prop / 10) * ((double)nGrey / 10));
        int numSpyAgents = nGrey - numBlueSidedAgents;
        Random rand = new Random();
        boolean sidedWithBlue = rand.nextInt(1,101) <= prop;
        int blueCount = 0;
        int redCount = 0;
        for(int i = 0; i < nGrey; i++) {
            if (blueCount >= numBlueSidedAgents) {
                greyTeam[i] = new GreyAgent(i,1);
                redCount++;
            }
            else if (redCount >= numSpyAgents) {
                greyTeam[i] = new GreyAgent(i, 0);
                blueCount++;
            }
            else if (sidedWithBlue) {
                greyTeam[i] = new GreyAgent(i, 0);
                blueCount++;
            } else {
                greyTeam[i] = new GreyAgent(i, 1);
                redCount++;
            }
        }
        // init array of green team members
        this.greenTeam = new GreenAgent[nGreen];
        for(int i = 0; i < nGreen; i++)
            greenTeam[i] = new GreenAgent(i);
        // initialise the network
        this.network = new int[nGreen][nGreen];
        try{
            FileInputStream file = new FileInputStream(edges);       
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){ 
                String row = scanner.nextLine();
                int id1 = Integer.valueOf(row.split(",")[0]);
                int id2 = Integer.valueOf(row.split(",")[1]);
                network[id1][id2] = 1;
                network[id2][id1] = 1;
            } 
            scanner.close();
        } catch(IOException e) {  
            e.printStackTrace();  
        }
        if (userTeam == 'r' || userTeam == 'R') {
            this.redAgent = new RedAgent(true);
            this.blueAgent = new BlueAgent((days / 2) * 5, false);
        } 
        else if (userTeam == 'b' || userTeam == 'B') {
            this.redAgent = new RedAgent(false);
            this.blueAgent = new BlueAgent((days / 2) * 5, true);
        }
        else {
            this.redAgent = new RedAgent(false);
            this.blueAgent = new BlueAgent((days / 2) * 5, false);
        }
        this.day = 0;
    }

    /**
     * method for updating the display for the game
     * run at the start of every round
     * @param g the window to display in
     */
    public void paint(Graphics g) {
        // draw the blue team
        g.setColor(new Color(31, 81, 255));
        g.fillOval(125, 380, 30, 30);
        g.setColor(Color.BLACK);
        g.drawString("Energy: " + blueAgent.energy, 110, 430);
        // draw the red team
        g.setColor(new Color(210, 4, 45));
        g.fillOval(1050, 380, 30, 30);
        // get the position of where to draw each green team node
        int[][] locations = new int[nodes][2];
        double radius = RADIUS;
        int counter = 0;
        for(double i = 0; i <= 359; i = i + (360.0 / nodes)){
            double theta = (i-90) * Math.PI/180;
            double x = radius * Math.cos(theta);
            double y = -1 * radius * Math.sin(theta);
            locations[counter][0] = (int)x + 600;
            locations[counter][1] = (int)y + 400;
            counter++;
        }
        // get the positions of where to draw each green nodes id
        int[][] idLocations = new int[nodes][2];
        radius = RADIUS + 18;
        counter = 0;
        for(double i = 0; i <= 359; i = i + (360.0 / nodes)){
            double theta = (i-90) * Math.PI/180;
            double x = radius * Math.cos(theta);
            double y = -1 * radius * Math.sin(theta);
            idLocations[counter][0] = (int)x + 600;
            idLocations[counter][1] = (int)y + 400;
            counter++;
        }
        // draw connections between green nodes
        // this is done first so the lines don't cover the green nodes
        g.setColor(Color.black);
        for(int i = 0; i < locations.length; i++){
            for(int j = 0; j < locations.length; j++){
                if(network[i][j] > 0)
                    if(20.0 - greenTeam[i].uncertainty - greenTeam[j].uncertainty < 2.5){
                        g.setColor(new Color(0, 153, 51));
                        g.drawLine(locations[i][0], locations[i][1], locations[j][0], locations[j][1]);
                    } else if(20.0 - greenTeam[i].uncertainty - greenTeam[j].uncertainty < 5.0){
                        g.setColor(new Color(102, 153, 0));
                        g.drawLine(locations[i][0], locations[i][1], locations[j][0], locations[j][1]);
                    } else if(20.0 - greenTeam[i].uncertainty - greenTeam[j].uncertainty < 7.5){
                        g.setColor(new Color(255, 102, 0));
                        g.drawLine(locations[i][0], locations[i][1], locations[j][0], locations[j][1]);
                    } else{
                        g.setColor(new Color(153, 0, 0));
                        g.drawLine(locations[i][0], locations[i][1], locations[j][0], locations[j][1]);
                    }
            }
        }
        // draw the green team
        g.setColor(new Color(0, 163, 108));
        for(int i = 0; i < locations.length; i++){
            g.fillOval(locations[i][0] - 4, locations[i][1] - 4, 10, 10);
        }
        // draw green team ids
        g.setColor(Color.BLACK);
        for(int i = 0; i < idLocations.length; i++){
            g.drawString(Integer.toString(i), idLocations[i][0] - 6, idLocations[i][1] + 4);
        }
        // draw the top bar
        g.setColor(new Color(220,220,220));
        g.fillRect(0, 0, 1200, 50);
        // draw the day into the election
        String dayCount = "Day: " + day + " of " + daysToElection;
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
        g.drawString(dayCount, 30, 33);
        // draw the total voters for the round
        int totalVoters = 0;
        for (int i = 0; i < greenTeam.length; i++){
            if (greenTeam[i].willVote) {
                totalVoters++;
            }
        }
        //System.out.println(totalVoters);
        g.drawString("Total voters: " + Integer.toString(totalVoters) + " of " + greenTeam.length, 200, 33);
        // draw the number of grey agents
        g.drawString("Grey agents: " + (greyTeam.length - blueAgent.selectedGreyAgent), 440, 33);
        // draw the number of green agents
        g.drawString("Green agents: " + greenTeam.length, 635, 33);
        // draw botton bar
        g.setColor(new Color(220,220,220));
        g.fillRect(0, 750, 1200, 50);
        // draw the next day button
        g.setColor(new Color(30,144,255));
        g.fillRect(1070, 750, 130, 50);
        g.setColor(Color.BLACK);
        g.drawString("Next Day >>", 1085, 780);
        // draw get green params button
        g.setColor(new Color(80, 200, 120));
        g.fillRect(0, 750, 125, 50);
        g.setColor(Color.BLACK);
        g.drawString("Green Info", 15, 780);
        // // draw a button to continue the game without playing
        // g.setColor(new Color(255, 191, 0));
        // g.fillRect(930, 750, 130, 50);
        // g.setColor(Color.BLACK);
        // if(auto){
        //     g.drawString(" Continue", 950, 780);
        // }
        // else{
        //     g.drawString("     Stop", 950, 780);
        // }
        // draw gameover
        if(!gameNotOver){
            g.setColor(new Color(220, 20, 60));
            g.drawString("GAME OVER", 1000, 33);
        }
    }

    /**
     * method to facilitate displaying the game 
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }

    /**
     * method to facilitate displaying the game
     */
    private void setUpDrawingGraphics()
    {
        g = getGraphics();
    }

    /**
     * inner class for detecting which green node has been selected
     */
    private class MyMouseHandler extends MouseAdapter
    {
        public void mousePressed( MouseEvent e )
        {
            x1 = e.getX();
            y1 = e.getY();
            System.out.println("Mouse is being pressed at X: " + x1 + " Y: " + y1);
            setUpDrawingGraphics();
            if (x1 > 1070 && y1 > 750 && gameNotOver){
                nextRound();
            }
            String sId = "id";
            int id = 0;
            if (x1 < 125 && y1 > 750){
                sId = (String) JOptionPane.showInputDialog(
                    frame,
                    "Please enter a green agent ID.\nA number between 0 and " + (greenTeam.length - 1) + ".",
                    "Get a green agents parameters",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "ID");
                try {
                    id = Integer.parseInt(sId);
                    if (id >= 0 && id < greenTeam.length){
                        JOptionPane.showMessageDialog(frame,
                            "ID: " + id + "\nVoting: " + greenTeam[id].willVote + "\nUncertainty: " + greenTeam[id].uncertainty + "\nListens to red: " + greenTeam[id].canRedCommunicate,
                            "Green agent parameters",
                            JOptionPane.INFORMATION_MESSAGE,
                            null);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid ID.", "Green agent parameters", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame, "Invalid ID.", "Green agent parameters", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    /**
     * method to initiate the next round of the game
     */
    public void nextRound(){
        day++;
        // AI plays for red
        int potency = 0;
        if (!redAgent.isUserPlaying) {
            potency = redAgent.useRedAI(greenTeam, daysToElection - day, 'R', 4, network);
        }
        redAgent.redTurn(greenTeam, potency);
        // AI plays for blue
        if (blueAgent.energy > 0) {
            int certainty = 0;
            if (!blueAgent.isUserPlaying) {
                certainty = blueAgent.useBlueAI(greenTeam, daysToElection - day, 'B', 4, network, greyTeam);
            }
            blueAgent.blueTurn(greenTeam, greyTeam, false, certainty);
        }
        GreenAgent.greenTurn(greenTeam, network);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        while(day != daysToElection){
            repaint();
            //nextRound();
        }
        // System.out.println("Game Over");
    }

    // Function to add up the total amount of votes and declare a winner
    public void end(){
        gameNotOver = false;
        int totalVoters = 0;
        for (int i = 0; i < greenTeam.length; i++){
            if (greenTeam[i].willVote) {
                totalVoters++;
            }
        }
        if (((double)totalVoters / greenTeam.length) > 0.5){
            System.out.println("Blue Team Wins!");
            winner = 1;
            JOptionPane.showMessageDialog(frame, "Game Over.\nBlue Team Wins!");
        }
        else if (((double)totalVoters / greenTeam.length) < 0.5){
            System.out.println("Red Team Wins");
            JOptionPane.showMessageDialog(frame, "Game Over.\nRed Team Wins!");
            winner = 0;
        }
        else {
            System.out.println("Game is a draw");
            JOptionPane.showMessageDialog(frame, "Game Over.\nGame is a Draw!");
            winner = 2;
        }
    }

    public void addAgent(String info){
        String[] columns = info.split(",");
    }
    
    public static void printUsage(){
        System.out.println("Usage: java Game <days> <size of green team> <size of grey team> <network % density> <% of greys on blue team> <is user playing (R for red / B for blue / N for no)>");
        System.out.println("     : java Game -f <days> <size of green team> <size of grey team> <network file> <% of greys on blue team> <is user playing (R for red / B for blue / N for no)>");
    }

    public static void main(String[] args){

        // the game initial state of the game;
        Game game = null;
        // print usage information
        if(args.length < 2){
            printUsage();
            return;
        }

        // FOR THE GUI
        JFrame frame = new JFrame( "War Game Simulation" );
        frame.setDefaultCloseOperation(3);

        // determine which constructor to call
        if(args[0].equals("-f")){
            game = new Game(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), args[4], Integer.valueOf(args[5]), args[6].charAt(0));
            nodes = Integer.valueOf(args[2]);
        } else {
            game = new Game(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), args[5].charAt(0));
            nodes = Integer.valueOf(args[1]);
        }

        frame.add( game );

        frame.pack();
        frame.setVisible( true );

        // start the game
        if(game != null)
            game.start();
        else
            System.out.println("something went wrong");
        game.end();
    }
}

