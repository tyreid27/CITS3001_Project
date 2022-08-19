import java.util.*;
/**
 * This class defines the properties of an agent on the green team.
 * The green team is the team that is to be convinced whether or not
 * to vote in the upcomming election.
 */
public class GreenAgent {
    // an adjacency list of the green agents that this agent can 'talk' to
    ArrayList<GreenAgent> connections;
    // true if the agent will be voting in the election, false otherwise
    boolean willVote;
    // higher uncertainity means higher probability the agents opinion will change (-10 to 10)
    int uncertainity;
    
    /**
     * constructor for green agent. Manually set the default parameters.
     * @param connections an array of GreenAgent that this agent can talk to.
     * @param willVote the value for if this agent will vote.
     */
    public GreenAgent(ArrayList<GreenAgent> connections, boolean willVote, int uncertainity){
        this.connections = new ArrayList<GreenAgent>();
        this.willVote = willVote;
        if(uncertainity <= 10 && uncertainity >= -10){
            this.uncertainity = uncertainity;
        } else{
            this.uncertainity = 0;
        }
    }

    /**
     * constructor for green agent that sets random parameters for willVote and uncertainity.
     */
     public GreenAgent(){
        this.connections = new ArrayList<GreenAgent>();
        Random rand = new Random();
        // randomly choose True or False
        this.willVote = rand.nextBoolean();
        // choose int between 0 and 21 for range -10 to 10
        this.uncertainity = rand.nextInt(21) - 10;
     }
}