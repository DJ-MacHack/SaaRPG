package de.unisaarland.sopra;

import de.unisaarland.sopra.model.Field;
import de.unisaarland.sopra.model.Map;
import de.unisaarland.sopra.utility.LogString;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Random;

/**
 * Created by Lukas Kirschner on 9/28/16.
 */
public class MapGenerator {

    private final static int[] RND = {
            1000, //Grass
            50, //Hill
            4, //Heal
            20, //Rock
            50, //Swamp
            50, //Bush
            50, //Tree
            30, //Water
            20, //Ice
            10, //Lava
            5, //Curse
            -1 //Heal_Disabled
    };

    private byte[][] myMap;
    private Path filepath;
    private Algorithm algorithm;

    public MapGenerator(int xdim, int ydim, String algorithm, String outFile) throws IllegalArgumentException {
        LogString.printDebug("Generating Map Array...");
        this.myMap = new byte[xdim][ydim];
        this.filepath = Paths.get(outFile);
        this.algorithm = Algorithm.valueOf(algorithm.toUpperCase());
    }

    public void generateMap() throws IllegalArgumentException {
        switch (this.algorithm) {

            case RANDOM:
                this.generateRandomMap((int)(System.currentTimeMillis() % Integer.MAX_VALUE));
                break;
            case PERLINNOISE:
                throw new UnsupportedOperationException();
                //break;
            default:
                throw new IllegalArgumentException("Unimplemented Algorithm");
        }
    }



    private void generateRandomMap(int seed) {
        LogString.printDebug(String.format("Generating Random Map. Size: %dx%d Seed: %d", myMap.length, myMap[0].length, seed));
        Random r = new Random(seed);
        int[] sumarr = new int[this.RND.length];
        for (int i = 0; i < RND.length; i++){
            int j = 0;
            for (int k = 0; k < i; k++){
                j += RND[k];
            }
            sumarr[i] = j;
        }
        for (int i = 0; i < this.myMap.length; i++){ //LOL maybe this works
            for (int j = 0; j < this.myMap[0].length; j++){
                int rr = Math.abs(r.nextInt() % sumarr[sumarr.length - 1]);
                for (int z = (RND.length - 1); z >= 0; z--){
                    if (sumarr[z] <= rr){
                        this.myMap[i][j] = Map.fieldToChar(Field.values()[z]);
                        //LogString.printDebug("Wrote " + Field.values()[z].toString());
                        break;
                    }
                }
            }
        }
        LogString.printDebug("Created Random Map.");
    }

    public void generatePlayerSpawns(int count){
        if (count > 1){
            for (int i = 0; i < 5; i++){ //Generate Team1 and Team2 Spawns
                this.myMap[i][0] = '1';
                this.myMap[i][1] = '1';
                this.myMap[this.myMap.length - 1][this.myMap[0].length - (i+1)] = '0';
                this.myMap[this.myMap.length - 2][this.myMap[0].length - (i+1)] = '0';
            }
        }
    }

    public void saveMap() throws IOException {
        this.writeToFile();
    }

    private void writeToFile() throws IOException {
        LogString.printDebug("Writing output map to " + this.filepath.toString());
        char[] outFile = new char[((myMap.length + 1) * myMap[0].length) - 1]; //Space for every character in map + \n s
        int i = 0;
        int width = myMap.length;
        int height = myMap[0].length;
        for (int a = 0; a < height; a++) {
            for (int b = 0; b < width; b++) {
                outFile[(b * (width + 1)) + a] = (char)myMap[a][b];
            }
            if (a != (height - 1)) {
                outFile[(a + 1) * (width + 1) - 1] = '\n';
            }
        }
        /*Writer outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.filepath.toString()),"UTF-8"));
        try {
            outWriter.write(outFile);
        } finally {
            outWriter.close();
        } */ //UNCOMMENT THIS - FILE OUTPUT IS FORBIDDEN IN SOPRA
        LogString.printDebug(String.format("Successfully wrote %d characters.",outFile.length));

        //Files.write(this.filepath, outFile, StandardOpenOption.CREATE);
    }

    enum Algorithm {
        RANDOM,
        PERLINNOISE
    }
}
