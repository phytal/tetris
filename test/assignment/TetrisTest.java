
package assignment;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import java.awt.Point;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import assignment.Board.Result;
import assignment.Piece.PieceType;

/*

* Any comments and methods here are purely descriptions or suggestions.

* This is your test file. Feel free to change this as much as you want.

*/

public class TetrisTest implements Board {

    PieceType type;
    Point[] body;
    ArrayList<int[]> skirts;
    int width;
    int height;
    int rotationIndex;
    ArrayList<Point[]> rotations;

    TetrisPiece testT = new TetrisPiece(Piece.PieceType.T);
    TetrisPiece testSquare = new TetrisPiece(Piece.PieceType.SQUARE);
    TetrisPiece testStick = new TetrisPiece(Piece.PieceType.STICK);
    TetrisPiece testLeft_L = new TetrisPiece(Piece.PieceType.LEFT_L);
    TetrisPiece testRight_L = new TetrisPiece(Piece.PieceType.RIGHT_L);
    TetrisPiece testLeft_DOG = new TetrisPiece(Piece.PieceType.LEFT_DOG);
    TetrisPiece testRight_DOG = new TetrisPiece(Piece.PieceType.RIGHT_DOG);

    // This will run ONCE before all other tests. It can be useful to setup up

    // global variables and anything needed for all of the tests.

    @BeforeAll
    static void setupAll() {

    }

    // This will run before EACH test.

    @BeforeEach
    void setupEach() {

    }

