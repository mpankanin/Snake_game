package Projekt1_Snake;

import javax.swing.*;

public class MyFrame extends JFrame{

    public MyFrame(){
        this.add(new MyPanel());

        ImageIcon img = new ImageIcon("src/Projekt1_Snake/apple.png");
        this.setIconImage(img.getImage());
        this.setTitle("Snake s24188");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

