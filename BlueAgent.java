import java.util.*;
/**
 * This class defines the properties of the blue team
 */
public class BlueAgent{
    // this agents unique id
    int energy;
    int certainty; // certainty of the blue team message
    int numGainedVoters;
    int selectedGreyAgent;
    boolean isUserPlaying;

    public BlueAgent(int energy, boolean isUserPlaying) {
        this.energy = energy;
        this.certainty = 0;
        this.numGainedVoters = 0;
        this.selectedGreyAgent = 0;
        this.isUserPlaying = isUserPlaying;
    }

    public void blueTurn(GreenAgent[] greenTeam, GreyAgent[] greyTeam) {
        System.out.println("Blue Teams Turn");
        boolean useGreyAgent = false;
        Random rand = new Random();
        Scanner s = new Scanner(System.in);
        
        // If any grey agents remain
        if (selectedGreyAgent > greyTeam.length) {
            System.out.println("You have run out of grey agents");
        }
        // No grey agents remain
        else {
            // If user is playing ask if they want to use a grey agent
            if (isUserPlaying) {
                while (true) {
                    try {
                        System.out.println("Do you want to use a grey agent? (y/n)");
                        char nextChar = s.next().charAt(0);
                        if (!(nextChar == 'y' || nextChar == 'Y' || nextChar == 'n' || nextChar == 'N')) {
                            throw new IllegalArgumentException();
                        }
                        if (nextChar == 'y' || nextChar == 'Y') {
                            useGreyAgent = true;
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Input was not valid, please try again\n");
                    }
                }
            } 
            // Else randomly generate response
            else {
                useGreyAgent = rand.nextInt(1,101) <= 20;
            }

            // If using grey agent ask for potency for grey agent and call grey turn
            if (useGreyAgent) {
                int greyPotency = 0;
                if (isUserPlaying) {
                    while (true) {
                        try {
                            System.out.println("Input a message potency from 1 to 5 for your grey agent");
                            greyPotency = s.nextInt();
                            if (greyPotency < 1 || greyPotency > 5) {
                                throw new IllegalArgumentException();
                            }
                        } catch (IllegalArgumentException e){
                            System.out.println("Your input was not in the correct range, please try again\n");
                        }
                    }
                } else {
                    greyPotency = rand.nextInt((5-1) + 1) + 1;
                }
                System.out.println("Using Grey Agent");
                greyTeam[selectedGreyAgent].greyTurn(greenTeam, greyPotency);
                selectedGreyAgent++;
                return;
            }
        }

        // if user is playing then ask for user input for message potency
        if (isUserPlaying) {
            // Continually asks for user input until it receives a valid number from 1 to 5
            while (true) {
                try {
                    System.out.println("Select a message certainty from 1 to 5");
                    certainty = s.nextInt();
                    if (certainty < 1 || certainty > 5) {
                        throw new IllegalArgumentException();
                    } else {
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Your input was not in the correct range, please try again\n");
                }
            }
        } else {
            certainty = rand.nextInt((5-1) + 1) + 1;
        }

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
        
        System.out.println("Sent out a certainty value of " + certainty);
        System.out.println("Energy Left: " + energy + "\n");
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
