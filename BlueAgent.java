import java.util.*;
/**
 * This class defines the properties of the blue team
 */
public class BlueAgent{
    // this agents unique id
    int energy;
    int certainty; // certainty of the blue team message
    int numGainedVoters;

    public BlueAgent(int energy) {
        this.energy = energy;
        this.certainty = 0;
        this.numGainedVoters = 0;
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
        Action action = minimax(gameState, daysLeft, maximisingPlayer, depth, network, greyTeam);
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

    public Action minimax(GreenAgent[] gameState, int daysLeft, char maximisingPlayer, int depth, int[][] network, GreyAgent[] greyTeam){
        int bestPotency = -1;
        if (depth == 0 || daysLeft == 0)
            return new Action(-1, getUtility(gameState, maximisingPlayer)); 
        if (maximisingPlayer == 'B'){
            int maxEvaluation = Integer.MIN_VALUE;
            for(int i = 1; i <= 5; i++){
                GreenAgent[] childState = copyState(gameState);
                blueTurn(childState, null, false, i);
                GreenAgent.greenTurn(childState, network);
                Action evaluation = minimax(childState, daysLeft - 1, 'R', depth - 1, network, null);
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
                RedAgent temporary = new RedAgent(); 
                temporary.redTurn(childState, i);
                Action evaluation = minimax(childState, daysLeft - 1, 'B', depth - 1, network, null);
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
        /*Scanner s = new Scanner(System.in);
        System.out.println("Select a message potency from 1 to 5");
        certainty = s.nextInt();*/
        // set range for certainty
        // make sure blue can't go below energy of 0
        
        Random rand = new Random();
        //boolean useGreyAgent = rand.nextInt(1,101) <= 20;
        if (useGreyAgent) {
            int selectedAgent = rand.nextInt(greyTeam.length);
            //System.out.println("Blue Teams Turn");
            //System.out.println("Using Grey Agent");
            greyTeam[selectedAgent].greyTurn(greenTeam);
            return;
        }
        //certainty = rand.nextInt((5-1) + 1) + 1;
        for (int i = 0; i < greenTeam.length; i++) {
            // Every time blue gains 3 voters they gain 1 energy
            if (numGainedVoters == 5) {
                energy += 1;
                numGainedVoters = 0;
                //System.out.println("Blue has gained an energy!");
            }
            double currentUncertainty = greenTeam[i].uncertainty;
            // uncertaintyChange calculated to change uncertainty by 0 - 2.5 based on current uncertainty level and message potency
            double uncertaintyChange = (currentUncertainty * (certainty / 2)) / 15;

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
        energy -= certainty;
        
        // System.out.println("Blue Teams Turn");
        // System.out.println("Sent out a certainty value of " + certainty);
        // System.out.println("Energy Left: " + energy + "\n");
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
