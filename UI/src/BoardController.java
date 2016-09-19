
import Game.BoardSquare;
import Game.Game;
import Game.GameBoard;
import GameXmlParser.Schema.Constraint;
import GameXmlParser.Schema.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by amitaihandler on 9/12/16.
 */

public class BoardController {

    private static final int SQUARE_SIZE = 20;
    private static int COLUMNS;
    private static int ROWS;
    private final List<Constraints> rowConstraints;
    private final List<Constraints> columnConstraints;
    private Square[][] boardGrid;
    private ConstraintSquare[][] columnConstraintsGrid;
    private ConstraintSquare[][] rowConstraintsGrid;
    private Parent root;
    private List<Square> selectedSquares;
    private int maxColConstraints;
    private int maxRowConstraints;


    public class ConstraintSquare extends StackPane {
        private int row, col;
        private Rectangle border = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        private Text constraint = new Text();
        private boolean isColumnConstraint;
        private boolean isPerfect;

        public ConstraintSquare(int x, int y, boolean isColumnConstraint, int i_constraint) {
            this.row = x;
            this.col = y;
            this.isColumnConstraint = isColumnConstraint;
            this.isPerfect = false;

            border.setFill(Color.GRAY);

            if (isColumnConstraint)
                this.setStyle("-fx-border-color: BLUEVIOLET; -fx-border-width: 0 1 0 1;");
            else
                this.setStyle("-fx-border-color: BLUEVIOLET; -fx-border-width: 1 0 1 0;");


            if (i_constraint > 0)
                this.constraint.setText(Integer.toString(i_constraint));
            else
                this.constraint.setText("");
            constraint.setVisible(true);
            constraint.setFont(Font.font("font-family: sans", 11));

            getChildren().addAll(border, constraint);
            setTranslateX(col * SQUARE_SIZE);
            setTranslateY(row * SQUARE_SIZE);
        }

        public void setConstraint(int i_constraint) {
            this.constraint.setText(Integer.toString(i_constraint));
        }
    }

    public class Square extends StackPane {
        private int row, col;
        private BoardSquare squareState;
        private boolean isSelected;
        private Rectangle border = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        private Text white = new Text();

        public Square(int x, int y) {
            this.row = x;
            this.col = y;
            this.squareState = BoardSquare.Empty;
            this.isSelected = false;

            border.setStroke(Color.BLUEVIOLET);
            border.setFill(Color.WHITE);

            white.setText("X");
            white.setFont(Font.font("font-family: sans", 18));
            white.setVisible(false);

            getChildren().addAll(border, white);

            setTranslateX(col * SQUARE_SIZE);
            setTranslateY(row * SQUARE_SIZE);

            setOnMouseEntered(e -> hover());
            setOnMouseExited(e -> noHover());
            setOnMouseClicked(e -> selected());
        }

        public int getRow() {
            return this.row;
        }

        public int getCol() {
            return this.col;
        }

        public void hover() {
            border.setStroke(Color.RED);
            this.toFront();
        }

        public void noHover() {
            border.setStroke(Color.BLUEVIOLET);
        }

        public void selected() {
            if (!this.isSelected) {
                border.setFill(Color.YELLOW);
                isSelected = true;
                selectedSquares.add(this);
                this.white.setVisible(false);
            } else {
                this.unSelect();
            }
        }

        public void unSelect() {
            this.isSelected = false;
            changeState(this.squareState);
            selectedSquares.remove(selectedSquares.indexOf(this));
        }


