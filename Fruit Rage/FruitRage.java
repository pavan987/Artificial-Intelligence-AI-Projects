import java.util.*;
import java.io.*;
import java.lang.Math;
import java.lang.management.*;
class Position{
  int i;
  int j;
  Position(int r, int c){
    i=r;
    j=c;
  }
}

class Node{
  int i;
  int j;
  char[][] board;
  double score;
  Node(int i, int j, double score, char[][] board){
    this.i=i;
    this.j=j;
    this.score=score;
    this.board=board;
  }
}

class FruitRage{
  static char[][] board;
  static double stime;
  static double ttime;
  static int nodecount=0;
  static int maxdepth=2;
  static double inf = Double.POSITIVE_INFINITY;
  static int n;
  static int t;

  public static double make_empty(char[][] clone, int i, int j, char value, ArrayList<String> allelements,double count){
      if(i<0 || j<0 || i > n-1 || j > n-1)
          return count;
      if(clone[i][j] != value)
          return count;
      clone[i][j] = '*';
      count=count+1;
      allelements.remove(i+"r"+j+"c");
      count=make_empty(clone,i-1, j,value,allelements,count);
      count=make_empty(clone,i, j-1,value,allelements,count);
      count=make_empty(clone,i+1, j,value,allelements,count);
      count=make_empty(clone,i, j+1,value,allelements,count);
      return count;
    }

  public static void make_finalempty(char[][] clone, int i, int j, char value){
      if(i<0 || j<0 || i > n-1 || j > n-1)
          return;
      if(clone[i][j] != value)
          return;
      clone[i][j] = '*';
      make_finalempty(clone,i-1, j,value);
      make_finalempty(clone,i, j-1,value);
      make_finalempty(clone,i+1, j,value);
      make_finalempty(clone,i, j+1,value);
      return;
    }

  public static void make_fall(char[][] clone) {
    char temp;
    int s;
    for(int c=0; c<n ; c++){
      s = n-1;
      for (int r=n-1; r>=0; r--){
        if(clone[r][c] != '*') {
          temp = clone[s][c];
          clone[s][c] = clone[r][c];
          clone[r][c] = temp;
          s-=1;
        }
      }
    }
  }

  public static double evaluate(char[][] b, boolean isMax, double score, ArrayList<String> allelements){
     ArrayList<Double> countlist = new ArrayList<Double>();
     while(!allelements.isEmpty()){
       Position p = getIndices(allelements.get(0));
       char value = b[p.i][p.j];
       double count=make_empty(b,p.i,p.j,value,allelements,0);
       countlist.add(count);
     }
     Collections.sort(countlist, Collections.reverseOrder());
     if (isMax){
       for (int i=0; i<countlist.size(); i++){
         if(i%2 ==0){
           score+=countlist.get(i);
         }
         else{
           score-=countlist.get(i);
         }
       }
     }
     else {
       for (int i=0; i<countlist.size(); i++){
         if(i%2 ==0){
           score-=countlist.get(i);
         }
         else{
           score+=countlist.get(i);
         }
       }
     }

    return score;
  }


  public static void printBoard(char[][] b){
    for(int i=0; i<n; i++){
      for(int j=0; j<n; j++){
        System.out.print(b[i][j]);
      }
      System.out.println();
    }
  }

  public static void getElementIndices(ArrayList<String> allelements, char[][] b){
    for(int i=0; i<n; i++){
      for(int j=0; j<n; j++){
        if( b[i][j]!='*'){
          allelements.add(i+"r"+j+"c");
        }
      }
    }
  }

  public static void deeparrayCopy(char b[][], char clone[][]){
    for(int i=0; i<n; i++) {
      for(int j=0; j<n; j++){
        clone[i][j]=b[i][j];
      }
    }
  }

  public static Position getIndices( String str){
    int rowindex = str.indexOf('r');
    int colindex = str.indexOf('c');
    int i = Integer.parseInt(str.substring(0,rowindex));
    int j = Integer.parseInt(str.substring(rowindex+1,colindex));
    return new Position(i,j);
  }

