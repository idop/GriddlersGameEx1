import Schema.GameType;
import Schema.Generated.GameDescriptor;
import Schema.Generated.Slice;
import Schema.Generated.Slices;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by ido on 13/08/2016.
 */
public class GameBoardXmlParser {
    private final String illegalXmlFileMessage = "Game Definition Xml File is illegal";
    private final String sliceIsDefinedMoreThenOneTime = "Slice with orientation {} and index {} is defined more then one time";
    private final String sliceIsDefinedWithIllegalId = "Slice with orientation {} and index {} exceeds the maximum index  of {}";
    private final String unknownSliceOrentation = "Unknown slice orientation  of {} is defined";
    private final String invalidGameTypeMessage = "Defined Game type of {} is not a valid Game Type";
    private final String orientationRow = "row";
    private final String orientationColumn = "column";
    private final int minIndexValue = 1;
    private GameDescriptor gameDescriptor;
    private File gameDefinitionsXmlFile;
    private GameType gametype;
    private List<Slice> rowSlices;
    private List<Slice> ColumnSlices;
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
        for (Slice slice : slices.getSlice()) {
            String currentSliceOrientation = slice.getOrientation();
            int currentSliceIndex = slice.getId().intValue();
            if (currentSliceOrientation.equals(orientationRow)) {
                extractBlocksFromSlice(isRowDefined, slice, currentSliceIndex, rows);
            } else if (currentSliceOrientation.equals(orientationColumn)) {
                extractBlocksFromSlice(isColumnDefined, slice, currentSliceIndex, columns);
            } else {
                throw new GameDefinitionsXmlParserExeption(String.format(unknownSliceOrentation, currentSliceOrientation));
            }
        }
    }

    private void extractBlocksFromSlice(Boolean[] isOrientationDefined, Slice slice, int currentSliceIndex, int maxIndexValue) throws GameDefinitionsXmlParserExeption {
        if (isvalidRange(currentSliceIndex, maxIndexValue)) {
            if (!isOrientationDefined[currentSliceIndex] || isOrientationDefined[currentSliceIndex] == null) {
                isOrientationDefined[currentSliceIndex] = true;
                slice.getBlocks() // TODO handle Blocks
            } else {
                throw new GameDefinitionsXmlParserExeption(String.format(sliceIsDefinedMoreThenOneTime, slice.getOrientation(), currentSliceIndex));
            }
        } else {
            throw new GameDefinitionsXmlParserExeption(String.format(sliceIsDefinedWithIllegalId, slice.getOrientation(), currentSliceIndex, maxIndexValue));
        }
    }

    private boolean isvalidRange(int currentSliceId, int maxIndexValue) {
        return (currentSliceId >= minIndexValue && currentSliceId <= maxIndexValue);
    }

    private void extractGameType() throws GameDefinitionsXmlParserExeption {
        gametype = GameType.valueOf(gameDescriptor.getGameType());
        if (gametype == null) {
            throw new GameDefinitionsXmlParserExeption(String.format(invalidGameTypeMessage, gameDescriptor.getGameType()));
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
