import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class SPMap{
	public HashMap<String,Integer> nameToNumber;
	public Path[][] path;
	public Path multiBest;
	public String[] numberToName;
	long debugCount, debugTime;

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
			return getPath(stations[0], stations[1]);
		else if (stations.length>2){
			multiBest=new Path();
			debugTime=System.currentTimeMillis();
			permutation(stations, 1, stations.length-2);
			return multiBest;
		}
		else throw new RuntimeException("Too few stations.");
	}

	public Path getPath(String src, String dst){
		return path[nameToNumber.get(src)][nameToNumber.get(dst)];
	}

	public Path getMultiPathPerm(String[] stations){
		Path t=getPath(stations[0], stations[1]);
		if (stations.length<3) throw new RuntimeException("Too few stations.");
		int preTimeCount=t.time;
		for (int i=2;i!=stations.length;++i)
			preTimeCount+=getPath(stations[i-1], stations[i]).time;
		if (preTimeCount>multiBest.time) return null;
		for (int i=2;i!=stations.length;++i){
			t=t.appendNew(getPath(stations[i-1], stations[i]));
		}
		if (t.time<multiBest.time || (t.time==multiBest.time && t.interchange<multiBest.interchange)){
			multiBest=t;
		}
		else if (t.time==multiBest.time && t.interchange==multiBest.interchange)
			multiBest.routes.addAll(t.routes);
		return multiBest;
	}

	public void permutation(String[] str, int first, int last){
		if(first == last) {
			++debugCount;
			if (System.currentTimeMillis()-debugTime>1000){
				debugTime=System.currentTimeMillis();
				System.out.print(""+(new DecimalFormat("0.000").format((double)debugCount/(6227020800L)*100))+"% ");
				for (String i:str) System.out.print(i+" ");
				System.out.println();
			}
			getMultiPathPerm(str);
		}

		for(int i = first; i <= last ; i++) {
			swap(str, i, first);
			permutation(str, first+1, last);
			swap(str, i, first);
		}
	}

	private void swap(String[] str, int i, int first) {
		String tmp;
		tmp = str[first];
		str[first] = str[i];
		str[i] = tmp;
	}
}