import java.util.*;
/**
 * This class defines the properties of the red team
 */

public class RedAgent {

    public RedAgent() {
        
    }

    public double uncertaintyChange(int messagePotency) {
        double uncertaintyChange
        Random rand = new Random();
        uncertainityChange = messagePotency-1 + (rand * (messagePotency - (messagePotency-1)));
        return uncertainityChange;
    }

    public GreenAgent[] redTurn(GreenAgent[] greenTeam) {
        Random rand = new Random();
        GreenAgent[] newGreenTeam = greenTeam;
        int messagePotency = rand.nextInt((5-1) + 1) + 1;
        for (int i = 0; i < greenTeam.length; i++) {
            double uncertaintyChange;
            // loop through greenteam members and interact with them.
            if (greenTeam[i].willVote) {

            } else {
                uncertaintyChange = calculateUncertainty(messagePotency);
                
            }
        }
        return newGreenTeam;
    }
}