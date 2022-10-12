import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Panel extends JPanel
{

  final int NODES = 50;
    final double RADIUS = 350;

  // PROPERTIES
  private final int DEFAULT_WIDTH  = 1200;
  private final int DEFAULT_HEIGHT = 800;
  private final Color BACK_COLOR   = Color.WHITE;

  private int x1, y1, x2, y2;

  private MyMouseHandler handler;
  private Graphics g;

  // CONSTRUCTOR
  public Panel()
  {
    setBackground( BACK_COLOR );
    setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );

    handler  = new MyMouseHandler();

    this.addMouseListener( handler );
    //this.addMouseMotionListener( handler );
  }

  public void paint(Graphics g) {
    g.setColor(Color.blue);
        g.fillOval(150, 400, 30, 30);
        g.setColor(Color.red);
        g.fillOval(1050, 400, 30, 30);
        int[][] locations = new int[NODES][2];
        double radius = RADIUS;
        int counter = 0;
        for(double i = 0; i <= 359; i = i + (360.0 / NODES)){
            double theta = (i-90) * Math.PI/180;
            double x = radius * Math.cos(theta);
            double y = -1 * radius * Math.sin(theta);
            locations[counter][0] = (int)x + 600;
            locations[counter][1] = (int)y + 400;
            counter++;
        }
        g.setColor(Color.black);
        Random rand = new Random();
        for(int i = 0; i < locations.length; i++){
            for(int j = 0; j < locations.length; j++){
                int random = rand.nextInt(101);
                if(random < 10)
                    g.drawLine(locations[i][0], locations[i][1], locations[j][0], locations[j][1]);
            }
        }
         g.setColor(Color.green);
        for(int i = 0; i < locations.length; i++){
            g.fillOval(locations[i][0] - 4, locations[i][1] - 4, 10, 10);
        }
  } 

  // METHOD
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
  }

  private void setUpDrawingGraphics()
  {
    g = getGraphics();
  }

  // INNER CLASS
  private class MyMouseHandler extends MouseAdapter
  {
    public void mousePressed( MouseEvent e )
    {
      x1 = e.getX();
      y1 = e.getY();

      System.out.println("Mouse is being pressed at X: " + x1 + " Y: " + y1);

      setUpDrawingGraphics();

      x2=x1;
      y2=y1;
    }

    public void mouseDragged( MouseEvent e )
    {
      x1 = e.getX();
      y1 = e.getY();

      System.out.println("Mouse is being dragged at X: " + x1 + " Y: " + y1);  

      g.drawLine(x1,y1,x2,y2);

      x2=x1;
      y2=y1;
    }
  }

  public static void main(String[] args)
  {
    JFrame frame = new JFrame( "Run Panel" );
    frame.setDefaultCloseOperation(3);

    Panel  panel = new Panel();
    frame.add( panel );

    frame.pack();
    frame.setVisible( true );
  }

}