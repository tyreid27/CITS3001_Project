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
    
    /**
     * constructor for green agent. Manually set the default parameters.
     * @param connections an array of GreenAgent that this agent can talk to.
     * @param willVote the value for if this agent will vote.
     */
    public GreenAgent(ArrayList<GreenAgent> connections, boolean willVote, int uncertainty, int id){
        this.willVote = willVote;
        this.id = id;
        this.canRedCommunicate = true;
        if(uncertainty <= 10 && uncertainty >= -10){
            this.uncertainty = uncertainty;
        } else{
            this.uncertainty = 0;
        }
        //incomplete constructor
    }

    /**
     * constructor for green agent that sets random parameters for willVote and uncertainty.
     */
     public GreenAgent(int id){
        Random rand = new Random();
        // exactly 50% of green agents start voting
        if(id % 2 == 0)
            willVote = true;
        else
            willVote = false;
        // choose int between 0 and 10
        this.uncertainty = rand.nextDouble(2.5, 7.5);
        this.id = id;
        this.canRedCommunicate = true;
     }


     public static void greenTurn(GreenAgent[] greenTeam, int[][] network){

        for(int i = 0; i < greenTeam.length; i++){
            for(int j = 0; j < greenTeam.length; j++){
                if(i != j && network[i][j] != 0){
                    // System.out.println(i + " -> " + j);
                    // System.out.println("level: " + network[i][j]);
                    // System.out.println("uncertainty of " + i + ": " + greenTeam[i].uncertainty);
                    // System.out.println("will " + i + " vote: " + greenTeam[i].willVote);
                    // System.out.println("uncertainty of " + j + ": " + greenTeam[j].uncertainty);
                    // System.out.println("will " + j + " vote: " + greenTeam[j].willVote);
                    //if the uncertainty of i is less than j then i influences j
                    if(greenTeam[i].uncertainty < greenTeam[j].uncertainty){
                        if(greenTeam[i].willVote != greenTeam[j].willVote){
                            greenTeam[j].willVote = greenTeam[i].willVote;
                            // update the uncertainty if they weren't both voting the same way
                            double swing = greenTeam[i].uncertainty + (((10.0 - greenTeam[i].uncertainty) + (10.0 - greenTeam[j].uncertainty)) / 2);
                            double diff = swing - greenTeam[i].uncertainty;
                            double bonus = diff * (network[i][j]/10.0);
                            // System.out.println("Updated uncertainty of " + j + ": " + (swing - bonus));
                            // System.out.println("Updated Vote: " + greenTeam[j].willVote);
                            greenTeam[j].uncertainty = swing - bonus;
                        }
                        else{
                            // update the uncertainty if they were both voting the same way
                            double difference = greenTeam[j].uncertainty - greenTeam[i].uncertainty;
                            double multiplier = (network[i][j]/10.0);
                            double change = difference * multiplier;
                            // System.out.println("Updated uncertainty of " + j + ": " + (greenTeam[j].uncertainty - change));
                            // System.out.println("Updated Vote: " + greenTeam[j].willVote);
                            greenTeam[j].uncertainty = greenTeam[j].uncertainty - change;
                        }
                    }
                }
            }
        }
    }
}