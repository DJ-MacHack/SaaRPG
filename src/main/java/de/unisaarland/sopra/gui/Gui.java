package de.unisaarland.sopra.gui;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.messages.ActNow;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.RoundBegin;
import de.unisaarland.sopra.messages.WarCry;
import de.unisaarland.sopra.model.Pc;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Team 14 on 13.09.2016.
 */
public class Gui extends Client {

    private GuiFrame frame;
    private boolean wraithCat, wraithDoctor, fairyMavis, ifritNatsu, rookBird, koboldChristmas, yetiAg, boarPig;
    private List<Event> eventsList;
    private Pc currentPlayer;
    private static final boolean PLAYMIDI = true;

    /**
     * create a new client
     *
     * @param ip      IP or host
     * @param port    port number
     * @param timeout timeout in ms
     */
    public Gui(String ip, int port, int timeout) {
        super(ip, port, timeout);

        super.clientConnection.sendWatch();

        eventsList = new ArrayList<>();
        Random r = new Random();
        wraithCat = (r.nextDouble() <= 0.05);
        wraithDoctor = false;
        fairyMavis = false;
        ifritNatsu = false;
        rookBird = false;
        koboldChristmas = false;
        yetiAg = false;
        boarPig = false;

        frame = new GuiFrame(this);
    }

    @Override
    public void actNow() {
        return;
    }

    public void run() {
        while (isRunning()) {
            Event event = this.clientConnection.nextEvent();

            eventsList.add(event);
            event.executeEvent(game, this);
            if (event instanceof WarCry) {
                WarCry cry = (WarCry) event;
                frame.addWarcy(cry.getMonsterId(), 20, cry.getCry());
                wraithCat = (wraithCat || catchCryingCat(cry));
                wraithDoctor = (wraithDoctor || catchWraithDoctor(cry));
                koboldChristmas = (koboldChristmas || catchKoboldChristmas(cry));
                ifritNatsu = (ifritNatsu || catchIfritNatsu(cry));
                fairyMavis = (fairyMavis || catchFairyMavis(cry));
                rookBird = (rookBird || catchRookBird(cry));
                yetiAg = (yetiAg || catchYetiAg(cry));
                boarPig = (boarPig || catchBoarPig(cry));
            }
            if (event instanceof ActNow){
                this.currentPlayer = this.game.getMonsterById(((ActNow) event).getMonsterId());
            }
            frame.repaint();

        }
        clientConnection.close();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getTeamName() {
        return null;
    }

    private boolean catchCryingCat(WarCry event) {
        String cry = event.getCry().toLowerCase();
        String name = "wraith_cat";
        boolean result = cry.split("hello kitty").length > 1;
        if (PLAYMIDI && result) {
            try {
                Music.run(name);
            } catch (InvalidMidiDataException | IOException | MidiUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean catchWraithDoctor(WarCry event) {
        String cry = event.getCry().toLowerCase();
        int res = cry.split("allonsy").length;
        int res1 = cry.split("allons-y").length;
        boolean result = res > 1 || res1 > 1;
        String name = "wraith_doctor";
        if (PLAYMIDI && result) {
            try {
                Music.run(name);
            } catch (InvalidMidiDataException | IOException | MidiUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean catchRookBird(WarCry event) {
        String cry = event.getCry().toLowerCase();
        int res = cry.split("flying is a dream").length;
        boolean result = res > 1;
        String name = "rook_bird";
        if (PLAYMIDI && result) {
            try {
                Music.run(name);
            } catch (InvalidMidiDataException | IOException | MidiUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean catchKoboldChristmas(WarCry event) {
        String cry = event.getCry().toLowerCase();
        int merry = (cry.split("merry christmas").length);
        int hohoho = (cry.split("hohoho").length);
        boolean result = (merry > 1 || hohoho > 1);
        String name = "kobold_christmas";
        if (PLAYMIDI && result) {
            try {
                Music.run(name);
            } catch (InvalidMidiDataException | IOException | MidiUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean catchIfritNatsu(WarCry event) {
        String cry = event.getCry().toLowerCase();
        int res = cry.split("we cannot live if we do not keep running forward").length;
        boolean result = res > 1;
        String name = "ifrit_natsu";
        if (PLAYMIDI && result) {
            try {
                Music.run(name);
            } catch (InvalidMidiDataException | IOException | MidiUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean catchFairyMavis(WarCry event) {
        String cry = event.getCry().toLowerCase();
        int cries = cry.split("unwavering faith and resilient bonds will bring even miracles to your side").length;
        boolean result = cries > 1;
        String name = "fairy_tail";
        if (PLAYMIDI && result) {
            try {
                Music.run(name);
            } catch (InvalidMidiDataException | IOException | MidiUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean catchYetiAg(WarCry event) {
        String cry = event.getCry().toLowerCase();
        return cry.split("schwaebisch").length > 1;
    }

    private boolean catchBoarPig(WarCry event) {
        String cry = event.getCry().toLowerCase();
        return cry.split("schweinisch").length > 1;
    }


    public boolean getWraithCat() {
        return wraithCat;
    }

    public boolean isWraithDoctor() {
        return wraithDoctor;
    }

    public boolean isFairyMavis() {
        return fairyMavis;
    }

    public boolean isRookBird() {
        return rookBird;
    }

    public boolean isIfritNatsu() {
        return ifritNatsu;
    }

    public boolean isKoboldChristmas() {
        return koboldChristmas;
    }

    public boolean isYetiAg() {
        return yetiAg;
    }

    public boolean isBoarPig() {
        return boarPig;
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public Pc getCurrentPlayer() {
        return this.currentPlayer;
    }
}
