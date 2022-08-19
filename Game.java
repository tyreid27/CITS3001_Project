import java.util.*;

/**
 * This is the class to call to start the game
 * Compile with "javac Game.java GreenAgent.java"
 * Run with "java Game"
 */
public class Game{
    // number of days (rounds) until the game stops
    int daysToElection;
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
    // ... more stuff ...

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
    }
    // class variables about the state of the game

    // constructor to make a new instance of the game

    // methods to execute certain moves which can be taken throughout the game


    public static void main(String[] args){
        Game game = new Game(5, 10);
        int day = 0;

        while(day != game.daysToElection){
            day++;
            System.out.println("day " + day);
        }

        System.out.println("game over");
    }
}

