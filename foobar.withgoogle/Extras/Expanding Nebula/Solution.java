import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Solution {
    public static int solution(boolean[][] g) {
        //in the form [a, b], with a is the integer representation of the right column,
        //and b is the number of previous columns that connect to this one.
        Map<Integer, Integer> grid = new HashMap<Integer, Integer>();
        //a temp variable for storing valid values in the current iteration.
        Map<Integer, Integer> tempGrid;
        //int the form [a, b], with a is the integer representation of the left column,
        //and b is of the right.
        Map<Integer, List<Integer>> cols;
        
        for (int i = 0; i < g[0].length; i++) {
            //add possible columns that transform to first column.
            if (i == 0) {//execute once.
                for (List<Integer> list : generateCols(g, i)) {
                    if (grid.containsKey(list.get(1))) {
                        //increase the count and skip this iteration.
                        grid.replace(list.get(1), grid.get(list.get(1)) + 1);
                        continue;
                    }
                    grid.put(list.get(1), 1);
                }
                continue;
            }
            
            //from the second iteration.
            cols = new HashMap<Integer, List<Integer>>();
            for (List<Integer> list : generateCols(g, i)) {
                if (cols.containsKey(list.get(0))) {
                    //add to the list and skip.
                    cols.get(list.get(0)).add(list.get(1));
                    continue;
                }
                cols.put(list.get(0), new ArrayList<Integer>(){{add(list.get(1));}});
            }
            //keep only the columns that can link to the next column.
            tempGrid = new HashMap<Integer, Integer>();
            for (int key : grid.keySet()) {
                if (cols.containsKey(key)) {//can match.
                    for (int value : cols.get(key)) {
                        //use the values in cols as new keys for the next iterations;
                        //and calculate the count to this value so far.
                        if (tempGrid.containsKey(value)) {
                            tempGrid.replace(value, tempGrid.get(value) + grid.get(key));
                            continue;
                        }
                        tempGrid.put(value, grid.get(key));
                    }
                }
            }
            //assign the grid to the newly computed tempGrid.
            grid = tempGrid;
        }
        
        //result is the sum values in the map.
        int result = 0;
        for (int key : grid.keySet()) {
            result += grid.get(key);
        }
        
        return result;
    }
    
    /*Given:
     *boolean[][] grid: an nxm matrix.
     *int indexCol: the index of the column in given matrix.
     *return: all possible columns that transform into the given column;
     *the column will be in the form [a, b] - with a and b are integer representation of each column in nx2 matrix that transform into this column, 
     *this is because a 2x2 block transforms into a cell.
    */
    public static List<List<Integer>> generateCols(boolean[][] grid, int indexCol) {
        //key is the second row of the (i*2)x2 block, value is [a, b] -
        //with a and b are integer representation of the first and second columns of the block, respectively.
        Map<Integer, List<List<Integer>>> cols = new HashMap<Integer, List<List<Integer>>>();
        //a temp variable for storing valid values in the current iteration.
        Map<Integer, List<List<Integer>>> tempCols;
        //key is the second row of the 2x2 block, value is [a, b] -
        //with a and b are integer representation of the first and second columns of the block, respectively.
        Map<Integer, List<Integer>> blocks;
        
        for (int i = 0; i < grid.length; i++) {
            //add possible blocks for first cell of the column.
            if (i == 0) {//execute once.
                for (List<Integer> list : getBlocks(grid[i][indexCol])) {
                    List<Integer> tempList = new ArrayList<Integer>(){{add(list.get(2)); add(list.get(3));}};
                    if (cols.containsKey(list.get(1))) {
                        cols.get(list.get(1)).add(tempList);
                        continue;
                    }
                    cols.put(list.get(1), new ArrayList<List<Integer>>(){{add(tempList);}});
                }
                continue;
            }
            
            //from second iteration.
            //get all possible blocks corresponding to this cell.
            blocks = new HashMap<Integer, List<Integer>>();
            for (List<Integer> list : getBlocks(grid[i][indexCol])) {
                if (blocks.containsKey(list.get(0))) {
                    blocks.get(list.get(0)).add(list.get(1));
                    continue;
                }
                blocks.put(list.get(0), new ArrayList<Integer>(){{add(list.get(1));}});
            }
            //keep only the blocks that can link to the next cell.
            tempCols = new HashMap<Integer, List<List<Integer>>>();
            for (int key : cols.keySet()) {
                if (blocks.containsKey(key)) {//can match.
                    for (List<Integer> values : cols.get(key)) {
                        for (int value : blocks.get(key)) {
                            //key: use the value of the current 2x2 block as the key for next iteration;
                            //value: and update the value of cols by shift bit left and add {0, 1} to the end,
                            //store the above key-value pair in tempCols as valid values for this iteration.
                            List<Integer> tempList = updatedValues(values, value);
                            if (tempCols.containsKey(value)) {
                                tempCols.get(value).add(tempList);
                                continue;
                            }
                            tempCols.put(value, new ArrayList<List<Integer>>(){{add(tempList);}});
                        }
                    }
                }
            }
            //assign tempCols to cols.
            cols = tempCols;
        }
        
        //flatten cols.values().
        List<List<Integer>> colsList = new ArrayList<List<Integer>>();
        for (List<List<Integer>> listList : cols.values()) {
            for (List<Integer> list : listList) {
                colsList.add(list);
            }
        }
        
        return colsList;
    }
    
    /*Given:
     *List<Integer> values: [a, b] - integer representations of columns.
     *int value: the integer representation of the second row in 2x2 block.
     *return: [a << 1 + {0, 1}, b << 1 + {0, 1}].
    */
    public static List<Integer> updatedValues(List<Integer> values, int value) {
        List<Integer> results = new ArrayList<Integer>();
        
        //shift bit left 1 place and add the value to it.
        switch (value) {
            case 0://0 0.
                results.add(values.get(0) * 2);
                results.add(values.get(1) * 2);
                break;
            case 1://0 1.
                results.add(values.get(0) * 2);
                results.add(values.get(1) * 2 + 1);
                break;
            case 2://1 0.
                results.add(values.get(0) * 2 + 1);
                results.add(values.get(1) * 2);
                break;
            case 3://1 1.
                results.add(values.get(0) * 2 + 1);
                results.add(values.get(1) * 2 + 1);
                break;
        }
        
        return results;
    }
    
    /*Given:
     *boolean bool: {true, false}.
     *return the list of possible blocks that has gas if true; 
     *the list of possible blocks that does not have gas otherwise.
    */
    public static List<List<Integer>> getBlocks(boolean bool) {
        return bool ? HAS_GAS_BLOCK_LIST : EMPTY_BLOCK_LIST;
    }
    
    /*All possible 2x2 block that transform into a has gas cell in the form - {a, b, c, d}.
     *a and b are the integer representation of the first and second row of the block, respectively;
     *c and d are the integer representation of the first and second column of the block, respectively.
    */
    protected final static List<List<Integer>> HAS_GAS_BLOCK_LIST = List.of(
                                                                        List.of(0, 1, 0, 1),//0 0 0 1.
                                                                        List.of(0, 2, 1, 0),//0 0 1 0.
                                                                        List.of(1, 0, 0, 2),//0 1 0 0.
                                                                        List.of(2, 0, 2, 0) //1 0 0 0.
                                                                    );
    /*Similar with the above list but for empty cells.*/
    protected final static List<List<Integer>> EMPTY_BLOCK_LIST = List.of(
                                                                        List.of(0, 0, 0, 0),//0 0 0 0.
                                                                        List.of(3, 3, 3, 3),//1 1 1 1.
                                                                        List.of(3, 0, 2, 2),//1 1 0 0.
                                                                        List.of(2, 2, 3, 0),//1 0 1 0.
                                                                        List.of(2, 1, 2, 1),//1 0 0 1.
                                                                        List.of(1, 2, 1, 2),//0 1 1 0.
                                                                        List.of(1, 1, 0, 3),//0 1 0 1.
                                                                        List.of(0, 3, 1, 1),//0 0 1 1.
                                                                        List.of(3, 2, 3, 2),//1 1 1 0.
                                                                        List.of(3, 1, 2, 3),//1 1 0 1.
                                                                        List.of(2, 3, 3, 1),//1 0 1 1.
                                                                        List.of(1, 3, 1, 3) //0 1 1 1.
                                                                    );
                                                                    
    public static void main(String args[]) {
        //boolean[][] grid = new boolean[][]{new boolean[]{true, false, true}, new boolean[]{false, true, false}, new boolean[]{true, false, true}};
        //boolean[][] grid = new boolean[][]{new boolean[]{true, false, true, false, false, true, true, true}, new boolean[]{true, false, true, false, false, false, true, false}, new boolean[]{true, true, true, false, false, false, true, false}, new boolean[]{true, false, true, false, false, false, true, false}, new boolean[]{true, false, true, false, false, true, true, true}};
        boolean[][] grid = new boolean[][]{new boolean[]{true, true, false, true, false, true, false, true, true, false}, new boolean[]{true, true, false, false, false, false, true, true, true, false}, new boolean[]{true, true, false, false, false, false, false, false, false, true}, new boolean[]{false, true, false, false, false, false, true, true, false, false}};
        
        System.out.print(solution(grid));
    }
}