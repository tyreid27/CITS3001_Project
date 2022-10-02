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
    double uncertainty;
    // this agents unique id
    int id;
    // determines if the red team has 'lost' this green agent, i.e. if false red can no longer communicate
    boolean canRedCommunicate;
    
    /**
     * constructor for green agent. Manually set the default parameters.
     * @param connections an array of GreenAgent that this agent can talk to.
     * @param willVote the value for if this agent will vote.
     */
    public GreenAgent(ArrayList<GreenAgent> connections, boolean willVote, int uncertainty, int id){
        this.connections = new ArrayList<GreenAgent>();
        this.willVote = willVote;
        this.id = id;
        this.canRedCommunicate = true;
        if(uncertainty <= 10 && uncertainty >= -10){
            this.uncertainty = uncertainty;
        } else{
            this.uncertainty = 0;
        }
        //????? this doesnt work yet
    }

    /**
     * constructor for green agent that sets random parameters for willVote and uncertainty.
     */
     public GreenAgent(int id){
        this.connections = new ArrayList<GreenAgent>();
        Random rand = new Random();
        // randomly choose True or False
        this.willVote = rand.nextBoolean();
        // choose int between 0 to 10
        // Maybe change to 0-5 to make green agents more neutral in the start of the game
        this.uncertainty = rand.nextInt((10-5) + 1) + 5;
        this.id = id;
        this.canRedCommunicate = true;
     }
}