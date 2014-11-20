public class Route{
	public String route;

	public Route(String route){
		this.route=route;
	}

	public Route(Route x, Route y){
		route=x.toString()+y;
	}

	@Override
	public String toString(){
		return route;
	}
}
