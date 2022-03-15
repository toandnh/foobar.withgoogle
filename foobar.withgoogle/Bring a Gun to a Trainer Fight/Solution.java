import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Solution {
    public static int solution(int[] dimensions, int[] your_position, int[] trainer_position, int distance) {
        /*Algorithm:
         *Find all the reflections of your_position and trainer_position within distance radius.
         *Filter the beams by bearing, if more than one has the same bearing, choose the one with the shortest distance to your_position.
         *Filter the beams that hit any of your_position's reflection before hitting trainer's reflection.
        */
        
        //trivial cases.
        if (distance(your_position, trainer_position) > (double) distance) {
            return 0;
        } else if (distance(your_position, trainer_position) == (double) distance) {
            return 1;
        }
        
        //store x and y values of the trainer and its reflections.
        List<Integer> xTrainer = new ArrayList<Integer>();
        List<Integer> yTrainer = new ArrayList<Integer>();
        getReflections(dimensions, your_position, trainer_position, distance, xTrainer, yTrainer);
        
        //store x and y values of the you and you's reflections.
        List<Integer> xYou = new ArrayList<Integer>();
        List<Integer> yYou = new ArrayList<Integer>();
        getReflections(dimensions, your_position, your_position, distance, xYou, yYou);
        
        int xT, yT;
        double bearing;
        Map<Double, Integer> distinctDir = new HashMap<Double, Integer>();
        //iterate through the trainer's locations arrays.
        for (int i = 0; i < xTrainer.size(); i++) {
            //this point cannot be reached, move to the next iteration.
            if (distance(your_position, new int[]{xTrainer.get(i), yTrainer.get(i)}) > distance) {continue;}
            //calculate x and y with your_position as the origin.
            xT = xTrainer.get(i) - your_position[0];
            yT = yTrainer.get(i) - your_position[1];
            bearing = getBearing(xT, yT);
            if (!distinctDir.containsKey(bearing)) {
                //getReflections() were performed in a way that for the same bearing, 
                //bearing vector (x, y) will have a shorter distance to the origin than
                //bearing vector (x', y') if index of (x, y) < index of (x', y') in xTrainer and yTrainer.
                distinctDir.put(bearing, i);
            }
        }
        
        //no need to filter vertical or horizontal shots;
        //since trainer's location is reflected to find possible shots,
        //if a trainer's reflection has the same x or y coordinate with your_position,
        //then the beam to that trainer's reflection has to go through your_position's
        //reflection first.
        //same logic can be applied with shots going through corners.
        int xY, yY, index;
        //iterate through the you locations arrays; skipping the first position, since it is the original you.
        for (int i = 1; i < xYou.size(); i++) {
            //this point is either cannot be reached or cannot affect the result.
            if (distance(your_position, new int[]{xYou.get(i), yYou.get(i)}) >= distance) {continue;}
            xY = xYou.get(i) - your_position[0];
            yY = yYou.get(i) - your_position[1];
            bearing = getBearing(xY, yY);
            if (distinctDir.containsKey(bearing)) {
                index = distinctDir.get(bearing);
                //the reflection of the trainer with the same bearing as the self reflection being checked.
                xT = xTrainer.get(index) - your_position[0];
                yT = yTrainer.get(index) - your_position[1];
                //the self reflection is strictly between your_position and the trainer's reflection;
                //meaning the beam will hit self before hitting the trainer.
                if (distance(new int[]{0, 0}, new int[]{xY, yY}) < distance(new int[]{0, 0}, new int[]{xT, yT})) {
                    distinctDir.remove(bearing);//reduce number of distinct directions by one.
                }
            }
        }
        return distinctDir.size();
    }
    
    /*Given 2 cartesian coordinates:
     *int[] a: an array size 2 - [xA, yA].
     *int[] b: an array size 2 - [xB, yB].
     *return the euclidean distance between a and b. 
    */
    public static double distance(int[] a, int[] b) {
        //sqrt((y1 - y0)^2 + (x1 - x0)^2).
        return Math.sqrt(Math.pow((b[1] - a[1]), 2) + Math.pow(b[0] - a[0], 2));
    }
    
    /*Given 2 integers:
     *int a: x coordinate in the xy plane with your_position is the origin.
     *int b: y coordinate in the xy plane with your_position is the origin.
     *return the angle from the north to the line (0, 0) - (a, b).
    */
    public static double getBearing(int a, int b) {
        //returns the angle theta from the conversion of rectangular coordinates (x, y) 
        //to polar coordinates (r, theta).
        return Math.atan2((double) a, (double) b);
    }
    
    /*Given:
     *int[] dimensions: an integer array of size 2 - [x, y], with x, y are the height and width of the room.
     *int[] first_position: an integer array of size 2 - [x1, y1], with x1, y1 are the coordinate on an xy plane.
     *int[] second_position: an integer array of size 2 - [x2, y2], with x2, y2 are the coordinate on an xy plane.
     *int distance: the maximum travel distance of the beam.
     *List<Integer> xPos: an empty list to store all possible x coordinates of the reflections of second_position.
     *List<Integer> yPos: an empty list to store all possible y coordinates of the reflections of second_position.
     *add all possible (x, y) of second_position's reflections to xPos and yPos.
    */
    public static void getReflections(int[] dimensions, int[] first_position, int[] second_position, int distance, List<Integer> xPos, List<Integer> yPos) {
        //temp to store location of the reflections.
        int xTemp, yTemp;
        //the number of times second_position can be reflected 
        //and still stays within distance from first_location.
        int xRange = (first_position[0] + distance) / dimensions[0];
        int yRange = (first_position[1] + distance) / dimensions[1];
        //find all the reflections' locations within distance.
        yTemp = second_position[1];
        for (int i = 0; i <= yRange; i++) {//along y axis.
            xTemp = second_position[0];
            if (i != 0) {//from the second i iteration.
                if (i % 2 == 0) {//odd number of times reflected.
                    yTemp += 2 * second_position[1];
                } else {//even number of times reflected.
                    yTemp += 2 * (dimensions[1] - second_position[1]);  
                }
            }
            for (int j = 0; j <= xRange; j++) {//along x axis.
                //get the reflections in all 4 quadrant.
                //upper right quadrant.
                xPos.add(xTemp);
                yPos.add(yTemp);
                //lower right quadrant.
                xPos.add(xTemp);
                yPos.add(-yTemp);
                //lower left quadrant.
                xPos.add(-xTemp);
                yPos.add(-yTemp);
                //upper left quadrant.
                xPos.add(-xTemp);
                yPos.add(yTemp);
                
                if (j % 2 == 0) {//even number of times reflected.
                    xTemp += 2 * (dimensions[0] - second_position[0]);   
                } else {//odd number of times reflected.
                    xTemp += 2 * second_position[0];
                }
            }
        }
    }
    
    public static void main(String args[]) {
        int[] dimensions = {2,5};
        int[] your_position = {1,2};
        int[] trainer_position = {1,4};
        int distance = 11;//27
        
        /*int[] dimensions = {42,59};
        int[] your_position = {34,44};
        int[] trainer_position = {6,34};
        int distance = 5000;//30904*/
        
        /*int[] dimensions = {4,4};
        int[] your_position = {3,3};
        int[] trainer_position = {1,1};
        int distance = 6;//??*/
        
        /*int[] dimensions = {10,10};
        int[] your_position = {4,4};
        int[] trainer_position = {3,3};
        int distance = 5000;//739323*/
        
        /*int[] dimensions = {23,10};
        int[] your_position = {6,4};
        int[] trainer_position = {3,2};
        int distance = 23;//8*/
        
        /*int[] dimensions = {1250,1250};
        int[] your_position = {1000,1000};
        int[] trainer_position = {500,400};
        int distance = 10000;//196*/
        
        /*int[] dimensions = {3,2};
        int[] your_position = {1,1};
        int[] trainer_position = {2,1};
        int distance = 4;//7*/
        
        /*int[] dimensions = {300,275};
        int[] your_position = {150,150};
        int[] trainer_position = {185,100};
        int distance = 500;//9*/
        
        System.out.println(solution(dimensions, your_position, trainer_position, distance));
    }
}