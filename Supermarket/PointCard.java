package supermarket;

public class PointCard {

	
	int id;
	int totalpoints=0;
	int livepoints=0;
	
	public PointCard(int id)
	{
		this.id=id;
	}

	public void setTotalpoints(int points) {
	   totalpoints = totalpoints+points;
	   livepoints=livepoints+points;
	}

	public void setLivepoints(int livepoints) {
		this.livepoints = this.livepoints-livepoints;
	}
	
}
