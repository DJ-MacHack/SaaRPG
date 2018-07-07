package de.unisaarland.sopra.gui;

import de.unisaarland.sopra.model.Field;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Team 14 on 25.09.2016.
 */
public class GuiHelper {

    public static final Color FONT_COLOR = new Color(0, 225, 52);
    public static final Color BACKGROUND_COLOR = new Color(25, 0, 55);

    private static final Color FIELD_GRASS_COLOR = new Color(161, 231, 35);
    private static final Color FIELD_TREE_COLOR = new Color(60, 135, 0);
    private static final Color FIELD_SWAMP_COLOR = new Color(82, 130, 79);
    private static final Color FIELD_WATER_COLOR = new Color(0, 0, 255);
    private static final Color FIELD_ICE_COLOR = new Color(0, 255, 255);
    private static final Color FIELD_LAVA_COLOR = new Color(255, 90, 21);
    private static final Color FIELD_CURSE_COLOR = new Color(108, 44, 151);
    private static final Color FIELD_HILL_COLOR = new Color(124, 181, 115);
    private static final Color FIELD_BUSH_COLOR = new Color(84, 186, 0);
    private static final Color FIELD_ROCK_COLOR = new Color(16, 16, 16);
    private static final Color FIELD_HEAL_COLOR = new Color(216, 255, 0);
    private static final Color FIELD_HEAL_DISABLED_COLOR = new Color(185, 185, 0);

    private GuiHelper() {

    }

    public static void drawSpeechBubble(Graphics g, int x, int y, String msg, boolean mirrored, BufferedImage speechBubble) {
        int length = g.getFontMetrics().stringWidth(msg);
        int m = mirrored ? 1 : -1;
        int xx = x + 14 * m;
        g.drawImage(speechBubble, x, y, xx, y + 31, 17, 0, 31, 31, null);
        xx = x - length * m;
        g.drawImage(speechBubble, x, y, xx, y + 31, 11, 0, 16, 31, null);
        int xx2 = xx - 11 * m;
        xx = xx2 + 11 * m;
        g.drawImage(speechBubble, xx2, y, xx, y + 31, 0, 0, 10, 31, null);

        xx = x - (mirrored ? length : 0);
        g.drawString(msg, xx, y + 16);
    }

    public static void drawBox(Graphics g, int x, int y, int width, int height, BufferedImage box) {
        int x2 = x + width;
        int y2 = y + height;

        g.drawImage(box, x - 1, y - 1, x + 10, y + 10, 0, 0, 10, 10, null);
        g.drawImage(box, x2 - 11, y - 1, x2, y + 10, 53, 0, 64, 10, null);
        g.drawImage(box, x - 1, y2 - 11, x + 10, y2, 0, 53, 10, 64, null);
        g.drawImage(box, x2 - 11, y2 - 11, x2, y2, 53, 53, 64, 64, null);

        g.drawImage(box, x + 10, y - 1, x2 - 11, y + 11, 11, 0, 12, 10, null);
        g.drawImage(box, x - 1, y + 10, x + 11, y2 - 11, 0, 11, 10, 12, null);
        g.drawImage(box, x2 - 11, y+10, x2, y2 - 11, 53, 11, 64, 12, null);
        g.drawImage(box, x + 10, y2-11, x2 - 11, y2, 11, 53, 12, 64, null);

        g.drawImage(box, x + 10, y+10, x2 - 11, y2-11, 11, 11, 12, 12, null);
    }

    public static BufferedImage getColoredImage(BufferedImage image, Color color) {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.drawImage(image, null, 0, 0);
        Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 175);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(c);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
        return img;
    }

    public static Color getColorForField(Field field) {
        if (field == null)
            return Color.BLACK;

        switch (field) {
            case BUSH:
                return FIELD_BUSH_COLOR;
            case SWAMP:
                return FIELD_SWAMP_COLOR;
            case GRASS:
                return FIELD_GRASS_COLOR;
            case WATER:
                return FIELD_WATER_COLOR;
            case ICE:
                return FIELD_ICE_COLOR;
            case LAVA:
                return FIELD_LAVA_COLOR;
            case CURSE:
                return FIELD_CURSE_COLOR;
            case HILL:
                return FIELD_HILL_COLOR;
            case TREE:
                return FIELD_TREE_COLOR;
            case ROCK:
                return FIELD_ROCK_COLOR;
            case HEAL:
                return FIELD_HEAL_COLOR;
            case HEAL_DISABLED:
                return FIELD_HEAL_DISABLED_COLOR;
            default:
                return Color.BLACK;
        }
    }
}
