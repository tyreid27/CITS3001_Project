import java.util.*;

/**
 * This is the class to call to start the game
 * Compile with "javac Game.java GreenAgent.java RedAgent.java GreyAgent.java"
 * Run with "java Game"
 */
public class Game{
    // number of days (rounds) until the game stops
    int daysToElection;
    // the day into the election campaign
    int day;
    // total number of agents in the game in total (green + grey)
    int numAgents;
    // number of green agents
    int nGreen;
    // number of grey agents
    int nGrey;
    // number of agents that will (currently) be voting at the election
    int willVote;
    // array of our green team agents
    GreenAgent[] greenTeam;
    // the red agent
    RedAgent redAgent;

    public Game(int days, int agents){
        this.daysToElection = days;
        this.numAgents = agents;
        // get the number of green agents
        this.nGreen = (int) Math.floor(agents * 0.9);
        // initialise array of green team members 
        this.greenTeam = new GreenAgent[nGreen];
        for(int i = 0; i < nGreen; i++){
            greenTeam[i] = new GreenAgent();
        }
        this.redAgent = new RedAgent();
    }

    public void nextRound(){
        day++;
        System.out.println("day " + day);
        //redAgent.redTurn(greenTeam);
    }

    public void start(){
        while(day != daysToElection){
            nextRound();
        }
        System.out.println("Game Over");
    }
    
    public static void main(String[] args){
        //open a file here and input the params into the game

        Game game = new Game(5, 10);
        game.start();
    }
}

