import javax.swing.*;
import java.io.IOException;

public class SPGenerator{
	public static void main(String[] args) throws IOException{
		int it=Integer.parseInt(JOptionPane.showInputDialog(null, "Input interchange time:"));
		long time=System.currentTimeMillis();
		SPMapGenerator smp=new SPMapGenerator(it);
		smp.readFile();
		System.out.println(System.currentTimeMillis()-time);
	}
}