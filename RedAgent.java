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

    // calculate probaility of continuing with current voting direction
    public int changeDirectionProbabilty(int uncertainty) {
        int probability = 0;
        if (uncertainty >= 0 && uncertainty <= 1) {
            probability = 95;
        }
        if (uncertainty > 1 && uncertainty <= 2) {
            probability = 90;
        }
        if (uncertainty > 2 && uncertainty <= 3) {
            probability = 85;
        }
        if (uncertainty > 3 && uncertainty <= 4) {
            probability = 80;
        }
        if (uncertainty > 4 && uncertainty <= 5) {
            probability = 75;
        }
        if (uncertainty > 5 && uncertainty <= 6) {
            probability = 70;
        }
        if (uncertainty > 6 && uncertainty <= 7) {
            probability = 65;
        }
        if (uncertainty > 7 && uncertainty <= 8) {
            probability = 60;
        }
        if (uncertainty > 8 && uncertainty <= 9) {
            probability = 55;
        }
        if (uncertainty > 9 && uncertainty <= 10) {
            probability = 50;
        }
        return probability;
    }

    public GreenAgent[] redTurn(GreenAgent[] greenTeam) {
        Random rand = new Random();
        Random random = new Random();
        messagePotency = rand.nextDouble((2.5-0.5) + 0.5) + 0.5;
        // loop through greenteam members and interact with them.
        for (int i = 0; i < greenTeam.length; i++) {
            double currentUncertainty = greenTeam[i].uncertainty;
             // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * messagePotency) / 10;
            double directionProbability = changeDirectionProbabilty(greenTeam[i].uncertainty);
            boolean towardCertainty = random.nextInt(1,101) <= directionProbability;

            // toward uncertainty
            if (!towardCertainty) {
                if (!greenTeam[i].willVote) {
                    // if new uncertainty value becomes higher than 10, which indicates the agent now switches to willVote
                    if ((greenTeam[i].uncertainty + uncertaintyChange) > 10) {
                        // from red to blue team
                        greenTeam[i].willVote = true;
                        greenTeam[i].uncertainty = 10 - ((currentUncertainty + uncertaintyChange) - 10); // 
                    } else {
                        greenTeam[i].uncertainty = currentUncertainty + uncertaintyChange;
                    }
                }
                else {
                    if ((greenTeam[i].uncertainty + uncertaintyChange) > 10) {
                        // from blue to red team
                        greenTeam[i].willVote = false;
                        greenTeam[i].uncertainty = 10 - ((currentUncertainty + uncertaintyChange) - 10);
                    } else {
                        greenTeam[i].uncertainty = currentUncertainty + uncertaintyChange;
                    }
                }
            } 
            // toward certainty
            else {
                if ((greenTeam[i].uncertainty - uncertaintyChange) < 0) {
                    greenTeam[i].uncertainty = 0;
                }
                else {
                    greenTeam[i].uncertainty -= uncertaintyChange;
                }
            }
        }
        return greenTeam;
    }
}