package GameXmlParser;

import Game.GameBoard;
import Game.Player.ComputerPlayer;
import Game.Player.HumenPlayer;
import Game.Player.PlayerType;
import Game.SolutionBoard;
import GameXmlParser.Schema.Constraint;
import GameXmlParser.Schema.Constraints;
import GameXmlParser.Schema.GameType;
import GameXmlParser.Schema.Generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


/**
 * Created by ido on 13/08/2016.
 */
public class GameBoardXmlParser {
    private static final String JAXB_XML_GAME_PACKAGE_NAME = "GameXmlParser.Schema.Generated";
    private final String illegalXmlFileMessage = "Game Definition Xml File is illegal";
    private final String sliceIsDefinedMoreThenOneTime = "Slice with orientation %s and index %d is defined more then one time";
    private final String sliceIsDefinedWithIllegalId = "Slice with orientation %s and index %d exceeds the maximum index  of %d";
    private final String unknownSliceOrentation = "Unknown slice orientation of %s is defined";
    private final String invalidGameTypeMessage = "Defined Game type of %s is not a valid Game Type";
    private final String invalidBlocksDefinition = "Invalid Blocks Definition at Slice with orientation %s and index %d";
    private final String invalidSlicesDefinition = "Invalid slices definition";
    private final String invalidSliceDefinition = "Invalid slice definition";
    private final String NumberDefinitionExceedsMaximum = "The sum the number constraints inside the Slice with orientation %s  and index %d exceeds the maximum allowed Of %d ";
    private final String invalidNumberFormatDefinition = "Invalid Number Definition inside a Slice with orientation %s and index %d ";
    private final String solutionIsNotDefined = "Solution is not defined in the provided xml file";
    private final String squareIsNotDefined = "a square is not defined correctly in the provided xml file";
    private final String orientationRow = "row";
    private final String orientationColumn = "column";
    private final String InvalidConstraintsOnSolution = "Solution has a %s Constraint that does not match the constraint defined in the xml file";
    private final String invlaidMultiplayersMoves = "Invalid Number Of Moves entered %s";
    private final int minIndexValue = 0;
    private final int maxDimension = 100;
    private final int minDimension = 10;
    private GameDescriptor gameDescriptor;
    private File gameDefinitionsXmlFile;
    private GameType gametype;
    private SolutionBoard solutionBoard;
    private Constraints[] rowConstraints;
    private Constraints[] columnConstraints;
    private int rows;
    private int columns;
    private int moves;
    private List<Game.Player.Player> players = new ArrayList<>();

    public int getMoves() {
        return moves;
    }


    public List<Game.Player.Player> getPlayers() {
        return players;
    }


    public List<Constraints> getRowConstraints() {
        return new ArrayList<>(Arrays.asList(rowConstraints));
    }

    public List<Constraints> getColumnConstraints() {

        return new ArrayList<>(Arrays.asList(columnConstraints));
    }

    public GameBoardXmlParser(String gameDefinitionsXmlFileName) throws GameDefinitionsXmlParserException {
        gameDefinitionsXmlFile = new File(gameDefinitionsXmlFileName);
        parseXml();
    }

    private void parseXml() throws GameDefinitionsXmlParserException {
        parseGameDescriptor();
        extractGameType();
        extractBoardDimensions();
        extractSlices();
        extractSolutionBoard();
        switch (gametype) {
            case SinglePlayer:
                createDefaultPlayer();
                break;
            case MultiPlayer:
                extractMultiPlayersInfo();
                break;
            case DynamicMultiPlayer:
                break;
        }
    }

    private void createDefaultPlayer() {
        players.add(new Game.Player.Player("default Player", PlayerType.Human, 0, new GameBoard(rows, columns)));
    }

