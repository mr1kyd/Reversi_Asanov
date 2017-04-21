package edu.ssau.reversi.server;

import edu.ssau.reversi.ReversiBoard;

class ReversiPlayer {

    public static String playMove(ReversiBoard board, char playerColor){
        if(board.hasMove(playerColor) == false)
            return "no move to play";
        int[] result = new int[3];
	/*if(board.getSpotsLeft() > 8 && board.getFillFactor() < .6){
	    result = minimaxWithAB(board, playerColor, 5, true, playerColor, -10000, 10000);
	}else{ //run a slightly different algorithm for endgame
	*/
        result = minimax(board, playerColor, 1, true, playerColor);
//	}

        int iVal = result[1];
        int jVal = result[2];
        board.playMove(playerColor, iVal, jVal);

        String playedMove = "";
        playedMove += (char)(iVal + 97);
        playedMove += (jVal + 1);
        return playedMove;

    }

    public static int[] minimax(ReversiBoard board, char playerColor, int depth, boolean maximizingPlayer, char myColor){
        if(depth == 0 || (board.hasMove(playerColor) == false && board.hasMove((char)(144 - (int)playerColor)) == false) || board.isFull() == true){
            int[] result = new int[3];
            if((board.hasMove(playerColor) == false && board.hasMove((char)(144 - (int)playerColor)) == false) || board.isFull() == true){
                if(board.getScore(myColor) == board.getScore((char)(144 - (int)myColor))){
                    result[0] = 0;
                }else{
                    result[0] = (board.getScore(myColor) > board.getScore((char)(144 - (int)myColor)) ? 10000 : -10000);
                }
                result[1] = -1;
                result[2] = -1;
                return result;
            }

            result[0] = board.calculateScore(myColor);
            result[1] = -1;
            result[2] = -1;
            return result;
        }

        if(maximizingPlayer == true){
            int bestVal = -999999999;
            int iVal = -1;
            int jVal = -1;

            boolean hasMoves = false;
            for(int i = 0; i < board.getBoardSize(); i++){
                for(int j = 0; j < board.getBoardSize(); j++){
                    if(board.isValid(playerColor, i, j)){
                        hasMoves = true;
                        ReversiBoard tempBoard = new ReversiBoard(board);
                        tempBoard.playMove(playerColor, i, j);


                        int result[] = minimax(tempBoard,  (char)(144-(int)playerColor), depth - 1, false, myColor);
                        if(result[0] > bestVal){
                            bestVal = result[0];
                            iVal = i;
                            jVal = j;
                        }
                    }

                }
            }
            if(hasMoves == false){

                return minimax(board, (char)(144 - (int)playerColor), depth - 1, false, myColor);
            }
            int returnArr[] = new int[3];
            returnArr[0] = bestVal;

            returnArr[1] = iVal;
            returnArr[2] = jVal;
            return returnArr;
        }else{ //minimizing player
            int bestVal = 9999999;
            int iVal = -1;
            int jVal = -1;

            boolean hasMoves = false;
            for(int i = 0; i < board.getBoardSize(); i++){
                for(int j = 0; j < board.getBoardSize(); j++){
                    if(board.isValid(playerColor, i, j)){
                        hasMoves = true;
                        ReversiBoard tempBoard = new ReversiBoard(board);
                        tempBoard.playMove(playerColor, i, j);

                        int result[] = minimax(tempBoard, (char)(144-(int)playerColor), depth - 1, true, myColor);
                        if(result[0] < bestVal){
                            bestVal = result[0];
                            iVal = i;
                            jVal = j;
                        }

                    }

                }
            }
            if(hasMoves == false){
                return minimax(board, (char)(144 - (int)playerColor), depth - 1, true, myColor);
            }
            int returnArr[] = new int[3];
            returnArr[0] = bestVal;
            returnArr[1] = iVal;
            returnArr[2] = jVal;
            return returnArr;
        }
    }

    public static String playRandomMove(ReversiBoard board, char playerColor){
        for(int i = 0; i < board.getBoardSize(); i++){
            for(int j = 0; j < board.getBoardSize(); j++){
                if(board.isValid(playerColor, i, j)){
                    board.playMove(playerColor, i, j);
                    String playedMove = "";
                    playedMove += (char)(i + 97);
                    playedMove += (j+1);
                    return playedMove;
                }
            }
        }
        return "cannot play a move";
    }
}