package edu.ssau.reversi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;


/**
 * Created by EAA on 15.04.2017.
 */
public class ReversiBoard implements Serializable{
    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    private int boardSize;
    private char board[][];
    public ReversiBoard(int size){
        boardSize = size;
        board = new char[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(((i == size/2) || (i == size/2 - 1)) &&
                        ((j == size/2) || (j == size/2 - 1))){
                    if(i == j){
                        board[i][j] = 'L';
                    }else{
                        board[i][j] = 'D';
                    }
                }else{
                    board[i][j] = ' ';
                }
            }
        }//end loop
    }

    public ReversiBoard(ReversiBoard oldBoard){
        boardSize = oldBoard.boardSize;
        board = new char[oldBoard.boardSize][oldBoard.boardSize];
        for(int i = 0; i < oldBoard.boardSize; i++){
            for(int j = 0; j < oldBoard.boardSize; j++){
                board[i][j] = oldBoard.board[i][j];
            }
        }
    }

    double getFillFactor(){
        int filledSlots = 0;
        for(int i = 0; i < boardSize; i ++){
            for(int j = 0; j < boardSize; j++){
                if(board[i][j] != ' ')
                    filledSlots ++;
            }
        }
        return (double)filledSlots/(double)(boardSize * boardSize);
    }
    int getSpotsLeft(){
        int emptySpots = 0;
        for(int i = 0; i < boardSize; i ++){
            for(int j = 0; j < boardSize; j++){
                if(board[i][j] == ' ')
                    emptySpots++;
            }
        }
        return emptySpots;
    }
    public void printScore(){
        System.out.println("Score: Light " + getScore('L') + " - Dark " + getScore('D'));
    }
    public boolean playMove(char playerColor, int i, int j){
        if(isValid(playerColor, i, j) == false){
            //	    System.out.println("Invalid Move");
            return false;
        }

        board[i][j] = playerColor;
        for(int x = -1; x <= 1; x++){//in each direction
            for(int y = -1; y <= 1; y++){

                if(y!= 0 || x!= 0 ){
                    if(checkDirection(playerColor, i, j, x, y)){
                        int tempI = i + x;
                        int tempJ = j + y;
                        while(board[tempI][tempJ] == (char)(144 - (int)playerColor)){
                            board[tempI][tempJ] = playerColor;
                            tempI += x;
                            tempJ += y;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isValid(char playerColor, int i, int j){
        //check every direction for a valid reason to accept move
        if(board[i][j] != ' ')
            return false;
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                if(y!= 0 || x!= 0 ){
                    if(checkDirection(playerColor, i, j, x, y)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkDirection(char playerColor, int i, int j, int iMod, int jMod){
        int currentI = i + iMod;
        int currentJ = j + jMod;
        //returns false if checking off edge of board
        if(currentI < 0 || currentI > boardSize - 1 || currentJ < 0 || currentJ > boardSize - 1)
            return false;
        //returns false if first piece in that direction is not the opposite color
        if(board[currentI][currentJ] != (char)(144 - (int)(playerColor))) //ascii hacking to check if opposite color
            return false;

        //adjust currentI and currentJ to check first square after opposite color
        currentI += iMod;
        currentJ += jMod;
        while(currentI > -1 && currentI < boardSize && currentJ > -1 && currentJ < boardSize){
            if(board[currentI][currentJ] == playerColor)
                return true;
            if(board[currentI][currentJ] == ' ')
                return false;
            currentI += iMod;
            currentJ += jMod;
        }
        return false;

    }
    public void printBoard(){

        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_RED = "\u001B[31m";

        String breakLine = "   +";
        for(int i = 0; i < boardSize; i++){
            breakLine += "---+";
        }
        String line = "";

        line += "    ";
        for(int i = 0; i < boardSize; i++){
            char letter = (char) (i + 97);
            line+= " " + letter + "  ";
        }
        System.out.println(line);
        System.out.println(breakLine);

        for(int j = 0; j < boardSize; j++){
            line = " " + (j+1);
            if(j + 1 < 10)
                line += " |";
            else
                line += "|";
            for(int i = 0; i < boardSize; i++){
                char printChar = board[i][j];
                if(printChar == 'L'){
                    line += " " + ANSI_RED + "L" + ANSI_RESET + " |";
                }else if(printChar == 'D'){
                    line += " " + ANSI_BLUE + "D" + ANSI_RESET + " |";
                }else{
                    line += "   |";
                }
            }
            System.out.println(line);
            System.out.println(breakLine);
        }

    }

    public boolean isFull(){
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(board[i][j] == ' '){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isOneColor(){
        char color = ' ';
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(board[i][j] != ' ' && color != ' ' && board[i][j] != color){
                    return false;
                }
                if(color == ' ')
                    color = board[i][j];
            }
        }
        return true;
    }

    public char getWinner(){
        if(getScore('L') > getScore('D'))
            return 'L';
        if(getScore('D') > getScore('L'))
            return 'D';
        else
            return 'T';
    }

    public boolean hasMove(char playerColor){
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(isValid(playerColor, i, j))
                    return true;
            }
        }
        return false;
    }

    public int getScore(char playerColor){
        int result = 0;
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(board[i][j] == playerColor)
                    result++;
            }
        }
        return result;
    }

    public int calculateScore(char playerColor){
        double result = getScore(playerColor) - getScore((char)(144 - (int) playerColor));

        if(board[0][0] == playerColor){
            result += 3;
            for(int i = 1; i < boardSize - 1; i++){
                if(board[0][i] == playerColor)
                    result += 1;
                else
                    i = boardSize + 1;
            }
            for(int i = 1; i < boardSize - 1; i++){
                if(board[i][0] == playerColor)
                    result += 1;
                else
                    i = boardSize + 1;
            }

        }
        if(board[0][boardSize - 1] == playerColor){
            result += 3;
            for(int i = boardSize - 2; i > 0; i--){
                if(board[0][i] == playerColor)
                    result += 1;
                else
                    i = -1;
            }
            for(int i = 1; i < boardSize - 1; i++){
                if(board[i][boardSize - 1] == playerColor)
                    result += 1;
                else
                    i = boardSize + 1;
            }
        }
        if(board[boardSize - 1][0] == playerColor){
            result += 3;
            for(int i = 1; i < boardSize - 1; i++){
                if(board[boardSize - 1][i] == playerColor)
                    result += 1;
                else
                    i = boardSize + 1;
            }
            for(int i = boardSize - 2; i > 0; i--){
                if(board[i][0] == playerColor)
                    result += 1;
                else
                    i = -1;
            }
        }
        if(board[boardSize - 1][boardSize - 1] == playerColor){
            result += 3;
            for(int i = boardSize - 2; i > 0; i--){
                if(board[boardSize - 1][i] == playerColor)
                    result += 1;
                else
                    i = -1;
            }
            for(int i = boardSize - 2; i > 0; i--){
                if(board[i][boardSize - 1] == playerColor)
                    result += 1;
                else
                    i = -1;
            }

        }
        return (int)result;
    }
}