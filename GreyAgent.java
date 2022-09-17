import java.util.*;

/**
 * This class defines the properties of an agent on the grey class
 * The grey team can either work for the blue team or red team
 * but they cannot vote in the election and the blue and red
 * do not know what side they are on
 */
public class GreyAgent{
    // the team that the particular grey agent works for. 0: green, 1: red
    int worksFor;
    // which green team agents this grey agent can talk to
    ArrayList<GreenAgent> connections;
    // the id of this greyAgent to keep track of grey agents
    int id;

    /**
     * constructor for random grey agent
     */
    public GreyAgent(int id){
        Random rand = new Random();
        this.worksFor = rand.nextInt(2);
        this.id = id;
    }
}