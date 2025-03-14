import java.util.Random;
class CacheOp{
     int num;
     int []A;
     int []B;
     int []C;
     Random rand;
     CacheOp(int n)
     {
      	num=n;
        A=new int[num];
	B=new int[num];
	C=new int[num];
	rand=new Random();
     }
     void assignValues()
     {
	for(int i=0; i<num;i++)
	{
		A[i]=i;
		B[i]=rand.nextInt(num);
	}
     }

    void readRandom()
    {
	for(int i=0;i<num;i++)
	{
		C[i]=A[B[i]];	
	}
    }
    void readSeq()
    {
	for(int i=0;i<num;i++)
	{
		C[i]=B[A[i]];
	}

    }
}

public class MyCacheProg1 {
    CacheOp instance;
    
    MyCacheProg1(int num){ instance=new CacheOp(num); }
    public static void main(String args[]) {
        int num=Integer.parseInt(args[0]);
	int choice=Integer.parseInt(args[1]);
	    MyCacheProg1 ob=new MyCacheProg1(num);
		ob.instance.assignValues();
		long start=System.currentTimeMillis();
		if(choice==1) ob.instance.readSeq();
		else ob.instance.readRandom();
		long end=System.currentTimeMillis();
		System.out.println(end-start);
    }
}
