import java.util.ArrayList;

public class Path{
	public int time=(int)1e6, interchange=0;
	public ArrayList<Route> routes=new ArrayList<>();

	public Path appendNew(Path p){
		Path t=new Path();
		t.interchange=interchange+p.interchange+1;
		t.time=time+p.time;
		for (Route i:routes)
			for (Route j:p.routes)
				t.routes.add(new Route(i,j));
		return t;
	}
}