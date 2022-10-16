import java.util.*;
/**
 * This class defines the properties of an agent on the green team.
 * The green team is the team that is to be convinced whether or not
 * to vote in the upcomming election.
 */
public class GreenAgent {
    // true if the agent will be voting in the election, false otherwise
    boolean willVote;
    // higher uncertainty means higher probability the agents opinion will change (0 to 10)
    double uncertainty;
    // this agents unique id
    int id;
    // determines if the red team has 'lost' this green agent, i.e. if false red can no longer communicate
    boolean canRedCommunicate;

    public GreenAgent(int id, boolean willVote, boolean canRedCommunicate, double uncertainty){
        this.willVote = willVote;
        this.uncertainty = uncertainty;
        this.id = id;
        this.canRedCommunicate = canRedCommunicate;
    }

    /**
     * constructor for green agent that sets random parameters for willVote and uncertainty.
     */
     public GreenAgent(int id, boolean willVote){
        Random rand = new Random();
        // about 50% of green agents start voting
        // if(rand.nextInt(0,2) == 0)
        //     willVote = true;
        // else
        //     willVote = false;
        // choose int between 0 and 10
        this.uncertainty = rand.nextDouble(7,8);
        this.id = id;
        this.canRedCommunicate = true;
        this.willVote = willVote;
     }


     public static void greenTurn(GreenAgent[] greenTeam, int[][] network){
        // a new green agent array so the influencing process is fair for all agents
        GreenAgent[] newGreenTeam = new GreenAgent[greenTeam.length];
        // go through each green agent
        for(int i = 0; i < greenTeam.length; i++){
            // the average weighted opinion of agent i's connections
            double netNeighbourOpionion = 0;
            // total level of connections
            int influence = 0;
            // total number of connections for this agent
            int neighbours = 0;
            // go through that green agents neighbours
            for(int j = 0; j < greenTeam.length; j++){
                // ensure agents can't influence themselves
                if(i != j && network[i][j] != 0){
                    influence += network[i][j];
                    neighbours++;
                    // check connection level and update net opinion
                    if(greenTeam[j].willVote == true){
                        netNeighbourOpionion += (double) network[i][j] * greenTeam[j].uncertainty;
                    }
                    else{
                        netNeighbourOpionion -= (double) network[i][j] * greenTeam[j].uncertainty;
                    }
                }
            }
            //System.out.println("net neighbour opinion: " + netNeighbourOpionion);
            // bonus influence
            double bonus = (double) influence / (double) neighbours;
            // the the total amount the green agents certainty changes by
            double totalChange = 0;
            // calculate how much to nudge the green agent depending on the net opinion of its neighbours
            if(netNeighbourOpionion > 0){
                totalChange = 0.05;
            }
            else if(netNeighbourOpionion < 0){
                totalChange = -0.05;
            }
            else{
                totalChange = 0.02;
            }
            // updated uncertainty for the green agent
            double updatedUncertainty = 0;
            // updated opinion for the green agent
            boolean updatedOpionion = false;
            // logic for updating the uncertainty and opinion if required to
            if(greenTeam[i].willVote){
                updatedUncertainty = greenTeam[i].uncertainty - (totalChange * bonus);
                if(updatedUncertainty > 10.0){
                    updatedOpionion = false;
                    updatedUncertainty = 10.0 - (updatedUncertainty - 10.0);
                }
            }
            else{
                updatedUncertainty = greenTeam[i].uncertainty + (totalChange * bonus);
                if(updatedUncertainty > 10.0){
                    updatedOpionion = true;
                    updatedUncertainty = 10.0 - (updatedUncertainty - 10.0);
                }
            }
            newGreenTeam[i] = new GreenAgent(greenTeam[i].id, updatedOpionion, greenTeam[i].canRedCommunicate, updatedUncertainty);
        }
        greenTeam = newGreenTeam;
    }
}