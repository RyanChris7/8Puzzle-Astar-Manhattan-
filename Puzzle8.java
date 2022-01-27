import java.util.*;
import java.io.*;

public class Puzzle8{

    public static void main(String args[])throws Exception{
        Integer [][]initial = new Integer[3][3];
        List<State> closedList = new ArrayList<>();
        PriorityQueue<State> openList = new PriorityQueue<State>(1, new Comparator<State>() {   //Priority queue for holding unexpanded states
            @Override
            public int compare(State first, State second) {     //overridden compare to remove the element with lowest f(n) cost first
                if(first.getF()>second.getF()){
                    return 1;
                }
                else
                    return -1;
            }
        });
        initial = getInitialStates(args[0]);                           //handles illegal character checking
        Integer[][] goal = {{1,2,3},{4,5,6},{7,8,0}};

        if(isSolvable(initial,goal)){                       // Check if instance is solvable based on parity of arranged tiles/blocks.
            State initialState = new State(initial);
            State goalState = new State(goal);
            initialState.setG(0);
            initialState.setF(initialState.calculateF(initialState.getG(),goalState.getTilePositions()));
            System.out.println();

            openList.add(initialState);                     //initial state added to OpenList
            while(!openList.isEmpty()) {
                State currentState = openList.poll();       //poll will return the state with lowest f(n)
                if (currentState.equals(goalState)) {
                    closedList.add(currentState);
                    currentState.printFinalPath(args[1]);
                    break;
                }
                else {
                    closedList.add(currentState);       //add to expanded list i.e closedList
                    if (currentState.isMovableLeft()) {
                        State successor = new State(currentState);  //currentState will be assigned as the parent
                        successor = successor.moveLeft(currentState);
                        successor.setG(successor.getParentState().getG()+1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {      //checking for already expanded states.
                            openList.add(successor);
                        }
                    }
                    if (currentState.isMovableRight()) {
                        State successor = new State(currentState);
                        successor = successor.moveRight(currentState);
                        successor.setG(successor.getParentState().getG() + 1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {
                            openList.add(successor);
                        }
                    }
                    if (currentState.isMovableUpwards()) {
                        State successor = new State(currentState);
                        successor = successor.moveUp(currentState);
                        successor.setG(successor.getParentState().getG()+1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {
                            openList.add(successor);
                        }
                    }
                    if (currentState.isMovableDownwards()) {
                        State successor = new State(currentState);
                        successor = successor.moveDown(currentState);
                        successor.setG(successor.getParentState().getG()+1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {
                            openList.add(successor);
                        }
                    }
                }
            }
        }
        else{
            System.out.println("The Given initial configuration is not solvable for the current Goal State");
        }
    }

    public static Integer[][] getInitialStates(String fileName) throws Exception{
        BufferedReader inputFile = new BufferedReader(new FileReader(fileName));

        String inputLine = new String();
        List<Character> currentList = new ArrayList<Character>();

        while((inputLine = inputFile.readLine()) != null){
            char[] currentArray = inputLine.toCharArray();
            for(char i : currentArray){
                currentList.add(i);
            }
        }
        inputFile.close();

        Integer[] puzzle = new Integer[9];
        int n = 0;
        for(char i : currentList){
            if(Character.isDigit(i)){
                puzzle[n] = (Integer.parseInt(String.valueOf(i)));
                n++;
            }
        }
        Integer[][] finalPuzzle = new Integer[3][3];
        n = 0;
        for(Integer i = 0; i < 3; i++){
            for(Integer j = 0; j < 3; j++){
                finalPuzzle[i][j] = puzzle[n];
                n++;
            }
        }
        return finalPuzzle;
    }

    public static boolean isSolvable(Integer[][] initial, Integer[][] goal){
    //return true if solvable else false
        boolean solvable = false;
        int initialInversions = getInversions(initial);
        int goalInversions = getInversions(goal);
        if(initialInversions % 2 == goalInversions % 2){
            solvable = true;
        }
        return solvable;
    }

    public static int getInversions(Integer[][] puzzle){
        int inversions = 0;
        int count=0;
        int[] temp = new int[9];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(puzzle[i][j]==0){
                    continue;
                }
                else{
                    temp[count++] = puzzle[i][j];
                }
            }
        }
        for(int i=0;i<9;i++){
            for(int j=i+1;j<9;j++){
                if(temp[i]>temp[j]){
                    inversions++;
                }
            }
        }

        return inversions;
    }
}