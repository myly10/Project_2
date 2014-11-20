public class Route{
	public int startLine, stopLine;
	public String startStation;
	public String route;
	public String startLineUID, stopLineUID;
	public boolean isStartSlaveLine, isStopSlaveLine;

	public Route(int startLine, String startStation, int stopLine, String route, String startLineUID, String stopLineUID, boolean isStartSlaveLine, boolean isStopSlaveLine){
		this.startLine=startLine;
		this.startStation=startStation;
		this.stopLine=stopLine;
		this.route=route;
		this.startLineUID=startLineUID;
		this.stopLineUID=stopLineUID;
		this.isStartSlaveLine=isStartSlaveLine;
		this.isStopSlaveLine=isStopSlaveLine;
	}

	public Route(Route x, Route y, int yInterchange){
		startLine=x.startLine;
		startStation=x.startStation;
		stopLine=y.stopLine;
		route=(!SPMapGenerator.isInterchange(x,y))?(yInterchange==0?x.route:x.route+y.route):x.route+y.startStation+"-Line "+y.startLine+"-"+y.route;
		startLineUID=x.startLineUID;
		stopLineUID=y.stopLineUID;
		isStartSlaveLine=x.isStartSlaveLine;
		isStopSlaveLine=y.isStopSlaveLine;
	}

	@Override
	public String toString(){
		return startLine+"\n"+stopLine+"\n"+startLineUID+"\n"+stopLineUID+"\n"+isStartSlaveLine+"\n"+isStopSlaveLine+"\n"+startStation+"-Line "+startLine+"-"+route+"\n";
	}

	@Override
	public boolean equals(Object o){
		if (this==o) return true;
		if (o==null || getClass()!=o.getClass()) return false;

		Route route1=(Route)o;

		if (isStartSlaveLine!=route1.isStartSlaveLine) return false;
		if (isStopSlaveLine!=route1.isStopSlaveLine) return false;
		if (startLine!=route1.startLine) return false;
		if (stopLine!=route1.stopLine) return false;
		if (route!=null?!route.equals(route1.route):route1.route!=null) return false;
		if (!startLineUID.equals(route1.startLineUID)) return false;
		if (!startStation.equals(route1.startStation)) return false;
		if (!stopLineUID.equals(route1.stopLineUID)) return false;

		return true;
	}
}
