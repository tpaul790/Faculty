package utils;

import domain.User;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Utils {
    public static void dfsIterative(int s, int[][] matrix, boolean[] visited) {
        Stack<Integer> stack = new Stack<>();
        int n = matrix.length;
        stack.push(s);
        while (!stack.isEmpty()) {
            s = stack.pop();
            visited[s] = true;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && matrix[s][i] == 1) {
                    stack.push(i);
                }
            }
        }
    }

    public static int numberOfConnectedComponents(int[][] matrix) {
        int n = matrix.length;
        boolean[] visited = new boolean[n];
        int rez = 0;
        for (int i = 0; i < n; i++) {
            if(!visited[i]) {
                dfsIterative(i, matrix, visited);
                rez++;
            }
        }
        return rez;
    }



}
