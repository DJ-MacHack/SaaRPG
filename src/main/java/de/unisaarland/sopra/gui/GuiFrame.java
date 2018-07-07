package de.unisaarland.sopra.gui;

import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.Map;

/**
 * Created by Team 14 on 13.09.2016.
 */
public class GuiFrame extends JFrame {

    public final static int SIZE = 32;
    private final static int INFOBOX = 110;

    //PIXEL-PEEPER'S CONSTANTS
    private final static int INFOBOX_CONTENT_X = 182;
    private final static int INFOBOX_BARGRAPH_HEIGHT = 22;
    private final static int INFOBOX_BARGRAPH_Y_HP = 43;
    private final static int INFOBOX_BARGRAPH_Y_EP = 68;
    private final static int INFOBOX_BARGRAPH_Y_TEXT_HP = 59;
    private final static int INFOBOX_BARGRAPH_Y_TEXT_EP = 84;
    private final static int INFOBOX_BARGRAPH_WIDTH = 150;
    private final static int INFOBOX_BARGRAPH_X_TEXT = 175;
    private final static int INFOBOX_TYPETEXT_Y = 38;
    private final static int PROFILE_DIMENSIONS = 100;
    private final static int PROFILE_HEIGHT = 80; //Grows up

    private final static GameVector VIEWPOSITION_OFFSET = new GameVector(0, -6);
    private final static GameVector STARTPOSITION_OFFSET = new GameVector(-1, -1);

    private int width;
    private int vertOff;
    private GameVector viewPosition;
    private GameVector selectedField;
    private Gui gui;
    private InputListener inputListener;
    private BufferedImage tile;
    private BufferedImage tileOutline;
    private BufferedImage speechBubble;
    private BufferedImage box;
    private HashMap<Field, Image> fieldImageMap;
    private HashMap<String, BufferedImage> creatureImageMap;
    private HashMap<String, BufferedImage> creatureProfileMap;

    private long lastTime;
    private HashMap<Integer, String> warcryMonsterMap;
    private HashMap<Integer, Long> warcryTimeMap;

    public GuiFrame(Gui gui) {
        super("SaaRPG | GUI");
        this.gui = gui;
        viewPosition = (new GameVector(0, 0)).add(STARTPOSITION_OFFSET);
        selectedField = new GameVector(0, 0);
        this.fieldImageMap = new HashMap<>();
        this.creatureImageMap = new HashMap<>();
        this.creatureProfileMap = new HashMap<>();
        this.setBackground(GuiHelper.BACKGROUND_COLOR);
        this.warcryMonsterMap = new HashMap<>();
        this.warcryTimeMap = new HashMap<>();

        inputListener = new InputListener(this);
        lastTime = System.currentTimeMillis();

        JPanel gamePanel = new GamePanel();
        this.setContentPane(gamePanel);
        gamePanel.setPreferredSize(new Dimension(800, 600));
        this.addKeyListener(inputListener);
        this.addMouseListener(inputListener);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        Load();
    }

    public void addWarcy(int monsterId, long time, String warCry){
        warcryMonsterMap.put(monsterId, warCry);
        warcryTimeMap.put(monsterId, time * 1000);
    }

