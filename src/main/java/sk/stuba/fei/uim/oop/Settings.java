package sk.stuba.fei.uim.oop;

import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Settings {
    private final JFrame window = new JFrame("Reversi");
    @Getter
    private final JPanel board = new JPanel();
    private final JPanel bottomMenu = new JPanel();
    private final JLabel currentSize = new JLabel();
    private final JPanel resizer = new JPanel();
    private final JPanel menu = new JPanel();
    @Getter
    private final JButton reset = new JButton();
    @Getter
    private final JLabel winner = new JLabel();
    private final Listeners listener = new Listeners();

    public int boardSize = 6;

    ArrayList<ArrayList<Token>> boardList = new ArrayList<>();

    Settings(){

        window.setResizable(false);

        listener.setSettings(this);

        board.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        board.setLayout(new GridLayout(boardSize, boardSize));

        window.addKeyListener(listener);
        window.setFocusable(true);


        String[] sizeList = { "6x6", "8x8", "10x10", "12x12" };
        JComboBox<String> sizes = new JComboBox<>(sizeList);


        sizes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardSize = sizes.getSelectedIndex()*2+6;
                resetGame();
            }
        });

        try {
            window.setIconImage(ImageIO.read(Objects.requireNonNull(Listeners.class.getResourceAsStream("/reversiIcon.png"))));
        } catch (IOException e) {
        }

        sizes.addKeyListener(listener);
        resizer.add(sizes);


        currentSize.setText("Current size of the board is:");
        bottomMenu.add(currentSize);
        bottomMenu.add(resizer);

        menu.add(winner);

        reset.setText("Reset");
        reset.addActionListener(listener);


        menu.add(reset);


        createBoard(boardSize);

        board.setSize(60*boardSize,60*boardSize);
        board.setVisible(true);

        resizer.setVisible(true);

        window.add(menu, BorderLayout.PAGE_START);
        window.add(board);
        window.add(bottomMenu, BorderLayout.PAGE_END);

        window.addKeyListener(listener);

        listener.player1.setMyColor(Color.BLACK);
        listener.player2.setMyColor(Color.WHITE);
        listener.startingNeighboursSet();
        listener.addToPlayer(listener.player1);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);

    }

    void createBoard(int sizeOfBoard){
        boardList.clear();
        for (int i = 0; i < sizeOfBoard; i++){
            ArrayList<Token> boardRow = new ArrayList<>();
            for (int j = 0; j < sizeOfBoard; j++){
                Token tile = new Token();
                tile.setPreferredSize(new Dimension(60,60));

                if (i%2 == 0 && j%2 == 0 || i%2 == 1 && j%2 == 1){
                    tile.setBackground(new Color(17, 119, 11));
                    board.add(tile);
                }
                else{
                    tile.setBackground(new Color(1, 239, 30));
                    board.add(tile);
                }

                tile.setColor(null);

                if (i == (sizeOfBoard/2)-1 && j == (sizeOfBoard/2)-1 || i == sizeOfBoard/2 && j == sizeOfBoard/2){
                    tile.setColor(Color.BLACK);
                    tile.putToken = true;
                }

                if (i == (sizeOfBoard/2) && j == (sizeOfBoard/2)-1 || i == (sizeOfBoard/2)-1 && j == sizeOfBoard/2){
                    tile.setColor(Color.WHITE);
                    tile.putToken = true;
                }

                boardRow.add(tile);

                tile.addMouseListener(listener);

                tile.setVisible(true);
            }
            boardList.add(boardRow);
        }
    }

    void resetGame(){
        winner.setText("");
        window.remove(board);
        board.removeAll();
        board.setSize(60*boardSize, 60*boardSize);
        board.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        board.setLayout(new GridLayout(boardSize,boardSize));
        createBoard(boardSize);
        window.add(board);
        listener.startingNeighboursSet();
        listener.addToPlayer(listener.player1);
        window.revalidate();
        window.pack();
        window.requestFocus();
    }

}
