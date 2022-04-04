package it.polimi.ingsw.GameModel.Board.Player;


import it.polimi.ingsw.GameModel.Board.Bag;
import it.polimi.ingsw.GameModel.BoardElements.Student;
import it.polimi.ingsw.GameModel.PlayerConfig;
import it.polimi.ingsw.Utils.Enum.Color;
import it.polimi.ingsw.Utils.Enum.TowerColor;
import it.polimi.ingsw.Utils.Exceptions.FullTeamException;

import java.util.List;
import java.util.NoSuchElementException;

public class Team {

    private List<Player> members;
    private final TowerColor color;
    private int teamSize;

    public Team(TowerColor color, int teamSize){
        this.color = color;
        this.teamSize = teamSize;
    }

    /**
     * @return list of players in this team
     */
    public List<Player> getMembers() {
        return members;
    }

    /**
     * @param nick name of the candidate to add to the team
     * @throws FullTeamException if the team is already full (size = max_players)
     */
    public void addMember(String nick, PlayerConfig playerConfig) throws FullTeamException {
        if(members.size() == 0) {
            members.add(new Player(nick, color, true, playerConfig));
        }
        else if (members.size() == 1) {
            members.add(new Player(nick, color, false, playerConfig));
        }
        else throw new FullTeamException();
    }

    /**
     * @param nick the nickname of the player to get
     * @return the player
     * @throws NoSuchElementException if there is no player in this team with specified nickname
     */
    public Player getPlayerByNick(String nick) throws NoSuchElementException {
        return members.stream().filter(p -> p.getNickname().equals(nick)).
                findAny().orElseThrow(NoSuchElementException::new);
    }

    /**
     * @return the tower holder of the team
     * @throws NoSuchElementException never. (if no players are tower holders in the team)
     */
    public Player getPlayerWithTowers() throws NoSuchElementException{
        return members.stream().filter(Player::isTowerHolder).findAny().orElseThrow(NoSuchElementException::new);
    }

    /**
     * @param ID student ID
     * @param player which player to search in
     * @return student with specified ID
     * @throws NoSuchElementException if the specified player does not hold the student with the specified ID
     */
    public Student getStudentByID(int ID, Player player) throws NoSuchElementException{
        return player.getStudentByID(ID);
    }

    // ALTERNATIVE
    /**
     * @param ID student ID
     * @param nick name of the player to search in
     * @return student with specified ID
     * @throws NoSuchElementException if the specified player does not hold the student with the specified ID
     */
    public Student getStudentByID(String nick, int ID) throws NoSuchElementException{
        return getPlayerByNick(nick).getStudentByID(ID);
    }

    /**
     * @return color of the Towers the team holds
     */
    public TowerColor getColor() {
        return color;
    }


    /**
     * @param nick name of the player whose score to get
     * @param color of the students whose score to get
     * @return number of Students of that color in the diningRoom
     */
    public int getScore(String nick, Color color){
        return getPlayerByNick(nick).getScore(color);
    }

    /**
     * @return number of towers placed by this team
     * @throws NoSuchElementException never. (if no players are tower holders in this team)
     */
    public int getTowersPlaced() throws NoSuchElementException{
        return members.stream().filter(Player::isTowerHolder).findAny().
                orElseThrow(NoSuchElementException::new).getTowersPlaced();
    }
    public void refillEntrance(List<Student> students, String nick) throws IllegalArgumentException, NoSuchElementException{
        getPlayerByNick(nick).refillEntrance(students);
    }
}
