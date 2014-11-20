import java.io.*;
import java.lang.Object;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class SPMap{
	public HashMap<String,Integer> nameToNumber;
	public Path[][] path;
	public String[] numberToName;
	long debugCount, debugTime;
	static int interchangeTime;

	public SPMap(File db) throws FileNotFoundException{
		Scanner scn=new Scanner(new BufferedInputStream(new FileInputStream(db)));
		int n=scn.nextInt();
		interchangeTime=scn.nextInt();
		nameToNumber=new HashMap<>(n*2);
		path=new Path[n][n];
		numberToName=new String[n];
		for (int i=0;i!=n;++i)
			for (int j=0;j!=n;++j)
				path[i][j]=new Path();
		scn.nextLine();
		for (int i=0;i!=n;++i){
			numberToName[i]=scn.nextLine();
			nameToNumber.put(numberToName[i],i);
		}
		for (int i=0;i!=n;++i)
			for (int j=0;j!=n;++j){
				Path pij=path[i][j];
				pij.time=scn.nextInt();
				pij.interchange=scn.nextInt();
				int t=scn.nextInt();
				scn.nextLine();
				for (int x=0;x!=t;++x){
					int startLine=scn.nextInt(), stopLine=scn.nextInt();
					scn.nextLine();
					String startLineUID=scn.nextLine(), stopLineUID=scn.nextLine();
					boolean isStartSlaveLine=scn.nextBoolean(), isStopSlaveLine=scn.nextBoolean();
					scn.nextLine();
					String route=scn.nextLine();
					pij.routes.add(new Route(startLine,stopLine,startLineUID,stopLineUID,isStartSlaveLine,isStopSlaveLine,route));
				}
			}
	}

	public Path getPath(String[] stations){
		for (String i:stations)
			if (!nameToNumber.containsKey(i))
				throw new StationNotFoundException(i);
		if (stations.length==2)
			return path[nameToNumber.get(stations[0])][nameToNumber.get(stations[1])];
		else if (stations.length>2){
			debugTime=System.currentTimeMillis();
			return new MultiPath(stations, ~(-1<<stations.length));
		}
		else throw new RuntimeException("Too few stations.");
	}

	private class MultiPath extends Path{
		long middleState=0;
		int stationCount, src, dst;
		int[] stationIndexToNum;

		public MultiPath(String[] stations, int middleState){
			HashMap<Long, MultiPath> dp=new HashMap<>(stationCount*stationCount*2);
			stationCount=stations.length;
			if (stationCount>31) throw new RuntimeException("Too many stations.");
			stationIndexToNum=new int[stationCount];
			for (int i=0;i!=stationCount;++i) stationIndexToNum[i]=nameToNumber.get(stations[i]);
			for (int s=0;s!=stationCount;++s)
				for (int d=s+1;d!=stationCount;++d){
					MultiPath t=new MultiPath(stations, (1<<))
					dp.put()
				}
			for (int c=2;c!=stationCount;++c){
				for (int s=1;s!=stationCount-1;++s)
					for (int d=1;d!=stationCount-1;++d){
						if (d==s) continue;
						permutation();
					}
			}
		}

		public void permutation(int middleStatus, int first, int last, int c, int s, int d, int depth, HashMap<Long, MultiPath> dp){
			if(first==last || depth==c){
				//TODO
			}

			for(int i=first;i<=last;i++){
				permutation(mark(middleStatus, i), first+1, last, c, s, d, depth+1, dp);
			}
		}

		private int mark(int str, int i) {
			return str|(1<<i);
		}

		@Override
		public int hashCode(){
			return Long.valueOf((((((long)src<<9)|(dst<<9))<<64-18)|middleState)>>32).hashCode();
		}

		public Long toLongKey(){return (((((long)src<<9)|(dst<<9))<<(64-18))|middleState);}

		public MultiPath connect(String[] stations){
			MultiPath t=new MultiPath();
			t.interchange=(int)3e6;
			t.time=time+p.time;
			for (Route i:routes)
				for (Route j:p.routes){
					if (interchange+p.interchange+1<t.interchange){
						t.interchange=interchange+p.interchange+1;
						t.routes.clear();
						t.routes.add(new Route(i,j));
					}
					else if (interchange+p.interchange+1==t.interchange)
						t.routes.add(new Route(i,j));
				}
			return t;
		}
	}
}