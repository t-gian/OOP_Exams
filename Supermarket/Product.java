package supermarket;

import java.util.ArrayList;

public class Product implements Comparable<Product>{
	
	String category;
	String name;
	String price;
	Integer discount;
	
	public Product(String category, String name, String price)
	{
		this.category=category; this.name=name;this.price=price;
		discount=0;
		discountscategory.add(0);
		discountsname.add(0);
	}
	ArrayList<Integer> discountsname = new ArrayList<>();
	ArrayList<Integer> discountscategory= new ArrayList<>();
	public void setDiscountCategory(int discount)
	{
		this.discount=discount;
		discountscategory.add(discount);
		discountsname.add(discount);
	}
	public void setDiscountName(int discount)
	{
		this.discount=discount;
		discountsname.add(discount);
	}
	
	
	public float getIntPrice()
	{
		return Float.valueOf(price);
	}


	@Override
	public int compareTo(Product o) {
		// TODO Auto-generated method stub
		return this.category.compareTo(o.category);
	}

}
