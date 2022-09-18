import java.util.*;
/**
 * This class defines the properties of the blue team
 */
public class BlueAgent{
    // this agents unique id
    int energy;
    double certainty; // certainty of the blue team message
    int numGainedVoters;

    public BlueAgent() {
        this.energy = 100;
        this.certainty = 0;
        this.numGainedVoters = 0;
    }

    public GreenAgent[] blueTurn(GreenAgent[] greenTeam) {
        Random randC = new Random();
        Random randTC = new Random();
        certainty = randC.nextDouble((2.5-0.5) + 0.5) + 0.5;
        for (int i = 0; i < greenTeam.length; i++) {
            // Every time blue gains 5 voters they gain 1 energy
            if (numGainedVoters == 5) {
                energy += 1;
                numGainedVoters = 0;
            }
            double currentUncertainty = greenTeam[i].uncertainty;
            // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * certainty) / 10;
            double directionProbaility = GameLibrary.changeDirectionProbabilty( (int) greenTeam[i].uncertainty);
            boolean towardCertainty = randTC.nextInt(1,101) <= directionProbaility;

            // toward uncertainty
            if (!towardCertainty) {
                // if they are sided with blue team, only increase their uncertainty by only three quaters of the red team value
                if (greenTeam[i].willVote) {
                    uncertaintyChange = uncertaintyChange * (6 / 8);
                }
                // if new uncertainty value becomes higher than 10, which indicates the agent now switches to willVote
                if ((currentUncertainty + uncertaintyChange) > 10) {
                    greenTeam[i].uncertainty = 10 - ((currentUncertainty + uncertaintyChange) - 10); 
                    // If green agent was voting for blue, switch to red and vice versa
                    if (greenTeam[i].willVote) {
                        greenTeam[i].willVote = false;
                    } else {
                        greenTeam[i].willVote = true;
                        numGainedVoters++;
                    }
                } else {
                    greenTeam[i].uncertainty = currentUncertainty + uncertaintyChange;
                }
            } 
            // toward certainty
            else {
                // if they are sided with red team, only increase their certainty by only three quarters compared to the blue value
                if (!greenTeam[i].willVote) {
                    uncertaintyChange = uncertaintyChange * (6 / 8);
                }
                if ((currentUncertainty - uncertaintyChange) < 0) {
                    greenTeam[i].uncertainty = 0;
                }
                else {
                    greenTeam[i].uncertainty = currentUncertainty - uncertaintyChange;
                }
            }
        }
        energy -= certainty;
        return greenTeam;
    }
}
