import java.util.*;

public class GameLibrary {
    public static int changeDirectionProbabilty(int uncertainty) {
        int probability = 0;
        if (uncertainty >= 0 && uncertainty <= 1) {
            probability = 95;
        }
        if (uncertainty > 1 && uncertainty <= 2) {
            probability = 90;
        }
        if (uncertainty > 2 && uncertainty <= 3) {
            probability = 85;
        }
        if (uncertainty > 3 && uncertainty <= 4) {
            probability = 80;
        }
        if (uncertainty > 4 && uncertainty <= 5) {
            probability = 75;
        }
        if (uncertainty > 5 && uncertainty <= 6) {
            probability = 70;
        }
        if (uncertainty > 6 && uncertainty <= 7) {
            probability = 65;
        }
        if (uncertainty > 7 && uncertainty <= 8) {
            probability = 60;
        }
        if (uncertainty > 8 && uncertainty <= 9) {
            probability = 55;
        }
        if (uncertainty > 9 && uncertainty <= 10) {
            probability = 50;
        }
        return probability;
    }
}