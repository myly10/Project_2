import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class SPMapGenerator{
	HashMap<String,Integer> nameToNumber=new HashMap<>();
	Path[][] path=new Path[1000][1000];
	File outFile=new File("SPMap.db");
	FileWriter w;
	String tempString="";
	int n=0, interchangeTime=3;

	public SPMapGenerator() throws IOException{
		w=new FileWriter(outFile);
		for (int i=0;i!=path.length;++i)
			for (int j=0;j!=path.length;++j)
				path[i][j]=new Path();
	}

	public void setRoute(String as, String bs, int d, int lineNum, String lineUID, boolean isSlaveLine){
		int a=nameToNumber.get(as), b=nameToNumber.get(bs);
		if (path[a][b].time<d)
			return;
		if (path[a][b].time>d){path[a][b].routes.clear(); path[b][a].routes.clear();}
		path[a][b].time=d;
		path[b][a].time=d;
		path[a][a].time=0;
		path[b][b].time=0;
		path[a][b].routes.add(new Route(lineNum, as, lineNum, "", lineUID, lineUID, isSlaveLine, isSlaveLine));
		path[b][a].routes.add(new Route(lineNum, bs, lineNum, "", lineUID, lineUID, isSlaveLine, isSlaveLine));
	}

	public void readFile() throws IOException{
		File dir=new File("Timetable");
		int indx=0;
		String prev, cur;
		for (File i : dir.listFiles()){
			Scanner scn=new Scanner(i);
			int lineNum=scn.nextInt();
			boolean isSlaveLine=false;
			if (scn.hasNextBoolean())
				isSlaveLine=scn.nextBoolean();
			scn.nextLine();
			if (!nameToNumber.containsKey(prev=scn.nextLine())){
				nameToNumber.put(prev, indx);
				printPair(prev);
				++indx;
			}
			while (scn.hasNextLine()){
				if (!nameToNumber.containsKey(cur=scn.nextLine())){
					nameToNumber.put(cur, indx);
					printPair(cur);
					++indx;
				}
				try{
					setRoute(prev, cur, scn.nextInt(), lineNum, i.getName(), isSlaveLine);
				}catch (Exception ex){
					ex.printStackTrace();
				}
				if (scn.hasNextLine()) scn.nextLine();
				prev=cur;
			}
		}
		n=indx;
		Path[][] t=new Path[n][n];
		for (int i=0;i!=n;++i)
			for (int j=0;j!=n;++j)
				t[i][j]=path[i][j];
		path=t;
		System.gc();
		generateSP();
		printSP();
		w.close();
	}

	public void generateSP(){
		for (int k=0;k!=n;++k)
			for (int i=0;i!=n;++i)
				for (int j=0;j!=n;++j){
					Path pij=path[i][j], pik=path[i][k], pkj=path[k][j];
					if (pik.time+pkj.time+interchangeCost(pik, pkj)<pij.time){
						pij.time=pik.time+pkj.time+interchangeCost(pik, pkj);
						pij.routes.clear();
						pij.interchange=(int)3e6;
						for (Route x : pik.routes)
							for (Route y : pkj.routes){
								if (pik.interchange+pkj.interchange+(isInterchange(x,y)?1:0)<pij.interchange){
									pij.interchange=pik.interchange+pkj.interchange+(isInterchange(x,y)?1:0);
									pij.routes.clear();
									pij.routes.add(new Route(x, y, pkj.interchange));
								}else if (pik.interchange+pkj.interchange+(isInterchange(x,y)?1:0)==pij.interchange)
									pij.routes.add(new Route(x, y, pkj.interchange));
							}
					}else if (i!=k && i!=j && k!=j && pik.time+pkj.time+interchangeCost(pik, pkj)==pij.time){
						for (Route x : pik.routes){
							for (Route y : pkj.routes){
								if (pik.interchange+pkj.interchange+((isInterchange(x, y))?1:0)<pij.interchange){
									pij.interchange=pik.interchange+pkj.interchange+(isInterchange(x, y)?1:0);
									pij.routes.clear();
									pij.routes.add(new Route(x, y, pkj.interchange));
								}else if (pik.interchange+pkj.interchange+(isInterchange(x, y)?1:0)==pij.interchange)
									pij.routes.add(new Route(x, y, pkj.interchange));
							}
						}
					}
				}
	}

	public static boolean isInterchange(Route x, Route y){
		return !(x.stopLineUID.equals(y.startLineUID) || (x.stopLine==y.startLine && !(x.isStopSlaveLine && y.isStartSlaveLine)));
	}

	public int interchangeCost(Path x, Path y){
		for (Route i:x.routes)
			for (Route j:y.routes)
				if (!isInterchange(i, j)) return 0;
		return interchangeTime;
	}

	public void printPair(String name) throws IOException{
		tempString+=name+"\n";
	}

	public void printSP() throws IOException{
		w.write(""+n+" "+interchangeTime+"\n");
		w.write(tempString);
		for (int i=0;i!=n;++i)
			for (int j=0;j!=n;++j)
				w.write(path[i][j].toString());
	}
}
