package de.unisaarland.sopra.gui;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Music{
    public static void run(String name) throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {
        Thread thread = new Thread(() -> {
            try{
                Sequence myseq = MidiSystem.getSequence(Music.class.getResource("/music/" + name + ".mid"));
                final Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.setSequence(myseq);
                sequencer.setLoopCount(1);
                sequencer.start();
                TimeUnit.SECONDS.sleep(15);
                sequencer.stop();
            }catch(InvalidMidiDataException | IOException | MidiUnavailableException | InterruptedException e){
                e.printStackTrace();
            }
        });

        thread.start();
    }
}