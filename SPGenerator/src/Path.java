import java.util.ArrayList;

public class Path{
	public int time=(int)1e6, interchange=0;
	public ArrayList<Route> routes=new ArrayList<>();

	@Override
	public String toString(){
		String s=""+time+" "+interchange+" "+routes.size()+"\n";
		for (Route i:routes)
			s+=i;
		return s;
	}
}
