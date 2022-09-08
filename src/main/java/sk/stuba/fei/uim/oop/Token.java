package sk.stuba.fei.uim.oop;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Token extends JPanel{

    ArrayList<Token> neighbours = new ArrayList<>();

    @Setter
    @Getter
    private Color color;

    public boolean putToken;
    public boolean isPlayable;

    public ArrayList<Integer> possibleMovesDirection = new ArrayList<>();
    public ArrayList<Token> possibleMoves = new ArrayList<>();


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        if (putToken){
            g.setColor(color);
            g.fillOval(0,0,this.getWidth(),this.getHeight());
        }
        if (!putToken && isPlayable){
            g.setColor(new Color(255, 0, 0));
            g2D.setStroke(new BasicStroke(3));
            g.drawOval(0,0,this.getWidth(),this.getHeight());
        }
    }


}
