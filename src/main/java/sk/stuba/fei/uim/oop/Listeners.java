package sk.stuba.fei.uim.oop;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Listeners implements MouseListener, KeyListener, ActionListener {

    public Player player1 = new Player();
    public Player player2 = new Player();

    private Settings settings;
    
    
    @Override
    public void mouseClicked(MouseEvent e) {

        Object c = e.getSource();

        if (((Token) c).isPlayable){
            ((Token) c).putToken = true;
            ((Token) c).setColor(Color.BLACK);
            player1.checkTakingMoves((Token)c);
            ((Token) c).repaint();
            settings.getBoard().repaint();
            enemyTurn();
        }


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object o = e.getSource();
        if (o instanceof Token && ((Token) o).isPlayable){
            ((Token) o).setBackground(((Token) o).getBackground().darker());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object o = e.getSource();
        if (o instanceof Token && ((Token) o).isPlayable){
            ((Token) o).setBackground(((Token) o).getBackground().brighter());
        }

    }

    void enemyTurn() {

        startingNeighboursSet();
        addToPlayer(player2);


        if (player2.possibleMovesPlayer.size() > 0){

            // choose best
            int moveIndex = 0;
            int mostTaken = -1;
            Token playedBy2;
            ArrayList<Integer> bestMovesList = new ArrayList<>();
            if(player2.possibleMovesPlayer.size() > 1){
                for (int i = 0; i < player2.possibleMovesWeight.size(); i++){
                    if (player2.possibleMovesWeight.get(i) > mostTaken){
                        bestMovesList.clear();
                        mostTaken = player2.possibleMovesWeight.get(i);
                        moveIndex = i;
                        bestMovesList.add(moveIndex);
                    }
                    else if (player2.possibleMovesWeight.get(i) == mostTaken){
                        bestMovesList.add(i);
                    }
                }
                if (bestMovesList.size() > 1){
                    moveIndex = new Random().nextInt(bestMovesList.size());
                }
                playedBy2 = player2.possibleMovesPlayer.get(moveIndex);
            }
            else{
                playedBy2 = null;
            }
            //

            if(playedBy2 != null){
                playedBy2.putToken = true;
                addToPlayer(player2);
                playedBy2.setColor(Color.WHITE);
                player2.checkTakingMoves(playedBy2);
                playedBy2.repaint();
            }
        }

        startingNeighboursSet();
        addToPlayer(player1);

        settings.getBoard().repaint();

        if (player1.possibleMovesPlayer.isEmpty() && player2.possibleMovesPlayer.size()-1 > 0){
            enemyTurn();
        }

        checkEndBoard();

    }

    void startingNeighboursSet(){

        player1.setScore(0);
        player2.setScore(0);
        for (int i = 0; i < settings.boardSize; i++){
            for (int j = 0; j < settings.boardSize; j++){
                settings.boardList.get(i).get(j).neighbours.clear();
                settings.boardList.get(i).get(j).isPlayable = false;
                if (j - 1 >= 0){        //N
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i).get(j-1));
                }
                else{
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }
                if (i + 1 <= settings.boardSize-1  && j - 1 >= 0){     //NW
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i+1).get(j-1));
                }
                else {
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }
                if (i + 1 <= settings.boardSize-1){        //W
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i+1).get(j));
                }
                else{
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }
                if (i + 1 <= settings.boardSize-1 && j + 1 <= settings.boardSize-1){      //SW
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i+1).get(j+1));
                }
                else{
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }
                if (j + 1 <= settings.boardSize-1){        //S
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i).get(j+1));
                }
                else{
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }
                if (i - 1 >= 0 && j + 1 <= settings.boardSize-1){      //SE
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i-1).get(j+1));
                }
                else{
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }
                if (i - 1 >= 0){        //E
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i-1).get(j));
                }
                else{
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }
                if (i - 1 >= 0 && j -1 >= 0){      //NE
                    settings.boardList.get(i).get(j).neighbours.add(settings.boardList.get(i-1).get(j-1));
                }
                else{
                    settings.boardList.get(i).get(j).neighbours.add(null);
                }

                if (settings.boardList.get(i).get(j).getColor() == Color.BLACK){
                    player1.scoreAdd();
                }

                if (settings.boardList.get(i).get(j).getColor() == Color.WHITE){
                    player2.scoreAdd();
                }

            }
        }
    }

    void addToPlayer(Player player){
        player.myToken.clear();
        for (int i = 0; i < settings.boardSize; i++){
            for (int j = 0; j < settings.boardSize; j++){
                if (settings.boardList.get(i).get(j).getColor() == player.getMyColor()){
                    player.myToken.add(settings.boardList.get(i).get(j));
                }
            }
        }
        player.checkPossibleMoves();
    }
    
    public void setSettings(Settings settings){
        this.settings = settings;
    }

    void checkEndBoard(){
        int playableTiles = 0;
        for (int i = 0; i < settings.boardSize; i++){
            for (int j = 0; j < settings.boardSize; j++){
                if (settings.boardList.get(i).get(j).getColor() == null && settings.boardList.get(i).get(j).isPlayable){
                    playableTiles++;
                }
            }
        }
        if (playableTiles == 0){
            gameEnd();
        }
    }


    void gameEnd(){
        if (player1.getScore() > player2.getScore()){
            settings.getWinner().setText("Vyhral hrac 1");
        }
        else if (player1.getScore() < player2.getScore()){
            settings.getWinner().setText("Vyhral hrac 2");
        }
        else{
            settings.getWinner().setText("Remiza");
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'r'){
            settings.resetGame();
        }
        if (e.getKeyChar() == 27){
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object c = e.getSource();
        if (c == settings.getReset()){
            settings.resetGame();
        }

    }
}
