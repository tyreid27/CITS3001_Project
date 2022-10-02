import java.util.*;
/**
 * This class defines the properties of the red team
 */

public class RedAgent {
    // potency of message
    int messagePotency;
    int totalFollowersLost;

    public RedAgent() {
        this.messagePotency = 0;
        this.totalFollowersLost = 0;
    }

    public GreenAgent[] redTurn(GreenAgent[] greenTeam) {
        Random rand = new Random();
        messagePotency = rand.nextInt((5-1) + 1) + 1;
        int followersLost = 0;
        // loop through greenteam members and interact with them.
        for (int i = 0; i < greenTeam.length; i++) {
            if (!greenTeam[i].canRedCommunicate) {
                continue;
            }
            double currentUncertainty = greenTeam[i].uncertainty;
            double lostFollowerProbability = GameLibrary.loseFollowerProbability( (int) currentUncertainty, messagePotency);
            boolean lostFollower = rand.nextInt(1,101) <= lostFollowerProbability;

            if (greenTeam[i].willVote ) {
                // code to see if follower is lost.
                if (lostFollower) {
                    greenTeam[i].canRedCommunicate = false;
                    followersLost++;
                    continue;
                }
            
             // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * (messagePotency / 2)) / 10;
            double directionProbaility = GameLibrary.changeDirectionProbabilty( (int) currentUncertainty);
            boolean towardCertainty = rand.nextInt(1,101) <= directionProbaility;

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
                    greenTeam[i].uncertainty += uncertaintyChange;
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
                    greenTeam[i].uncertainty -= uncertaintyChange;
                }
            }
        }
        totalFollowersLost += followersLost;
        System.out.println("Red Teams Turn");
        System.out.println("Sent out a Potency value of " + messagePotency);
        System.out.println("Followers lost this round: " + followersLost + "\n");
        /*
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return greenTeam;
    }
}