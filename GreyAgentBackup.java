import java.util.*;

/**
 * This class defines the properties of an agent on the grey class
 * The grey team can either work for the blue team or red team
 * but they cannot vote in the election and the blue and red
 * do not know what side they are on
 */
public class GreyAgent{
    // the team that the particular grey agent works for. 0: blue, 1: red
    int worksFor;
    // which green team agents this grey agent can talk to
    ArrayList<GreenAgent> connections;
    // the id of this greyAgent to keep track of grey agents
    int id;

    /**
     * constructor for random grey agent
     */
    public GreyAgent(int id, int worksFor){
        Random rand = new Random();
        this.worksFor = worksFor;
        this.id = id;
    }

    public GreenAgent[] greyTurn(GreenAgent[] greenTeam) {
        Random rand = new Random();
        int potency = rand.nextInt((5-1) + 1) + 1;
        for (int i = 0; i < greenTeam.length; i++) {
            double currentUncertainty = greenTeam[i].uncertainty;
            // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * (potency / 2)) / 10;
            double directionProbaility = GameLibrary.changeDirectionProbabilty( (int) greenTeam[i].uncertainty);
            boolean towardCertainty = rand.nextInt(1,101) <= directionProbaility;

            // toward uncertainty
            if (!towardCertainty) {
                // if they are sided with blue team, only increase their uncertainty by only three quaters of the red team value
                if (greenTeam[i].willVote && worksFor == 0) {
                    uncertaintyChange = uncertaintyChange * 0.75;  
                }
                if (!greenTeam[i].willVote && worksFor == 1) {
                    uncertaintyChange = uncertaintyChange / 2;
                }
                // if new uncertainty value becomes higher than 10, which indicates the agent now switches to willVote
                if ((currentUncertainty + uncertaintyChange) > 10) {
                    greenTeam[i].uncertainty = 10 - ((currentUncertainty + uncertaintyChange) - 10); 
                    // If green agent was voting for blue, switch to red and vice versa
                    if (greenTeam[i].willVote) {
                        greenTeam[i].willVote = false;
                    } else {
                        greenTeam[i].willVote = true;
                    }
                } else {
                    greenTeam[i].uncertainty += uncertaintyChange;
                }
            } 
            // toward certainty
            else {
                // if they are sided with red team, only increase their certainty by only three quarters compared to the blue value
                if (!greenTeam[i].willVote && worksFor == 0) {
                    uncertaintyChange = uncertaintyChange * 0.75;
                }
                if (greenTeam[i].willVote && worksFor == 1) {
                    uncertaintyChange = uncertaintyChange / 2;
                }
                if ((currentUncertainty - uncertaintyChange) < 0) {
                    greenTeam[i].uncertainty = 0;
                }
                else {
                    greenTeam[i].uncertainty -= uncertaintyChange;
                }
            }
        }
        if (worksFor == 0) {
            System.out.println("I am a lifeline for Blue Team");
        } else {
            System.out.println("I am a spy for Red Team");
        }
        System.out.println("Sent out a potency value of " + potency + "\n");
        return greenTeam;
    }
}