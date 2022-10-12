import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is the class to call to start the game
 * Compile with "javac Game.java"
 * Run with "java Game"
 */
public class Game extends JPanel{
    // FOR THE GUI
    // number of green nodes
    static int nodes;
    final double RADIUS = 350;
    static JFrame frame;
    private final int DEFAULT_WIDTH  = 1200;
    private final int DEFAULT_HEIGHT = 800;
    private final Color BACK_COLOR   = Color.WHITE;
    private int x1, y1, x2, y2;
    private MyMouseHandler handler;
    private Graphics g;

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
        // initialise array of grey team members
        this.greyTeam = new GreyAgent[nGrey];
        for(int i = 0; i < nGrey; i++)
            if (i % 3 == 0) {
                greyTeam[i] = new GreyAgent(i, 1);
            } else {
                greyTeam[i] = new GreyAgent(i, 0);
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
        Random rand = new Random();
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
     * constructor that creates a user defined network
     * @param days the number of days that the election runs for
     * @param nGreen the number of agents on the green team 
     * @param nGrey the number of agents on the grey team
     * @param network the input file containing edges between green nodes
     * @param prop the percentage of grey agents on blue team
     * @param userTeam what team the user is playing on, null if user not playing
     */
     public Game (int days, int nGreen, int nGrey, String edges, int prop, char userTeam){
        this.daysToElection = days;
        // initialise array of grey team members
        this.greyTeam = new GreyAgent[nGrey];
        for(int i = 0; i < nGrey; i++)
            if (i % 2 == 0) {
                greyTeam[i] = new GreyAgent(i, 1);
            } else {
                greyTeam[i] = new GreyAgent(i, 0);
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
        g.setColor(Color.blue);
        g.fillOval(150, 400, 30, 30);
        // draw the red team
        g.setColor(Color.red);
        g.fillOval(1050, 400, 30, 30);
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
        g.setColor(new Color(51, 158, 0));
        for(int i = 0; i < locations.length; i++){
            g.fillOval(locations[i][0] - 4, locations[i][1] - 4, 10, 10);
        }
        // draw the day into the election
        String dayCount = "day " + day + " of " + daysToElection;
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
        g.drawString(dayCount, 10, 20);
        // draw the total voters for the round
        int totalVoters = 0;
        for (int i = 0; i < greenTeam.length; i++){
            if (greenTeam[i].willVote) {
                totalVoters++;
            }
        }
        System.out.println(totalVoters);
        g.drawString("Total voters: " + Integer.toString(totalVoters), 10, 50);
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
            x2=x1;
            y2=y1;
        }
    }

    /**
     * method to initiate the next round of the game
     */
    public void nextRound(){
        day++;
        //System.out.println("Day " + day);
        // AI plays for red
        int potency = redAgent.useRedAI(greenTeam, daysToElection - day, 'R', 4, network);
        System.out.println("Red Teams Turn");
        redAgent.redTurn(greenTeam, potency);
        System.out.println("Sent out a message potency of: " + redAgent.previousTurn);
        System.out.println("Total followers lost: " + redAgent.totalFollowersLost);
        // AI plays for blue
        if (blueAgent.energy > 0) {
            System.out.println("Blue Teams Turn");
            int certainty = blueAgent.useBlueAI(greenTeam, daysToElection - day, 'B', 4, network, greyTeam);
            blueAgent.blueTurn(greenTeam, greyTeam, false, certainty);
            System.out.println("Send out a certainty of: " + blueAgent.certainty);
            System.out.println("Energy left: " + blueAgent.energy);
        }
        GreenAgent.greenTurn(greenTeam, network);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(day != daysToElection){
            repaint();
            nextRound();
        }
        System.out.println("Game Over");
    }

    // Function to add up the total amount of votes and declare a winner
    public void end(){
        int totalVoters = 0;
        for (int i = 0; i < greenTeam.length; i++){
            if (greenTeam[i].willVote) {
                totalVoters++;
            }
        }
        if (((double)totalVoters / greenTeam.length) > 0.5){
            System.out.println("Blue Team Wins!");
        }
        else if (((double)totalVoters / greenTeam.length) < 0.5){
            System.out.println("Red Team Wins");
        }
        else {
            System.out.println("Game is a draw");
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