    private void extractMultiPlayersInfo() throws GameDefinitionsXmlParserException {
        try {
            moves = Integer.parseInt(gameDescriptor.getMultiPlayers().getMoves());
            Players xmlPlayers = gameDescriptor.getMultiPlayers().getPlayers();
            HashSet<Integer> idSet = new HashSet<>();
            for (Player player : xmlPlayers.getPlayer()) {
                int id = player.getId().intValue();
                if (idSet.contains(id)) {
                    throw new GameDefinitionsXmlParserException("Duplicate Player ID was found in the xml");
                } else {
                    idSet.add(id);
                }
                PlayerType playerType = PlayerType.valueOf(player.getPlayerType());
                String name = player.getName();
                Game.Player.Player thePlayer;
                if (playerType.equals(PlayerType.Human)) {
                    thePlayer = new HumenPlayer(name, playerType, id, new GameBoard(rows, columns));
                } else {
                    thePlayer = new ComputerPlayer(name, playerType, id, new GameBoard(rows, columns));
                }
                players.add(thePlayer);

            }
        } catch (NumberFormatException e) {
            throw new GameDefinitionsXmlParserException(String.format("Invalid Number Of Moves entered: %s", gameDescriptor.getMultiPlayers().getMoves() == null ? "null" : gameDescriptor.getMultiPlayers().getMoves()));
        } catch (Exception e) {
            throw new GameDefinitionsXmlParserException(e.getMessage());
        }
    }

    private void extractSolutionBoard() throws GameDefinitionsXmlParserException {
        Solution solution = gameDescriptor.getBoard().getSolution();
        if (solution != null) {
            solutionBoard = new SolutionBoard(rows, columns);
            for (Square square : solution.getSquare()) {
                if (square != null) {
                    int row = square.getRow().intValue() - 1;
                    int column = square.getColumn().intValue() - 1;
                    if (isValidRange(row, rows) && isValidRange(column, columns)) {
                        solutionBoard.setBoardSquareAsBlack(row, column);
                    } else {
                        throw new GameDefinitionsXmlParserException(squareIsNotDefined);
                    }
                } else {
                    throw new GameDefinitionsXmlParserException(squareIsNotDefined);
                }
            }
        } else {
            throw new GameDefinitionsXmlParserException(solutionIsNotDefined);
        }
        checkConstraintsOnSolutionBoard();
    }

    private void checkConstraintsOnSolutionBoard() throws GameDefinitionsXmlParserException {
        if (!solutionBoard.validRowsConstraints(rowConstraints)) {
            throw new GameDefinitionsXmlParserException(String.format(InvalidConstraintsOnSolution, orientationRow));
        }
        if (!solutionBoard.validColumnsConstraints(columnConstraints)) {
            throw new GameDefinitionsXmlParserException(String.format(InvalidConstraintsOnSolution, orientationColumn));
        }
    }

    private void extractBoardDimensions() throws GameDefinitionsXmlParserException {
        rows = gameDescriptor.getBoard().getDefinition().getRows().intValue();
        columns = gameDescriptor.getBoard().getDefinition().getColumns().intValue();
        if (!validDimension(rows) || !validDimension(columns)) {
            throw new GameDefinitionsXmlParserException("Board Dimensions should be greater of equal then 10 and less then 100");
        }
    }

    private boolean validDimension(int dimension) {
        return (dimension >= minDimension) && (dimension < maxDimension);
    }

    private void extractSlices() throws GameDefinitionsXmlParserException {
        Slices slices = gameDescriptor.getBoard().getDefinition().getSlices();
        boolean[] isRowDefined = new boolean[rows];
        boolean[] isColumnDefined = new boolean[columns];
        rowConstraints = new Constraints[rows];
        columnConstraints = new Constraints[columns];
        if (slices != null) {
            for (Slice slice : slices.getSlice()) {
                if (slice != null) {
                    int currentSliceIndex = slice.getId().intValue() - 1;
                    switch (slice.getOrientation()) {
                        case orientationRow:
                            extractBlocksFromSlice(isRowDefined, slice, currentSliceIndex, rows);
                            break;
                        case orientationColumn:
                            extractBlocksFromSlice(isColumnDefined, slice, currentSliceIndex, columns);
                            break;
                        default:
                            throw new GameDefinitionsXmlParserException(String.format(unknownSliceOrentation, slice.getOrientation()));
                    }
                } else {
                    throw new GameDefinitionsXmlParserException(invalidSliceDefinition);
                }
            }
        } else {
            throw new GameDefinitionsXmlParserException(invalidSlicesDefinition);
        }
    }

