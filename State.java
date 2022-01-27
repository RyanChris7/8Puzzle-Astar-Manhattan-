import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;

public class State {
    private Integer[][] tilePositions;              // denotes a 3*3 matrix of 8 puzzle problem
    private int g;                                  // the path cost
    private int h;                                  // the Heurstic (manhattan distance)
    private int f;                                  // evaluation function f(n) = g(n)+h(n)
    private State parentState;                      // parent of the current state(for final path tracking)

    public State(State parent){                     // Constructor where parent is sent as a parameter
        this.parentState = parent;
        this.g = getG();
        this.h = getH();
        this.f = getF();

    }

    public State(Integer[][] initialTilePosition){   // this is meant for the initial state. A clone of the input matrix is made
        this.parentState = null;
        this.tilePositions = new Integer[initialTilePosition.length][];
        for(int i=0;i<3;i++){
            this.tilePositions[i] = initialTilePosition[i].clone();
        }
    }

    public void printTilePositions(){               // method for printing the positions of the blocks/tiles
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                System.out.print(tilePositions[i][j] +" ");
            }
            System.out.println();
        }
    }

    public State returnState(){
        return this;
    }       // Returns the current state in the context

    public void setTilePositions(Integer[][] fromSet){   // method for setting the appropriate positions.
        this.tilePositions = new Integer[3][3];
        for(int i=0;i<3;i++){
            for(int j = 0;j<3;j++){
                this.tilePositions[i][j] = fromSet[i][j];
            }
        }
    }

    public int getG() {
        return g;
    }                   //getter method for cost path

    public void setG(int g) {                        //setter method for cost path
        this.g = g;
    }

    public int getH() {
        return h;
    }                   //getter method for heuristic

    public Integer[][] getTilePositions(){
        return this.tilePositions;
    }   //returns the current 8 puzzle configuration of blocks

    public int getF() {
        return f;
    }                  //getter method for evaluation function

    public void setF(int f) {
        this.f = f;
    }          //setter method for cost path

    public int calculateF(int pathCost,Integer[][]goalStateTilePositions){  //calculate f(n)
        int h = calculateH(goalStateTilePositions);
        f = pathCost + h;
        return f;
    }

    public Integer getXPosition(int number,Integer[][]goalStateTilePositions){  //returns the row position of given element
        int xPosition=-1;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(goalStateTilePositions[i][j]==number){
                    xPosition = i;
                }
            }
        }
        return xPosition;
    }

    public int getYPosition(int number,Integer[][]goalStateTilePositions){      //returns the column position of given element
        int yPosition=-1;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(goalStateTilePositions[i][j]==number){
                    yPosition = j;
                }
            }
        }
        return yPosition;
    }

    public int calculateH(Integer[][] goalStateTilePositions){

        int heuristic = 0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(this.tilePositions[i][j] == goalStateTilePositions[i][j]){   //check with goal state positions
                    //do nothing
                    continue;
                }
                else if(this.tilePositions[i][j]==0){                           //ignore 0
                    continue;
                }
                else{
                    int number = tilePositions[i][j];
                    int x = getXPosition(number,goalStateTilePositions);
                    int y = getYPosition(number,goalStateTilePositions);
                    heuristic+=Math.abs(i-x)+Math.abs(j-y);                   //Manhattan distance is difference in x,y positions  of tiles
                }
            }
        }
        return heuristic;
    }

    @Override
    public boolean equals(Object obj) {                       // positions of individual tiles checked for comparing 2 states.
        boolean flag= false;
        State comparingState = (State)obj;
        if (Arrays.deepEquals(this.tilePositions,comparingState.tilePositions)){
            flag = true;
        }
        else
            flag =  false;

        return flag;
    }

    public int getBlankXPosition(){                         //returns blank's row position in the matrix
        int xPosition = -1;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(this.tilePositions[i][j]==0){
                    xPosition = i;
                    break;
                }
            }
        }
        return xPosition;
    }

    public int getBlankYPosition(){                         //returns blank's row position in the matrix
        int YPosition = -1;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(this.tilePositions[i][j]==0){
                    YPosition = j;
                    break;
                }
            }
        }
        return YPosition;
    }

    public boolean isMovableUpwards(){                   //method to check if blank can be moved upwards in current config
        boolean upFlag = false;
        if(this.getBlankXPosition()!=0){
            upFlag = true;
        }
        else{
            upFlag = false;
        }
        return upFlag;

    }

    public boolean isMovableDownwards(){              //method to check if blank can be moved downwards in current config
        boolean downFlag = false;
        if(this.getBlankXPosition()!=2){
            downFlag = true;
        }
        else{
            downFlag = false;
        }
        return downFlag;
    }

    public boolean isMovableLeft(){                 //method to check if blank can be moved left in current config
        boolean leftFlag = false;
        if(this.getBlankYPosition()!=0){
            leftFlag = true;
        }
        else{
            leftFlag = false;
        }
        return leftFlag;
    }

    public boolean isMovableRight(){                //method to check if blank can be moved right in current config
        boolean rightFlag = false;
        if(this.getBlankYPosition()!=2){
            rightFlag = true;
        }
        else{
            rightFlag = false;
        }
        return rightFlag;
    }

    public State moveUp(State currentState){      //method to move blank upwards and swap it with previous element
        //move blank upwards
        State newState = new State(currentState);
        newState.setTilePositions(currentState.getTilePositions());
        int x = currentState.getBlankXPosition();
        int y = currentState.getBlankYPosition();
        int blank = currentState.tilePositions[x][y];    //saving 0's position
        newState.tilePositions[x][y] = currentState.tilePositions[x-1][y];
        newState.tilePositions[x-1][y] = blank;

        return newState;
    }

    public State moveDown(State currentState){      //method to move blank downwards and swap it with previous element
        State newState = new State(currentState);
        newState.setTilePositions(currentState.getTilePositions());
        int x = currentState.getBlankXPosition();
        int y = currentState.getBlankYPosition();
        int blank = currentState.tilePositions[x][y];    //saving 0's position
        newState.tilePositions[x][y] = currentState.tilePositions[x+1][y];
        newState.tilePositions[x+1][y] = blank;

        return newState;
    }
    public State moveLeft(State currentState) {     //method to move blank left and swap it with previous element
        State newState = new State(currentState);
        newState.setTilePositions(currentState.getTilePositions());
        int x = currentState.getBlankXPosition();
        int y = currentState.getBlankYPosition();
        int blank = currentState.tilePositions[x][y];    //saving 0's position
        newState.tilePositions[x][y] = currentState.tilePositions[x][y-1];
        newState.tilePositions[x][y-1] = blank;

        return newState;
    }
    public State moveRight(State currentState){     //method to move blank right and swap it with previous element
        State newState = new State(currentState);
        newState.setTilePositions(currentState.getTilePositions());
        int x = currentState.getBlankXPosition();
        int y = currentState.getBlankYPosition();
        int blank = currentState.tilePositions[x][y];    //saving 0's position
        newState.tilePositions[x][y] = currentState.tilePositions[x][y+1];
        newState.tilePositions[x][y+1] = blank;

        return newState;
    }

    public State getParentState() {
        return parentState;
    }

    public void printFinalPath(String fileName) throws Exception{
        FileWriter fWriter = new FileWriter(fileName, false);
        BufferedWriter bWriter = new BufferedWriter(fWriter);

        State finalPathTrace = this.returnState();
        List<State> finalPathList = new ArrayList<>();
        while(finalPathTrace!=null){
            finalPathList.add(finalPathTrace);
            finalPathTrace = finalPathTrace.getParentState();
        }
        for(int i=finalPathList.size()-1;i>=0;i--){
            Integer[][] position = finalPathList.get(i).tilePositions;
            for(int j=0;j<3;j++){
                for(int k=0;k<3;k++){
                    bWriter.write((position[j][k] +" "));
                }
                bWriter.newLine();
            }
            if(i > 0){
                bWriter.newLine();
            }         
        }
        bWriter.close();
    }
}