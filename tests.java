import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

  public class tests extends JPanel {
  private double[] wins;
  private String[] team;
  private String title;

  public tests(double[] wins, String[] team, String t) {
    this.wins = wins;
    this.team = team;
    title = t;
  }
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    if (wins == null || wins.length == 0) {
      return;
    }
    double minWins = 0;
    double maxWins = 0;
    for (int i = 0; i < wins.length; i++) {
      if (minWins > wins[i]) {
        minWins = wins[i];
      }
      if (maxWins < wins[i]) {
        maxWins = wins[i];
      }
    }
    Dimension dim = getSize();
    int clientWidth = dim.width;
    int clientHeight = dim.height;
    int barWidth = clientWidth / wins.length;
    Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
    FontMetrics titleFontMetrics = graphics.getFontMetrics(titleFont);
    Font labelFont = new Font("Book Antiqua", Font.PLAIN, 10);
    FontMetrics labelFontMetrics = graphics.getFontMetrics(labelFont);
    int titleWidth = titleFontMetrics.stringWidth(title);
    int q = titleFontMetrics.getAscent();
    int p = (clientWidth - titleWidth) / 2;
    graphics.setFont(titleFont);
    graphics.drawString(title, p, q);
    int top = titleFontMetrics.getHeight();
    int bottom = labelFontMetrics.getHeight();
    if (maxWins == minWins) {
      return;
    }
    double scale = (clientHeight - top - bottom) / (maxWins - minWins);
    q = clientHeight - labelFontMetrics.getDescent();
    graphics.setFont(labelFont);
    for (int j = 0; j < wins.length; j++) {
      int valueP = j * barWidth + 1;
      int valueQ = top;
      int height = (int) (wins[j] * scale);
      if (wins[j] >= 0) {
        valueQ += (int) ((maxWins - wins[j]) * scale);
      } else {
        valueQ += (int) (maxWins * scale);
        height = -height;
      }
      if (j == 0) {
        graphics.setColor(Color.red);
      } else {
        graphics.setColor(Color.blue);
      }
      graphics.fillRect(valueP, valueQ, barWidth - 2, height);
      graphics.setColor(Color.black);
      graphics.drawRect(valueP, valueQ, barWidth - 2, height);
      int labelWidth = labelFontMetrics.stringWidth(team[j]);
      p = j * barWidth + (barWidth - labelWidth) / 2;
      graphics.drawString(team[j], p, q);
    }
  }
 public static void main(String[] args) {
  int redWins = 0;
  int blueWins = 0;
  for (int i = 0; i < 100; i++) {
    Game game = new Game(15, 51, 10, 3, 50, 'n');
    game.start();
    game.end();
    if (game.winner == 0) {
      redWins++;
    }
    if (game.winner == 1) {
      blueWins++;
    }
  }
  JFrame frame = new JFrame();
  frame.setSize(350, 300);
  double[] wins = new double[2];
  String[] team = new String[2];
  wins[0] = redWins;
  team[0] = "Red Team Wins: " + redWins;

  wins[1] = blueWins;
  team[1] = "Blue Team Wins: " + blueWins;
  
  frame.getContentPane().add(new tests(wins, team,
    "Starting Uncertainty 0 - 10"));

  WindowListener winListener = new WindowAdapter() {
    public void windowClosing(WindowEvent event) {
      System.exit(0);
    }
  };
  frame.addWindowListener(winListener);
  frame.setVisible(true);
  }
}