package de.unisaarland.sopra.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by DJ MacHack on 28.09.2016.
 */
public class Cry {

    private Random random;
    private List<String> cries = new ArrayList<String>(Arrays.asList("You're late.", "You look terrible.", "A red sun rises, blood has been spilled this night.", "You would die before your stroke fell.", "They may yet be alive.", "This is no ordinary Ranger. He is Aragorn, son of Arathorn. You owe him your allegiance.", "Have you heard nothing Lord Elrond has said? The Ring must be destroyed.", "And you have my bow.", "There is a fell voice on the air.", "We must move on. We cannot linger.", "A lament for Gandalf.",
            "I have not the heart to tell you. For me the grief is still to near.",
            "It is not the eastern shore that worries me. A shadow and a threat has been growing in my mind. Something draws near. I can feel it.",
            "You mean not to follow them.", "Vamos a la mensa?", "#networkisthebettername",
            "Alas! Alas!", "You were tracking a company of thirteen dwarves. Why?",
            " The stars are veiled. Something stirs in the East. A sleepless malice. The eye of the enemy is moving. He is HERE.",
            "Come on Gimli!", "These bats are bred for one purpose... for war!", "Avoid floating point numbers.",
            "Why doesn't that surprise me?", "The horses are restless, and the men are quiet.",
            "Lembas!", " The way is shut. It was made by those who are dead, and the dead keep it. The way is shut.",
            "Do not think I won't kill you, dwarf!", "Wat?", "Defensive copies are savior.",
            "And who is this horrid creature? A goblin mutant?", "Never let exception come out of finally block.",
            "There is no King Under the Mountain, nor will there ever be!", "Never throw “Exception”.",
            "They may yet be alive.", "My mother died here. There is no grave nor memory. My father never speaks of her.",
            "Lothlorien! Lothlorien! We have come to the eaves of the Golden Wood. Alas that it is winter!",
            "A plague upon the stiff necks of dwarves.", "Wherever possible try to use primitive types instead of wrapper classes.",
            "Five hundred times have the red leaves fallen in Mirkwood in m home since then, and but a little while does that seem to us.",
            "There are eyes! Eyes looking out from the shadows of the boughs! I never saw such eyes before.",
            "A red sun rises. Blood has been spilled this night.", "Use strings with utmost care.",
            "They're taking the Hobbits to Isengard.", "Try to use standard library instead of writing your own from scratch.",
            "We should leave now.", "What else can we slay? Is that a hobbit over there?",
            "Schwaebisch is cool!", "God help us!", "#networkisthebettername", "Always return empty collections and arrays instead of null.",
            "Hohoho its christmas", "So all you had to do was say friend... and enter.",
            "Something draws near. I can feel it.", "Try to prefer interfaces instead of abstract classes.",
            "We cannot live if we do not keep running forward!", "Always try to limit the scope of local variable.",
            "Flying is a dream for me!", "Never make an instance fields of class public.",
            "Allonsy!", "My heart weeps at the sight of such destruction. Let us ensure no more trees fall to the Orcs.",
            "Hello Kitty is snorring!", "Avoid creating unnecessary objects and always prefer to do lazy Initialization",
            "Unwavering faith and resilient bonds will bring even miracles to your side.",
            "Schweinisch geil!", "It was not close, it was exciting!", "Always try to minimize mutability of a class.",
            "You would die before your stroke fell!", "Ihr jubelt über die Macht der Presse - graut euch nie vor ihrer Tyrannei?"
    ));

    public Cry(Random r) {
        this.random = r;
    }

    public String getCry() {
        if (!this.cries.isEmpty()) {
            int r = random.nextInt(cries.size());
            String res = cries.get(r);
            cries.remove(r);
            return res;
        } else {
            return "Lululu!";
        }
    }
}
