package de.unisaarland.sopra.modeltest;

import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Field;
import de.unisaarland.sopra.model.FieldEffects;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Karla on 13.09.2016.
 */
public class FieldEffectsTest {

    private List<CreatureType> list;

    @Before
    public void setUp(){
       list = new LinkedList<>();
        list.add(CreatureType.BOAR);
        list.add(CreatureType.FAIRY);
        list.add(CreatureType.ELF);
        list.add(CreatureType.IFRIT);
        list.add(CreatureType.KOBOLD);
        list.add(CreatureType.NAGA);
        list.add(CreatureType.ROOK);
        list.add(CreatureType.WRAITH);
        list.add(CreatureType.YETI);

    }


    @Test
    public void healFieldEffectTest(){
        for (int i = 0; i < list.size(); i++) {
            TestCase.assertEquals(FieldEffects.getDamage(Field.HEAL, list.get(i)), -20);
        }

    }

    @Test
    public void healFieldDeactivatedEffectTest(){
        for (int i = 0; i < list.size(); i++) {
            assertEquals(FieldEffects.getDamage(Field.HEAL_DISABLED, list.get(i)), 0);
        }

    }

    @Test
    public void bushFieldEffectsTest(){

       for(int i= 0; i < list.size(); i ++){
            if(list.get(i).equals(CreatureType.KOBOLD)){
                assertEquals(FieldEffects.getDefensiveMultiplier(Field.BUSH, list.get(i)),0.5);
            } else
                assertEquals(FieldEffects.getDefensiveMultiplier(Field.BUSH, list.get(i)), 1.0);
       }
    }

    @Test
    public void treeFieldEffectTest(){

        for(int i= 0; i < list.size(); i ++){
            if(list.get(i) != CreatureType.ELF) {
                assertEquals(FieldEffects.getDefensiveMultiplier(Field.TREE, list.get(i)), 0.7);
                assertEquals(FieldEffects.getAttackCostMultiplier(Field.TREE, list.get(i)), 2);
                assertEquals(FieldEffects.getRangeIncrease(Field.TREE,list.get(i)),0);

            }else
                assertEquals(FieldEffects.getRangeIncrease(Field.TREE,list.get(i)),4);
        }

    }

    @Test
    public void hillFieldEffectTest() {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(CreatureType.ROOK)){
                assertEquals(FieldEffects.getRangeIncrease(Field.HILL, list.get(i)), 3);
            }else
                assertEquals(FieldEffects.getRangeIncrease(Field.HILL,list.get(i)),0);
        }
    }

    @Test
    public void waterFieldEffectTest(){
        for (int i = 0; i < list.size(); i++) {
            assertFalse(FieldEffects.canAttack(Field.WATER,list.get(i)));
            if(list.get(i).equals(CreatureType.NAGA)){
                assertEquals(FieldEffects.getMovementCostMultiplier(Field.WATER, list.get(i)),2);
            }else {
                assertEquals(FieldEffects.getMovementCostMultiplier(Field.WATER, list.get(i)), 4);
            }
            assertEquals(FieldEffects.getDamage(Field.WATER,list.get(i)),20);
        }
     }

    @Test
    public void boarsFairiesShallNotPassWaterTest(){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(CreatureType.BOAR) || list.get(i).equals(CreatureType.FAIRY)){
                assertFalse(FieldEffects.canEnter(Field.WATER,list.get(i)));
            } else
                assertTrue(FieldEffects.canEnter(Field.WATER, list.get(i)));
        }

    }

     @Test
    public void lavaFieldEffectTest(){
         for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(CreatureType.IFRIT)){
                assertEquals(FieldEffects.getDamage(Field.LAVA,list.get(i)),0);
            }else
                assertEquals(FieldEffects.getDamage(Field.LAVA,list.get(i)),25);
         }
     }
     @Test
    public void curseFieldEffectTest(){
         for (int i = 0; i < list.size(); i++) {
             if(list.get(i).equals(CreatureType.WRAITH)){
                 assertEquals(FieldEffects.getAttackCostMultiplier(Field.CURSE,list.get(i)), 1);
             }else
                 assertEquals(FieldEffects.getAttackCostMultiplier(Field.CURSE,list.get(i)), 3);
         }

     }



}
