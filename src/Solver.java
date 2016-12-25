import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;


public class Solver
{
    private Queue<Board> solution;

    private boolean solvable = false;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        Queue<Board> twinSolution = findBestPath(initial.twin());
        if (twinSolution == null)
        {
            solution = findBestPath(initial);
            if (solution != null) solvable = true; 
        }
    }
    
    private Queue<Board> findBestPath(Board initial)
    {
        boolean reachGoal = false;
        Queue<Board> bestPath = new Queue<Board>();
        
        MinPQ<GameTreeNode> pq = new MinPQ<GameTreeNode>();
        int upperLimit = initial.manhattan();
        
        GameTreeNode searchNode = new GameTreeNode(null, initial, 0);
        int i = 0;
        while (searchNode != null)
        {
            Board item = searchNode.board;
            bestPath.enqueue(item);
            if (item.isGoal())
            {
                reachGoal = true;
                break;
            }
            
            // if exceed max steps, maybe unsolvable
            if (i > upperLimit) break;
            
            i++;
            GameTreeNode psn = searchNode.previous;
            
            for (Board b : item.neighbors())
            {
                if (psn != null && psn.board.equals(b)) continue;
                pq.insert(new GameTreeNode(searchNode, b, i));
            }
            
            searchNode = pq.delMin();
        }
        
        if (!reachGoal) return null;
        
        return bestPath;
    }
    
    private class GameTreeNode implements Comparable<GameTreeNode>
    {
        private Board board;
        private int moves;
        private GameTreeNode previous;
        
        public GameTreeNode(GameTreeNode previous, Board board, int moves)
        {
            this.previous = previous;
            this.board = board;
            this.moves = moves;
        }
        
        public int priority()
        {
            return this.board.manhattan() + this.moves;
        }
        
        public int compareTo(GameTreeNode that)
        {
            return this.priority() - that.priority();
        }
    }
    
    // is the initial board solvable?
    public boolean isSolvable()
    {
        return solvable;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (this.solution == null) return -1;
        
        return this.solution.size()-1;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        return this.solution;
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
