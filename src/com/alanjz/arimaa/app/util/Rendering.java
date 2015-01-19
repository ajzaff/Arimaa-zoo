package com.alanjz.arimaa.app.util;

import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Team;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Rendering {

    private static BufferedImage goldElephant;
    private static BufferedImage goldCamel;
    private static BufferedImage goldHorse;
    private static BufferedImage goldDog;
    private static BufferedImage goldCat;
    private static BufferedImage goldRabbit;

    private static BufferedImage silverElephant;
    private static BufferedImage silverCamel;
    private static BufferedImage silverHorse;
    private static BufferedImage silverDog;
    private static BufferedImage silverCat;
    private static BufferedImage silverRabbit;

    public static void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private static BufferedImage loadImage(String teamName, String pieceName) {
        BufferedImage image = null;
        String filePath =
                "res/images/" + pieceName + "/" + pieceName + "-" + teamName + "-sm.png";
        try {
            image = ImageIO.read(new File(filePath));
        }
        catch (IOException e) {
            System.err.println("Can't read input file `" + filePath +"'.");
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage getImage(Piece piece, Team team) {
        if(team == Team.GOLD) {
            if(piece == Piece.ELEPHANT) {
                return goldElephant == null? (goldElephant = loadImage("gold", "elephant")) : goldElephant;
            }
            if(piece == Piece.CAMEL) {
                return goldCamel == null? (goldCamel = loadImage("gold", "camel")) : goldCamel;
            }
            if(piece == Piece.HORSE) {
                return goldHorse == null? (goldHorse = loadImage("gold", "horse")) : goldHorse;
            }
            if(piece == Piece.DOG) {
                return goldDog == null? (goldDog = loadImage("gold", "dog")) : goldDog;
            }
            if(piece == Piece.CAT) {
                return goldCat == null? (goldCat = loadImage("gold", "cat")) : goldCat;
            }
            if(piece == Piece.RABBIT) {
                return goldRabbit == null? (goldRabbit = loadImage("gold", "rabbit")) : goldRabbit;
            }
        }
        else {
            if(piece == Piece.ELEPHANT) {
                return silverElephant == null? (silverElephant = loadImage("silver", "elephant")) : silverElephant;
            }
            if(piece == Piece.CAMEL) {
                return silverCamel == null? (silverCamel = loadImage("silver", "camel")) : silverCamel;
            }
            if(piece == Piece.HORSE) {
                return silverHorse == null? (silverHorse = loadImage("silver", "horse")) : silverHorse;
            }
            if(piece == Piece.DOG) {
                return silverDog == null? (silverDog = loadImage("silver", "dog")) : silverDog;
            }
            if(piece == Piece.CAT) {
                return silverCat == null? (silverCat = loadImage("silver", "cat")) : silverCat;
            }
            if(piece == Piece.RABBIT) {
                return silverRabbit == null? (silverRabbit = loadImage("silver", "rabbit")) : silverRabbit;
            }
        }
        return null;
    }
}
