import java.util.*;
import javax.swing.*;
/**
 * This class defines the properties of the red team
 */

public class RedAgent {
    int messagePotency; // potency of message
    int totalFollowersLost; // total followers lost
    int previousTurn; // mP they sent out last turn
    int previousPreviousTurn; // mP they sent out 2 turns ago
    boolean isUserPlaying; // true if the user is playing

    public RedAgent(boolean isUserPlaying) {
        this.messagePotency = 0;
        this.totalFollowersLost = 0;
        this.previousTurn = 0;
        this.previousPreviousTurn = 0;
        this.isUserPlaying = isUserPlaying;
    }

    // make a copy of the state of the game
    public GreenAgent[] copyState(GreenAgent[] greenTeam){
        GreenAgent[] copy = new GreenAgent[greenTeam.length];
        for(int i = 0; i < greenTeam.length; i++){
            copy[i] = new GreenAgent(i);
            copy[i].willVote = greenTeam[i].willVote;
            copy[i].uncertainty = greenTeam[i].uncertainty;
            copy[i].id = greenTeam[i].id;
            copy[i].canRedCommunicate = greenTeam[i].canRedCommunicate;
        }
        return copy;
    }

    public int useRedAI (GreenAgent[] gameState, int daysLeft, char maximisingPlayer, int depth, int[][] network){
        Action action = minimax(gameState, daysLeft, maximisingPlayer, depth, network);
        return action.potency;
    }

    public int getUtility(GreenAgent[] gameState, char maximiser){
        // the heuristic is the number of green agents voting
        int count = 0;
        for(int i = 0; i < gameState.length; i++){
            if(gameState[i].willVote == true)
                count++;
        }
        return count;
    }

    public Action minimax(GreenAgent[] gameState, int daysLeft, char maximisingPlayer, int depth, int[][] network){
        int bestPotency = -1;
        if (depth == 0 || daysLeft == 0)
            return new Action(-1, getUtility(gameState, maximisingPlayer)); 
        if (maximisingPlayer == 'B'){
            int maxEvaluation = Integer.MIN_VALUE;
            for(int i = 1; i <= 5; i++){
                GreenAgent[] childState = copyState(gameState);
                // create a blue agent temporarily to use its methods
                BlueAgent temp = new BlueAgent(420, false);
                temp.blueTurn(childState, null, false, i);
                GreenAgent.greenTurn(childState, network);
                Action evaluation = minimax(childState, daysLeft - 1, 'R', depth - 1, network);
                if (evaluation.utility > maxEvaluation){
                    bestPotency = i;
                    maxEvaluation = evaluation.utility;
                }
            }
            return new Action(bestPotency, maxEvaluation); 
        }
        if (maximisingPlayer == 'R'){
            int minEvaluation = Integer.MAX_VALUE;
            for(int i = 1; i <= 5; i++){
                GreenAgent[] childState = copyState(gameState);
                RedAgent tempRed = new RedAgent(false);
                tempRed.redTurn(childState, i);
                Action evaluation = minimax(childState, daysLeft - 1, 'B', depth - 1, network);
                if (evaluation.utility < minEvaluation){
                    bestPotency = i;
                    minEvaluation = evaluation.utility;
                }
            }
            return new Action(bestPotency, minEvaluation);
        }
        System.out.println("minimax did not recieve 'R' or 'B'");
        return null;
    }

    private class Action{
        int potency;
        int utility;
        public Action(int potency, int utility){
            this.potency = potency;
            this.utility = utility;
        }
    }

    public void redTurn(GreenAgent[] greenTeam, int messagePotency) {
        // if user is playing then ask for user input for message potency
        if (isUserPlaying) {
            Scanner s = new Scanner(System.in);
            Object[] possibilities  = {"1 - blue is bad", "2", "3", "4", "5"};
            String[] sPossibilities = {"1 - blue is bad", "2", "3", "4", "5"};
            String string = (String)JOptionPane.showInputDialog(
                        null,
                        "Please select a message potency.\n"
                        + "Higher is more potent.",
                        "Red turn",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        "1");
            for(int i = 0; i < sPossibilities.length; i++){
                if(string.equals(sPossibilities[i])){
                    messagePotency = i + 1;
                }
            }
            // Continually asks for user input until it receives a valid number from 1 to 5
            // while (true) {
            //     try {
            //         System.out.println("Select a message potency from 1 to 5");
            //         messagePotency = s.nextInt();
            //         if (messagePotency < 1 || messagePotency > 5) {
            //             throw new IllegalArgumentException();
            //         } else {
            //             break;
            //         }
            //     } catch (IllegalArgumentException e) {
            //         System.out.println("Your input was not in the correct range, please try again\n");
            //     }
            // }
        } else {
            this.messagePotency = messagePotency;
        }
        int followersLost = 0;
        boolean willLose = false; // boolean to have so that only every second follower is lost when message potency is high rather than every follower
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
            double uncertaintyChange = (currentUncertainty * (messagePotency / 2)) / 8;

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
        totalFollowersLost += followersLost;
        // System.out.println("Red Teams Turn");
        // System.out.println("Sent out a Potency value of " + messagePotency);
        // System.out.println("Followers lost this round: " + followersLost + "\n");
        previousPreviousTurn = previousTurn;
        previousTurn = messagePotency;
    }
}