    @Test
    void testClockWiseRotations() {

        assertArrayEquals(new Point[] { new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(2, 1) },
                testT.clockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(0, 1), new Point(1, 1), new Point(0, 0), new Point(1, 0) },
                testSquare.clockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(2, 3), new Point(2, 2), new Point(2, 1), new Point(2, 0) },
                testStick.clockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(2, 2) },
                testLeft_L.clockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(2, 0) },
                testRight_L.clockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(2, 2), new Point(2, 1), new Point(1, 1), new Point(1, 0) },
                testLeft_DOG.clockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 2), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
                testRight_DOG.clockwisePiece().getBody());

    }

    @Test
    void testSkirt() {
        assertArrayEquals(new int[] { 1, 1, 1 }, testT.getSkirt());
        assertArrayEquals(new int[] { 1, 1, 1 }, testLeft_L.getSkirt());
        assertArrayEquals(new int[] { 1, 1, 1 }, testRight_L.getSkirt());
        assertArrayEquals(new int[] { 2, 1, 1 }, testLeft_DOG.getSkirt());
        assertArrayEquals(new int[] { 1, 1, 2 }, testRight_DOG.getSkirt());
        assertArrayEquals(new int[] { 2, 2, 2, 2 }, testStick.getSkirt());
        assertArrayEquals(new int[] { 0, 0 }, testSquare.getSkirt());
    }

    @Test
    void testCounterClockWiseRotations() {
        assertArrayEquals(new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 1) },
                testT.counterclockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 0), new Point(0, 0), new Point(1, 1), new Point(0, 1) },
                testSquare.counterclockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
                testStick.counterclockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) },
                testLeft_L.counterclockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
                testRight_L.counterclockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
                testLeft_DOG.counterclockwisePiece().getBody());

        assertArrayEquals(new Point[] { new Point(1, 0), new Point(1, 1), new Point(0, 1), new Point(0, 2) },
                testRight_DOG.counterclockwisePiece().getBody());

    }

    // Test load species. You may want to make more tests for different cases here.

    @Test
    void testTetrisBoard() {

    }

    @Test
    void testMoveDown() {

        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        for (int i = 0; i < 4; i++) {

            assertEquals(Result.SUCCESS, tempBoard.move(Action.DOWN));

        }

    }

    @Test
    void testRight() {

        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        for (int i = 0; i < 3; i++) {

            assertEquals(Result.SUCCESS, tempBoard.move(Action.RIGHT));

        }

    }

    @Test

    void testLeft() {

        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        for (int i = 0; i < 2; i++) {

            assertEquals(Result.SUCCESS, tempBoard.move(Action.LEFT));

        }

    }

    @Test
    void leftOutofBounds() {

        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        assertEquals(Result.SUCCESS, tempBoard.move(Action.LEFT));

        assertEquals(Result.SUCCESS, tempBoard.move(Action.LEFT));

        assertEquals(Result.OUT_BOUNDS, tempBoard.move(Action.LEFT));

    }

    @Test
    void rightOutofBounds() {

        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        for (int i = 0; i < 5; i++) {

            assertEquals(Result.SUCCESS, tempBoard.move(Action.RIGHT));

        }

        assertEquals(Result.OUT_BOUNDS, tempBoard.move(Action.RIGHT));

    }

    @Test
    void collisionWithOtherPieces() {
        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.SQUARE);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 10);

        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        TetrisPiece currentPiece1 = new TetrisPiece(PieceType.SQUARE);
        Point currentPiecePosition1 = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 10);

        tempBoard.nextPiece(currentPiece1, currentPiecePosition1);

        tempBoard.grid[3][8] = PieceType.STICK;
        tempBoard.grid[3][9] = PieceType.STICK;
        tempBoard.grid[3][10] = PieceType.STICK;
        tempBoard.grid[3][11] = PieceType.STICK;
        tempBoard.grid[3][12] = PieceType.STICK;
        tempBoard.grid[5][8] = PieceType.STICK;
        tempBoard.grid[5][9] = PieceType.STICK;
        tempBoard.grid[5][10] = PieceType.STICK;
        tempBoard.grid[5][11] = PieceType.STICK;
        tempBoard.grid[5][12] = PieceType.STICK;

        assertEquals(Result.OUT_BOUNDS, tempBoard.move(Action.LEFT));

        assertEquals(Result.OUT_BOUNDS, tempBoard.move(Action.RIGHT));

    }

    @Test
    void rotationSkirtCheck() {
        assertArrayEquals(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE}, testStick.clockwisePiece().getSkirt());
        assertArrayEquals(new int[] {Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE}, testStick.counterclockwisePiece().getSkirt());
        assertArrayEquals(new int[] {1, 1, 1, 1}, testStick.counterclockwisePiece().counterclockwisePiece().getSkirt());
    }

    @Test
    void rowClear() {
        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.SQUARE);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 10);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        for (int i = 0; i < width; i++) {
            tempBoard.grid[3][i] = PieceType.STICK;
        }

        tempBoard.clearRows();

        for (int i = 0; i < width; i++) {
            assertEquals(null, tempBoard.grid[3][i]);
        }

        // multi-row clears

        for (int i = 0; i < width; i++) {
            tempBoard.grid[3][i] = PieceType.STICK;
            tempBoard.grid[5][i] = PieceType.SQUARE;
            tempBoard.grid[8][i] = PieceType.LEFT_DOG;
        }

        tempBoard.clearRows();

        for (int i = 0; i < width; i++) {
            assertEquals(null, tempBoard.grid[3][i]);
            assertEquals(null, tempBoard.grid[5][i]);
            assertEquals(null, tempBoard.grid[8][i]);
        }

    }

    @Test
    void archDrop() {
        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.SQUARE);
        Point currentPiecePosition = new Point(5, 3);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        tempBoard.grid[0][0] = PieceType.STICK;
        tempBoard.grid[1][0] = PieceType.STICK;
        tempBoard.grid[2][0] = PieceType.STICK;
        tempBoard.grid[3][0] = PieceType.STICK;
        tempBoard.grid[4][1] = PieceType.STICK;
        tempBoard.grid[3][2] = PieceType.STICK;
        tempBoard.grid[4][3] = PieceType.STICK;
        tempBoard.grid[5][4] = PieceType.STICK;
        tempBoard.grid[6][5] = PieceType.STICK;
        tempBoard.grid[7][6] = PieceType.STICK;
        tempBoard.grid[8][7] = PieceType.STICK;

        assertEquals(Result.PLACE, tempBoard.move(Action.DROP));

    }

    @Test
    void drop() {

        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        assertEquals(Result.PLACE, tempBoard.move(Action.DROP));

        TetrisPiece currentPiece1 = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition1 = new Point(tempBoard.getWidth() / 2 - currentPiece.getWidth(), 17);
        tempBoard.nextPiece(currentPiece1, currentPiecePosition1);

        assertEquals(Result.PLACE, tempBoard.move(Action.DROP));

    }

    @Test
    void wallCWKick() {

        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.STICK);
        Point currentPiecePosition = new Point(5, 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        assertEquals(Result.SUCCESS, tempBoard.performsCWKick());

        TetrisPiece currentPiece1 = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition1 = new Point(2, 12);

        tempBoard.nextPiece(currentPiece1, currentPiecePosition1);
        tempBoard.grid[3][8] = PieceType.SQUARE;
        tempBoard.grid[3][9] = PieceType.SQUARE;
        tempBoard.grid[3][10] = PieceType.SQUARE;
        tempBoard.grid[3][11] = PieceType.SQUARE;
        tempBoard.grid[3][12] = PieceType.SQUARE;

        assertEquals(Result.SUCCESS, tempBoard.performsCWKick());

    }

    @Test
    void wallCCWKick() {
        TetrisBoard tempBoard = boardCreation();
        TetrisPiece currentPiece = new TetrisPiece(PieceType.STICK);
        Point currentPiecePosition = new Point(0, 17);
        tempBoard.nextPiece(currentPiece, currentPiecePosition);

        assertEquals(Result.SUCCESS, tempBoard.performsCCWKick());

        TetrisPiece currentPiece1 = new TetrisPiece(PieceType.LEFT_DOG);
        Point currentPiecePosition1 = new Point(4, 12);
        tempBoard.nextPiece(currentPiece1, currentPiecePosition1);

        tempBoard.grid[3][8] = PieceType.SQUARE;
        tempBoard.grid[3][9] = PieceType.SQUARE;
        tempBoard.grid[3][10] = PieceType.SQUARE;
        tempBoard.grid[3][11] = PieceType.SQUARE;
        tempBoard.grid[3][12] = PieceType.SQUARE;

        assertEquals(Result.SUCCESS, tempBoard.performsCCWKick());

    }


    public TetrisBoard boardCreation() {
        int width = 10;
        int height = 20;
        PieceType[][] grid = new Piece.PieceType[width][height];
        TetrisPiece currentPiece = new TetrisPiece(PieceType.LEFT_DOG);

        Point currentPiecePosition = new Point(width / 2 - currentPiece.getWidth() / 2, height);

        int rowsCleared = 0;
        Result lastResult = Result.NO_PIECE;
        Action lastAction = Action.NOTHING;
        int[] rowWidths = new int[height];
        int[] columnHeights = new int[width];
        int maxHeight = 0;

        TetrisBoard tempBoard = new TetrisBoard(width, height);

        return tempBoard;

    }

    // Implementation of a fake board

    public boolean equals(Object other) {

        return false;

    }

    public Result getLastResult() {

        return null;

    }

    public Action getLastAction() {

        return null;

    }

    public int getRowsCleared() {

        return 0;

    }

    public int getWidth() {

        return 0;

    }

    public int getHeight() {

        return 0;

    }

    public int getMaxHeight() {

        return 0;

    }

    public int dropHeight(Piece piece, int x) {

        return 0;

    }

    public int getColumnHeight(int x) {

        return 0;

    }

    public int getRowWidth(int y) {

        return 0;

    }

    public Piece.PieceType getGrid(int x, int y) {

        return getGrid(x, y);

    }

    public Result move(Action act) {

        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'move'");

    }

    public Board testMove(Action act) {

        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'testMove'");

    }

    public Piece getCurrentPiece() {

        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPiece'");

    }

    public void nextPiece(Piece p, Point startingPosition) {

        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'nextPiece'");

    }

    @Override

    public Point getCurrentPiecePosition() {

        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPiecePosition'");

    }

}