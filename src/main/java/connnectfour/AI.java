package connnectfour;

import java.util.Arrays;

public class AI {
    static int cols;
    static int rows;
    static int center = 3;
    static int linetwo = 4;
    static int linethree = 6;


    static class Move {
        int col;
    }

    public AI(int colss, int rowss) {
        cols = colss;
        rows = rowss;
    }
    public static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public static boolean getsInRow(int x, int y, int n, int[][] board, int l) {
        int maxX = Math.min(x+1, board[0].length - n + 1);
        int minX = Math.max(0, x-n+1);
        int minY = Math.max(0, y-n+1);
        int maxY = Math.min(y+1, board.length - n + 1);

        for(int j = minX; j < maxX; j++){
            boolean praeguneCheck = true;
            int nullid = 0;
            for (int k = 0; k < n; k++) {
                int value = board[y][j + k];
                if (value != l && value != 0) {
                    praeguneCheck = false;
                    break;
                }
                if (value == 0) {
                    nullid++;
                    if (k == 0 || k == n-1) {
                        praeguneCheck = false;
                        break;
                    }
                }
            }
            if (nullid >= n-1) {
                praeguneCheck = false;
            }
            if(praeguneCheck){
                return true;
            }
        }
        for(int i = minY; i < maxY; i++){
            boolean praeguneCheck = true;
            int nullid = 0;
            for (int k = 0; k < n; k++) {
                int value = board[i + k][x];
                if (value != l && value != 0) {
                    praeguneCheck = false;
                    break;
                }
                if (value == 0) {
                    nullid++;
                    if (k == 0 || k == n-1) {
                        praeguneCheck = false;
                        break;
                    }
                }
            }
            if (nullid >= n-1) {
                praeguneCheck = false;
            }
            if(praeguneCheck){
                return true;
            }
        }
        for (int i = n-1; i >= 0; i--) {
            boolean praeguneCheck = true;
            int nullid = 0;
            for (int k = 0; k < n; k++) {
                if (y+k-i >= board.length || x+k-i >= board[0].length || y+k-i < 0 || x+k-i < 0) {
                    praeguneCheck = false;
                    break;
                }
                int value = board[y + k - i][x + k - i];
                if (value != l && value != 0) {
                    praeguneCheck = false;
                    break;
                }
                if (value == 0) {
                    nullid++;
                    if (k == 0 || k == n-1) {
                        praeguneCheck = false;
                        break;
                    }
                }
            }
            if (nullid >= n-1) {
                praeguneCheck = false;
            }
            if(praeguneCheck){
                return true;
            }
        }

        for (int i = n-1; i >= 0; i--) {
            boolean praeguneCheck = true;
            int nullid = 0;
            for (int k = 0; k < n; k++) {
                if (y-k+i < 0 || y-k+i >= board.length || x+k-i < 0 || x+k-i >= board[0].length) {
                    praeguneCheck = false;
                    break;
                }
                int value = board[y - k + i][x + k - i];
                if (value != l && value != 0) {
                    praeguneCheck = false;
                    break;
                }
                if (value == 0) {
                    nullid++;
                    if (k == 0 || k == n-1) {
                        praeguneCheck = false;
                        break;
                    }
                }
            }
            if (nullid >= n-1) {
                praeguneCheck = false;
            }
            if (praeguneCheck) {
                return true;
            }
        }
        return false;
    }

    public static int checkWin(int[][] board){
        int voitja = 0;
        for (int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length-3; j++){
                for(int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (board[i][j + k] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if(praeguneCheck){
                        voitja = l;
                        return l;
                    }
                }
            }
        }
        for(int i = 0; i < board.length-3; i++){
            for(int j = 0; j < board[0].length; j++){
                for(int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (board[i + k][j] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if(praeguneCheck){
                        voitja = l;
                        return l;
                    }
                }
            }
        }
        for(int i = 0; i < board.length-3; i++){
            for(int j = 0; j < board[0].length-3; j++){
                for(int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (board[i + k][j + k] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if(praeguneCheck){
                        voitja = l;
                        return l;
                    }
                }
            }
        }
        for(int i = 3; i < board.length; i++){
            for(int j = 0; j < board[0].length-3; j++){
                for(int l = 1; l < 3; l++) {
                    boolean praeguneCheck = true;
                    for (int k = 0; k < 4; k++) {
                        if (board[i - k][j + k] != l) {
                            praeguneCheck = false;
                            break;
                        }
                    }
                    if(praeguneCheck){
                        voitja = l;
                        return l;
                    }
                }
            }
        }
        return voitja;
    }

    public static int getKaidud(int[][] b) {
        int moves = 0;
        for (int i = 0; i < b.length; i++) {
            for (int k:b[i]) {
                if (k != 0) {
                    moves++;
                }
            }
        }
        return moves;
    }

    static int minimax(int[][] board, int depth, Boolean isMax, int move, int y) {
        int winner = checkWin(board);

        // If player has won the game
        if (winner == 1) {
            return -1000;
        }

        // If AI has won the game
        else if (winner == 2) {
            return 1000;
        }

        // If there are no more moves and
        // no winner then it is a tie
        if (getKaidud(board) == cols*rows || depth == 0) {
            return 0;
        }
        int this_move = 0;
        if (move == cols/2) {
            this_move += center;
        }
        int p = isMax ? 1 : 2;
        if (getsInRow(move, y, 2, board, p)) {
            this_move += linetwo;
        }
        if (getsInRow(move, y, 3, board, p)) {
            this_move += linethree;
        }

        // If this is maximizer's move
        if (isMax)
        {
            int best = Integer.MIN_VALUE;
            int player = 2;
            // Traverse all cells
            for (int i = 0; i < cols; i++)
            {
                for (int j = 0; j < rows; j++)
                {
                    // Check if cell is empty
                    if (board[j][i] == 0 && (j == rows-1 || board[j+1][i] != 0))
                    {
                        // Make the move
                        board[j][i] = player;

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(board,
                                depth - 1, !isMax, i, j));

                        // Undo the move
                        board[j][i] = 0;
                    }
                }
            }
            return best;
        }

        // If this is minimizer's move
        else
        {
            int opponent = 1;
            int best = Integer.MAX_VALUE;

            // Traverse all cells
            for (int i = 0; i < cols; i++)
            {
                for (int j = 0; j < rows; j++)
                {
                    // Check if cell is empty
                    if (board[j][i] == 0 && (j == rows-1 || board[j+1][i] != 0))
                    {
                        // Make the move
                        board[j][i] = opponent;

                        // Call minimax recursively and choose
                        // the minimum value
                        best = Math.min(best, minimax(board,
                                depth - 1, !isMax, i, j));

                        // Undo the move
                        board[j][i] = 0;
                    }
                }
            }
            return best + this_move;
        }

    }

    static int findBestMove(int board[][], int depth) {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = new Move();
        bestMove.col = -1;

        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < cols; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                // Check if cell is empty
                if (board[j][i] == 0 && (j == rows-1 || board[j+1][i] != 0))
                {
                    // Make the move
                    board[j][i] = 2;

                    // compute evaluation function for this
                    // move.
                    int moveVal = minimax(board, depth, false, i, j);
                    // Undo the move
                    board[j][i] = 0;

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal)
                    {
                        bestMove.col = i;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove.col;
    }


}
