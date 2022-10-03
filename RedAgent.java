import java.util.*;
/**
 * This class defines the properties of the red team
 */

public class RedAgent {
    // potency of message
    int messagePotency;
    int totalFollowersLost;
    int previousTurn;
    int previousPreviousTurn;

    public RedAgent() {
        this.messagePotency = 0;
        this.totalFollowersLost = 0;
        this.previousTurn = 0;
        this.previousPreviousTurn = 0;
    }

    public void redTurn(GreenAgent[] greenTeam) {
        Random rand = new Random();
        messagePotency = rand.nextInt((5-1) + 1) + 1;
        int followersLost = 0;
        boolean willLose = false; // boolean to have so that only every second follower is lost when message potency is high.
        // loop through greenteam members and interact with them.
        for (int i = 0; i < greenTeam.length; i++) {
            if (!greenTeam[i].canRedCommunicate) {
                continue;
            }
            double currentUncertainty = greenTeam[i].uncertainty;
            
            if (greenTeam[i].willVote && currentUncertainty < 4) {
                if (messagePotency == 5) {
                    if (willLose) {
                        greenTeam[i].canRedCommunicate = false;
                        followersLost++;
                        willLose = false;
                    } else {
                        willLose = true;
                    }
                }
                else if (messagePotency >= 4 && previousTurn >= 4) {
                    if (willLose) {
                        greenTeam[i].canRedCommunicate = false;
                        followersLost++;
                        willLose = false;
                    } else {
                        willLose = true;
                    }
                }
                else if (messagePotency >= 3 && previousTurn >= 3 && previousPreviousTurn >= 3) {
                    if (willLose) {
                        greenTeam[i].canRedCommunicate = false;
                        followersLost++;
                        willLose = false;
                    } else {
                        willLose = true;
                    }
                }
            }

             // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * (messagePotency / 2)) / 10;

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
            
            totalFollowersLost += followersLost;
        }
        System.out.println("Red Teams Turn");
        System.out.println("Sent out a Potency value of " + messagePotency);
        System.out.println("Followers lost this round: " + followersLost + "\n");
        previousPreviousTurn = previousTurn;
        previousTurn = messagePotency;
        /*
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        
    }
}
