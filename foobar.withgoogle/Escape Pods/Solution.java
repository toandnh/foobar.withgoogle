import java.util.Deque;
import java.util.ArrayDeque;
import java.lang.Math;

public class Solution {
    static final int INFINITY = Integer.MAX_VALUE;
    
    public static int solution(int[] entrances, int[] exits, int[][] path) {
        int[][] graph = makeGraph(entrances, exits, path);
        return maxFlowPreflowFIFOSelection(0, graph.length - 1, graph);
    }
    
    public static int[][] makeGraph(int[] entrances, int[] exits, int[][] path) {
        int[][] graph = new int[path.length + 2][path.length + 2];
        //create super source.
        for (int i = 0; i < entrances.length; i++) {
            graph[0][entrances[i] + 1] = INFINITY;
        }
        //create super sink.
        for (int i = 0; i < exits.length; i++) {
            graph[exits[i] + 1][graph.length - 1] = INFINITY;
        }
        //move all other elements to graph.
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path.length; j++) {
                graph[i + 1][j + 1] = path[i][j];
            }
        }
        return graph;
    }
    
    public static int maxFlowPreflowFIFOSelection(int source, int sink, int[][] capacity) {
        int n = capacity.length;
        int[] height = new int[n];
        height[source] = n;
        
        int[][] preflow = new int[n][n];
        int[] excess = new int[n];
        
        Deque<Integer> queue = new ArrayDeque<Integer>();
        for (int i = 0; i < n; i++) {
            preflow[source][i] = capacity[source][i];
            preflow[i][source] = -capacity[source][i];
            excess[i] = capacity[source][i];
            //e = (u, w) - if u == s and w != t, then add w to queue. 
            if (i != sink && capacity[source][i] != 0) {queue.add(i);}
        }
        
        while (queue.size() != 0) {
            int i = queue.removeFirst();
            //e(v) > 0, r(v, w) > 0 and d(v) = d(w) + 1.
            for (int j = 0; j < n && excess[i] > 0; j++) {
                if (capacity[i][j] - preflow[i][j] > 0 && height[i] == height[j] + 1) {
                    //push operation.
                    int delta = Math.min(capacity[i][j] - preflow[i][j], excess[i]);
                    preflow[i][j] += delta;
                    preflow[j][i] -= delta;
                    excess[i] -= delta;
                    excess[j] += delta;
                    //w != s,t and w not in queue, then add w to queue.
                    if (j != source && j != sink && !queue.contains(j)) {queue.add(j);}
                }
            }
            if (excess[i] > 0) {
                //relabel operation.
                height[i] = INFINITY;
                //h(v) = min(h(w) + 1 | (v, w) is an edge in E).
                for (int j = 0; j < n; j++) {
                    if (capacity[i][j] - preflow[i][j] > 0 && height[i] > height[j] + 1) {
                        height[i] = height[j] + 1;
                    }
                }
                queue.add(i);
            }
        }
        
        //max flow is the sum of flows out of source.
        int flow = 0;
        for (int i = 0; i < n; i++) {
            flow += preflow[source][i];
        }
        return flow;
    }
    
    public static void main(String args[]) {
        int[] entrances = {0,1};
        int[] exits = {4,5};
        int[][] path = {{0,0,4,6,0,0},{0,0,5,2,0,0},{0,0,0,0,4,4},{0,0,0,0,6,6},{0,0,0,0,0,0},{0,0,0,0,0,0}};//16
        
        /*int[] entrances = {0};
        int[] exits = {3};
        int[][] path = {{0,7,0,0},{0,0,6,0},{0,0,0,8},{9,0,0,0}};//6*/
        
        System.out.println(solution(entrances, exits, path));
    }
}