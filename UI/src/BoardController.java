
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Game.Game;
import Game.BoardSquare;
import Game.GameBoard;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.util.List;
import java.awt.*;


/**
 * Created by amitaihandler on 9/12/16.
 */

public class BoardController {

    private static final int SQUARE_SIZE = 20;
    private static int COLUMNS;
    private static int ROWS;
    private Game game;
    private Square[][] boardGrid;
    private StackPane root;
    private List<Square> selectedSquares;

    private class Square extends StackPane{
        private int row, col;
        private BoardSquare squareState;
        private boolean isSelected;
        private Rectangle border = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        private Text white = new Text();

        public Square(int x, int y)
        {
            this.row = x;
            this.col = y;
            this.squareState = BoardSquare.Empty;
            this.isSelected = false;

            border.setStroke(Color.BLUEVIOLET);
            border.setFill(Color.WHITE);

            white.setText("X");
            white.setFont(Font.font("font-family: sans",16));
            white.setVisible(false);

            getChildren().addAll(border, white);

            setTranslateX(row * SQUARE_SIZE);
            setTranslateY(col * SQUARE_SIZE);

            setOnMouseEntered(e -> hover());
            setOnMouseExited(e -> noHover());
            setOnMouseClicked(e -> selected());
        }

        public void hover()
        {
            border.setStroke(Color.RED);
            this.toFront();
        }

        public void noHover()
        {
            border.setStroke(Color.BLUEVIOLET);
        }

        public void selected()
        {
            border.setFill(Color.YELLOW);
            isSelected = true;
            selectedSquares.add(this);
        }

        public void unSelect()
        {
            changeState(this.squareState);
        }

        public void changeState(BoardSquare newState)
        {
            this.squareState = newState;
            switch (newState)
            {
                case Black:
                    border.setFill(Color.BLACK);
                    break;
                case White:
                    border.setFill(Color.WHITE);
                    this.white.setVisible(true);
                    break;
                case Empty:
                    border.setFill(Color.WHITE);
                    this.white.setVisible(false);
                    break;
                default:
                    break;
            }
        }



    }

    public BoardController(StackPane e, Game i_game)
    {
        this.root = e;
        this.game = i_game;
        this.COLUMNS = game.getGameBoard().getColumns();
        this.ROWS = game.getGameBoard().getRows();
        this.boardGrid = new Square[ROWS][COLUMNS];
        root.getChildren().add(createBoardUI());
    }

    public List<Square> getSelectedSquares()
    {
        return this.selectedSquares;
    }

    private Parent createBoardUI()
    {
        Pane boardNode = new Pane();
        boardNode.setPrefSize(ROWS * SQUARE_SIZE, COLUMNS * SQUARE_SIZE);
        for (int y = 0; y < COLUMNS; y++) {
            for (int x = 0; x < ROWS; x++) {
                Square square = new Square(x, y);
                boardGrid[x][y] = square;
                boardNode.getChildren().add(square);
            }
        }
        return boardNode;
    }


    public Node getBoardUI(Game game) {
        /*
        Pane newBoardNode = new Pane();
        newBoardNode.setPrefSize(ROWS * SQUARE_SIZE, COLUMNS * SQUARE_SIZE);
        GameBoard currentGameBoard = game.getGameBoard();
        for (int y = 0; y < COLUMNS; y++) {
            for (int x = 0; x < ROWS; x++) {
                boardGrid[x][y].changeState(currentGameBoard.getBoardSquare(x,y));
            }
        }
        root.getChildren().add(newBoardNode);
        */
        return root;
    }
}
