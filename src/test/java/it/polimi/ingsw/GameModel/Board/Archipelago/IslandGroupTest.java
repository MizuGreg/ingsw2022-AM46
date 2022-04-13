package it.polimi.ingsw.GameModel.Board.Archipelago;

import it.polimi.ingsw.GameModel.Board.Bag;
import it.polimi.ingsw.GameModel.Board.Player.TeamManager;
import it.polimi.ingsw.GameModel.BoardElements.Student;
import it.polimi.ingsw.GameModel.BoardElements.Tower;
import it.polimi.ingsw.GameModel.GameConfig;
import it.polimi.ingsw.Utils.Enum.Color;
import it.polimi.ingsw.Utils.Enum.TowerColor;
import it.polimi.ingsw.Utils.Exceptions.FullTeamException;
import it.polimi.ingsw.Utils.Exceptions.GameOverException;
import it.polimi.ingsw.Utils.PlayerList;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




/**
 * Tests the IslandGroup class
 */
class IslandGroupTest {

    /**
     * Tests that starting IslandGroup has mother nature as it should
     */
    @Test
    void hasMotherNature() {
        IslandGroup islandGroup = new IslandGroup(true);
        assertTrue(islandGroup.hasMotherNature());
    }

    /**
     * Tests that methods returns an IslandTile containing MotherNature
     */
    @Test
    void getMotherNatureTile() {
        IslandGroup islandGroup = new IslandGroup(true);
        assertTrue(islandGroup.getMotherNatureTile().hasMotherNature());
    }

    /**
     * Test that counting influence works when IslandGroup contains multiple IslandTiles
     */
    @Test
    void countInfluence() {
        IslandGroup islandGroup = new IslandGroup(true);
        List<IslandTile> islandTiles = new ArrayList<>();
        islandTiles.add(new IslandTile(null, false, null));
        islandTiles.add(new IslandTile(null, false, null));
        islandTiles.get(0).placePawn(new Student(Color.PINK, null));
        islandTiles.get(0).placePawn(new Student(Color.PINK, null));
        islandTiles.get(0).placePawn(new Student(Color.PINK, null));
        islandTiles.get(0).placePawn(new Student(Color.PINK, null));
        islandTiles.get(0).placePawn(new Student(Color.RED, null));
        islandTiles.get(0).placePawn(new Student(Color.RED, null));
        islandTiles.get(0).placePawn(new Student(Color.RED, null));
        islandTiles.get(1).placePawn(new Student(Color.PINK, null));
        islandTiles.get(1).placePawn(new Student(Color.PINK, null));
        islandTiles.get(1).placePawn(new Student(Color.PINK, null));
        islandGroup.addIslandTilesAfter(islandTiles);
        assertTrue(islandGroup.countInfluence(Color.PINK) == 7 && islandGroup.countInfluence(Color.RED) == 3 && islandGroup.countInfluence(Color.BLUE) == 0);
    }

    /**
     *  Tests that team correctly conquers
     * @throws FullTeamException not used
     * @throws GameOverException not used
     */
    @Test
    void conquer() throws FullTeamException, GameOverException {
        IslandGroup islandGroup = new IslandGroup(true);
        Bag bag = new Bag();
        bag.fillRemaining();
        GameConfig gameConfig = new GameConfig(4);
        gameConfig.getPlayerConfig().setBag(bag);

        HashMap<String, TowerColor> teamConfiguration = new HashMap<>();
        teamConfiguration.put("Simo", TowerColor.BLACK);
        teamConfiguration.put("Greg", TowerColor.BLACK);
        teamConfiguration.put("Pirovano", TowerColor.WHITE);
        teamConfiguration.put("Ceruti", TowerColor.WHITE);
        TeamManager teamManager = new TeamManager();
        PlayerList players = teamManager.create(gameConfig, teamConfiguration);

        islandGroup.conquer(players.getTowerHolder(TowerColor.BLACK));
        assertEquals(islandGroup.getTowerColor(), TowerColor.BLACK);
        islandGroup.conquer(players.getTowerHolder(TowerColor.WHITE));
        assertEquals(islandGroup.getTowerColor(), TowerColor.WHITE);
        assertFalse(islandGroup.conquer(players.getTowerHolder(TowerColor.WHITE)));
    }

