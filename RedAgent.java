import java.util.*;
/**
 * This class defines the properties of the red team
 */

public class RedAgent {
    int messagePotency;

    public RedAgent() {
        this.messagePotency = 1;
    }

    public void redTurn(GreenAgent[] greenTeam) {
        Random rand = new Random();
        messagePotency = rand.nextInt((5-1) + 1) + 1;
        for (int i = 0; i < greenTeam.length; i++) {
            // loop through greenteam members and interact with them.
        }
    }
}