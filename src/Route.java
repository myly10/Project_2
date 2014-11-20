public class Route{
	public int startLine, stopLine;
	public String route;
	public String startLineUID, stopLineUID;
	public boolean isStartSlaveLine, isStopSlaveLine;

	public Route(int startLine, int stopLine, String startLineUID, String stopLineUID, boolean isStartSlaveLine, boolean isStopSlaveLine, String route){
		this.startLine=startLine;
		this.stopLine=stopLine;
		this.route=route;
		this.startLineUID=startLineUID;
		this.stopLineUID=stopLineUID;
		this.isStartSlaveLine=isStartSlaveLine;
		this.isStopSlaveLine=isStopSlaveLine;
	}

	public Route(Route x, Route y){
		startLine=x.startLine;
		stopLine=y.stopLine;
		route=x.toString()+y;
		startLineUID=x.startLineUID;
		stopLineUID=y.stopLineUID;
		isStartSlaveLine=x.isStartSlaveLine;
		isStopSlaveLine=y.isStopSlaveLine;
	}

	@Override
	public String toString(){
		return route;
	}
}
