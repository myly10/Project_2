import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
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
		if (stationCount>31) throw new RuntimeException("Too many stations.");
		int[] stationIndexToNum=new int[stations.length];
		for (int i=0;i!=stations.length;++i) stationIndexToNum[i]=nameToNumber.get(stations[i]);
		Path[][] dp=new Path[((1<<stations.length)+1)>>1][stations.length];
		for (Path[] i:dp)
			Arrays.fill(i, null);
		for (int i=0;i!=stations.length;++i)
			dp[(1|(1<<i))>>1][i]=path[stationIndexToNum[0]][stationIndexToNum[i]];
		for (int state=3, limit=(1<<(stationCount-1))-1;state<limit;state+=2)
			for (int p=findNextTrueBit(state, 0);p<stationCount-1;p=findNextTrueBit(state, p))
				for (int exit=findNextFalseBit(state, 0);exit<stationCount-1;exit=findNextFalseBit(state, exit)){
					int nextState=(state|(1<<exit));
					if (nextState>limit) continue;
					if (dp[nextState>>1][exit]==null ||
								dp[nextState>>1][exit].time>dp[state>>1][p].time+path[stationIndexToNum[p]][stationIndexToNum[exit]].time)
						dp[nextState>>1][exit]=connect(dp[state>>1][p], path[stationIndexToNum[p]][stationIndexToNum[exit]], stations[p]);
					else if (dp[nextState>>1][exit].time==dp[state>>1][p].time+path[stationIndexToNum[p]][stationIndexToNum[exit]].time){
						if (dp[nextState>>1][exit].interchange>dp[state>>1][p].interchange+path[stationIndexToNum[p]][stationIndexToNum[exit]].interchange+1)
							dp[nextState>>1][exit]=connect(dp[state>>1][p],path[stationIndexToNum[p]][stationIndexToNum[exit]], stations[p]);
						else if (dp[nextState>>1][exit].interchange==dp[state>>1][p].interchange+path[stationIndexToNum[p]][stationIndexToNum[exit]].interchange+1)
							dp[nextState>>1][exit].routes.addAll(connect(dp[state>>1][p], path[stationIndexToNum[p]][stationIndexToNum[exit]], stations[p]).routes);
					}
				}
		final int exit=stationCount-1, prev=(1<<(stationCount-2))-1;
		Path result=null;
		for (int i=1;i!=stationCount-1;++i){
			if (result==null ||
						result.time>dp[prev][i].time+path[stationIndexToNum[i]][stationIndexToNum[exit]].time)
				result=connect(dp[prev][i], path[stationIndexToNum[i]][stationIndexToNum[exit]], stations[i]);
			else if (result.time==dp[prev][i].time+path[stationIndexToNum[i]][stationIndexToNum[exit]].time){
				if (result.interchange>dp[prev][i].interchange+path[stationIndexToNum[i]][stationIndexToNum[exit]].interchange+1)
					result=connect(dp[prev][i],path[stationIndexToNum[i]][stationIndexToNum[exit]], stations[i]);
				else if (result.interchange==dp[prev][i].interchange+path[stationIndexToNum[i]][stationIndexToNum[exit]].interchange+1)
					result.routes.addAll(connect(dp[prev][i],path[stationIndexToNum[i]][stationIndexToNum[exit]], stations[i]).routes);
			}
		}
		System.gc();
		return result;
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

	public Path connect(Path p, Path n, String middleStationName){
		Path t=new Path();
		t.interchange=p.interchange+n.interchange+1;
		t.time=n.time+p.time;
		for (Route i : p.routes)
			for (Route j : n.routes)
				t.routes.add(new Route(i, j, middleStationName));
		return t;
	}
}