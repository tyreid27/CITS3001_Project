import java.util.*;

public class BlueAI{
    // the day that we're on
    int day;
    // number of voters after red turn
    int voterCountAfterRed;
    // number of voters after blue turn
    int voterCountAfterBlue;
    // number of voters after green turn
    int voterCountAfterGreen;
    // difference in voters that red caused
    int voterDiffAfterRed;
    // difference in voters that blue caused
    int voterDiffAfterBlue;
    // the total certainty so far
    int totalCertainty;
    // the average certainty sent out so far (total/days)
    double avgCertainty;
    // number of grey agents used so far
    int greyCount;
    // number of times where using the grey agent was not good for blue
    int greyBad;
    // number of rounds that we gain followers
    int gainFollowerCount;
    // probability grey is blue
    double probGreyBlue;
    // did the ai pick the grey agent the previous round
    boolean greyPreviousRound;

    public BlueAI(){
        day = 0;
        voterCountAfterRed = 0;
        voterCountAfterBlue = 0;
        voterCountAfterGreen = 0;
        voterDiffAfterRed = 0;
        voterDiffAfterBlue = 0;
        totalCertainty = 0;
        avgCertainty = 0;
        greyCount = 0;
        greyBad = 0;
        gainFollowerCount = 0;
        probGreyBlue = 0.3;
        greyPreviousRound = false;
    }

    // get a good certainty to send out
    public int nextCertainty(int vCAR){
        // get some information about the environment for the first round
        if (day == 0) {
            Random rand = new Random();
            int certainty = rand.nextInt(3,6);
            totalCertainty += certainty;
            return certainty;
        }
        // get avergae certainty
        avgCertainty = (double) totalCertainty / (double) day;
        // the return value
        int certainty = 3;
        // see the effect of reds turn
        voterDiffAfterRed = voterCountAfterRed - voterCountAfterGreen;
        // see the effect of our turn from last round
        voterDiffAfterBlue = voterCountAfterBlue - voterCountAfterRed;
        // big problem if our followers have decreased after our own turn
        if(voterDiffAfterBlue < 0){
            certainty++;
            // our followers decreased after red turn
            if (voterDiffAfterRed < 0){
                certainty++;
            }
            else{
                certainty--;
            }
        }
        else{
            gainFollowerCount++;
            certainty--;
        }
        // we dont want to run out of energy before election is over
        if (avgCertainty > 4.5){
            // we need to dial down our energy spending
            certainty--;
            if (certainty < 1){
                certainty = 1;
            }
        }
        else if (avgCertainty < 3.5){
            // spend more!!
            certainty += 2;
            if(certainty > 5){
                certainty = 5;
            }
        }
        // update total certainty sent out
        totalCertainty += certainty;
        // update the probability of selecting a grey agent
        if(greyPreviousRound){
            // grey screwed blue over
            if(voterDiffAfterBlue <= 0){
                greyBad++;
                probGreyBlue = (0.2 * probGreyBlue) / (0.2 * probGreyBlue + (1.0-probGreyBlue) * 0.8);
            }
            // grey helped blue
            else if(voterDiffAfterBlue > 0){
                probGreyBlue = (0.7 * probGreyBlue) / (0.7 * probGreyBlue + (1.0-probGreyBlue) * 0.3);
            }
            greyPreviousRound = false;
        }
        // update the voter after red turn to this round because blue no longer needs the previous rounds one
        voterCountAfterRed = vCAR;
        return certainty;
    }

    // we choose a grey agent using the bayesian model
    public boolean useGrey(){
        // get some information about the environment for the first round
        if (day == 0){
            greyPreviousRound = false;
            day++;
            return false;
        }
        day++;
        Random rand = new Random();
        double dub = rand.nextDouble(0, 1);
        if (dub < probGreyBlue){
            greyPreviousRound = true;
            return true;
        }
        else{
            greyPreviousRound = false;
            return false;
        }
    }

    // method to update the agents knowledge after its own turn
    public void updateVCABlue(int count){
        voterCountAfterBlue = count;
    }

    // method to update the agents knowledge after greens turn
    public void updateVCAGreen(int count){
        voterCountAfterGreen = count;
    }
}