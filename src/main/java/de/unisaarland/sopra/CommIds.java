package de.unisaarland.sopra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Team 14 on 12.09.2016.
 * implementation Karla
 */
public class CommIds {

    public HashMap<Integer, Integer> commLibIdsToMonsterIds;
    public List<Integer> commLibIds;

    /**
     *
     */
    public CommIds() {
        commLibIdsToMonsterIds = new HashMap<Integer, Integer>();
        commLibIds = new ArrayList<Integer>();
    }

    /**
     * @param commId
     * @param monsterId
     */

    public void addCommMonsterId(int commId, int monsterId) {
        commLibIds.add(commId);
        commLibIdsToMonsterIds.put(commId, monsterId);
    }

    /**
     * @param commId
     * @return
     */

    public int getMonsterId(int commId) {
        if (commLibIdsToMonsterIds.containsKey(commId)) {
            return commLibIdsToMonsterIds.get(commId);
        }
        return -1;
    }

    /**
     * Adds CommlibId to the List of Commlib Ids
     *
     * @param commId
     */
    public void addCommLibId(int commId) {
        commLibIds.add(commId);
    }

    /**
     * Gets the list of Commlib Ids
     *
     * @return List of CommlibIds
     */
    public List<Integer> getCommLibIds() {
        return commLibIds;
    }
}
