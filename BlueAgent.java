import java.util.*;
import javax.swing.*;
/**
 * This class defines the properties of the blue team
 */
public class BlueAgent{
    int energy; // energy of the blue team
    int certainty; // certainty of the blue team message
    int numGainedVoters; // variable to keep track of number of voters blue team has gained
    int selectedGreyAgent; // used to keep track of grey agents
    boolean isUserPlaying; // determines if user is playing

    public BlueAgent(int energy, boolean isUserPlaying) {
        this.energy = energy;
        this.certainty = 0;
        this.numGainedVoters = 0;
        this.selectedGreyAgent = 0;
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

    public int useBlueAI (GreenAgent[] gameState, int daysLeft, char maximisingPlayer, int depth, int[][] network, GreyAgent[] greyTeam){
        Action action = minimax(gameState, daysLeft, maximisingPlayer, depth, network, greyTeam, this.selectedGreyAgent);
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

    public Action minimax(GreenAgent[] gameState, int daysLeft, char maximisingPlayer, int depth, int[][] network, GreyAgent[] greyTeam, int greySelected){
        int bestPotency = -1;
        if (depth == 0 || daysLeft == 0)
            return new Action(-1, getUtility(gameState, maximisingPlayer)); 
        if (maximisingPlayer == 'B'){
            int maxEvaluation = Integer.MIN_VALUE;
            // if ai does not use the grey agent
            for(int i = 1; i <= 5; i++){
                GreenAgent[] childState = copyState(gameState);
                BlueAgent tempBlue = new BlueAgent(100000, false);
                tempBlue.blueTurn(childState, greyTeam, false, i);
                GreenAgent.greenTurn(childState, network);
                Action evaluation = minimax(childState, daysLeft - 1, 'R', depth - 1, network, greyTeam, greySelected);
                if (evaluation.utility > maxEvaluation){
                    bestPotency = i;
                    maxEvaluation = evaluation.utility;
                }
            }
            // if ai use the grey agent
            for(int i = 1; i <= 5; i++){
                GreenAgent[] childState = copyState(gameState);
                BlueAgent tempBlue = new BlueAgent(10000, false);
                tempBlue.selectedGreyAgent = greySelected;
                tempBlue.blueTurn(childState, greyTeam, true, i);
                GreenAgent.greenTurn(childState, network);
                Action evaluation = minimax(childState, daysLeft - 1, 'R', depth - 1, network, greyTeam, greySelected + 1);
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
                RedAgent temporary = new RedAgent(false); 
                temporary.redTurn(childState, i);
                Action evaluation = minimax(childState, daysLeft - 1, 'B', depth - 1, network, greyTeam, greySelected + 1);
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

    public void blueTurn(GreenAgent[] greenTeam, GreyAgent[] greyTeam, boolean useGreyAgent, int certainty) {
        Scanner s = new Scanner(System.in);
        // If grey team is populated
        if (greyTeam != null) {
            // if there are no more grey agents then don't allow a grey agent to be selected
            if (selectedGreyAgent >= greyTeam.length) {
                if (isUserPlaying) {
                    System.out.println("You have run out of grey agents");
                }
            // offer the option to select a grey agent 
            } else {
                // If user is playing ask if they want to use a grey agent
                if (isUserPlaying) {
                    useGreyAgent = false;
                    int useGreyPrompt = JOptionPane.showConfirmDialog(null, "Would you like to use a grey agent?\nEnergy left: " + energy + "\nGrey agents remaining: " + (greyTeam.length - selectedGreyAgent), "Blue turn", JOptionPane.YES_NO_OPTION);
                    if(useGreyPrompt == 0){
                        useGreyAgent = true;
                    }
                }

                // If using grey agent ask for potency for grey agent and call grey turn
                if (useGreyAgent) {
                    int greyPotency = 0;
                    if (isUserPlaying) {
                        Object[] possibilities  = {"1 - We", "2 - Answer", "3 - To", "4 - No", "5 - One"};
                        String[] sPossibilities = {"1 - We", "2 - Answer", "3 - To", "4 - No", "5 - One"};
                        String string = (String)JOptionPane.showInputDialog(
                                    null,
                                    "Please select a message potency for your grey agent.\n"
                                    + "Higher is more potent.",
                                    "Blue turn",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    possibilities,
                                    "1");
                        for(int i = 0; i < sPossibilities.length; i++){
                            if(string.equals(sPossibilities[i])){
                                greyPotency = i + 1;
                            }
                        }
                    } else {
                        greyPotency = certainty;
                    }
                    //System.out.println("Using Grey Agent");
                    greyTeam[selectedGreyAgent].greyTurn(greenTeam, greyPotency);
                    selectedGreyAgent++;
                    return;
                }
            }
        }

        // if user is playing then ask for user input for message potency
        if (isUserPlaying) {
            Object[] possibilities  = {"1 - I smell fine thank you", "2 - We strive to help the community", "3 - Red is fake news", "4 - Red has lower taxes but higher corruption!", "5 - Siding with red will our country in ruins"};
            String[] sPossibilities = {"1 - I smell fine thank you", "2 - We strive to help the community", "3 - Red is fake news", "4 - Red has lower taxes but higher corruption!", "5 - Siding with red will our country in ruins"};
            while(true) {
                String string = (String)JOptionPane.showInputDialog(
                            null,
                            "Please select a message certainty.\n"
                            + "Higher is more potent.",
                            "Blue turn",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            "1");
            
                for(int i = 0; i < sPossibilities.length; i++){
                    if(string.equals(sPossibilities[i])){
                        this.certainty = i + 1;
                    }
                }
                if (this.certainty <= energy){
                    break;
                }
                JOptionPane.showMessageDialog(null,
                            "You do not have enough energy to do that.",
                            "Blue turn",
                            JOptionPane.WARNING_MESSAGE,
                            null);
            }
        } else {
            this.certainty = certainty;
        }

        for (int i = 0; i < greenTeam.length; i++) {
            // Allows blue team to gain energy when they gain voters. Different numbers of voters gained is required based on how many green agents there are
            if (greenTeam.length > 1000 && numGainedVoters == 50) {
                energy += 1;
                numGainedVoters = 0;
            }
            if (greenTeam.length > 100 && numGainedVoters == 15) {
                energy += 1;
                numGainedVoters = 0;
            }
            if (greenTeam.length > 10 && numGainedVoters == 5) {
                energy += 1;
                numGainedVoters = 0;
            }

            // uncertainty of current green agent
            double currentUncertainty = greenTeam[i].uncertainty;
            // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * (this.certainty / 2)) / 10;

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
                    numGainedVoters++;
                }
                else {
                    greenTeam[i].uncertainty += uncertaintyChange;
                }
            }
        }
        // deduct energy from blue based on the certainty they sent out
        energy -= this.certainty;
    }
}
