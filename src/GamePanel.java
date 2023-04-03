import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

class GamePanel extends JPanel implements ActionListener, KeyListener {
  Timer timer;
  ArrayList<Rectangle> pipes;
  int ticks, yMotion, score;
  boolean gameOver, gameStarted;

  public GamePanel() {
    timer = new Timer(20, this);
    pipes = new ArrayList<>();
    addPipe(true);
    addPipe(true);
    addPipe(true);
    addPipe(true);

    addKeyListener(this);
    setFocusable(true);
    timer.start();
  }

  public void addPipe(boolean start) {
    int space = 300;
    int width = 100;
    int height = 50 + new Random().nextInt(300);

    if (start) {
      pipes.add(new Rectangle(800 + width + pipes.size() * 300,
                              600 - height - 150, width, height));
      pipes.add(new Rectangle(800 + width + (pipes.size() - 1) * 300,
                              0, width, 600 - height - space));
    } else {
      pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 600,
                              600 - height - 150, width, height));
      pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x,
                              0, width, 600 - height - space));
    }
  }

  public void paintPipe(Graphics g, Rectangle pipe) {
    g.setColor(Color.GREEN.darker());
    g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
  }

  public void jump() {
    if (gameOver) {
      pipes.clear();
      yMotion = 0;
      ticks = 0;
      addPipe(true);
      addPipe(true);
      addPipe(true);
      addPipe(true);
      gameOver = false;
      score = 0; // Reset the score to 0
    }

    if (!gameStarted) {
      gameStarted = true;
    }

    if (yMotion > 0) {
      yMotion = 0;
    }

    yMotion -= 15;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    draw(g);
  }

  public void draw(Graphics g) {
    g.setColor(Color.cyan);
    g.fillRect(0, 0, 800, 600);

    g.setColor(Color.orange);
    g.fillRect(0, 600 - 150, 800, 150);

    g.setColor(Color.green);
    g.fillRect(0, 600 - 150, 800, 20);

    for (Rectangle pipe : pipes) {
      paintPipe(g, pipe);
    }

    int birdX = 400 - 10;
    int birdY = 600 / 2 + yMotion - 10;

    if (gameOver) {
      int shakeX = new Random().nextInt(6) - 3; // Random value between -3 and 3
      int shakeY = new Random().nextInt(6) - 3; // Random value between -3 and 3
      birdX += shakeX;
      birdY += shakeY;
    }

    g.setColor(Color.red);
    g.fillRect(birdX, birdY, 20, 20);

    g.setColor(Color.white);
    g.setFont(new Font("Arial", Font.PLAIN, 100));

    if (!gameStarted) {
      g.drawString("Press SPACE", 75, 300);
    }

    if (gameOver) {
      g.drawString("Game Over", 100, 300);
    }

    g.setFont(new Font("Arial", Font.PLAIN, 30));
    g.drawString(String.valueOf(score), 400 - 10, 50);
  }

  public void actionPerformed(ActionEvent e) {
    int speed = 5;

    ticks++;

    if (gameStarted && !gameOver) {
      for (int i = 0; i < pipes.size(); i++) {
        Rectangle pipe = pipes.get(i);
        pipe.x -= speed;
      }

      if (ticks % 2 == 0 && yMotion < 15) {
        yMotion += 3;
      }

      for (int i = 0; i < pipes.size(); i++) {
        Rectangle pipe = pipes.get(i);

        if (pipe.x + pipe.width < 0) {
          pipes.remove(pipe);

          if (pipe.y == 0) {
            addPipe(false);
          }
        }
      }

      Rectangle bird = new Rectangle(400 - 10, 600 / 2 + yMotion - 10, 20, 20);

      for (Rectangle pipe : pipes) {
        if (pipe.intersects(bird)) {
          gameOver = true;
          bird.x = pipe.x - bird.width;
        }
      }

      if (!gameOver && bird.x == pipes.get(0).x + pipes.get(0).width) {
        score++;
      }

      if (bird.y > 600 - 150 || bird.y < 0) {
        gameOver = true;
      }

      if (bird.y + yMotion >= 600 - 150) {
        bird.y = 600 - 150 - bird.height;
        gameOver = true;
      }
    }

    repaint();
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      jump();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
