package GameXmlParser;

import GameXmlParser.Schema.Constraint;
import GameXmlParser.Schema.Constraints;
import GameXmlParser.Schema.GameType;
import GameXmlParser.Schema.Generated.GameDescriptor;
import GameXmlParser.Schema.Generated.Slice;
import GameXmlParser.Schema.Generated.Slices;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by ido on 13/08/2016.
 */
public class GameBoardXmlParser {
    private final String illegalXmlFileMessage = "Game.Game Definition Xml File is illegal";
    private final String sliceIsDefinedMoreThenOneTime = "Slice with orientation %s and index %d is defined more then one time";
    private final String sliceIsDefinedWithIllegalId = "Slice with orientation %s and index %d exceeds the maximum index  of %d";
    private final String unknownSliceOrentation = "Unknown slice orientation of %s is defined";
    private final String invalidGameTypeMessage = "Defined Game type of %s is not a valid Game Type";
    private final String invalidBlocksDefinition = "Invalid Blocks Definition at Slice with orientation %s and index %d";
    private final String invalidSlicesDefinition = "Invalid slices definition";
    private final String invalidSliceDefinition = "Invalid slice definition";
    private final String NumberDefinitionExceedsMaximum = "The sum the number constraints inside the Slice with orientation %s  and index %d exceeds the maximum allowed Of %d ";
    private final String invalidNumberFormatDefinition = "Invalid Number Definition inside a Slice with orientation %s and index %d ";
    private final String orientationRow = "row";
    private final String orientationColumn = "column";
    private final int minIndexValue = 1;
    private GameDescriptor gameDescriptor;
    private File gameDefinitionsXmlFile;
    private GameType gametype;

    public List<Constraints> getRowConstraints() {
        return rowConstraints;
    }

    public List<Constraints> getColumnConstraints() {
        return columnConstraints;
    }

    private List<Constraints> rowConstraints;
    private List<Constraints> columnConstraints;
    private int rows;
    private int columns;


    public GameBoardXmlParser(String gameDefinitionsXmlFileName) throws GameDefinitionsXmlParserExeption {
        gameDefinitionsXmlFile = new File(gameDefinitionsXmlFileName);
        parseXml();

    }

    private void parseXml() throws GameDefinitionsXmlParserExeption {
        parseGameDescriptor();
        extractGameType();
        extractBoardDimensions();
        extractSlices();
    }

    private void extractBoardDimensions() {
        rows = gameDescriptor.getBoard().getDefinition().getRows().intValue();
        columns = gameDescriptor.getBoard().getDefinition().getColumns().intValue();
    }

    private void extractSlices() throws GameDefinitionsXmlParserExeption {
        Slices slices = gameDescriptor.getBoard().getDefinition().getSlices();
        Boolean[] isRowDefined = new Boolean[rows];
        Boolean[] isColumnDefined = new Boolean[columns];
        if (slices != null) {
            for (Slice slice : slices.getSlice()) {
                if (slice != null) {
                    int currentSliceIndex = slice.getId().intValue();
                    switch (slice.getOrientation()) {
                        case orientationRow:
                            extractBlocksFromSlice(isRowDefined, slice, currentSliceIndex, rows);
                            break;
                        case orientationColumn:
                            extractBlocksFromSlice(isColumnDefined, slice, currentSliceIndex, columns);
                            break;
                        default:
                            throw new GameDefinitionsXmlParserExeption(String.format(unknownSliceOrentation, slice.getOrientation()));
                    }
                } else {
                    throw new GameDefinitionsXmlParserExeption(invalidSliceDefinition);
                }
            }
        } else {
            throw new GameDefinitionsXmlParserExeption(invalidSlicesDefinition);
        }
    }

    private void extractBlocksFromSlice(Boolean[] isOrientationDefined, Slice slice, int currentSliceIndex, int maxIndexValue) throws GameDefinitionsXmlParserExeption {
        if (isValidRange(currentSliceIndex, maxIndexValue)) {
            if (!isOrientationDefined[currentSliceIndex] || isOrientationDefined[currentSliceIndex] == null) {
                isOrientationDefined[currentSliceIndex] = true;
                setConstraints(slice);
            } else {
                throw new GameDefinitionsXmlParserExeption(String.format(sliceIsDefinedMoreThenOneTime, slice.getOrientation(), currentSliceIndex));
            }
        } else {
            throw new GameDefinitionsXmlParserExeption(String.format(sliceIsDefinedWithIllegalId, slice.getOrientation(), currentSliceIndex, maxIndexValue));
        }
    }

    private void setConstraints(Slice slice) throws GameDefinitionsXmlParserExeption {
        String blocks = slice.getBlocks();
        String parts[];
        String Regex = "\\s\t*,\\s\t*";
        if (blocks != null) {
            parts = blocks.split(Regex);
            Constraints constraints = new Constraints(parts.length);
            int sum = 0;
            for (String part : parts) {
                try {
                    int currentInt = Integer.parseInt(part);
                    sum += currentInt;
                    constraints.addConstraint(new Constraint(currentInt));
                } catch (NumberFormatException e) {
                    throw new GameDefinitionsXmlParserExeption(String.format(invalidNumberFormatDefinition, slice.getOrientation(), slice.getId()), e);
                }

            }
            if (slice.getOrientation().equals(orientationRow)) {
                if ((sum + parts.length - 1) > columns) {
                    throw new GameDefinitionsXmlParserExeption(String.format(NumberDefinitionExceedsMaximum, slice.getOrientation(), slice.getId(), columns));
                } else {
                    rowConstraints.add(constraints);
                }
            } else {
                if ((sum + parts.length - 1) > rows) {
                    throw new GameDefinitionsXmlParserExeption(String.format(NumberDefinitionExceedsMaximum, slice.getOrientation(), slice.getId(), rows));
                } else {
                    columnConstraints.add(constraints);
                }
            }
        } else {
            throw new GameDefinitionsXmlParserExeption(String.format(invalidBlocksDefinition, slice.getOrientation(), slice.getId()));
        }
    }

    private boolean isValidRange(int currentSliceId, int maxIndexValue) {
        return (currentSliceId >= minIndexValue && currentSliceId <= maxIndexValue);
    }

    private void extractGameType() throws GameDefinitionsXmlParserExeption {
        try {
            gametype = GameType.valueOf(gameDescriptor.getGameType());
        } catch (Exception e) {
            throw new GameDefinitionsXmlParserExeption(String.format(invalidGameTypeMessage, gameDescriptor.getGameType()), e);
        }
    }

    private void parseGameDescriptor() throws GameDefinitionsXmlParserExeption {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            gameDescriptor = (GameDescriptor) jaxbUnmarshaller.unmarshal(gameDefinitionsXmlFile);
        } catch (JAXBException e) {
            throw new GameDefinitionsXmlParserExeption(illegalXmlFileMessage, e);
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
}
