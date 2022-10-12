import java.util.*;
import java.io.*;  
import java.lang.Math;
/**
 * This is the class to call to start the game
 * Compile with "javac Game.java GreenAgent.java RedAgent.java BlueAgent.java GreyAgent.java GameLibrary.java"
 * Run with "java Game"
 */
public class Game{
    // number of days in the election campaign
    int daysToElection;
    // the day into the election campaign
    int day;
    // our agents
    GreenAgent[] greenTeam;
    GreyAgent[] greyTeam;
    RedAgent redAgent;
    BlueAgent blueAgent;
    // data structure for green team network
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

    public void nextRound(){
        day++;
        //System.out.println("Day " + day);
        // AI plays for red
        int potency = 0;
        if (!redAgent.isUserPlaying) {
            potency = redAgent.useRedAI(greenTeam, daysToElection - day, 'R', 4, network);
        }
        System.out.println("Red Teams Turn");
        redAgent.redTurn(greenTeam, potency, false);
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
    }

    public void start(){
        while(day != daysToElection){
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
        // determine which constructor to call
        if(args[0].equals("-f")){
            game = new Game(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), args[4], Integer.valueOf(args[5]), args[6].charAt(0));
        } else {
            game = new Game(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), args[5].charAt(0));
        }
        // start the game
        if(game != null)
            game.start();
        else
            System.out.println("something went wrong");
        game.end();
    }
}