        public void changeState(BoardSquare newState) {
            this.squareState = newState;
            this.isSelected = false;
            switch (newState) {
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

    public BoardController(Game i_game) {
        this.COLUMNS = i_game.getGameBoard().getColumns();
        this.ROWS = i_game.getGameBoard().getRows();
        this.boardGrid = new Square[ROWS][COLUMNS];
        maxColConstraints = i_game.getMaxColumnConstraints();
        maxRowConstraints = i_game.getMaxRowConstraints();
        rowConstraints = i_game.getRowConstraints();
        columnConstraints = i_game.getColumnConstraints();
        this.root = createBoardUI();
        this.selectedSquares = new ArrayList<>();
    }


    public ObservableList<Square> getSelectedSquares() {
        ObservableList<Square> list = FXCollections.observableList(selectedSquares);
        return list;
    }

    private Parent createBoardUI() {
        Pane boardNode = new Pane();
        Pane columnConstraintsNode = new Pane();
        Pane rowConstraintsNode = new Pane();


        boardNode.setPrefSize((COLUMNS + maxRowConstraints) * SQUARE_SIZE, (ROWS + maxColConstraints) * SQUARE_SIZE);
        for (int y = 0; y < COLUMNS; y++) {
            for (int x = 0; x < ROWS; x++) {
                Square square = new Square(x, y);
                boardGrid[x][y] = square;
                boardNode.getChildren().add(square);
            }
        }

        this.columnConstraintsGrid = new ConstraintSquare[maxColConstraints][COLUMNS];
        columnConstraintsNode.setPrefSize(ROWS * SQUARE_SIZE, maxColConstraints * SQUARE_SIZE);
        for (int y = 0; y < COLUMNS; y++) {
            for (int x = 0; x < maxColConstraints; x++) {
                ConstraintSquare colSquare = new ConstraintSquare(x, y, true, 0);
                columnConstraintsGrid[x][y] = colSquare;
                columnConstraintsNode.getChildren().add(colSquare);
            }
        }
        columnConstraintsNode.setTranslateY(-maxColConstraints * SQUARE_SIZE);

        for (int y = 0; y < COLUMNS; y++) {
            int x = maxColConstraints - 1;
            List<Constraint> constraintList = columnConstraints.get(y).getConstraints();
            for (ListIterator<Constraint> iterator = constraintList.listIterator(constraintList.size()); iterator.hasPrevious(); ) {
                Constraint constraint = iterator.previous();
                columnConstraintsGrid[x][y].setConstraint(constraint.getConstraint());
                x--;
            }
        }

        this.rowConstraintsGrid = new ConstraintSquare[ROWS][maxRowConstraints];
        rowConstraintsNode.setPrefSize(maxRowConstraints * SQUARE_SIZE, COLUMNS * SQUARE_SIZE);
        for (int y = 0; y < maxRowConstraints; y++) {
            for (int x = 0; x < ROWS; x++) {
                ConstraintSquare rowSquare = new ConstraintSquare(x, y, false, 0);
                rowConstraintsGrid[x][y] = rowSquare;
                rowConstraintsNode.getChildren().add(rowSquare);
            }
        }
        rowConstraintsNode.setTranslateX(-maxRowConstraints * SQUARE_SIZE);
        rowConstraintsNode.setTranslateY(1);

        for (int x = 0; x < ROWS; x++) {
            int y = maxRowConstraints - 1;
            List<Constraint> constraintList = rowConstraints.get(x).getConstraints();
            for (ListIterator<Constraint> iterator = constraintList.listIterator(constraintList.size()); iterator.hasPrevious(); ) {
                Constraint constraint = iterator.previous();
                rowConstraintsGrid[x][y].setConstraint(constraint.getConstraint());
                System.out.println(String.format("row: %d, column:%d", x, y));
                y--;
            }
        }


        boardNode.getChildren().addAll(rowConstraintsNode, columnConstraintsNode);
        boardNode.setTranslateX((maxRowConstraints + 3) * SQUARE_SIZE);
        boardNode.setTranslateY((maxColConstraints + 3) * SQUARE_SIZE);
        return boardNode;
    }


    public void redrawBoardUI(Game game) {
        GameBoard currentGameBoard = game.getGameBoard();
        for (int y = 0; y < COLUMNS; y++) {
            for (int x = 0; x < ROWS; x++) {
                boardGrid[x][y].changeState(currentGameBoard.getBoardSquare(x, y));
            }
        }
    }


    public Node getBoardUI(GameBoard gameBoard) {
        GameBoard currentGameBoard = gameBoard;
        for (int y = 0; y < COLUMNS; y++) {
            for (int x = 0; x < ROWS; x++) {
                boardGrid[x][y].changeState(currentGameBoard.getBoardSquare(x, y));
            }
        }
        return root;
    }

    public void unSelectAllSquares() {
        for (Iterator<Square> iterator = selectedSquares.iterator(); iterator.hasNext(); ) {
            Square square = iterator.next();
            square.unSelect();
            iterator.remove();
        }
    }
}
