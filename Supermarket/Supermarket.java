package supermarket;
import java.util.*;
import java.util.stream.Collectors;

public class Supermarket {

	//R1
	
	HashMap<Product,String> productcategories = new HashMap<>();
	HashMap<String,Product> productnames = new HashMap<>();
	ArrayList<String> categories = new ArrayList<>();
	public int addProducts (String categoryName, String productNames, String productPrices) throws SMException {
		if (productcategories.containsValue(categoryName))
			throw (new SMException(""));
		String split_names[]=productNames.split(",");
		String split_prices[]=productPrices.split(",");
		if (split_names.length!=split_prices.length)
			throw (new SMException (""));
		for (String s: split_names)
		{
			if (productnames.containsKey(s))
				throw (new SMException(""));
		}
		int count=0;
		categories.add(categoryName);
		for(int i=0;i<split_names.length;i++)
		{
		
			Product p = new Product(categoryName,split_names[i],split_prices[i]);
			productcategories.put(p, categoryName);
			productnames.put(split_names[i],p);
			count++;	
		}
		return count;
	}

	public double getPrice (String productName) throws SMException {
		if(!productnames.containsKey(productName))
			throw (new SMException(""));
		return productnames.get(productName).getIntPrice();
	}
	
	public String maxpricecategory(String categoryname)
	{
	     Product p= productcategories.entrySet().stream()
	      .filter(x -> x.getValue().compareTo(categoryname)==0)
	      .map(x -> x.getKey()).max((x,y) -> Math.round(x.getIntPrice()-y.getIntPrice())).get();
	     return p.name;
	}
    
	public SortedMap<String,String> mostExpensiveProductPerCategory () {
		SortedMap<String,String> sm = new TreeMap<>();
		for (String s:categories)
		{
			sm.put(s, maxpricecategory(s));
		}
		return sm;
		
	}
	

	//R2
	public void setDiscount (String categoryName, int percentage) throws SMException {
		if (!productcategories.containsValue(categoryName))
			throw (new SMException(""));
		if (percentage>40)
			throw (new SMException(""));
		productcategories.keySet()
		.stream().filter(x -> x.category.compareTo(categoryName)==0)
		.forEach(x -> x.setDiscountCategory(percentage));
		
	}

	public void setDiscount (int percentage, String... productNames) throws SMException {
		if (percentage>40)
			throw (new SMException(""));
	     for (String s: productNames)
	     {
	    		productnames.values().stream()
	    		.filter(x -> x.name.compareTo(s)==0)
	    		.forEach(x -> x.setDiscountName(percentage));
	     }
	}

	public List<Integer> getDiscountHistoryForCategory(String categoryName) {
		Product p = productcategories.keySet()
				.stream().filter(x -> x.category.compareTo(categoryName)==0)
				.findFirst().get();
		return p.discountscategory;
				
	}

	public List<Integer> getDiscountHistoryForProduct(String productName) {
		Product p = productnames.values()
				.stream().filter(x -> x.name.compareTo(productName)==0)
				.findFirst().get();
		return p.discountsname;
	}

	//R3
	ArrayList<String> check = new ArrayList<>();
	HashMap<Customer,PointCard> cards = new HashMap<>();
	int id=1000;
	public int issuePointsCard (String name, String dateOfBirth) throws SMException {
		if (check.contains(name+dateOfBirth))
			throw (new SMException(""));
		check.add(name+dateOfBirth);
		Customer c = new Customer(name,dateOfBirth);
		PointCard p= new PointCard(id);
		cards.put(c, p);
		return id++;
	}


   TreeMap<Integer,Integer> discountmap= new TreeMap<>();
	public void fromPointsToDiscounts (String points, String discounts) throws SMException {
		String s[]=points.split(",");
		String ss[]=discounts.split(",");
		if (s.length!=ss.length)
			throw (new SMException(""));
		for (int i=0;i<s.length;i++)
		{
			discountmap.put(Integer.valueOf(s[i]),Integer.valueOf(ss[i]));
		}
		
	}

