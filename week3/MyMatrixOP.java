class Matrix{
                int rows;
                int cols;
                int[][] ele;
                Matrix(int m, int n)
                {
                        rows=m;
                        cols=n;
                        ele=new int[rows][cols];
                }
                void printMatrix()
                {
                        for(int i=0;i<rows;i++)
                        {
                                for(int j=0;j<cols;j++)
                                        System.out.print(ele[i][j]+"  ");
                                System.out.print("\n");
                        }
                }
		void readMatrix()
		{
			for(int i=0;i<rows;i++)
			{
				for(int j=0;j<cols;j++)
				{
					ele[i][j]=1;
				}
			}
		}
}
class MatrixMulti{
void multiply(int tid)
{
		for(int i=tid;i<A.rows;i=i+THREADS)
		for(int j=0;j<B.cols;j++)
		for(int k=0;k<A.cols;k++)
			C.ele[i][j]+=A.ele[i][k]*B.ele[k][j];
}
void multiply()
{               
                for(int i=0;i<A.rows;i++)
                for(int j=0;j<B.cols;j++)
                for(int k=0;k<A.cols;k++)
                        C.ele[i][j]+=A.ele[i][k]*B.ele[k][j];
}
void readMatrices()
{
A.readMatrix();
B.readMatrix();
}
void printMat()
{
C.printMatrix();
}	
Matrix A,B,C;
int THREADS;
Thread th[];
MatrixMulti(int num_th, int frows, int fcols, int srows, int scols)
{
	THREADS=num_th;
	A=new Matrix(frows,fcols);
	B=new Matrix(srows,scols);
	C=new Matrix(frows,scols);
	th=new Thread[THREADS];

}
public void testParallel()throws Exception{
	for(int i=0;i<THREADS;i++)
	{
		th[i]=new CreateThread();
	}
	for(int i=0;i<THREADS;i++)
        {
            	th[i].start();
        }
	for(int i=0;i<THREADS;i++)
        {
            	th[i].join();
        }	
}
class CreateThread extends Thread{
	public void run()
	{
 		int tid=ThreadID.get();
 		multiply(tid);
	}
}
}

class ThreadID {
  // The next thread ID to be assigned
  private static volatile int nextID = 0;
  // My thread-local ID.
  private static ThreadLocalID threadID = new ThreadLocalID();
  public static int get(){
    return threadID.get();
  }
  public static void reset(){
    nextID = 0;
  }
  private static class ThreadLocalID extends ThreadLocal<Integer> {
    protected synchronized Integer initialValue() {
      return nextID++;
    }
  }
}
public class MyMatrixOP {
    MatrixMulti instance;
    
    MyMatrixOP(int num_th, int frows, int fcols, int srows, int scols){
        instance=new MatrixMulti(num_th,frows,fcols,srows,scols);
    }
    public static void main(String args[]) {
        int num_th=Integer.parseInt(args[0]);
	int frows=Integer.parseInt(args[1]);
	int fcols=Integer.parseInt(args[2]);
	int srows=Integer.parseInt(args[3]);
	int scols=Integer.parseInt(args[4]);
	int choice=Integer.parseInt(args[5]);
	if(fcols!=srows)
	{
		System.out.println("Multiplication is not possible");
	}
	else{
		MyMatrixOP ob=new MyMatrixOP(num_th,frows,fcols,srows,scols);
		ob.instance.readMatrices();
	//	int processors = Runtime.getRuntime().availableProcessors();
        //	System.out.println("CPU cores: " + processors);
		long start=System.currentTimeMillis();
		if(choice==1){ ob.instance.multiply();}
		else { try{ ob.instance.testParallel();} catch(Exception e){ System.out.println(e); }}
		long end=System.currentTimeMillis();
		System.out.println(num_th+"    "+"    "+frows+"     "+(end-start));
		//ob.instance.printMat();
	}
}
}
