import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver
{
    private Stack<Board> solution = null;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial.isGoal())
        {
            solution = new Stack<Board>();
            solution.push(initial);
        }
        else
        {
            solution = null;
            
            MinPQ<GameTreeNode> pq = new MinPQ<GameTreeNode>();
            MinPQ<GameTreeNode> twinPq = new MinPQ<GameTreeNode>();
            
            GameTreeNode searchNode = new GameTreeNode(null, initial);
            GameTreeNode twinSearchNode = new GameTreeNode(null, initial.twin());
            
            while (true)
            {
                if (searchNode.board.isGoal())
                {
                    solution = makeSolutionBySearchNode(searchNode);
                    break;
                }
                
                GameTreeNode psn = searchNode.previous;
                
                for (Board b : searchNode.board.neighbors())
                {
                    if (psn != null && psn.board.equals(b)) continue;
                    pq.insert(new GameTreeNode(searchNode, b));
                }
                
                searchNode = pq.delMin();
                
                // check twin 
                
                if (twinSearchNode.board.isGoal())
                {
                    break;
                }
                
                GameTreeNode tpsn = twinSearchNode.previous;
                
                for (Board b : twinSearchNode.board.neighbors())
                {
                    if (tpsn != null && tpsn.board.equals(b)) continue;
                    twinPq.insert(new GameTreeNode(twinSearchNode, b));
                }
                
                twinSearchNode = twinPq.delMin();
            }
        }
    }
    
    private Stack<Board> makeSolutionBySearchNode(GameTreeNode lastNode)
    {
        Stack<Board> tmp = new Stack<Board>();
        GameTreeNode prevNode = lastNode;
        while (prevNode != null)
        {
            tmp.push(prevNode.board);
            prevNode = prevNode.previous;
        }
        
        return tmp;
    }

    private class GameTreeNode implements Comparable<GameTreeNode>
    {
        private Board board;
        private int moves;
        private GameTreeNode previous;
        
        public GameTreeNode(GameTreeNode previous, Board board)
        {
            this.previous = previous;
            this.board = board;
            if (this.previous == null) this.moves = 0;
            else this.moves = this.previous.moves + 1;
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
        return !(solution == null);
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
