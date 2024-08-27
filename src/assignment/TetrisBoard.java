package assignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import assignment.Board.Result;
import assignment.Piece.PieceType;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
    int width;
    int height;
    PieceType[][] grid;
    Piece currentPiece;
    Point currentPiecePosition;
    int[] currentPieceSkirt;
    int rowsCleared;
    Result lastResult;
    Action lastAction;
    int[] rowWidths;
    int[] columnHeights;
    int maxHeight;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.lastAction = Action.NOTHING;
        this.lastResult = Result.NO_PIECE;
        this.width = width;
        this.height = height;
        this.rowWidths = new int[height];
        this.columnHeights = new int[width];
        grid = new PieceType[width][height];
    }

    @Override
    public Result move(Action act) { 
        lastAction = act;
        switch (act) {
            case LEFT: 
                for (Point p : currentPiece.getBody()) {
                    if (currentPiecePosition.x + p.x - 1 < 0 || getGrid(currentPiecePosition.x + p.x - 1, currentPiecePosition.y + p.y) != null) {
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                            
                }
                currentPiecePosition.setLocation(currentPiecePosition.x-1, currentPiecePosition.y);
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case RIGHT:
                for (Point p : currentPiece.getBody()) {
                    if (currentPiecePosition.x + p.x + 1 >= width || getGrid(currentPiecePosition.x + p.x + 1, currentPiecePosition.y + p.y) != null) { 
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                }
                currentPiecePosition.setLocation(currentPiecePosition.x+1, currentPiecePosition.y);
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case DOWN:
                // int lowestPoint = Integer.MAX_VALUE;
                // for (int p : currentPieceSkirt) {
                //     lowestPoint = Math.min(p, lowestPoint);
                // }    
                boolean place = false;
                for (int i  = 0; i < currentPiece.getSkirt().length; i++) {
                    // if the location below the lowest point is below the bottom or if the piecetype at position x, y+
                    if (currentPiece.getSkirt()[i] == Integer.MAX_VALUE)
                        continue;
                    if (currentPiecePosition.y + currentPiece.getSkirt()[i] - 1 < 0 || getGrid(currentPiecePosition.x+i, currentPiecePosition.y + currentPiece.getSkirt()[i] - 1) != null){
                        place = true;
                    }
                }
                if (place) {
                    currentPiecePosition.setLocation(currentPiecePosition.x, currentPiecePosition.y);
                    for (Point p : currentPiece.getBody()) {
                        rowWidths[currentPiecePosition.y + p.y]++;
                        columnHeights[currentPiecePosition.x + p.x] = currentPiecePosition.y + p.y;
                        grid[currentPiecePosition.x + p.x][currentPiecePosition.y + p.y] = currentPiece.getType();
                    }

                    updateMaxHeight();
                    updateColumnHeights();
                    clearRows();

                    lastResult = Result.PLACE;
                    return Result.PLACE; 
                }
                else {
                    currentPiecePosition.setLocation(currentPiecePosition.x, currentPiecePosition.y-1);
                    lastResult = Result.SUCCESS;
                    return Result.SUCCESS;
                }
            case DROP:
                currentPiecePosition.setLocation(currentPiecePosition.x, dropHeight(currentPiece, currentPiecePosition.x, currentPiecePosition.y));
                for (Point p : currentPiece.getBody()) {
                    rowWidths[currentPiecePosition.y + p.y]++;
                    columnHeights[currentPiecePosition.x + p.x] = currentPiecePosition.y + p.y;
                    grid[currentPiecePosition.x + p.x][currentPiecePosition.y + p.y] = currentPiece.getType();
                }

                updateMaxHeight();
                updateColumnHeights();
                clearRows();

                lastResult = Result.PLACE;
                return Result.PLACE; 
            
            case CLOCKWISE:
                Piece tempPiece = currentPiece.clockwisePiece();
               
                for (Point point : tempPiece.getBody()) { // checks if this temporary rotation is out of bounds
                    if (currentPiecePosition.x + point.x >= width || currentPiecePosition.x + point.x < 0 || 
                        currentPiecePosition.y + point.y >= height || currentPiecePosition.y + point.y < 0 || 
                            getGrid(currentPiecePosition.x + point.x, currentPiecePosition.y + point.y) != null) {
                                
                            if (performsCWKick() == Result.OUT_BOUNDS) {
                                lastAction = Action.NOTHING;
                                lastResult = Result.OUT_BOUNDS;
                                return Result.OUT_BOUNDS;
                            }
                            else break;
                      }
                }

                // rotation was successful
                currentPiece = tempPiece;
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case COUNTERCLOCKWISE:
                // todo: literally clockwise but replace variables
                Piece tempPiece2 = currentPiece.counterclockwisePiece();
                for (Point point : tempPiece2.getBody()) { // checks if this temporary rotation is out of bounds
                    if (currentPiecePosition.x + point.x >= width || currentPiecePosition.x + point.x < 0 || 
                        currentPiecePosition.y + point.y >= height || currentPiecePosition.y + point.y < 0 || 
                            getGrid(currentPiecePosition.x + point.x, currentPiecePosition.y + point.y) != null) {
                                
                            if (performsCCWKick() == Result.OUT_BOUNDS) {
                                currentPiece = currentPiece.counterclockwisePiece();
                                lastAction = Action.NOTHING;
                                lastResult = Result.OUT_BOUNDS;
                                return Result.OUT_BOUNDS;
                            }
                            else break;
                      }
                }

                // rotation was successful
                currentPiece = tempPiece2;
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case NOTHING: 
                lastResult = Result.SUCCESS;
                return Result.SUCCESS; 
            default:
                lastResult = Result.NO_PIECE;
                return Result.NO_PIECE;      
        }
    }

    void updateMaxHeight() {
        int max = 0;
        for (int h = 0; h < height; h++) {
            boolean notEmpty = false;
            for (int x = 0; x < width; x++) {
                if (getGrid(x, h) != null) {
                    notEmpty = true;
                    break;
                }
            }
            if (notEmpty)
                max = h;
            else 
                break;
        }
        maxHeight = max;
    }

    void updateColumnHeights() {
        for (int i = 0; i < width; i++) {
            int a = 0;
            for (int j = height -2; j >= 0; j--) {
                if (getGrid(i, j) != null) {
                    a = j;
                    break;
                }
            }
            
            columnHeights[i] = a;
        }
    }

    void clearRows() {
        ArrayList<Integer> rowsToBeCleared = getFullRows();
        rowsCleared = rowsToBeCleared.size();
        int counter = 0;
        for (int h : rowsToBeCleared) {
            h -= counter;
            // clear the row
            for (int x = 0; x < width; x++) {
                grid[x][h] = null;
                //move rows above down
                for (int i = h; i < height; i++) {
                    if (getRowWidth(i) == 0)
                        break;
                    grid[x][i] = grid[x][i+1];
                }
            }
            // update columnHeights
            updateColumnHeights();
            
            // update rowWidths
            for (int i = h; i < rowWidths.length-1; i++) {
                rowWidths[i] = rowWidths[i+1];
            }
            counter++;
        }
    }

    ArrayList<Integer> getFullRows() {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int h = 0; h < getMaxHeight(); h++) {
            boolean full = true;
            for (int i = 0; i < width; i++) {
                if (getGrid(i, h) == null) {
                    full = false;
                    break;
                }
            }
            if (full)
                rows.add(h);
        }
        return rows;
    }

    boolean validKick(int x, int y, Piece tempPiece) {
        for (Point p : tempPiece.getBody()) {
            int x1 = p.x+x, y1 = p.y+y;
            if (currentPiecePosition.x + x1 >= width || currentPiecePosition.x + x1 < 0 || 
                        currentPiecePosition.y + y1 >= height || currentPiecePosition.y + y1 < 0 || 
                            getGrid(currentPiecePosition.x + x1, currentPiecePosition.y + y1) != null)
                            return false;
        }
        return true;
    }

    public Result performsCWKick(){
        Point[] kicks;
        if(currentPiece.getType() == PieceType.STICK){
            kicks = Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
        }
        else{
            kicks = Piece.NORMAL_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
        }
        for(Point point: kicks){
            if (validKick(point.x, point.y, currentPiece.clockwisePiece())) {
                currentPiecePosition.setLocation(currentPiecePosition.x + point.x, currentPiecePosition.y + point.y);
                currentPiece = currentPiece.clockwisePiece();
                return Result.SUCCESS;
            }            
        }
        return Result.OUT_BOUNDS;
    }

    public Result performsCCWKick(){
        Point[] kicks;
        if(currentPiece.getType() == PieceType.STICK){
            kicks = Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
        }
        else{
            kicks = Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
        }
        for(Point point: kicks){
            if (validKick(point.x, point.y, currentPiece.counterclockwisePiece())) {
                currentPiecePosition.setLocation(currentPiecePosition.x + point.x, currentPiecePosition.y + point.y);
                currentPiece = currentPiece.counterclockwisePiece();
                return Result.SUCCESS;
            }            
        }
        return Result.OUT_BOUNDS;
    }
    @Override
    public Board testMove(Action act) { 
        TetrisBoard testBoard = new TetrisBoard(width, height);
        testBoard.grid = new PieceType[width][];
        for(int i = 0; i < testBoard.grid.length; i++)
             testBoard.grid[i] = Arrays.copyOf(this.grid[i], this.grid[i].length);

        testBoard.currentPiece = this.currentPiece;
        testBoard.currentPiecePosition = new Point(this.currentPiecePosition);
        testBoard.currentPieceSkirt = this.currentPieceSkirt;
        testBoard.move(act);

        return testBoard; 
    }

    @Override
    public Piece getCurrentPiece() { 
        return currentPiece; 
    }

    @Override
    public Point getCurrentPiecePosition() { 
        return currentPiecePosition; 
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        for (Point point : p.getBody()) {
            // checks if any coordinate of piece is out of bounds or collides with pieces on the board
            if (spawnPosition.x + point.x > width || spawnPosition.x + point.x < 0 || 
                    spawnPosition.y + point.y > height || spawnPosition.y + point.y < 0 || 
                        getGrid(spawnPosition.x + point.x, spawnPosition.y + point.y) != null)
                throw new IllegalArgumentException();
        }
        currentPiece = p;
        currentPiecePosition = spawnPosition;
        currentPieceSkirt = p.getSkirt();
    }

    @Override
    public boolean equals(Object other) { 
        if(!(other instanceof TetrisBoard)) 
            return false;
        TetrisBoard otherBoard = (TetrisBoard) other;
        return (otherBoard.grid == grid && otherBoard.currentPiece == currentPiece && otherBoard.currentPiecePosition == currentPiecePosition);
     }

    @Override
    public Result getLastResult() { 
        return lastResult; 
    }

    @Override
    public Action getLastAction() { 
        return lastAction; 
    }

    @Override
    public int getRowsCleared() { 
        return rowsCleared; 
    }

    @Override
    public int getWidth() { 
        return width;
     }

    @Override
    public int getHeight() { 
        return height;
     }

    @Override 
    public int getMaxHeight() {
        return maxHeight;
     }

    @Override 
    public int dropHeight(Piece piece, int x) { 
        int res = Integer.MAX_VALUE;
        for (int p : piece.getSkirt()) {
            res = Math.min(p, res);
        } 
        res *= -1; 
        outer:
        for (int i = height-2; i >= -3; i--) {
            for (int j = 0; j < piece.getSkirt().length; j++) {
                if (piece.getSkirt()[j] == Integer.MAX_VALUE)
                    continue;
                if (i + piece.getSkirt()[j] - 1 < 0 || getGrid(x+j, i + piece.getSkirt()[j] - 1) != null){
                    res = i ;
                    break outer;
                }
            }
        }
        
        return res;
     }

    public int dropHeight(Piece piece, int x, int y) { 
        int res = Integer.MAX_VALUE;
        for (int p : piece.getSkirt()) {
            res = Math.min(p, res);
        } 
        res *= -1; 
        outer:
        for (int i = y-1; i >= -3; i--) {
            for (int j = 0; j < piece.getSkirt().length; j++) {
                if (piece.getSkirt()[j] == Integer.MAX_VALUE)
                    continue;
                if (i + piece.getSkirt()[j] - 1 < 0 || getGrid(x+j, i + piece.getSkirt()[j] - 1) != null){
                    res = i;
                    break outer;
                }
            }
        }
        return res;
     }


    @Override
    public int getColumnHeight(int x) {
        return columnHeights[x];
    }

    @Override
    public int getRowWidth(int y) { 
        return rowWidths[y];
     }

    @Override
    public Piece.PieceType getGrid(int x, int y) { 
        return grid[x][y]; 
    }
}
