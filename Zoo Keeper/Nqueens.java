import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;
import java.io.*;


class Node {
  int[][] queens;
  int qcount;
  int row;
  int col;
  int numTrees;
  Node(int[][] q, int count, int r, int c, int nt){
    queens = q;
    qcount = count;
    row = r;
    col = c;
    numTrees = nt;
  }
}

class Nqueens {

  static int n;
  static int queens;
  static char[][] trees;
  static LinkedList<Node> q;
  static int[][] board;
  static int numTrees;
  static long startTime;
  static ArrayList<int[]> rlist;

  public static void randState(int matrix[][], ArrayList<int[]> queens){
    int row, col;

    Collections.shuffle(queens);
    row=queens.get(0)[0];
    col=queens.get(0)[1];
    matrix[row][col]=0;
    queens.remove(0);


    Collections.shuffle(rlist);
    row=rlist.get(0)[0];
    col=rlist.get(0)[1];
    // pick randomly one and put
    while(matrix[row][col]==1){
      Collections.shuffle(rlist);
      row=rlist.get(0)[0];
      col=rlist.get(0)[1];
    }
    queens.add(new int[]{row,col});
    matrix[row][col]=1;

  }

  public static int conflictQueens (int[][] queens, int row, int col) {
    int conflicts =0;
    for (int i=row-1; i >= 0; i--)
     {
       if (trees[i][col] == '2')
         break;
       if (queens[i][col] == 1){
        conflicts++;
       }
     }
      for (int i=col-1; i >= 0; i--)
       {
         if (trees[row][i] == '2')
           break;
         if (queens[row][i] == 1){
           conflicts++;
         }
       }
    for (int i=row-1, j=col-1; i>=0 && j>=0; i--, j--){
      if (trees[i][j] == '2')
        break;
      if (queens[i][j] == 1){
        conflicts++;
      }
    }

    for (int i=row-1, j=col+1; i>=0 && j<n; i--, j++){
      if (trees[i][j] == '2')
        break;
      if (queens[i][j] == 1){
        conflicts++;
      }
    }

    for (int i=row+1; i <n ; i++)
     {
       if (trees[i][col] == '2')
         break;
       if (queens[i][col] == 1){
         conflicts++;
       }
     }
      for (int i=col+1; i < n; i++)
       {
         if (trees[row][i] == '2')
           break;
         if (queens[row][i] == 1){
           conflicts++;
         }
       }
    for (int i=row+1, j=col+1; i<n && j<n; i++, j++){
      if (trees[i][j] == '2')
        break;
      if (queens[i][j] == 1){
        conflicts++;
      }
    }

    for (int i=row+1, j=col-1; i<n && j>=0; i++, j--){
      if (trees[i][j] == '2')
        break;
      if (queens[i][j] == 1){
        conflicts++;
      }
    }
    return conflicts;
  }


  public static int value(int matrix[][]){
    int conflicts=0;
    for(int i=0; i<n; i++)
      for(int j=0; j<n; j++)
        if(matrix[i][j] == 1)
          conflicts += conflictQueens(matrix, i, j);
      return conflicts;
  }

public static boolean sima(){
    int entered=0;
    Random randomno = new Random();
    double r;
    double prob;
    int deltaE;
    int curvalue, nextvalue;
    ArrayList<int[]> nqueens;
    boolean flag;
    rlist = new ArrayList<int[]>();
    for (int i=0; i<n; i++){
      for (int j=0; j<n; j++){
        if(trees[i][j] != '2'){
          rlist.add(new int[]{i,j});
        }
      }
    }
  Collections.shuffle(rlist);
  if(rlist.size()<queens)
      return false;
  ArrayList<int[]> cqueens = new ArrayList<int[]>();
  for(int i=0; i<queens; i++){
    cqueens.add(new int[]{rlist.get(i)[0],rlist.get(i)[1]});
    board[rlist.get(i)[0]][rlist.get(i)[1]]=1;
  }
    double count=0;
    double temp;
    while(System.currentTimeMillis()-startTime < 280000){
        temp=(1/Math.log(++count))/10.0;
        curvalue = value(board);

        if(curvalue == 0){
          return true;
        }
        int[][] next = new int[n][n];
        for(int l=0; l<n; l++)
        {
          for(int k=0; k<n; k++)
          next[l][k]=board[l][k];
        }

        nqueens = new ArrayList<int[]>();
        for(int[] m: cqueens) {
          nqueens.add(new int[]{m[0],m[1]});
        }

        randState(next,nqueens);
        nextvalue= value(next);


        deltaE = nextvalue-curvalue;
        if ( deltaE < 0){
          board = next;
          cqueens=nqueens;
          curvalue = nextvalue;
          entered++;
        }
        else{
          prob =  Math.exp(-1*deltaE/temp);
          r = randomno.nextDouble();

          if(r<prob){
            board = next;
            curvalue = nextvalue;
            cqueens=nqueens;
            entered++;
          }
        }

     }
return false;
}


