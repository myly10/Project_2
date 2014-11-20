import java.io.IOException;

public class SPGenerator{
	public static void main(String[] args) throws IOException{
		long time=System.currentTimeMillis();
		SPMapGenerator smp=new SPMapGenerator();
		smp.readFile();
		System.out.println(System.currentTimeMillis()-time);
	}
}