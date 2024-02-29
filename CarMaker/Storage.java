package it.polito.oop.production;

public class Storage {
	
	private String name;
	private int capacity;
	private int livecapacity=0;
	
	public Storage (String name , int capacity)
	{
		this.name=name; this.capacity=capacity;
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}
	public int getLivecapacity() {
		return livecapacity;
	}
	public void addLivecapacity()
	{
		livecapacity=livecapacity+1;
	}
	public void minusLivecapacity()
	{
		livecapacity=livecapacity-1;
	}

}