	public SortedMap<Integer, Integer>  getMapPointsDiscounts() {
		return discountmap;
	}

	public int getDiscountFromPoints (int points) {
		if (!discountmap.containsKey(points))
			return 0;
		return discountmap.get(points);
	}

	//R4
    
	
	public int getCurrentPoints (int pointsCardCode) throws SMException {
		if (cards.values().stream().filter(x -> x.id==pointsCardCode).findFirst().get()==null)
			throw (new SMException(""));
		return cards.values().stream().filter(x -> x.id==pointsCardCode).findFirst().get().livepoints;
		
	}

	public int getTotalPoints (int pointsCardCode) throws SMException {
		if (cards.values().stream().filter(x -> x.id==pointsCardCode).findFirst().get()==null)
			throw (new SMException(""));
		return cards.values().stream().filter(x -> x.id==pointsCardCode).findFirst().get().totalpoints;
	}
	int purchasecode=100;
	HashMap<Integer,Purchase> purchases = new HashMap<>();
	public int addPurchase (int pointsCardCode, int pointsRedeemed, String ... productNames) throws SMException {
		if (pointsRedeemed > (cards.values().stream().filter(x -> x.id==pointsCardCode).findFirst().get().livepoints))
			throw (new SMException(""));
		Purchase p=new Purchase(purchasecode);
		float price=0; float dis_price=0;
		for (String s:productNames)
		{
			price=price+productnames.get(s).getIntPrice();
			dis_price= dis_price + productnames.get(s).getIntPrice()-(productnames.get(s).getIntPrice()*productnames.get(s).discount/100);
			p.price=dis_price;
			p.discount=price-dis_price;
		}
		if(discountmap.containsKey(pointsRedeemed))
			{
			dis_price=dis_price-discountmap.get(pointsRedeemed);
			cards.values().stream().filter(x -> x.id==pointsCardCode).findFirst().get().setLivepoints(pointsRedeemed);
			p.price=dis_price; p.discount=price-dis_price;
			}
		purchases.put(purchasecode,p);
		int points=Math.round(dis_price);
		cards.values().stream().filter(x -> x.id==pointsCardCode).findFirst().get().setTotalpoints(points);
		return purchasecode++;
	}


	public double getPurchasePrice (int purchaseCode) throws SMException {
		if(!purchases.containsKey(purchaseCode))
			throw (new SMException(""));
		return purchases.get(purchaseCode).price;
	}

	public double getPurchaseDiscount (int purchaseCode) throws SMException {
		if(!purchases.containsKey(purchaseCode))
			throw (new SMException(""));
		return purchases.get(purchaseCode).discount;
	}

	
	//R5

	public SortedMap<Integer, List<Integer>> pointsCardsPerTotalPoints () {
		return cards.values().stream().filter(p -> p.totalpoints!=0)
				.collect(Collectors.groupingBy(x -> x.totalpoints,()->new TreeMap<Integer,List<Integer>>(),Collectors.mapping(s -> s.id, Collectors.toList())));
	}


	public SortedMap<String, SortedSet<String>> customersPerCategory () {
		return null;
	}

	public SortedMap<Integer, List<String>> productsPerDiscount() {
		return null;
	}


	// R6

	public int newReceipt() { // return code of new receipt
		return -1;
	}

	public void receiptAddCard(int receiptCode, int PointsCard)  throws SMException { // add the points card info to the receipt
	}

	public int receiptGetPoints(int receiptCode)  throws SMException { // return available points on points card if added before
		return -1;
	}

	public void receiptAddProduct(int receiptCode, String product)  throws SMException { // add a new product to the receipt
	}

	public double receiptGetTotal(int receiptCode)  throws SMException { // return current receipt code
		return -1;
	}

	public void receiptSetRedeem(int receiptCode, int points)  throws SMException { // sets the amount of points to be redeemed
	}

	public int closeReceipt(int receiptCode)  throws SMException { // close the receipt and add the purchase (calls addPurchase() ) and return purchase code (could be the same as receipt code)
		return -1;
	}


}