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
    int numAgents;
    // number of green agents
    int nGreen;
    // number of grey agents
    int nGrey;
    // number of agents that are currently going to vote at the election
    int willVote;
    // our agents
    ArrayList<GreyAgent> greyTeam;
    ArrayList<GreenAgent> greenTeam;
    RedAgent redAgent;
    BlueAgent blueAgent;

    public Game(int days){
        this.daysToElection = days;
        // initialise array of green team members 
        this.greenTeam = new ArrayList<GreenAgent>();
        this.redAgent = new RedAgent();
        this.blueAgent = new BlueAgent();
    }

    public void nextRound(){
        day++;
        System.out.println("day " + day);
        //redAgent.redTurn(greenTeam);
    }

    public void addAgent(String info){
        String[] columns = info.split(",");
    }

    public void newConnection(int id1, int id2){
        // stuff here
    }

    public void start(){
        while(day != daysToElection){
            nextRound();
        }
        System.out.println("Game Over");
    }
    
    public static void main(String[] args){
        // create a new game
        Game game = new Game(5);
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

