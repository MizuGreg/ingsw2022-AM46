package it.polimi.ingsw.GameModel.Board;

import it.polimi.ingsw.GameModel.Board.Player.Player;
import it.polimi.ingsw.GameModel.Board.Player.Team;
import it.polimi.ingsw.GameModel.BoardElements.Professor;
import it.polimi.ingsw.Utils.Enum.Color;
import it.polimi.ingsw.Utils.Enum.TowerColor;
import it.polimi.ingsw.Utils.PlayerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorSet {

    private final Map<Color, Professor> professors = new HashMap<>();

    public ProfessorSet() {
        for (Color color : Color.values()) {
            professors.put(color, new Professor(null, color));
        }
    }

    public Professor getProfessor(Color color) {
        return professors.get(color);
    }

    public void setOwner(Color color, Player owner) {
        getProfessor(color).setOwner(owner);
    }

    public int getNumberOfProfessors(TowerColor towerColor) {
        int score = 0;
        for (Professor prof : professors.values()) {
            if (prof.getOwner().getTowerColor().equals(towerColor)) score++;
        }
        return score;
    }
}