  public static double minimax(char[][] b, int depth, boolean isMax, double score, double alpha, double beta){

    // Check terminal condition
    ArrayList<String> allelements = new ArrayList<String>();
    getElementIndices(allelements,b);
    if(allelements.isEmpty()){
      // System.out.println("------------------------");
      return score;
    }
    if(depth >=maxdepth){
      return evaluate(b,isMax,score,allelements);
    }

    // Create all Boards at one shot
    ArrayList<Node> boards = new ArrayList<Node>();
    while(!allelements.isEmpty()){
      char[][] clone = new char[n][n];
      deeparrayCopy(b,clone);
      Position p = getIndices(allelements.get(0));
      char value = clone[p.i][p.j];
      double count=make_empty(clone,p.i,p.j,value,allelements,0);
      boards.add(new Node(p.i,p.j,count*count,clone));
    }
    boards.sort(Comparator.comparing(a -> a.score));
    Collections.reverse(boards);

    // for(Node item: boards){
    //   System.out.println(Arrays.deepToString(item.board));
    //   System.out.println(item.score);
    // }

    if (isMax){
      double best=-1*inf;
      // System.out.println("Entered Max Depth:"+depth);
      for(Node item: boards){
        nodecount++;
        // System.out.println(Arrays.deepToString(item.board));
        make_fall(item.board);
        best = Math.max(best, minimax(item.board, depth+1, false, score+item.score, alpha, beta));
        alpha = Math.max(alpha, best);
        if (alpha >= beta){
            // System.out.println("Entered alpha "+alpha);
            break;
          }
      }
      return best;
    }
    else {
      double best=inf;
      // System.out.println("Entered Min Depth:"+depth);
      for(Node item: boards){
        nodecount++;
        // System.out.println( best);
        // System.out.println(Arrays.deepToString(item.board));
        make_fall(item.board);
        best = Math.min(best, minimax(item.board, depth+1, true, score-item.score, alpha, beta));
        beta = Math.min(beta, best);
        if (beta <= alpha){
          // System.out.println("Entered beta "+beta);
            break;
          }
      }
      return best;
    }

  }

  public static Position findBestMove() {
    // System.out.println("Entered");
    Position result = new Position(0,0);
    ArrayList<String> allelements = new ArrayList<String>();
    getElementIndices(allelements,board);
    double alpha = -1*inf;
    double beta = inf;
    double bestscore = -1*inf;

    // Create all Boards at one shot
    ArrayList<Node> boards = new ArrayList<Node>();
    while(!allelements.isEmpty()){
      char[][] clone = new char[n][n];
      deeparrayCopy(board,clone);
      Position p = getIndices(allelements.get(0));
      char value = clone[p.i][p.j];
      double count=make_empty(clone,p.i,p.j,value,allelements,0);
      boards.add(new Node(p.i,p.j,count*count,clone));
    }
    boards.sort(Comparator.comparing(a -> a.score));
    Collections.reverse(boards);
    if(ttime<10) {
      result.i= boards.get(0).i;
      result.j= boards.get(0).j;
      return result;
    }
    // for(Node item: boards){
    //   System.out.println(Arrays.deepToString(item.board));
    //   System.out.println(item.score);
    // }''
    if(n<=5){
      maxdepth=8;
    } else if(n<=10) {
      maxdepth=4;
    } else if (n<=13) {
      maxdepth=3;
    } else {
      maxdepth=2;
    }

    for(Node item: boards){
      nodecount++;
      // System.out.println("Entered Max Depth:0");
      // System.out.println(Arrays.deepToString(item.board));
      make_fall(item.board);
      double score = minimax(item.board, 1, false, item.score, alpha, beta);
      if (score > bestscore) {
        result.i=item.i;
        result.j=item.j;
        bestscore=score;
      }
      alpha = Math.max(alpha, bestscore);
      if (alpha >= beta)
          break;
    }
    return result;
  }


  public static void main(String args[]) throws IOException, FileNotFoundException{
    // Read Input
    //ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    Scanner scan = new Scanner(new File("input.txt"));
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    n = scan.nextInt();
    t = scan.nextInt();
    ttime = scan.nextDouble();
    board = new char[n][n];
    for (int i=0; i<n; i++){
      String line = scan.next();
      for (int j=0; j<n; j++){
      board[i][j] =line.charAt(j);
      }
    }

    // call findBestMove
    Position result = findBestMove();
    char col = (char)((int)'A'+result.j);
    writer.write(col+""+(result.i+1)+"\n");
    make_finalempty(board, result.i, result.j, board[result.i][result.j]);
    make_fall(board);
    for(int i=0; i<n; i++){
      for(int j=0; j<n; j++){
        writer.write(board[i][j]);
      }
      writer.write("\n");
    }
    writer.close();
    //System.out.println(bean.getCurrentThreadCpuTime( )/1000000);
    // System.out.println(nodecount);
   }
}
