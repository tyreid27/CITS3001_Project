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
     * @param prop the proportion of green agents initially voting / not voting
     */
    public Game(int days, int nGreen, int nGrey, int prob, int prop){
        this.daysToElection = days;
        // initialise array of green team members 
        this.greenTeam = new GreenAgent[nGreen];
        for(int i = 0; i < nGreen; i++)
            greenTeam[i] = new GreenAgent();
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
    
    public static void main(String[] args){
        // create a new game
        Game game = new Game(5, 100, 10, 50, 50);
        // initialise all the agents
        try{
            File agents = new File("agents.csv");
            Scanner agent = new Scanner(agents);
            while (agent.hasNextLine()){
                String data = agent.nextLine();
                game.addAgent(data);
            }
            agent.close();
        }
        catch(FileNotFoundException e){
            System.out.println("An error occured while trying to open 'agents.csv");
        }
        // start the game
        game.start();
    }
}

