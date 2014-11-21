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
	long debugTime;

	public SPMap(File db) throws FileNotFoundException{
		Scanner scn=new Scanner(new BufferedInputStream(new FileInputStream(db)));
		int n=scn.nextInt();
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
					String route=scn.nextLine();
					pij.routes.add(new Route(route));
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
			return getMultiPath(stations);
		}
		else throw new RuntimeException("Too few stations.");
	}

	public Path getMultiPath(String[] stations){
		int stationCount=stations.length;
		int[] stationIndexToNum=new int[stationCount];
		for (int i=0;i!=stationCount;++i) stationIndexToNum[i]=nameToNumber.get(stations[i]);
		Path[][] dp=new Path[1<<stationCount][stationCount];
		for (Path[] i:dp)
			for (Path j:i)
				j=null;
		for (int i=0;i!=stationCount;++i)
			dp[1|(1<<i)][i]=path[stationIndexToNum[0]][stationIndexToNum[i]];
		if (stationCount>31) throw new RuntimeException("Too many stations.");
		stationIndexToNum=new int[stationCount];
		for (int state=3, limit=(1<<(stationCount-1))-1;state<limit;++state){
			for (int p=findNextTrueBit(state, 0);p<stationCount;p=findNextTrueBit(state, p))
				for (int exit=findNextFalseBit(state, 0);exit<stationCount;exit=findNextFalseBit(state, exit)){
					if (dp[state|(1<<exit)][exit]==null)
						dp[state|(1<<exit)][exit]=connect(dp[state][p], path[stationIndexToNum[p]][stationIndexToNum[exit]]);
					else if (dp[state|(1<<exit)][exit].time>dp[state][p].time+path[stationIndexToNum[p]][stationIndexToNum[exit]].time)
						dp[state|(1<<exit)][exit]=connect(dp[state][p], path[stationIndexToNum[p]][stationIndexToNum[exit]]);
					else if (dp[state|(1<<exit)][exit].time==dp[state][p].time+path[stationIndexToNum[p]][stationIndexToNum[exit]].time)
						dp[state|(1<<exit)][exit].routes.addAll(connect(dp[state][p], path[stationIndexToNum[p]][stationIndexToNum[exit]]).routes);
				}
		}
		return dp[(1<<(stationCount))-1][stationCount-1];
	}

	private int findNextFalseBit(int middleState, int p){
		middleState>>=p;
		if (middleState==-1) throw new RuntimeException("No other false bit.");
		while (true){
			middleState>>=1;
			++p;
			if ((middleState&1)==0) return p;
		}
	}

	private int findNextTrueBit(int middleState, int p){
		middleState>>=p;
		while (middleState!=0){
			middleState>>=1;
			++p;
			if ((middleState&1)==1) return p;
		}
		return Integer.MAX_VALUE;
	}

	public Path connect(Path p, Path n){
		Path t=new Path();
		t.interchange=p.interchange+n.interchange+1;
		t.time=n.time+p.time;
		for (Route i:p.routes)
			for (Route j:n.routes)
				t.routes.add(new Route(i,j));
		return t;
	}
}