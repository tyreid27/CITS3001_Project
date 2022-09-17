import java.util.*;
import java.io.*;  
/**
 * This is the class to call to start the game
 * Compile with "javac Game.java GreenAgent.java RedAgent.java GreyAgent.java"
 * Run with "java Game"
 */
public class Game{
    // number of days in the election campaign
    int daysToElection;
    // the day into the election campaign
    int day;
    // total number of agents in the game in total
    int nGreen;
    // number of grey agents
    int nGrey;
    // number of agents that are currently going to vote at the election
    int willVote;
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
     */
    public Game(int days, int nGreen, int nGrey, int prob, int prop){
        this.daysToElection = days;
        // initialise array of grey team members
        this.greyTeam = new GreyAgent[nGrey];
        for(int i = 0; i < nGrey; i++)
            greyTeam[i] = new GreyAgent(i);
        // initialise array of green team members 
        this.greenTeam = new GreenAgent[nGreen];
        for(int i = 0; i < nGreen; i++)
            greenTeam[i] = new GreenAgent(i);
        // initialise the network
        this.network = new int[nGreen][nGreen];
        // make connections according to the prob
        Random rand = new Random();
        for(int i = 0; i < nGreen; i++){
            for(int j = 0; j < nGreen; j++){
                if(network[i][j] == 0){
                    if(rand.nextInt(1,101) <= prob){
                        network[i][j] = 2;
                        network[j][i] = 2;
                        greenTeam[i].connections.add(greenTeam[j]);
                        greenTeam[j].connections.add(greenTeam[i]);
                    } else {
                        network[i][j] = 1;
                        network[j][i] = 1;
                    }
                }
            }
        }
        this.redAgent = new RedAgent();
        this.blueAgent = new BlueAgent();
        this.day = 0;
    }

    /**
     * constructor that creates a user defined network
     * @param days the number of days that the election runs for
     * @param nGreen the number of agents on the green team 
     * @param nGrey the number of agents on the grey team
     * @param network the input file containing edges between green nodes
     * @param prop the percentage of grey agents on blue team
     */
     public Game (int days, int nGreen, int nGrey, String edges, int prop){
        this.daysToElection = days;
        // initialise array of grey team members
        this.greyTeam = new GreyAgent[nGrey];
        for(int i = 0; i < nGrey; i++)
            greyTeam[i] = new GreyAgent(i);
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
                network[id1][id2] = 2;
                network[id2][id1] = 2;
                greenTeam[id1].connections.add(greenTeam[id2]);
                greenTeam[id2].connections.add(greenTeam[id1]);
            }  
            scanner.close();
        } catch(IOException e) {  
            e.printStackTrace();  
        }
        this.redAgent = new RedAgent();
        this.blueAgent = new BlueAgent();
        this.day = 0;
    }

    public void nextRound(){
        day++;
        System.out.println("day " + day);
        redAgent.redTurn(greenTeam);
    }

    public void start(){
        while(day != daysToElection){
            nextRound();
        }
        System.out.println("Game Over");
    }

    public void addAgent(String info){
        String[] columns = info.split(",");
    }
    
    public static void printUsage(){
        System.out.println("Usage: java Game <days> <size of green team> <size of grey team> <network % density> <% of greys on blue team>");
        System.out.println("     : java Game -f <days> <size of green team> <size of grey team> <network file> <% of greys on blue team>");
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
            game = new Game(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), args[4], Integer.valueOf(args[5]));
        } else {
            game = new Game(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
        }
        // start the game
        if(game != null)
            game.start();
        else
            System.out.println("something went wrong");
    }
}