    private void Load() {
        int height = SIZE * 2;
        width = (int) (Math.sqrt(3) / 2 * height);
        vertOff = height * 3 / 4;

        try {
            tile = ImageIO.read(Image.class.getResourceAsStream("/tile.png"));
            tileOutline = ImageIO.read(Image.class.getResourceAsStream("/tile_outline.png"));
            speechBubble = ImageIO.read(Image.class.getResourceAsStream("/speechbubble.png"));
            box = ImageIO.read(Image.class.getResourceAsStream("/box.png"));

            ArrayList<String> names = new ArrayList<>();

            for (CreatureType creatureType : CreatureType.values()) {
                String name = creatureType.name().toLowerCase();
                if (creatureType == CreatureType.WRAITH) {
                    name = "wraith_default";
                }
                names.add(name);
            }

            names.add("wraith_cat");
            names.add("wraith_doctor");
            names.add("ifrit_natsu");
            names.add("fairy_tail");
            names.add("kobold_christmas");
            names.add("rook_bird");
            names.add("yeti_ag");
            names.add("boar_pig");

            for (String name : names) {
                BufferedImage image = ImageIO.read(Image.class.getResourceAsStream("/creatures/" + name + ".png"));
                BufferedImage profile = ImageIO.read(Image.class.getResourceAsStream("/profile/" + name + ".jpg"));
                creatureImageMap.put(name, image);
                creatureProfileMap.put(name, profile);
            }
            for (Field f : Field.values()) {
                BufferedImage image = ImageIO.read(Image.class.getResourceAsStream("/tiles/" + f.toString().toLowerCase() + ".png"));
                Color fieldColor = GuiHelper.getColorForField(f);
                image = GuiHelper.getColoredImage(image, fieldColor);
                fieldImageMap.put(f, image);
            }
        } catch (IOException e) {
            e.printStackTrace();
            gui.setRunning(false);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private class GamePanel extends JPanel {

        public void paintComponent(Graphics g) {
            synchronized (gui.getGame()) {
                int xMaxTiles = getWidth() / SIZE;
                int yMaxTiles = getHeight() / SIZE;

                for (int y = 0; y < yMaxTiles; y++) {
                    for (int x = 0; x < xMaxTiles; x++) {
                        int xa = x + viewPosition.getX();
                        int ya = y + viewPosition.getY();

                        GameVector vector = new GameVector(xa - (ya / 2), ya);

                        int xx = x * width;
                        int yy = y * vertOff;

                        if (ya % 2 != 0) {
                            xx += width / 2;
                        }

                        Field field = gui.getGame().getFieldAt(vector);

                        if (field != null) {
                            Image fieldImage;
                            if (fieldImageMap.containsKey(field)) {
                                fieldImage = fieldImageMap.get(field);
                            } else { //This case is impossible to happen
                                Color fieldColor = GuiHelper.getColorForField(field);
                                fieldImage = GuiHelper.getColoredImage(tile, fieldColor);
                                fieldImageMap.put(field, fieldImage);
                            }

                            g.drawImage(fieldImage, xx - (tile.getWidth() / 2), yy - (tile.getHeight() / 2), null);

                            if (vector.equals(selectedField)) {
                                g.drawImage(tileOutline, xx - (tileOutline.getWidth() / 2), yy - (tileOutline.getHeight() / 2), null);
                            }
                        }

                        Creature creature = gui.getGame().getCreatureByPosition(vector);

                        if (creature != null) {
                            String name = creature.getCreatureType().name().toLowerCase();
                            if (creature.getCreatureType() == CreatureType.WRAITH) {
                                if (gui.getWraithCat()) {
                                    name = "wraith_cat";
                                } else if (gui.isWraithDoctor()) {
                                    name = "wraith_doctor";
                                } else {
                                    name = "wraith_default";
                                }
                            }
                            if (creature.getCreatureType() == CreatureType.IFRIT) {
                                if (gui.isIfritNatsu()) {
                                    name = "ifrit_natsu";
                                } else {
                                    name = "ifrit";
                                }
                            }
                            if (creature.getCreatureType() == CreatureType.FAIRY) {
                                if (gui.isFairyMavis()) {
                                    name = "fairy_tail";
                                } else {
                                    name = "fairy";
                                }
                            }
                            if (creature.getCreatureType() == CreatureType.KOBOLD) {
                                if (gui.isKoboldChristmas()) {
                                    name = "kobold_christmas";
                                } else {
                                    name = "kobold";
                                }
                            }
                            if (creature.getCreatureType() == CreatureType.ROOK) {
                                if (gui.isRookBird()) {
                                    name = "rook_bird";
                                } else {
                                    name = "rook";
                                }
                            }
                            if (creature.getCreatureType() == CreatureType.YETI) {
                                if (gui.isYetiAg()) {
                                    name = "yeti_ag";
                                } else {
                                    name = "yeti";
                                }
                            }
                            if (creature.getCreatureType() == CreatureType.BOAR) {
                                if (gui.isBoarPig()) {
                                    name = "boar_pig";
                                } else {
                                    name = "boar";
                                }
                            }

                            BufferedImage creatureImg = creatureImageMap.get(name);
                            g.drawImage(creatureImg, xx - (creatureImg.getWidth() / 2), yy - creatureImg.getHeight() + 16, null);

                            int id = creature.getId();
                            if(warcryMonsterMap.containsKey(id)){
                                int sX = xx - (creatureImg.getWidth() / 2);
                                int sY = yy - creatureImg.getHeight() - 4;
                                String cry = warcryMonsterMap.get(id);
                                boolean mirror = false;
                                int length = g.getFontMetrics().stringWidth(cry);
                                if(sX + length >= getWidth()){
                                    mirror = true;
                                }

                                System.out.println("draw speechbubble");
                                GuiHelper.drawSpeechBubble(g, sX, sY, cry, mirror, speechBubble);
                            }
                        }
                    }
                }

                //g.setColor(Color.BLACK);
                //g.fillRect(0, getHeight() - INFOBOX, 150, INFOBOX);
                //g.setColor(Color.WHITE);
                //g.fillRect(0, getHeight() - INFOBOX + 2, 148, INFOBOX - 2);

                GuiHelper.drawBox(g, 0, getHeight() - INFOBOX, 150, INFOBOX, box);

                g.setColor(GuiHelper.FONT_COLOR);
                g.drawString("X: " + selectedField.getX(), 16, getHeight() - INFOBOX + 24);
                g.drawString("Y: " + selectedField.getY(), 16, getHeight() - INFOBOX + 40);

                Field f = gui.getGame().getFieldAt(selectedField);
                if (f != null) {
                    g.drawString("Field: " + f.name(), 16, getHeight() - INFOBOX + 60);
                }

                Creature creature = gui.getGame().getCreatureByPosition(selectedField);
                if (creature != null) {
                    //g.setColor(Color.BLACK);
                    //g.fillRect(getWidth() - 200, getHeight() - INFOBOX, 200, INFOBOX);
                    //g.setColor(Color.WHITE);
                    //g.fillRect(getWidth() - 198, getHeight() - INFOBOX + 2, 198, INFOBOX - 2);
                    GuiHelper.drawBox(g, getWidth() - 200, getHeight() - INFOBOX, 200, INFOBOX, box);

                    g.setColor(Color.BLACK);
                    g.fillRect(getWidth() - (PROFILE_DIMENSIONS + 4), getHeight() - INFOBOX - (PROFILE_HEIGHT + 2), (PROFILE_DIMENSIONS + 4), (PROFILE_DIMENSIONS + 4));

                    String name = creature.getCreatureType().name().toLowerCase();
                    if (creature.getCreatureType() == CreatureType.WRAITH) {
                        if (gui.getWraithCat()) {
                            name = "wraith_cat";
                        } else if (gui.isWraithDoctor()) {
                            name = "wraith_doctor";
                        } else {
                            name = "wraith_default";
                        }

                    }
                    if (creature.getCreatureType() == CreatureType.IFRIT) {
                        if (gui.isIfritNatsu()) {
                            name = "ifrit_natsu";
                        } else {
                            name = "ifrit";
                        }
                    }
                    if (creature.getCreatureType() == CreatureType.FAIRY) {
                        if (gui.isFairyMavis()) {
                            name = "fairy_tail";
                        } else {
                            name = "fairy";
                        }
                    }
                    if (creature.getCreatureType() == CreatureType.KOBOLD) {
                        if (gui.isKoboldChristmas()) {
                            name = "kobold_christmas";
                        } else {
                            name = "kobold";
                        }
                    }
                    if (creature.getCreatureType() == CreatureType.ROOK) {
                        if (gui.isRookBird()) {
                            name = "rook_bird";
                        } else {
                            name = "rook";
                        }
                    }
                    if (creature.getCreatureType() == CreatureType.YETI) {
                        if (gui.isYetiAg()) {
                            name = "yeti_ag";
                        } else {
                            name = "yeti";
                        }
                    }
                    if (creature.getCreatureType() == CreatureType.BOAR) {
                        if (gui.isBoarPig()) {
                            name = "boar_pig";
                        } else {
                            name = "boar";
                        }
                    }
                    g.drawImage(creatureProfileMap.get(name), getWidth() - (PROFILE_DIMENSIONS + 2), getHeight() - INFOBOX - PROFILE_HEIGHT, PROFILE_DIMENSIONS, PROFILE_DIMENSIONS, null);

                    g.setColor(GuiHelper.FONT_COLOR);
                    if (creature instanceof Pc) {
                        Player p = gui.getGame().getPlayerByMonsterId(creature.getId());
                        String playerName = "Unknown";
                        if (p != null) {
                            playerName = p.getName();
                        }
                        g.drawString(playerName, getWidth() - INFOBOX_CONTENT_X, getHeight() - INFOBOX + INFOBOX_TYPETEXT_Y - 16);
                    }

                    g.setColor(GuiHelper.FONT_COLOR);
                    String creatureName = creature.getCreatureType().name().charAt(0) + creature.getCreatureType().name().substring(1).toLowerCase();
                    if (creature.getCreatureType() == CreatureType.WRAITH) {
                        if (gui.getWraithCat()) {
                            creatureName = "Wraith Cat";
                        } else if (gui.isWraithDoctor()) {
                            creatureName = "The Doctor";
                        } else {
                            creatureName = "Wraith";
                        }
                    }
                    if (creature.getCreatureType() == CreatureType.FAIRY) {
                        if (gui.isFairyMavis())
                            creatureName = "Mavis Vermillion";
                    }
                    if (creature.getCreatureType() == CreatureType.IFRIT) {
                        if (gui.isIfritNatsu())
                            creatureName = "Natsu Dragneel";
                    }
                    if (creature.getCreatureType() == CreatureType.KOBOLD) {
                        if (gui.isKoboldChristmas())
                            creatureName = "Buddy";
                    }
                    if (creature.getCreatureType() == CreatureType.ROOK) {
                        if (gui.isRookBird())
                            creatureName = "Rook";
                    }
                    if (creature.getCreatureType() == CreatureType.YETI) {
                        if (gui.isYetiAg())
                            creatureName = "Yeti wie auch immer";
                    }
                    if (creature.getCreatureType() == CreatureType.BOAR) {
                        if (gui.isBoarPig())
                            creatureName = "Pig";
                    }
                    g.drawString(creatureName, getWidth() - INFOBOX_CONTENT_X, getHeight() - INFOBOX + INFOBOX_TYPETEXT_Y);

                    g.setColor(Color.BLACK);
                    g.fillRect(getWidth() - INFOBOX_CONTENT_X, getHeight() - INFOBOX + INFOBOX_BARGRAPH_Y_HP, INFOBOX_BARGRAPH_WIDTH, INFOBOX_BARGRAPH_HEIGHT);
                    g.setColor(Color.RED);
                    double hpW = (creature.getHp() / (double) creature.getMaxHp()) * (INFOBOX_BARGRAPH_WIDTH - 2);
                    g.fillRect(getWidth() - (INFOBOX_CONTENT_X - 1), getHeight() - INFOBOX + INFOBOX_BARGRAPH_Y_HP + 1, (int) hpW, INFOBOX_BARGRAPH_HEIGHT - 2);
                    g.setColor(Color.WHITE);
                    g.drawString("HP: " + creature.getHp() + "/" + creature.getMaxHp(), getWidth() - INFOBOX_BARGRAPH_X_TEXT, getHeight() - INFOBOX + INFOBOX_BARGRAPH_Y_TEXT_HP);

                    if (creature instanceof Pc) {
                        g.setColor(Color.BLACK);
                        g.fillRect(getWidth() - INFOBOX_CONTENT_X, getHeight() - INFOBOX + INFOBOX_BARGRAPH_Y_EP, INFOBOX_BARGRAPH_WIDTH, INFOBOX_BARGRAPH_HEIGHT);
                        g.setColor(Color.GREEN);
                        Pc pc = (Pc) creature;
                        double enW = (pc.getEnergy() / 1000.0) * (INFOBOX_BARGRAPH_WIDTH - 2);
                        g.fillRect(getWidth() - (INFOBOX_CONTENT_X - 1), getHeight() - INFOBOX + INFOBOX_BARGRAPH_Y_EP + 1, (int) enW, INFOBOX_BARGRAPH_HEIGHT - 2);
                        g.setColor(Color.WHITE);
                        g.drawString("EP: " + pc.getEnergy() + "/1000", getWidth() - INFOBOX_BARGRAPH_X_TEXT, getHeight() - INFOBOX + INFOBOX_BARGRAPH_Y_TEXT_EP);
                    }
                }

                // GuiHelper.drawSpeechBubble(g, 170, 80, "Wuhuuuuu, ich bin ein Geist!", false, speechBubble);
                long time = System.currentTimeMillis();
                decreaseWarCryTime(time - lastTime);
                lastTime = time;
            }
        }
    }

    private void decreaseWarCryTime(long timePast){
        Set<Map.Entry<Integer, Long>> clone = new HashSet<>();
        clone.addAll(warcryTimeMap.entrySet());

        for(Map.Entry<Integer, Long> e : clone){
            long t = e.getValue() - timePast;

            System.out.println(warcryMonsterMap.get(e.getKey()));
            System.out.println(e.getValue());

            if(t <= 0){
                warcryTimeMap.remove(e.getKey());
                warcryMonsterMap.remove(e.getKey());
            }else{
                warcryTimeMap.put(e.getKey(), t);
            }
        }
    }

    public GameVector getViewPosition() {
        return this.viewPosition;
    }

    public void setViewPosition(GameVector pos) {
        this.viewPosition = pos;
    }

    public void setSelectedField(GameVector pos) {
        this.selectedField = pos;
    }

    public void setSelectedFieldToCurrentPlayer() {
        Pc curr = this.gui.getCurrentPlayer();
        if (curr != null) {
            this.selectedField = curr.getPosition();
        }
    }

    public void setViewPositionToCurrentPlayer() {
        Pc curr = this.gui.getCurrentPlayer();
        if (curr != null) {
            this.viewPosition = curr.getPosition().add(VIEWPOSITION_OFFSET);
        }
    }
}
