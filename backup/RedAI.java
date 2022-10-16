import java.util.*;

// red ai is much simpler than blue ai because there are less options for red
public class RedAI{

    // number of days so far
    int days;
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
    // total potency
    int totalPotency;
    // average potency sent out so far
    double avgPotency;

    public RedAI(){
        days = 0;
        voterCountAfterRed = 0;
        voterCountAfterBlue = 0;
        voterCountAfterGreen = 0;
        voterDiffAfterRed = 0;
        voterDiffAfterBlue = 0;
        totalPotency = 0;
        avgPotency = 0.0;
    }

    public int nextPotency(int vCag){
        // get some information about the environment for the first round
        if (days == 0) {
            days++;
            Random rand = new Random();
            int potency = rand.nextInt(1,6);
            totalPotency += potency;
            return rand.nextInt(1,6);
        }
        // default potency
        int potency = 3;
        // get proportion of times red increased the voter count to decrease
        // get avergae potency
        avgPotency = (double) totalPotency / (double) days;
        // see the effect of reds turn
        voterDiffAfterRed = voterCountAfterRed - voterCountAfterGreen;
        // see the effect of our turn from last round
        voterDiffAfterBlue = voterCountAfterBlue - voterCountAfterRed;
        // reflex agent model (don't want to unecissarily send out high potency to not lose followers)
        // red did good job
        if(voterDiffAfterRed < 0){
            potency--;
            // blue did bad job
            if(voterDiffAfterBlue < 0){
                potency--;
            }
            // blue did a good job
            else{
                potency++;
            }
        }
        // if red lost followers then decrease the potency because we've been sending too many high potency messages

        // update total potency
        totalPotency += potency;
        // update days
        days++;
        // update the voter after green turn to this round because red no longer needs the previous rounds one
        voterCountAfterGreen = vCag;
        return potency;
    }

    // method to update the agents knowledge after its own turn
    public void updateVCARed(int count){
        voterCountAfterRed = count;
    }

    // method to update the agents knowledge after greens turn
    public void updateVCABlue(int count){
        voterCountAfterBlue = count;
    }
    
}