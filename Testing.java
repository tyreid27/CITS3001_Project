import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Color;
import java.util.*;

public class Testing extends Canvas {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Simulation");
        Canvas canvas = new Testing();
        canvas.setSize(1200, 800);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void paint(Graphics g) {
        g.setColor(Color.blue);
        g.fillOval(150, 400, 30, 30);
        g.setColor(Color.red);
        g.fillOval(1050, 400, 30, 30);
        int[][] locations = new int[25][2];
        double radius = 300;
        for(int i = 0; i <= 360; i = i + 15){
            double theta = (i-90) * Math.PI/180;
            double x = radius * Math.cos(theta);
            double y = -1 * radius * Math.sin(theta);
            locations[i / 15][0] = (int)x + 600;
            locations[i / 15][1] = (int)y + 400;
            draw(g);
        }
        g.setColor(Color.black);
        Random rand = new Random();
        int random = rand.newInt(11);
        for(int i = 0; i < locations.length; i++){
            for(int j = 0; j < locations.length; j++){
                if(random  > 5)
                    g.drawLine(locations[i][0], locations[i][1], locations[j][0], locations[j][1]);
            }
        }
         g.setColor(Color.green);
        for(int i = 0; i < locations.length; i++){
            g.fillOval(locations[i][0], locations[i][1], 10, 10);
        }
    }

    public void draw(Graphics g) {

    }
}