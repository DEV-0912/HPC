import java.util.Random;
class Op1{
     int num;
     int []A;
     int []B;

     Random rand;
     Op1(int n)
     {
      	num=n;
        A=new int[num];
	B=new int[num];	
	rand=new Random();
     }
     void assignValues()
     {
	for(int i=0; i<num;i++)
	{
		B[i]=rand.nextInt(num);
		A[i]=rand.nextInt(num)+1;
	}
     }

    void add()
    {
	for(int i=0;i<num;i++)
	{
		B[i]=A[i]+B[i];	
	}
    }
    void sub()
    {
	for(int i=0;i<num;i++)
	{
		B[i]=A[i]-B[i];
	}
    }
    void multi()
    {
	for(int i=0;i<num;i++)
	{
		B[i]=A[i]*B[i];
	}
    }
    void div()
    {
	for(int i=0;i<num;i++)
	{
		B[i]=B[i]/A[i];
	}
    }
}

public class MyOP1{
    Op1 instance;
    MyOP1(int num){ instance=new Op1(num);  }
    public static void main(String args[]) {
        int num=Integer.parseInt(args[0]);
	int choice=Integer.parseInt(args[1]);
	    MyOP1 ob=new MyOP1(num);
		ob.instance.assignValues();
		long start=System.currentTimeMillis();
		if(choice==1) ob.instance.add();
		else if(choice==2) ob.instance.sub();
		else if(choice==3) ob.instance.multi();
		else ob.instance.div();
		long end=System.currentTimeMillis();
		System.out.println(end-start);
    }
}
