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

    public void greyTurn(GreenAgent[] greenTeam, int potency) {
        for (int i = 0; i < greenTeam.length; i++) {
            double currentUncertainty = greenTeam[i].uncertainty;
            // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * (potency / 2)) / 10;
            
            if (worksFor == 0) {
                if (greenTeam[i].willVote) {
                    if ((currentUncertainty - uncertaintyChange) < 0) {
                        greenTeam[i].uncertainty = 0;
                    }
                    else {
                        greenTeam[i].uncertainty -= uncertaintyChange;
                    }
                }
                else {
                    if ((currentUncertainty + uncertaintyChange) > 10) {
                        greenTeam[i].uncertainty = 10 - (currentUncertainty + uncertaintyChange - 10);
                        greenTeam[i].willVote = true;
                    }
                    else {
                        greenTeam[i].uncertainty += uncertaintyChange;
                    }
                }
            } else {
                if (!greenTeam[i].willVote) {
                    if ((currentUncertainty - uncertaintyChange) < 0) {
                        greenTeam[i].uncertainty = 0;
                    }
                    else {
                        greenTeam[i].uncertainty -= uncertaintyChange;
                    }
                }
                else {
                    if ((currentUncertainty + uncertaintyChange) > 10) {
                        greenTeam[i].uncertainty = 10 - (currentUncertainty + uncertaintyChange - 10);
                        greenTeam[i].willVote = false;
                    }
                    else {
                        greenTeam[i].uncertainty += uncertaintyChange;
                    }
                }
            }
           
        }
        if (worksFor == 0) {
            System.out.println("I am a lifeline for Blue Team");
        } else {
            System.out.println("I am a spy for Red Team");
        }
        System.out.println("Sent out a potency value of " + potency + "\n");
    }
}