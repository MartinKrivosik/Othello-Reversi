package sk.stuba.fei.uim.oop;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;

public class Player {

    @Setter
    @Getter
    private int score;

    @Setter
    @Getter
    private Color myColor;


    public ArrayList<Token> possibleMovesPlayer = new ArrayList<>();
    public ArrayList<Integer> possibleMovesWeight = new ArrayList<>();

    public ArrayList<Token> myToken = new ArrayList<>();

    void checkPossibleMoves(){
        possibleMovesPlayer.clear();
        possibleMovesWeight.clear();
        for (Token token : myToken) {
            token.possibleMoves.clear();
            token.possibleMovesDirection.clear();
            for (int i = 0; i < 8; i++) {
                int weight = 0;
                Token place = token;
                if (place.neighbours.get(i) != null){
                    Color nColor = place.neighbours.get(i).getColor();
                    while (nColor != myColor && nColor != null && place.neighbours.get(i) != null) {
                        weight++;
                        place = place.neighbours.get(i);
                        if (place.neighbours.get(i) != null){
                            nColor = place.neighbours.get(i).getColor();
                        }
                        else{
                            break;
                        }
                    }
                    if (nColor == null && place.getColor() != myColor && place.getColor() != null) {
                        token.possibleMoves.add(place.neighbours.get(i));
                        possibleMovesPlayer.add(place.neighbours.get(i));
                        possibleMovesWeight.add(weight);
                        place.neighbours.get(i).isPlayable = true;
                        token.possibleMovesDirection.add(1);
                    } else {
                        token.possibleMoves.add(null);
                        token.possibleMovesDirection.add(0);
                    }
                }
                else{
                    token.possibleMoves.add(null);
                    token.possibleMovesDirection.add(0);
                }
            }
        }
    }

    void checkTakingMoves(Token played){
        for (Token token : myToken){
            for (int i = 0; i < 8; i++){
                if (token.possibleMoves.get(i) == played){
                    if (token.possibleMovesDirection.get(i) == 1){
                        Token place = token.neighbours.get(i);
                        while(place != played){
                            place.setColor(myColor);
                            place.repaint();
                            place = place.neighbours.get(i);
                        }
                    }
                }
            }
        }
    }

    void scoreAdd(){
        score++;
    }

}
