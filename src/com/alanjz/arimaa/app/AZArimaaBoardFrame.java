package com.alanjz.arimaa.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AZArimaaBoardFrame {
    public static final int MAX_SIZE = AZArimaaBoard.MAX_SQUARE_SIZE*8;
    public static final int MIN_SIZE = AZArimaaBoard.MIN_SQUARE_SIZE*8;

    private final JFrame frame;
    private final String boardName;
    private final AZArimaaBoard arimaaBoard;
    private final int titleBarHeight;

    AZArimaaBoardFrame(String boardName) {
        this.boardName = boardName;
        arimaaBoard = new AZArimaaBoard();
        frame = new JFrame("Arimaa Zoo\t[" + getBoardName() + "]");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(MIN_SIZE, MIN_SIZE));
        frame.setMaximumSize(new Dimension(MAX_SIZE, MAX_SIZE));
        frame.getContentPane().add(getArimaaBoard());
        frame.pack();
        frame.setVisible(true);
        titleBarHeight = frame.getHeight() - frame.getContentPane().getHeight();

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int minDim = Math.min(frame.getWidth(), frame.getHeight());
                frame.setSize(minDim, minDim);
            }
        });
    }

    public String getBoardName() {
        return boardName;
    }

    public AZArimaaBoard getArimaaBoard() {
        return arimaaBoard;
    }
}
