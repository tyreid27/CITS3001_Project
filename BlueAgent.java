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

    public void blueTurn(GreenAgent[] greenTeam, GreyAgent[] greyTeam) {
        /*Scanner s = new Scanner(System.in);
        System.out.println("Select a message potency from 1 to 5");
        certainty = s.nextInt();*/
        // set range for certainty
        // make sure blue can't go below energy of 0
        
        Random rand = new Random();
        boolean useGreyAgent = rand.nextInt(1,101) <= 20;
        if (useGreyAgent) {
            int selectedAgent = rand.nextInt(greyTeam.length);
            System.out.println("Blue Teams Turn");
            System.out.println("Using Grey Agent");
            greyTeam[selectedAgent].greyTurn(greenTeam);
            return;
        }
        certainty = rand.nextInt((5-1) + 1) + 1;
        for (int i = 0; i < greenTeam.length; i++) {
            // Every time blue gains 3 voters they gain 1 energy
            if (numGainedVoters == 5) {
                energy += 1;
                numGainedVoters = 0;
                System.out.println("Blue has gained an energy!");
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
        
        System.out.println("Blue Teams Turn");
        System.out.println("Sent out a certainty value of " + certainty);
        System.out.println("Energy Left: " + energy + "\n");
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
