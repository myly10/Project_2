public class Route{
	public String route;

	public Route(String route){
		this.route=route;
	}

	public Route(Route x, Route y, String middleStationName){
		route=x.toString()+middleStationName+"（下车）"+y;
	}

	@Override
	public String toString(){
		return route;
	}
}