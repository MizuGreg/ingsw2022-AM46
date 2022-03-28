package it.polimi.ingsw.GameModel.Board.Archipelago;

import it.polimi.ingsw.GameModel.Board.Archipelago.MoveMotherNatureStrategy.MotherNatureStrategy;
import it.polimi.ingsw.GameModel.Board.Archipelago.MoveMotherNatureStrategy.StandardMotherNatureStrategy;
import it.polimi.ingsw.GameModel.Board.Archipelago.ResolveStrategy.ResolveStrategy;
import it.polimi.ingsw.GameModel.Board.Player.Team;
import it.polimi.ingsw.GameModel.BoardElements.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that models all the islands on the table.
 * It contains a list of IslandGroup to model the merged IslandTiles.
 */
public class Archipelago {

    /**
     * List that contains all the IslandGroups. The order of the list is used to merge and render the islands
     */
    private List<IslandGroup> islandGroups;

    /**
     * Strategy to apply when resolving an island
     */
    private ResolveStrategy resolveStrategy;

    /**
     * Strategy to apply when moving MotherNature
     */
    private MotherNatureStrategy motherNatureStrategy;

    /**
     * Creates the Archipelago and creates 12 IslandGroups,
     */
    public Archipelago(){
        islandGroups = new ArrayList<IslandGroup>();
        int startingIsland = selectStartingIsland();
        for (int i = 0; i < 12; i++) {
            islandGroups.add(new IslandGroup(i==startingIsland));
        }
        //TODO: set strategies to default
        motherNatureStrategy = new StandardMotherNatureStrategy();
    }

    /**
     * Selects the IslandGroup which holds the starting IslandTile
     * @return A random int between 0 and 11
     */
    private int selectStartingIsland(){
        Random random = new Random();
        return random.nextInt(12);
    }

    /**
     * Calls the motherNatureStrategy method to handle moving MotherNature
     * @param islandTileDestination Island group selected by the user
     * @param moveCount Allowed island that MotherNature can move
     */
    public void moveMotherNature(IslandTile islandTileDestination, int moveCount){
        motherNatureStrategy.moveMotherNature(getMotherNatureIslandTile(), islandTileDestination, moveCount, islandGroups);
    }

    /**
     * Finds which IslandGroups contains the IslandTile containing MotherNature
     * @return The IslandTile containing MotherNature. Should never return null
     */
    private IslandTile getMotherNatureIslandTile(){
        IslandTile temp;
        for(IslandGroup islandGroup : islandGroups){
            if(islandGroup.hasMotherNature())
                return islandGroup.getMotherNatureTile();
        }
        return null; //should never reach here
    }

    public void resolveIslandGroup(IslandGroup islandGroup, List<Team> teams){
        Team winner = resolveStrategy.resolveIslandGroup(islandGroup,teams);
        conquerIslandGroup(islandGroup, winner);
        mergeIslandGroup(islandGroup);
    }

    private void conquerIslandGroup(IslandGroup islandGroup, Team team){
        islandGroup.conquer(team);
    }

    private void mergeIslandGroup(IslandGroup islandGroupToMerge){

    }

    /**
     * Places the Student in the given IslandTile
     * @param student Student to place
     * @param islandTile IslandTile where student must be placed
     */
    public void placeStudent(Student student, IslandTile islandTile){
        for(IslandGroup islandGroup : islandGroups){ //FIXME: you could just call islandTile.placeStudent withouth checking anything, this just delegates the action to the islandgroup which actually contains the tile
            if(islandGroup.hasIslandTile(islandTile))
                islandGroup.placeStudent(student, islandTile);
        }
    }

    public void removeStudent(Student student, IslandTile islandTile){
        //FIXME: not actually needed i think
    }

    //TODO: add serch by IDs

}
