package Projekt1_Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Random;

public class MyPanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    private static final int DELAY = 100;
    private int[] x = new int[GAME_UNITS];
    private int[] y = new int[GAME_UNITS];
    private int bodySize = 6;
    private int appleX, appleY, appleEaten, highScore;
    private char direction = 'R';
    private boolean running = false;
    private boolean startScreen = false;
    private Timer timer;
    private Random random;
    private Image apple, startIcon;

    public MyPanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        startScreen = true;
        this.setFocusable(true);
        super.addKeyListener(new MyKeyAdapter());
        //startGame();

    }

    public void startGame() {
        addApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (startScreen){
            startIcon = new ImageIcon("src/Projekt1_Snake/MySnakeImage.png").getImage();
            g.drawImage(startIcon, 0, 0, null);
            g.setFont(new Font("Helvetica", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString("Press ENTER to play!", 150, 530);
            //g.drawString("High score: " + highScore, 195, 80);
        }
        else if (running) {
            ImageIcon imageApple = new ImageIcon("src/Projekt1_Snake/apple.png");
            apple = imageApple.getImage();
            g.drawImage(apple, appleX, appleY, this);
            //g.setColor(Color.GREEN);
            //g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.GREEN);
            for (int i = 0; i < bodySize; i++) {
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        else{
            gameOver(g);
        }
    }

    public void move(){
        for(int i = bodySize; i>0; i--){
            //System.out.println("X: " + x[i] + "," + "Y :" + y[i]);
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void addApple(){
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)) {
            appleEaten++;
            bodySize++;
            addApple();
        }
    }

    public void checkCollisions(){
        for (int i = 1; i <= bodySize; i++) {
            if(x[0] == x[i] && y[0] == y[i])
                running = false;
        }

        if(x[0] < 0 || x[0] >= SCREEN_WIDTH)
            running = false;

        if(y[0] < 0 || y[0] >= SCREEN_HEIGHT)
            running = false;

        if(!running)
            timer.stop();
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH-metrics.stringWidth("Game over"))/2, SCREEN_HEIGHT/2);

        try {
            checkHighScore();
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.setFont(new Font("Helvetica",Font.BOLD, 30));
        metrics = getFontMetrics(g.getFont());
        g.drawString("High score: " + highScore, (SCREEN_WIDTH-metrics.stringWidth("High score:  "))/2, (SCREEN_HEIGHT/2)+80);
        g.drawString("Current score: " + appleEaten, (SCREEN_WIDTH-metrics.stringWidth("Current score:  "))/2, (SCREEN_HEIGHT/2)+40);
        g.setColor(Color.RED);
        g.drawString("Press ENTER to play again", (SCREEN_WIDTH-metrics.stringWidth("Press ENTER to play again"))/2, (SCREEN_HEIGHT/2)-100);
        if( (appleEaten >= highScore) && (appleEaten != 0) ) {
            g.setFont(new Font("Helvetica",Font.BOLD, 15));
            g.setColor(Color.RED);
            g.drawString("Congratulations! You have just beaten highscore!", 120 , (SCREEN_HEIGHT/2)+120);
        }
    }

    public void checkHighScore() throws IOException {
        FileReader read = new FileReader("src/Projekt1_Snake/HighScore.txt");
        BufferedReader input = new BufferedReader(read);
        highScore = input.read();
        input.close();

        if(appleEaten > highScore){
            highScore = appleEaten;
            FileWriter file = new FileWriter("src/Projekt1_Snake/HighScore.txt");
            BufferedWriter output = new BufferedWriter(file);
            output.write(highScore);
            output.close();
        }
    }

    public void playAgain(){
        bodySize=6;
        appleEaten=0;
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];
        direction = 'R';
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT -> {
                    if(direction != 'R') {direction = 'L';}
                }
                case KeyEvent.VK_RIGHT -> {
                    if(direction != 'L') {direction = 'R';}
                }
                case KeyEvent.VK_UP -> {
                    if(direction != 'D') {direction = 'U';}
                }
                case KeyEvent.VK_DOWN -> {
                    if(direction != 'U') {direction = 'D';}
                }
                case KeyEvent.VK_ENTER -> {
                    if(!running && !startScreen) {
                        playAgain();
                    }
                    else if(startScreen){
                        startScreen = false;
                        startGame();
                    }

                }

                    }
                }
    }


}

