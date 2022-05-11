package it.polimi.ingsw.View.CLI;


import it.polimi.ingsw.GameModel.ObservableByClient;
import it.polimi.ingsw.Network.Message.UserAction.*;
import it.polimi.ingsw.Utils.Enum.*;
import it.polimi.ingsw.Utils.InputParser;
import it.polimi.ingsw.View.Client;
import it.polimi.ingsw.View.UI;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class CLI implements UI {

    private final PrintStream output;
    private final Client client;
    private final InputParser parser;
    private ObservableByClient game;
    private String nickname;


    /**
     * maps user-friendly ints to actual cloud IDs
     */
    private final HashMap<Integer, Integer> cloudMap = new HashMap<>();



    public CLI(Client client){
        output = new PrintStream(System.out);
        this.client = client;
        this.parser = new InputParser();
        for(int cloudID : game.getCloudIDs()){
            cloudMap.put(game.getCloudIDs().indexOf(cloudID), cloudID);
        }
    }



    @Override
    public void askTryConnecting() {

    }


    @Override
    public Map<String, String> askServerInfo() {
        return null;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @Override
    public String requestNickname() {
        displayMessage("Please input a nickname:");

        String nickname = parser.readLine();
        UserAction loginRequest = new LoginUserAction(nickname);

        client.sendUserAction(loginRequest);

        return nickname;
    }

    private String studentFrequencyString(List<Color> students){
        HashMap<Color, Integer> frequencyMap = new HashMap<>();
        for(Color c : students){
            if(frequencyMap.containsKey(c)){
                frequencyMap.put(c, frequencyMap.get(c) + 1);
            } else {
                frequencyMap.put(c, 1);
            }
        }
        StringBuilder colorFrequency = new StringBuilder();
        for(Color c : frequencyMap.keySet()){
            colorFrequency.append(String.format("%d %s students\n", frequencyMap.get(c), c));
        }
        return colorFrequency.toString();
    }

    public void setGame(ObservableByClient game){
        this.game = game;
    }

    public void displayLogin() {
        displayMessage("Placeholder welcome message");

    }



    public void displayMessage(String message) {
        message = message + "\n";
        System.out.println(message);
    }


    public void requestTowerColor(){

        displayMessage("Please choose a tower color from the following: " + Arrays.toString(game.getAvailableTowerColors().toArray()));

        // lambda maps the contents of the array of available TowerColors to String to check against user input, then converts back
        TowerColor towerColorSelection = TowerColor.valueOf(parser.readLineFromSelection(game.getAvailableTowerColors().stream()
                .map(o->o.toString().toLowerCase()).collect(Collectors.toList())).toUpperCase());
        UserAction towerColorRequest = new TowerColorUserAction(nickname, towerColorSelection);

        client.sendUserAction(towerColorRequest);
    }

    public void requestGameSettings(){

        GameMode gameMode = requestGameMode();
        int numOfPlayers = requestPlayerNumber();

        UserAction gameSettingsRequest = new GameSettingsUserAction(nickname, numOfPlayers, gameMode);

        client.sendUserAction(gameSettingsRequest);

    }


    private GameMode requestGameMode(){

        displayMessage("Please type 1 for normal or 2 for hard.");
        return GameMode.values()[parser.readBoundNumber(1, 2)];
    }

    private int requestPlayerNumber(){

        displayMessage("Please type the number of players this game will have.");
        return parser.readBoundNumber(2, 4);
    }


    public void requestWizard (){

        displayMessage("Please choose a type of wizard from the following: " + Arrays.toString(game.getAvailableWizards().toArray()));

        // lambda maps the contents of the array of available TowerColors to String to check against user input, then converts back
        WizardType wizardSelection = WizardType.valueOf(parser.readLineFromSelection(game.getAvailableWizards().stream()
                .map(o->o.toString().toLowerCase()).collect(Collectors.toList())).toUpperCase());
        UserAction wizardRequest = new WizardUserAction(nickname, wizardSelection);

        client.sendUserAction(wizardRequest);
    }

    @Override
    public void showText(String text) {
        displayMessage(text);

    }

    @Override
    public void showInfo(String info) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void reset() {

    }


    public void requestAssistant(){


        displayHand();
        displayMessage("Type the number of the assistant you would like to play.");

        int assistantIDSelection = parser.readNumberFromSelection(game.getCardsLeft(nickname));
        UserAction assistantRequest = new PlayAssistantUserAction(nickname, assistantIDSelection);

        client.sendUserAction(assistantRequest);
    }


    public void requestCloud(){

        displayClouds();
        displayMessage("Type the number of the cloud you would like to choose.");

        int cloudSelection = cloudMap.get(parser.readBoundNumber(0, cloudMap.size() - 1));
        UserAction cloudRequest = new TakeFromCloudUserAction(nickname, cloudSelection);

        client.sendUserAction(cloudRequest);
    }

    public void requestMotherNature(){

    }

    public void requestMoveFromEntrance(){
        HashMap<Integer, List<Integer>> islandTileIDs = game.getIslandTilesIDs();
        HashMap<Integer, Color> studentIDs = game.getEntranceStudentsIDs(nickname);

        displayEntrance(nickname);
        // lambda maps the contents of the array of available Colors to String to check against user input, then converts back
        Color colorSelection = Color.valueOf(parser.readLineFromSelection(studentIDs.values().stream()
                .map(Object::toString).collect(Collectors.toList())));
        // lambda looks for an entry with value equal to color in studentIDs, then returns the corresponding studentID if present, -1 otherwise
        // NOTE: should never actually assign the -1 since the chosen color is guaranteed to be present
        int studentID = studentIDs.entrySet().stream().filter(o -> o.getValue() == colorSelection)
                .map(Map.Entry::getKey).findAny().orElse(-1);
        int destinationID;

        displayMessage("Select destination type:\n1: Islands\n2: Dining Room");
        if(parser.readBoundNumber(1, 2) == 1){

            displayArchipelago();
            displayMessage("Select the island group number you would like to place your student in:");

            destinationID = islandTileIDs.get(parser.readBoundNumber(0, islandTileIDs.size() - 1)).get(0);

        } else {

            destinationID = game.getTableIDs(nickname).get(colorSelection);

        }

        UserAction moveStudentRequest = new MoveStudentUserAction(nickname, studentID, destinationID);

        client.sendUserAction(moveStudentRequest);
    }


    public void requestCharacter(){

        displayCharacters();

        int characterID = parser.readNumberFromSelection(game.getCurrentCharacterIDs());
        UserAction characterRequest = new UseCharacterUserAction(nickname, characterID);

        client.sendUserAction(characterRequest);
    }

    public void requestEndTurn(){

        displayMessage("Would you to play a character before ending your turn? y/n");

        String endTurnSelection = parser.readLineFromSelection(new ArrayList<>(Arrays.asList("y", "n")));

        if(endTurnSelection.equals("n")){
            UserAction endTurnRequest = new EndTurnUserAction(nickname);
            client.sendUserAction(endTurnRequest);
        }
    }


    public void standings(){
        displayArchipelago();
        displayClouds();
        displayCharacters();
        for(String nickname : game.getPlayerOrder()) {
            displayEntrance(nickname);
            displayTables(nickname);
        }
        displayHand();
    }


    public void displayHelp(){

    }

    // not really used atm but could be cool
    public void displayHelp(String context){
    }

    public void displayUnavailable(){
        displayMessage("You cannot execute this command at this time.");
    }

    public void displayInvalid(){
        displayMessage("Unrecognized command - please type help for a list of available commands.");
    }


    public void displayEntrance(String nickname){

        if(nickname.equals(this.nickname)){ nickname = "your"; } else nickname += "'s";

        StringBuilder toPrint = new StringBuilder(String.format("These are the students in %s entrance:\n\n", nickname));
        toPrint.append(studentFrequencyString(new ArrayList<>(game.getEntranceStudentsIDs(nickname).values())));

        displayMessage(toPrint.toString());

    }

    //TODO: display mother nature
    public void displayArchipelago(){
        HashMap<Integer, List<Integer>> islandGroups = game.getIslandTilesIDs();
        HashMap<Integer, List<Integer>> islandTileStudentIDs = game.getIslandTilesStudentsIDs();
        HashMap<Integer, Color> archipelagoStudentColors = game.getArchipelagoStudentIDs();
        HashMap<Integer, TowerColor> towerInfo = game.getIslandGroupsOwners();
        HashMap<Integer, Integer> noEntryTiles = game.getNoEntryTiles();


        StringBuilder toPrint = new StringBuilder("These are the Island Groups and the students they contain:\n\n");

        for(int islandGroupIdx : islandGroups.keySet()){
            List<Color> students = new ArrayList<>();
            if(towerInfo.get(islandGroupIdx) == null) {
                toPrint.append(String.format("\nIsland group %d contains the following students: \n", islandGroupIdx));
            } else {
                toPrint.append(String.format("\nIsland group %d contains a %s tower and the following students: \n", islandGroupIdx, towerInfo.get(islandGroupIdx)));
            }

            for(int islandTileID : islandGroups.get(islandGroupIdx)) {
                for (int studentID : islandTileStudentIDs.get(islandTileID)) {
                    students.add(archipelagoStudentColors.get(studentID));
                }
                toPrint.append(studentFrequencyString(students));
            }
            if(noEntryTiles.containsKey(islandGroupIdx)){
                toPrint.append("There is also a no entry tile present in this island group.\n");
            }

        }
        displayMessage(toPrint.toString());
    }

    public void displayTables(String nickname){


        if(nickname.equals(this.nickname)){ nickname = "your"; } else nickname += "'s";

        StringBuilder toPrint = new StringBuilder("These are %s tables:\n\n");

        for(Color c : Color.values()) {
            toPrint.append(String.format("%s table: %d students and %d coins\n",
                    c, game.getTableStudentsIDs(nickname, c).size(), game.getCoinsLeft(nickname, c)));
        }

        displayMessage(toPrint.toString());
    }


    public void displayClouds(){
        StringBuilder toPrint = new StringBuilder("These are the clouds and the students they contain:\n\n");
        for(int cloudIdx : cloudMap.keySet()){
            toPrint.append(String.format("Cloud %d contains the following students:\n", cloudIdx));

            toPrint.append(studentFrequencyString(new ArrayList<>(game.getCloudStudentsIDs(cloudMap.get(cloudIdx)).values())));
        }
        displayMessage(toPrint.toString());
    }


    public void displayCharacters(){
        StringBuilder toPrint = new StringBuilder("These are the characters which have been picked for this game:\n\n");
        for(int ID : game.getCurrentCharacterIDs())
            toPrint.append(String.format("Character %d\ncost:%d\ndescription: %s\n",
                    ID, game.getCharacterByID(ID).getCost(), Characters.values()[ID]));
        if(game.getCurrentCharacterIDs().size() > 0) displayMessage(toPrint.toString());

    }

    public void displayHand(){
        StringBuilder toPrint = new StringBuilder("These are the Assistant cards still in your hand:\n\n");
        for(int c : game.getCardsLeft(this.nickname)){
            toPrint.append(String.format("Card %d with move power %d\n", c, (c + 1)/2));
        }

        displayMessage(toPrint.toString());
    }



}