    /**
     * Ensures IslandGroup has no IslandTiles after operation
     */
    @Test
    void removeIslandTiles() {
        IslandGroup islandGroup = new IslandGroup(true);
        List<IslandTile> islandTiles = islandGroup.removeIslandTiles();
        assertTrue(islandTiles.get(0) != null && islandTiles.get(0).hasMotherNature());
        assertFalse(islandGroup.hasIslandTile(islandTiles.get(0)));
    }

    /**
     * Tests if method correctly puts IslandTiles after those already present
     */
    @Test
    void addIslandTilesBefore() {
        IslandGroup islandGroup1 = new IslandGroup(true);
        IslandGroup islandGroup2 = new IslandGroup(false);
        IslandGroup islandGroup3 = new IslandGroup(false);
        List<IslandTile> islandTiles;
        islandGroup2.addIslandTilesBefore(islandGroup1.removeIslandTiles());
        islandGroup3.addIslandTilesBefore(islandGroup2.removeIslandTiles());
        islandTiles = islandGroup3.removeIslandTiles();
        assertTrue(islandTiles.get(0).hasMotherNature() && islandTiles.size() == 3);
    }

    /**
     * Tests if method correctly puts IslandTiles after those already present
     */
    @Test
    void addIslandTilesAfter() {
        IslandGroup islandGroup1 = new IslandGroup(true);
        IslandGroup islandGroup2 = new IslandGroup(false);
        IslandGroup islandGroup3 = new IslandGroup(false);
        List<IslandTile> islandTiles;
        islandGroup2.addIslandTilesAfter(islandGroup1.removeIslandTiles());
        islandGroup3.addIslandTilesAfter(islandGroup2.removeIslandTiles());
        islandTiles = islandGroup3.removeIslandTiles();
        assertTrue(islandTiles.get(2).hasMotherNature() && islandTiles.size() == 3);
    }

    /**
     * Adds IslandTile to an IslandGroup and tests if methods returns true
     */
    @Test
    void hasIslandTile() {
        IslandGroup islandGroup = new IslandGroup(true);
        List<IslandTile> islandTiles = new ArrayList<>();
        islandTiles.add(new IslandTile(null, false,null ));
        islandGroup.addIslandTilesBefore(islandTiles);
        assertTrue(islandGroup.hasIslandTile(islandTiles.get(0)));
    }

    /**
     * Tests that method correctly returns the color of the tower placed in the IslandGroup
     */
    @Test
    void getTowerColor() {
        IslandGroup islandGroup = new IslandGroup(true);
        List<IslandTile> islandTiles = new ArrayList<>();
        islandTiles.add(new IslandTile(null, false,null ));
        islandTiles.get(0).swapTower(new Tower(TowerColor.BLACK, null));
        islandGroup.addIslandTilesBefore(islandTiles);
        assertSame(islandGroup.getTowerColor(), TowerColor.BLACK);
    }

    /**
     * Tests that method actually finds the right student
     */
    @Test
    void findStudentByID(){
        IslandGroup islandGroup = new IslandGroup(true);
        List<IslandTile> islandTiles = new ArrayList<>();
        IslandTile islandTile = new IslandTile(null, true, null);
        islandTile.placePawn(new Student(Color.GREEN, null));
        islandTile.placePawn(new Student(Color.PINK, null));
        islandTile.placePawn(new Student(Color.RED, null));
        islandTile.placePawn(new Student(Color.BLUE, null));
        Student studentToFind = new Student(Color.YELLOW, null);
        islandTile.placePawn(studentToFind);
        islandTiles.add(islandTile);
        islandGroup.addIslandTilesBefore(islandTiles);
        assertEquals(studentToFind, islandGroup.getStudentByID(studentToFind.getID()));
    }

    /**
     * Tests that method actually finds the right IslandTile
     */
    @Test
    void findIslandTileByID(){
        IslandGroup islandGroup = new IslandGroup(true);
        List<IslandTile> islandTiles = new ArrayList<>();
        IslandTile islandTileToFind = new IslandTile(null, true, null);
        IslandTile islandTile1 = new IslandTile(null, true, null);
        islandTiles.add(islandTileToFind);
        islandTiles.add(islandTile1);
        islandGroup.addIslandTilesBefore(islandTiles);
        assertEquals(islandTileToFind, islandGroup.getIslandTileByID(islandTileToFind.getID()));
    }
}