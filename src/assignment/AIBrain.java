package assignment;

import java.util.*;

import assignment.Board.Action;

/**
 * A Lame Brain implementation for JTetris; tries all possible places to put the
 * piece (but ignoring rotations, because we're lame), trying to minimize the
 * total height of pieces on the board.
 */
public class AIBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<LinkedList<Action>> moveOrder;
    private double score; 
    private double best;
    private int bestIndex;
    private boolean end = true;
    private LinkedList<Action> currentOrder;
    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Action nextMove(Board currentBoard) {
        if (end) {
            end = false;
            // Fill the our options array with versions of the new Board
            options = new ArrayList<Board>();
            moveOrder = new ArrayList<LinkedList<Action>>();
            enumerateOptions(currentBoard);
            best = Integer.MIN_VALUE;
            bestIndex = 0;
            // Check all of the options and get the one with the highest score
            for (int i = 0; i < options.size(); i++) {
                score = scoreBoard(options.get(i));
                if (score > best) {
                    best = score;
                    bestIndex = i;
                }
            }
            currentOrder = moveOrder.get(bestIndex);
        }

        // We want to return the first move on the way to the best Board
        
        System.out.println("bestindex: " + bestIndex);

        Action res = currentOrder.remove();
        System.out.println(res.toString());

        if (currentOrder.size() == 0)
            end = true;
        
        return res;
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {
        // We can always drop our current Piece
        LinkedList<Action> actions = new LinkedList<>();

        options.add(currentBoard.testMove(Action.DROP));
        actions.add(Action.DROP);
        moveOrder.add(copyActions(actions));
        
        // actions = new LinkedList<>();
        // Board newBoard = currentBoard.testMove(Action.LEFT);
        //  while (newBoard.getLastResult() == Board.Result.SUCCESS) {
        //         LinkedList<Action> tempActions = copyActions(actions);

        //         options.add(newBoard.testMove(Board.Action.DROP));
        //         tempActions.add(Action.DROP);
        //         moveOrder.add(tempActions);

        //         newBoard.move(Action.LEFT);
        //         actions.add(Action.LEFT);
        //     }

        //     actions = new LinkedList<>();
        //  newBoard = currentBoard.testMove(Action.RIGHT);
        // while (newBoard.getLastResult() == Board.Result.SUCCESS) {
        //         LinkedList<Action> tempActions = copyActions(actions);

        //         options.add(newBoard.testMove(Board.Action.DROP));
        //         tempActions.add(Action.DROP);
        //         moveOrder.add(tempActions);
                
        //         newBoard.move(Action.RIGHT);
        //         actions.add(Action.RIGHT);
        //     }

        Board newBoard = currentBoard.testMove(Action.NOTHING);
        LinkedList<Action> startingActions = new LinkedList<>();
        for (int i = 0; i < 4; i++) { // for each rotation
            actions = new LinkedList<>(startingActions);

            Board left = newBoard.testMove(Board.Action.NOTHING);
            while (left.getLastResult() == Board.Result.SUCCESS) {
                LinkedList<Action> tempActions = copyActions(actions);

                options.add(left.testMove(Board.Action.DROP));
                tempActions.add(Action.DROP);
                moveOrder.add(tempActions);

                left.move(Action.LEFT);
                actions.add(Action.LEFT);
            }
            newBoard = newBoard.testMove(Action.CLOCKWISE);
            startingActions.add(Action.CLOCKWISE);
        }
    
        newBoard = currentBoard.testMove(Action.NOTHING);
        startingActions = new LinkedList<>();
        for (int i = 0; i < 4; i++) { // for each rotation
            actions = new LinkedList<>(startingActions);
            
            Board right = newBoard.testMove(Board.Action.NOTHING);
            while (right.getLastResult() == Board.Result.SUCCESS) {
                LinkedList<Action> tempActions = copyActions(actions);

                options.add(right.testMove(Board.Action.DROP));
                tempActions.add(Action.DROP);
                moveOrder.add(tempActions);
                
                right.move(Action.RIGHT);
                actions.add(Action.RIGHT);
            }
            newBoard = newBoard.testMove(Action.CLOCKWISE);
            startingActions.add(Action.CLOCKWISE);
        }
    }

    private static final double xHeightMax = -5.71;
   // private static final double xHeightSum = -3.71;
    private static final double perClear = 63.87; // 60.87
    private static final double perHole = -8.70; //-8.79
    // private static final double perBlockade = 1.4;
    //private static final double perEdge = 8.5; // incorrect 4.8
    private static final double perWall = .92;
    private static final double perFloor = 6.7;
   // private static final double xHeightDifference = -6;
   // private static final double xHeightStdDev = -8.5; // incorrect -3.5
    private static final double xBumpiness = -.48;
    private static final double xRowWidthDifference = -4;
    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private double scoreBoard(Board newBoard) {
        int sumOfHeights = calculateSumOfHeights(newBoard);
        int numClears = newBoard.getRowsCleared();
        int numHoles = calculateNumHoles(newBoard);
        // int numBlockades = calculateNumBlockades(newBoard);
        int numEdgeTouches = calculateNumEdgeTouches(newBoard);
        int numWallTouches = calculateNumWallTouches(newBoard);
        int numFloorTouches = calculateNumFloorTouches(newBoard);
        int heightDifference = calculateDifferenceHeights(newBoard);
        int maxHeight = newBoard.getMaxHeight();
        double stdDevHeight = Math.abs(calculateStdDevOfHeights(newBoard));
        int bumpiness = calculateBumpiness(newBoard);
        int rowWidthDifference = calculateRowWidthDifferences(newBoard);
        
// + perBlockade * numBlockades + //perEdge * numEdgeTouches +
        // return xHeightSum * sumOfHeights + perClear * numClears + perHole * numHoles +  perWall * numWallTouches + perFloor*numFloorTouches;
        // return (8888- 3 * sumOfHeights) + perClear * numClears + (100 - 3.8 * numHoles) + perClear * numClears + perEdge * numEdgeTouches ;
        // return xHeightStdDev *stdDevHeight + xHeightMax * maxHeight + xHeightDifference * heightDifference + perClear * numClears + perHole * numHoles + perFloor*numFloorTouches;
        // return xHeightStdDev * stdDevHeight + xHeightSum * sumOfHeights + perClear * numClears + perFloor*numFloorTouches;
        // return xHeightSum * sumOfHeights;
        return maxHeight * xHeightMax + xBumpiness * bumpiness + perClear * numClears + perFloor * numFloorTouches + rowWidthDifference * xRowWidthDifference + numHoles * perHole + numWallTouches * perWall;
        // return maxHeight * xHeightMax +  xHeightStdDev * stdDevHeight + xBumpiness * bumpiness + perClear * numClears + perFloor * numFloorTouches + rowWidthDifference * xRowWidthDifference + numHoles * perHole + numWallTouches * perWall;
        // return 100 - (newBoard.getMaxHeight() * 5);
    }

    private int calculateRowWidthDifferences(Board newBoard) {
        int differenceSum = 0;
        for (int i = 0; i < newBoard.getHeight()-1; i++) {
            differenceSum += Math.abs(newBoard.getRowWidth(i+1) - newBoard.getRowWidth(i));
        }
        return differenceSum;
    }

    private int calculateNumHoles(Board newBoard) {
        int res = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            for (int j = 0; j < newBoard.getHeight() -1; j++) {
                if (newBoard.getGrid(i, j) == null && j < newBoard.getColumnHeight(i) - 1)
                    res++;
            }
        }
        return res; 
    }

    private int calculateSumOfHeights(Board newBoard) {
        int sum = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            sum += newBoard.getColumnHeight(i);
        }
        return sum; 
    }

    private double calculateStdDevOfHeights(Board newBoard) {
        double sum = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            sum += newBoard.getColumnHeight(i);
        }
        double avg = sum / newBoard.getWidth();
        double stddevsum = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            stddevsum += newBoard.getColumnHeight(i) - avg;
        }
        return Math.sqrt(stddevsum);
    }

    private int calculateBumpiness(Board newBoard) {
        int bumpiness = 0; 
        for (int i = 0; i < newBoard.getWidth() -1; i++) {
            bumpiness += Math.pow(newBoard.getColumnHeight(i+1)- newBoard.getColumnHeight(i), 2);
        }
        return bumpiness;
    }

    private int calculateDifferenceHeights(Board newBoard) {
        int minHeight = newBoard.getHeight();
        int maxHeight = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            minHeight = Math.min(minHeight, newBoard.getColumnHeight(i));
            maxHeight = Math.max(maxHeight, newBoard.getColumnHeight(i));
        }
        return maxHeight - minHeight;
    }

    private int calculateNumEdgeTouches(Board newBoard) {
        int res = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            for (int j = 0; j < newBoard.getHeight(); j++) {
                if (i-1 >= 0 && newBoard.getGrid(i-1, j) != null)
                    res++;
                if (i-1 >= 0 && j-1 >= 0 && newBoard.getGrid(i-1, j-1) != null) 
                    res++;
                 if (j-1 >= 0 && newBoard.getGrid(i, j-1) != null) 
                    res++;
                if (i+1 < newBoard.getWidth() && newBoard.getGrid(i+1, j) != null)
                    res++;
                if (i+1 < newBoard.getWidth() && j+1 < newBoard.getHeight() && newBoard.getGrid(i+1, j+1) != null)
                    res++;
            }
        }
        return res; 
    }

    private int calculateNumWallTouches(Board newBoard) {
        int res = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            for (int j = 0; j < newBoard.getHeight(); j++) {
                if (newBoard.getGrid(i, j) != null && i-1 == 0)
                    res++;
                if (newBoard.getGrid(i, j) != null && i+1 == newBoard.getWidth())
                    res++;
            }
        }
        return res; 
    }

    private int calculateNumFloorTouches(Board newBoard) {
        int res = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            for (int j = 0; j < newBoard.getHeight(); j++) {
                if (newBoard.getGrid(i, j) != null && j == 0)
                    res++;
            }
        }
        return res; 
    }

    LinkedList<Action> copyActions(LinkedList<Action> a) {
        LinkedList<Action> newActions = new LinkedList<>();
        
        for (Action action : a) {
            newActions.add(action);
        }
        return newActions;
    }
}
