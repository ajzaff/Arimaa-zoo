package com.alanjz.arimaa.app;

import com.alanjz.arimaa.app.util.Rendering;
import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Team;

import javax.swing.*;
import java.awt.*;

public class AZArimaaBoard extends JPanel {
    public static final int MAX_SQUARE_SIZE = 100;
    public static final int MIN_SQUARE_SIZE = 48;

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Rendering.setRenderingHints(g2d);

        int minDim = Math.min(getSize().width, getSize().height);
        int squareSize = minDim / 8;

        g2d.setColor(new Color(210,180,140));
        g2d.fillRect(0, 0, minDim, minDim);

        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, minDim, minDim);
        for(int i=0; i < 8; i++) {
            g2d.drawLine(squareSize*i, 0, squareSize*i, minDim);
            g2d.drawLine(0, squareSize * i, minDim, squareSize * i);
        }

        for(int i=0; i < 3; i++) {
            g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.GOLD), squareSize * i, squareSize * 7, squareSize, squareSize, null);
        }
        g2d.drawImage(Rendering.getImage(Piece.DOG, Team.GOLD), squareSize*3, squareSize * 7, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.DOG, Team.GOLD), squareSize*4, squareSize * 7, squareSize, squareSize, null);
        for(int i=5; i < 8; i++) {
            g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.GOLD), squareSize * i, squareSize * 7, squareSize, squareSize, null);
        }
        g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.GOLD), 0, squareSize * 6, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.HORSE, Team.GOLD), squareSize*1, squareSize * 6, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.CAT, Team.GOLD), squareSize*2, squareSize * 6, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.ELEPHANT, Team.GOLD), squareSize*3, squareSize * 6, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.CAMEL, Team.GOLD), squareSize*4, squareSize * 6, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.CAT, Team.GOLD), squareSize*5, squareSize * 6, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.HORSE, Team.GOLD), squareSize*6, squareSize * 6, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.GOLD), squareSize*7, squareSize * 6, squareSize, squareSize, null);


        for(int i=0; i < 3; i++) {
            g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.SILVER), squareSize * i, 0, squareSize, squareSize, null);
        }
        g2d.drawImage(Rendering.getImage(Piece.DOG, Team.SILVER), squareSize*3, 0, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.DOG, Team.SILVER), squareSize*4, 0, squareSize, squareSize, null);
        for(int i=5; i < 8; i++) {
            g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.SILVER), squareSize * i, 0, squareSize, squareSize, null);
        }
        g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.SILVER), 0, squareSize, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.HORSE, Team.SILVER), squareSize, squareSize, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.CAT, Team.SILVER), squareSize*2, squareSize, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.ELEPHANT, Team.SILVER), squareSize*4, squareSize, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.CAMEL, Team.SILVER), squareSize*3, squareSize, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.CAT, Team.SILVER), squareSize*5, squareSize, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.HORSE, Team.SILVER), squareSize*6, squareSize, squareSize, squareSize, null);
        g2d.drawImage(Rendering.getImage(Piece.RABBIT, Team.SILVER), squareSize*7, squareSize, squareSize, squareSize, null);

        g2d.fillRect(squareSize*2, squareSize*2, squareSize, squareSize);
        g2d.fillRect(squareSize*2, squareSize*5, squareSize, squareSize);
        g2d.fillRect(squareSize*5, squareSize*2, squareSize, squareSize);
        g2d.fillRect(squareSize*5, squareSize*5, squareSize, squareSize);


        g2d.fillRect(squareSize*2, squareSize*2, squareSize, squareSize);
        g2d.fillRect(squareSize*2, squareSize*5, squareSize, squareSize);
        g2d.fillRect(squareSize*5, squareSize*2, squareSize, squareSize);
        g2d.fillRect(squareSize*5, squareSize*5, squareSize, squareSize);
    }
}
