import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Flappy Bird");
    GamePanel gamePanel = new GamePanel();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setResizable(false);
    frame.add(gamePanel);
    frame.setVisible(true);
  }
}