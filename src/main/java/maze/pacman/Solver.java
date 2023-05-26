package maze.pacman;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Solver {
    private static final int[] entitySTART = {1, 0};
    private int[] end;
    private int[] playerSTART;
    private ArrayList<ArrayList<Integer>> maze;

    public Solver() {
    }

    public int[] getEnd() {
        return end;
    }

    public int[] getPlayerSTART() {
        return playerSTART;
    }

    public void setEnd(int[] end) {
        this.end = end;
    }

    public ArrayList<ArrayList<Integer>> getMaze() {
        return maze;
    }

    public void setMaze(ArrayList<ArrayList<Integer>> maze) {
        this.maze = maze;
    }

    public void setMaze(Scanner scanner) {
        // Khởi tạo ArrayList để lưu trữ ma trận
        this.maze = new ArrayList<>();

        // Đọc từng dòng và tách các giá trị bằng khoảng trắng
        while (scanner.hasNextLine()) {
            ArrayList<Integer> lineList = new ArrayList<>();
            String line = scanner.nextLine();
            String[] values = line.split("\\s+");

            // Lưu trữ các giá trị vào ma trận
            for (String value : values) {
                int intValue = Integer.parseInt(value);
                lineList.add(intValue);
                if (intValue == 3) {
                    end = new int[]{this.maze.size(), lineList.size() - 1};
                }
                if (intValue == -1) {
                    playerSTART = new int[]{this.maze.size(), lineList.size() - 1};
                }
            }
            this.maze.add(lineList);
        }
    }

    // Tìm đường đi từ vị trí start đến vị trí end bằng thuật toán DFS
    public ArrayList<int[]> dfs() {
        int[][] visited = new int[maze.size()][maze.get(0).size()];
        Stack<int[]> stack = new Stack<>();
        stack.push(entitySTART);

        ArrayList<int[]> path = new ArrayList<>();
        path.add(entitySTART);
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];
            visited[x][y] = 1;
            if (x == end[0] && y == end[1]) {
                for (int[] m : path)
//                    System.out.println(m[0] + " " + m[1]);
                // Tìm thấy đích
                return path;
            }
            boolean have_way = false;

            // Tìm các ô lân cận chưa được thăm
            if (x > 0 && maze.get(x - 1).get(y) != 1 && visited[x - 1][y] == 0 && !have_way) {
                int[] neighbor = new int[] {x-1,y};
                stack.push(neighbor);
                path.add(neighbor);
                have_way = true;
            }
            if (y > 0 && maze.get(x).get(y - 1) != 1 && visited[x][y - 1] == 0 && !have_way) {
                int[] neighbor = new int[] {x,y-1};
                stack.push(neighbor);
                path.add(neighbor);
                have_way = true;
            }
            if (x < maze.size() - 1 && maze.get(x + 1).get(y) != 1 && visited[x + 1][y] == 0 && !have_way) {
                int[] neighbor = new int[] {x+1,y};
                path.add(neighbor);
                stack.push(neighbor);
                have_way = true;
            }
            if (y < maze.get(0).size() - 1 && maze.get(x).get(y + 1) != 1 && visited[x][y + 1] == 0 && !have_way) {
                int[] neighbor = new int[] {x,y+1};
                path.add(neighbor);
                stack.push(neighbor);
                have_way = true;
            }

            int[] last_path = path.get(path.size()-1);
            if(!have_way) {
                stack.pop();
                path.add(stack.peek());
            }

        }

        return null;
    }

}

