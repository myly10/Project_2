import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Project_2{
	public static void main(String[] args) throws FileNotFoundException{
		//System.out.print("Initializing database...");
		long time=System.currentTimeMillis();
		System.out.print("Stations > ");
		File db=new File("SPMap.db");
		SPMap spm=new SPMap(db);
		//System.out.println("done in "+(System.currentTimeMillis()-time)+"ms");
		String stationsLine;
		Vector<String> stations=new Vector<>(15);
		Scanner stationsReader=new Scanner(System.in);
		while (true){
			stationsLine=stationsReader.nextLine();
			if (stationsLine.equals("exit")) return;
			Scanner scn=new Scanner(stationsLine);
			stations.clear();
			while (scn.hasNext()) stations.add(scn.next());
			time=System.currentTimeMillis();
			String[] t=new String[stations.size()];
			stations.toArray(t);
			try{
				Path p=spm.getPath(t);
				time=System.currentTimeMillis()-time;
				System.out.println("Time: "+p.time+"min\nInterchange: "+p.interchange+"\nRoute: ");
				for (Route i : p.routes) System.out.println(i+stations.get(stations.size()-1));
				System.out.println("Finished in "+time+"ms.");
			}catch (StationNotFoundException ex){
				System.out.println("ERROR: Station "+ex.getMessage()+" doesn't exist.");
			}catch (RuntimeException ex){
				System.out.println("ERROR: "+ex.getMessage());
			}
			System.out.println();
			System.out.print("Stations > ");
		}
	}
}