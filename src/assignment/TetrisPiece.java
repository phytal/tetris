package assignment;

import java.awt.*;
import java.util.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {
    PieceType type;
    Point[] body;
    ArrayList<int[]> skirts;
    int width;
    int height;
    int rotationIndex;
    ArrayList<Point[]> rotations;

    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) {
        body = type.getSpawnBody();
        this.type = type;
        rotationIndex = 0;
        skirts = new ArrayList<>();

        Dimension boundingBox = type.getBoundingBox();
        
        width = (int) boundingBox.getWidth();
        height = (int) boundingBox.getHeight();

        // add rotations into arraylist
        generateRotations(body);

        // generates skirts for each rotation
        for (Point[] body : rotations) {
            TreeMap<Integer, ArrayList<Integer>> map = new TreeMap<>();
            for (Point p : body) {
                if (!map.containsKey(p.x))
                    map.put(p.x, new ArrayList<>());
                map.get(p.x).add(p.y);
            }

            int[] skirt = new int[width];

            // sorts the arraylist to get the smallest y-value for each x-value (skirt)
            for (int i = 0; i < width; i++) {
                ArrayList<Integer> keySet = new ArrayList<>(map.keySet());
                if (!keySet.contains(i)) {
                    skirt[i] = Integer.MAX_VALUE;
                    continue;
                }
                map.get(i).sort(null);
                skirt[i] = map.get(i).get(0);
            }

            skirts.add(skirt);
        }
    }

    private TetrisPiece(PieceType type, int rotationIndex, ArrayList<int[]>skirts, ArrayList<Point[]>rotations){
        body = type.getSpawnBody();
        this.type = type;
        this.rotationIndex = rotationIndex;

        Dimension boundingBox = type.getBoundingBox();
        
        width = (int) boundingBox.getWidth();
        height = (int) boundingBox.getHeight();

        this.skirts = skirts;
        this.rotations = rotations;
    }

    void generateRotations(Point[] points) {
        rotations = new ArrayList<Point[]>();
        rotations.add(body);

        for (int i = 0; i < 3; i++) {
            Point[] rotation = new Point[body.length];
            for (int j = 0; j < rotation.length; j++) {
                rotation[j] = new Point((int)rotations.get(i)[j].getY(), (int)(width - rotations.get(i)[j].getX()-1));
            }
            rotations.add(rotation);
        }
    }

    @Override
    public PieceType getType() {
        return type;
    }

    @Override
    public int getRotationIndex() {
        return rotationIndex;
    }

    @Override
    public Piece clockwisePiece() {
        return new TetrisPiece(type, (rotationIndex+1)%4, skirts, rotations);
    }

    @Override
    public Piece counterclockwisePiece() {
        return new TetrisPiece(type, (rotationIndex+3)%4, skirts, rotations);
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
    public Point[] getBody() {
        return rotations.get(rotationIndex);
    }

    @Override
    public int[] getSkirt() {
        return skirts.get(rotationIndex);
    }

    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        return this.rotationIndex == otherPiece.rotationIndex && this.type == otherPiece.type;
    }
}