  public static boolean checkIfSafe(int[][] queens, int row, int col) {
    for (int i=row-1; i >= 0; i--)
     {
       if (trees[i][col] == '2')
         break;
       if (queens[i][col] == 1)
         return false;
     }
      for (int i=col-1; i >= 0; i--)
       {
         if (trees[row][i] == '2')
           break;
         if (queens[row][i] == 1)
           return false;
       }
    for (int i=row-1, j=col-1; i>=0 && j>=0; i--, j--){
      if (trees[i][j] == '2')
        break;
      if (queens[i][j] == 1)
        return false;
    }

    for (int i=row-1, j=col+1; i>=0 && j<n; i--, j++){
      if (trees[i][j] == '2')
        break;
      if (queens[i][j] == 1)
        return false;
    }
    return true;
  }

  public static void expand (Node nod) {
    int j = nod.col+1;
    int nt = nod.numTrees;
    int qcount = nod.qcount;
    for(int i=nod.row; i<n ; i++) {
      if(queens-qcount > (n-i)+(numTrees-nt))
        break;
      while (j<n){
        if(trees[i][j] == '2'){
          nt++;
          j++;
          continue;
        }
        if (checkIfSafe(nod.queens, i, j)){
          int[][] clone = new int[n][n];
          for(int l = 0; l < n; l++){
            int[] aMatrix = nod.queens[l];
            clone[l] = new int[n];
            System.arraycopy(aMatrix, 0, clone[l], 0, n);
          }
          clone[i][j] = 1;
          q.add(new Node(clone, nod.qcount+1, i, j, nt));
        }
        j++;
      }
      j=0;
    }
  }

  public static boolean bfs(){
    q = new LinkedList<Node>();
    Node root = new Node(new int[n][n],0, 0, -1,0);
    q.add(root);
    while(true){
      if(q.isEmpty()) return false;
      Node nod = q.remove();

      if(nod.qcount == queens) {
        board =nod.queens;
        return true;
      }
      expand(nod);
   }
 }

public static boolean dfs(int row, int col, int currq, int nt) {
 if (currq == queens)
 {
   return true;
 }

  int j=col;
  for(int i=row; i<n; i++) {
    if(queens-currq > (n-row)+(numTrees-nt))
       break;
    while(j<n){
      if(trees[i][j]=='2'){
        nt++;
        j++;
        continue;
      }
      if(checkIfSafe(board,i,j)){
        board[i][j]=1;
        if(dfs(i,j+1,currq+1,nt))
          return true;
        board[i][j]=0;
      }
      j++;
    }
    j=0;
  }
return false;

}

public static void main (String args[]) throws IOException, FileNotFoundException {

            Scanner scan = new Scanner(new File("input.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            String stype = scan.next();
            n = scan.nextInt();
            queens = scan.nextInt();
            board = new int[n][n];
            trees = new char[n][n];
            numTrees =0;
            for (int i=0; i<n; i++){
              String line = scan.next();
              for (int j=0; j<n; j++){
              trees[i][j] =line.charAt(j);
              if (line.charAt(j) == '2')
                numTrees++;
              }
            }
            startTime = System.currentTimeMillis();

            boolean finalresult=false;
            if(stype.equals("BFS"))
              finalresult=bfs();
            else if (stype.equals("DFS"))
              finalresult=dfs(0,0,0,0);
            else if (stype.equals("SA"))
              finalresult=sima();

            if (finalresult) {
              writer.write("OK\n");
              for (int i=0; i<n; i++){
                for (int j=0; j<n; j++){
                  if (trees[i][j] == '2')
                    writer.write("2");
                  else
                    writer.write(""+board[i][j]);
                }
                writer.write("\n");
              }
            }
            else
              writer.write("FAIL\n");
            writer.close();
            scan.close();
  }
}
