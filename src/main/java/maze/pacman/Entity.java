package maze.pacman;

import java.util.ArrayList;

class Entity {
    private int[] position;

    public Entity(int[] position) {
        this.position = position;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
    public void moveUp(ArrayList<ArrayList<Integer>> maze, int[] entityPos) {
        int newRow = position[0] - 1;
        int newCol = position[1];
        if (newRow >= 0 && maze.get(newRow).get(newCol) != 1 && (newRow != entityPos[0] || newCol != entityPos[1])) {
            position[0] = newRow;
        }
    }

    public void moveDown(ArrayList<ArrayList<Integer>> maze, int[] entityPos) {
        int newRow = position[0] + 1;
        int newCol = position[1];
        if (newRow < maze.size() && maze.get(newRow).get(newCol) != 1 && (newRow != entityPos[0] || newCol != entityPos[1])) {
            position[0] = newRow;
        }
    }

    public void moveLeft(ArrayList<ArrayList<Integer>> maze, int[] entityPos) {
        int newRow = position[0];
        int newCol = position[1] - 1;
        if (newCol >= 0 && maze.get(newRow).get(newCol) != 1 && (newRow != entityPos[0] || newCol != entityPos[1])) {
            position[1] = newCol;
        }
    }

    public void moveRight(ArrayList<ArrayList<Integer>> maze, int[] entityPos) {
        int newRow = position[0];
        int newCol = position[1] + 1;
        if (newCol < maze.get(0).size() && maze.get(newRow).get(newCol) != 1 && (newRow != entityPos[0] || newCol != entityPos[1])) {
            position[1] = newCol;
        }
    }

}
