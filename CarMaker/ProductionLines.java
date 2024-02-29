package it.polito.oop.production;

public class ProductionLines implements Comparable<ProductionLines>{
	private String name;
	private int capacity,motorizzazione;int livecapacity,remaining;
	public ProductionLines(String name,int capacity,int motorizzazione)
	{
		this.name=name; this.capacity=capacity;this.motorizzazione=motorizzazione;remaining=capacity;
	}
	public String getName() {
		return name;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getMotorizzazione() {
		return motorizzazione;
	}
	public void setCapacity(int qty)
	{
		livecapacity=livecapacity+qty; remaining=remaining-qty;
	}
	public int compareTo(ProductionLines o) {
		return this.getName().compareTo(o.getName());
	}
}
