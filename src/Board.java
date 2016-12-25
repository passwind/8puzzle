import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


public class Board
{
    private int[][] blocks;
    private Stack<Board> neighbors;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)
    {
        int n = blocks.length;
        int[][] b = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            {
                b[i][j] = blocks[i][j];
            }
        
        this.blocks = b;
    }
    
    private void makeBoardByMoveBlank(int[][] tiles, int bRow, int bCol,
            int row, int col)
    {
        tiles[bRow][bCol] = tiles[row][col];
        tiles[row][col] = 0;
        Board b = new Board(tiles);
        this.neighbors.push(b);
        tiles[row][col] = tiles[bRow][bCol];
        tiles[bRow][bCol] = 0;
    }

    // board dimension n
    public int dimension()
    {
        return this.blocks.length;
    }
    
    // number of blocks out of place
    public int hamming()      
    {
        int total = 0;
        int m = this.dimension();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < m; j++)
            {
                if (i == m-1 && j == m-1) continue;
                if (i*m+j+1 != this.blocks[i][j]) total++;
            }
        
        return total;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan()
    {
        int total = 0;
        int n = this.blocks.length;
        
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            {
                if (this.blocks[i][j] == 0) continue;
                int row = (this.blocks[i][j]-1)/n;
                int col = (this.blocks[i][j]-1) % n;
                int rowOffset = i-row;
                if (rowOffset < 0) rowOffset = -rowOffset;
                int colOffset = j-col;
                if (colOffset < 0) colOffset = -colOffset;
                total += rowOffset+colOffset;
            }
        return total;
    }
    
    // is this board the goal board?
    public boolean isGoal()
    {
        return (this.hamming() == 0);
    }
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin()
    {
        int n = this.blocks.length;
        int[][] b = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                b[i][j] = this.blocks[i][j];
        
        int idx = StdRandom.uniform(1, n+1);
        int row = (idx-1)/n;
        int col = (idx-1) % n;
        
        if (b[row][col] == 0)
        {
            col++;
            if (col == n)
            {
                col = 0;
                row++;
                if (row == n) row = 0;
            }
        }
        
        int row1 = row;
        int col1 = col+1;
        if (col1 == n)
        {
            col1 = 0;
            row1++;
            if (row1 == n) row1 = 0;
        }
        
        if (b[row1][col1] == 0)
        {
            col1++;
            if (col1 == n)
            {
                col1 = 0;
                row1++;
                if (row1 == n) row1 = 0;
            }
        }
        
        int tmp = b[row][col];
        b[row][col] = b[row1][col1];
        b[row1][col1] = tmp;
        
        return new Board(b);
    }
    
    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this) return true;
        
        if (y == null) return false;
        
        if (y.getClass() != this.getClass())
            return false;
        
        Board that = (Board) y;
        
        int n = this.blocks.length;
        int m = that.blocks.length;
        if (m != n) return false;
        
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (that.blocks[i][j] != this.blocks[i][j]) return false;
        
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        if (this.neighbors == null)
        {
            int n = this.blocks.length;
            int blankRow = n-1;
            int blankCol = n-1;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (this.blocks[i][j] == 0)
                    {
                        blankRow = i;
                        blankCol = j;
                    }
            
            this.neighbors = new Stack<Board>();
            // up
            int row = blankRow-1;
            int col = blankCol;
            if (row >= 0)
            {
                makeBoardByMoveBlank(blocks, blankRow, blankCol, row, col);
            }
            
            // down
            row = blankRow+1;
            col = blankCol;
            if (row < n)
            {
                makeBoardByMoveBlank(blocks, blankRow, blankCol, row, col);
            }
            
            // left
            row = blankRow;
            col = blankCol-1;
            if (col >= 0)
            {
                makeBoardByMoveBlank(blocks, blankRow, blankCol, row, col);
            }
            
            // right
            row = blankRow;
            col = blankCol+1;
            if (col < n)
            {
                makeBoardByMoveBlank(blocks, blankRow, blankCol, row, col);
            }
        }
        return this.neighbors;
    }
    
    // string representation of this board (in the output format specified below)
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        int n = this.blocks.length;
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    // unit tests (not graded)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board b1 = new Board(blocks);
        
        StdOut.println("dimension is : " + b1.dimension());
        StdOut.println("hamming is : " + b1.hamming());
        StdOut.println("manhattan is : " + b1.manhattan());
        StdOut.println("block is \n " + b1.toString());
        
        int tmp = blocks[n-1][n-1];
        blocks[n-1][n-1] = blocks[n-1][n-2];
        blocks[n-1][n-2] = tmp;
        Board b2 = new Board(blocks);
        
        StdOut.println("block2 is \n " + b2.toString());
        StdOut.println("block1 equals block2 \n " + b1.equals(b2));
        
        StdOut.println("block2 is goal? " + b2.isGoal());
        
        int[][] gb = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
            {
                if (i == 2 && j == 2) gb[i][j] = 0;
                else gb[i][j] = i*3+j+1;
            }
        
        Board b3 = new Board(gb);
        StdOut.println("block3 is \n " + b3.toString());
        StdOut.println("block3 is goal? " + b3.isGoal());
        
        Board b4 = b1.twin();
        StdOut.println("block1.twin is \n " + b4.toString());
        
        StdOut.println("block1.neighbors are \n ");
        for (Board b : b1.neighbors())
        {
            StdOut.println(b.toString());
        }
        
    }
}
