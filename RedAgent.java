import java.util.*;
/**
 * This class defines the properties of the red team
 */

public class RedAgent {
    // this agents unique id
    double messagePotency;

    public RedAgent() {
        this.messagePotency = 0.0;
    }

    public GreenAgent[] redTurn(GreenAgent[] greenTeam) {
        Random randMP = new Random();
        Random randLF = new Random();
        Random randTC = new Random();
        messagePotency = randMP.nextDouble((2.5-0.5) + 0.5) + 0.5;
        // loop through greenteam members and interact with them.
        for (int i = 0; i < greenTeam.length; i++) {
            if (!greenTeam[i].canRedCommunicate) {
                continue;
            }
            double currentUncertainty = greenTeam[i].uncertainty;
            double lostFollowerProbability = GameLibrary.lossFollowerProbability( (int) currentUncertainty);
            boolean lostFollower = randLF.nextInt(1,101) <= lostFollowerProbability;

            // code to see if follower is lost.
            if (lostFollower) {
                greenTeam[i].canRedCommunicate = false;
                continue;
            }

             // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * messagePotency) / 10;
            double directionProbaility = GameLibrary.changeDirectionProbabilty( (int) currentUncertainty);
            boolean towardCertainty = randTC.nextInt(1,101) <= directionProbaility;

            // toward uncertainty
            if (!towardCertainty) {
                // if they are sided with red team, only increase their uncertainty by only half compared to if they were sided with blue
                if (!greenTeam[i].willVote) {
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
                    greenTeam[i].uncertainty = currentUncertainty + uncertaintyChange;
                }
            } 
            // toward certainty
            else {
                // if they are sided with blue team, only increase their certainty by only half compared to if they were sided with red
                if (greenTeam[i].willVote) {
                    uncertaintyChange = uncertaintyChange / 2;
                }
                if ((currentUncertainty - uncertaintyChange) < 0) {
                    greenTeam[i].uncertainty = 0;
                }
                else {
                    greenTeam[i].uncertainty = currentUncertainty - uncertaintyChange;
                }
            }
        }
        return greenTeam;
    }
}