package main;

public class Station {
	 
	private String name = "";
	private int dist = 0;
	
	/**
	 * Constructor for Station object
	 * @param name
	 * @param dist
	 */
	public Station(String name, int dist) {
		this.name = name;
		this.dist = dist;
	}
	
	/**
	 * This method returns the value of the station name
	 * @return name
	 */
	public String getCityName() {
		return name;
	}
	
	/**
	 * This method sets the value of the station name
	 * @param cityName
	 */
	public void setCityName(String cityName) {
		this.name = cityName;
	}
	
	/**
	 * This method returns the value of the station distance
	 * @return dist
	 */
	public int getDistance() {
		return dist;
	}
	
	/**
	 * This method sets the value of the station distance
	 * @param distance
	 */
	public void setDistance(int distance) {
		this.dist = distance;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		return this.getCityName().equals(other.getCityName()) && this.getDistance() == other.getDistance();
	}
	@Override
	public String toString() {
		return "(" + this.getCityName() + ", " + this.getDistance() + ")";
	}
	
	public static void main(String[] args) {
		
		Station test1 = new Station("Alaska", 31);
		System.out.println(test1.getCityName());
		System.out.println(test1.getDistance());
		test1.setCityName("Georgia");
		test1.setDistance(48);
		System.out.println(test1.getCityName());
		System.out.println(test1.getDistance());

	}

}