    private void extractBlocksFromSlice(boolean[] isOrientationDefined, Slice slice, int currentSliceIndex, int maxIndexValue) throws GameDefinitionsXmlParserException {
        if (isValidRange(currentSliceIndex, maxIndexValue)) {
            if (!isOrientationDefined[currentSliceIndex]) {
                isOrientationDefined[currentSliceIndex] = true;
                setConstraints(currentSliceIndex, slice);
            } else {
                throw new GameDefinitionsXmlParserException(String.format(sliceIsDefinedMoreThenOneTime, slice.getOrientation(), currentSliceIndex));
            }
        } else {
            throw new GameDefinitionsXmlParserException(String.format(sliceIsDefinedWithIllegalId, slice.getOrientation(), currentSliceIndex, maxIndexValue));
        }
    }

    private void setConstraints(int index, Slice slice) throws GameDefinitionsXmlParserException {
        String blocks = slice.getBlocks();
        String parts[];
        if (blocks != null) {
            parts = blocks.split(",");
            Constraints constraints = new Constraints(parts.length);
            int sum = 0;
            for (String part : parts) {
                try {
                    int currentInt = Integer.parseInt(part.trim());
                    sum += currentInt;
                    constraints.addConstraint(new Constraint(currentInt));
                } catch (NumberFormatException e) {
                    throw new GameDefinitionsXmlParserException(String.format(invalidNumberFormatDefinition, slice.getOrientation(), slice.getId()), e);
                }

            }
            if (slice.getOrientation().equals(orientationRow)) {
                if ((sum + parts.length - 1) > columns) {
                    throw new GameDefinitionsXmlParserException(String.format(NumberDefinitionExceedsMaximum, slice.getOrientation(), slice.getId(), columns));
                } else {
                    rowConstraints[index] = constraints;
                }
            } else {
                if ((sum + parts.length - 1) > rows) {
                    throw new GameDefinitionsXmlParserException(String.format(NumberDefinitionExceedsMaximum, slice.getOrientation(), slice.getId(), rows));
                } else {
                    columnConstraints[index] = constraints;
                }
            }
        } else {
            throw new GameDefinitionsXmlParserException(String.format(invalidBlocksDefinition, slice.getOrientation(), slice.getId()));
        }
    }

    private boolean isValidRange(int currentSliceId, int maxIndexValue) {
        return (currentSliceId >= minIndexValue && currentSliceId < maxIndexValue);
    }

    private void extractGameType() throws GameDefinitionsXmlParserException {
        try {
            gametype = GameType.valueOf(gameDescriptor.getGameType());
        } catch (Exception e) {
            throw new GameDefinitionsXmlParserException(String.format(invalidGameTypeMessage, gameDescriptor.getGameType()), e);
        }
    }

    private void parseGameDescriptor() throws GameDefinitionsXmlParserException {
        try {
            gameDescriptor = GameBoardXmlParser.deserializeFrom(new FileInputStream(gameDefinitionsXmlFile));
        } catch (JAXBException e) {
            throw new GameDefinitionsXmlParserException(illegalXmlFileMessage, e);
        } catch (FileNotFoundException e) {

            throw new GameDefinitionsXmlParserException(e.getMessage(), e);
        }
    }

    public GameType getGameType() {
        return gametype;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public SolutionBoard getSolutionBoard() {
        return solutionBoard;
    }

    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }
}
