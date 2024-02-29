package it.polito.oop.production;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Facade class used to interact with the system.
 *
 */
public class Carmaker {

	/** unique code for diesel engine **/
	public static final int DIESEL = 0;
	/** unique code for gasoline engine **/
	public static final int GASOLINE = 1;
	/** unique code for gpl engine **/
	public static final int GPL = 2;
	/** unique code for electric engine **/
	public static final int ELECTRIC = 3;

	
	// **************** R1 ********************************* //
	
	/**
	 * Add a new model to the brand factory.
	 * 
	 * Models are uniquely identified by a code
	 * 
	 * @param code 	code
	 * @param name  name
	 * @param year	year of introduction in the market
	 * @param displacement  displacement of the engine in cc
	 * @param enginetype	the engine type (e.g., gasoline, diesel, electric)
	 * @return {@code false} if code is duplicate, 
	*/
	Map<String,Model> models = new HashMap<>();
	public boolean addModel(String code, String name,  int year, float displacement, int enginetype) {
		
		if (models.containsKey(code))
			return false;
		Model m = new Model(code,name,year,displacement,enginetype);
		models.put(code, m); return true;
		
	}
	
	/**
	 * Count the number of models produced by the brand
	 * 
	 * @return models count
	 */
	public int countModels() {
		return models.size();
	}
	
	/**
	 * Retrieves information about a model.
	 * Information is formatted as code, name, year, displacement, enginetype
	 * separate by {@code ','} (comma).
	 * 	 
	 * @param code code of the searched model
	 * @return info about the model
	 */
	public String getModel(String code) {
		if(!models.containsKey(code)) return null;return models.get(code).toString();
	}
	
	
	/**
	 * Retrieves the list of codes of active models.
	 * Active models not older than 10 years with respect to the execution time.
	 * 	 
	 * @return a list of codes of the active models
	 */
	public List<String> getActiveModels() {
	  return models.values().stream().filter(x -> x.getYear()>= (java.time.LocalDate.now().getYear()-10)).map(Model::getId).collect(Collectors.toList());
	}
	
	
	/**
	 * Loads a list of models from a file.
	 * @param Filename path of the file
	 * @throws IOException in case of IO problems
	 */
	public void loadFromFile(String Filename) throws IOException  {
		String s;
		BufferedReader br= new BufferedReader(new FileReader(Filename));
		while ((s=br.readLine())!=null)
		{
			String split[] = s.split("\t");
			if (!models.containsKey(split[0]))
			    addModel(split[0],split[1],Integer.valueOf(split[2]),Float.valueOf(split[3]),Integer.valueOf(split[4]));	
		}
	}
	
	// **************** R2 ********************************* //

	
	
	/**
	 * Creates a new factory given its name. Throws Brand Exception if the name of the factory already exists.
	 * 
	 * @param name the unique name of the factory.
	 * @throws BrandException
	 */
	ArrayList<String> factoriesnames = new ArrayList<>();
	public void buildFactory(String name) throws BrandException {
		if (factoriesnames.contains(name))throw ( new BrandException(""));
		factoriesnames.add(name);
		
	}
	
	
	
	/**
	 * Returns a list of the factory names. The list is empty if no factories are created.
	 * @return A list of factory names.
	 */
	public List<String> getFactories() {
		return factoriesnames;
	}
	
	
	/**
	 * Create a set of production lines for a factory.
	 * Each production line is identified by name,capacity and type of engines it can handle.
	 * 
	 * @param fname The factory name
	 * @parm  line	An array of strings. Each string identifies a production line.
	 * 
	 * @throws BrandException if factory name is not defined or line specification is malformed
	 */
	Map<ProductionLines,Factory> produzione = new TreeMap<>();
	Map<ProductionLines,String> produzionenome = new TreeMap<>();
	public void setProductionLines (String fname, String... line) throws BrandException {
		if (!factoriesnames.contains(fname)) throw (new BrandException(""));
		Factory f=new Factory(fname);
		for (String s:line)
		{
			String split[]=s.split(":");
			if (split.length!=3) throw (new BrandException("")); if (Integer.valueOf(split[2])<0 || Integer.valueOf(split[2]) >4 || Integer.valueOf(split[1])< 0) throw (new BrandException(""));
			ProductionLines p = new ProductionLines(split[0],Integer.valueOf(split[1]),Integer.valueOf(split[2]));
			produzione.put(p, f); produzionenome.put(p,fname);
		}
	}
	
	/**
	 * Returns a map reporting for each engine type the yearly production capacity of a factory.
	 * 
	 * @param fname factory name
	 * @return A map of the yearly productions
	 * @throws BrandException if factory name is not defined or it has no lines
	 */
	public Map<Integer, Integer> estimateYearlyProduction(String fname) throws BrandException {
		if (!factoriesnames.contains(fname)) throw (new BrandException(""));
		if (!produzionenome.containsValue(fname)) throw (new BrandException(""));
		return produzionenome.entrySet().stream().filter(x -> (x.getValue().compareTo(fname))==0)
		.map(x -> x.getKey()).collect(Collectors.groupingBy(ProductionLines::getMotorizzazione,Collectors.summingInt(ProductionLines::getCapacity)));
		
	}

	// **************** R3 ********************************* //

	
	/**
	 * Creates a new storage for the car maker
	 * 
	 * @param name		Name of the storage
	 * @param capacity	Capacity (number of cars) of the storage
	 * @throws BrandException if name already defined or capacity &le; 0
	 */
	ArrayList<String> storagesnames = new ArrayList<>();
	ArrayList<Storage> storages = new ArrayList<>();
	public void buildStorage (String name, int capacity) throws BrandException {
		if (storagesnames.contains(name)) throw (new BrandException(""));
		if (capacity<=0) throw (new BrandException(""));
		Storage s= new Storage(name,capacity); storages.add(s); storagesnames.add(name);
	}
	
	/**
	 * Retrieves the names of the available storages. 
	 * The list is empty if no storage is available
	 * 
	 * @return List<String> list of storage names
	 */
	public List<String> getStorageList() {
		return storagesnames;
	}
	
	/**
	 * Add a car to the storage if possible
	 * 
	 * @param sname		storage name
	 * @param model		car model
	 * 
	 * @throws BrandException if storage or model not defined or storage is full
	 */
   HashMap<String,List<Model>> modelsinside = new HashMap<>();
   ArrayList<Model> listInsideModels=new ArrayList<>();
	public void storeCar(String sname, String model) throws BrandException {
		if (!models.containsKey(model)) throw (new BrandException(""));
		if (!storagesnames.contains(sname)) throw (new BrandException(""));
		Storage s = storages.stream().filter(x -> x.getName().compareTo(sname)==0)
				.findFirst().get();
		if (s.getCapacity()==s.getLivecapacity()) throw (new BrandException(""));
		s.addLivecapacity();
		Model m = models.get(model);listInsideModels.add(m);if (!modelsinside.containsKey(sname)) {ArrayList<Model> l = new ArrayList<>(); l.add(m);modelsinside.put(sname, l);}
		else {List<Model> l = modelsinside.get(sname);l.add(m); modelsinside.put(sname,l);}
	}
	
	/**
	 * Remove a car to the storage if possible.
	 * 
	 * @param sname		Storage name
	 * @param model		Car model
	 * @throws BrandException  if storage or model not defined or storage is empty
	 */
	public void removeCar(String sname, String model) throws BrandException {
		if (!models.containsKey(model)) throw (new BrandException(""));
		if (!storagesnames.contains(sname)) throw (new BrandException(""));
		Storage s = storages.stream().filter(x -> x.getName().compareTo(sname)==0)
				.findFirst().get();
		if (s.getLivecapacity()==0) throw (new BrandException(""));
		s.minusLivecapacity();
		Model m = models.get(model);List<Model> l = modelsinside.get(sname); 
		    for (int i=0;i<l.size();i++)if(l.get(i)==m) {l.remove(i); break;}
		    for (int i=0;i<listInsideModels.size();i++)if(listInsideModels.get(i)==m) {listInsideModels.remove(i);break;}
	}
	
	/**
	 * Generates a summary of the storage.
	 * 
	 * @param sname		storage name
	 * @return map of models vs. quantities
	 * @throws BrandException if storage is not defined
	 */
	public Map<String,Integer> getStorageSummary(String sname) throws BrandException {
		if (!storagesnames.contains(sname)) throw (new BrandException(""));
		List<Model> l=modelsinside.get(sname);Map<String,Long> m = l.stream().collect(Collectors.groupingBy(Model::getId,Collectors.counting())); return m.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().intValue()));
	}
	
	// **************** R4 ********************************* //
	
	
	/**
	 * Sets the thresholds for the sustainability level.
	 * 
	 * @param ismin	lower threshold
	 * @param ismax upper threshold
	 */
	 float ismin,ismax;
	public void setISThresholds (float ismin, float ismax) {
	   models.values().stream().forEach(x -> x.setIs(ismin, ismax));
	}
	
	
	
	
	/**
	 * Retrieves the models classified in the given Sustainability class.
	 * 
	 * @param islevel sustainability level, 0:low 1:medium 2:high
	 * @return the list of model names in the class
	 */
	public List<String> getModelsSustainability(int islevel) {
	return models.values().stream().filter(x -> (x.calcolaFascia()==islevel)).map(x -> x.getId()).collect(Collectors.toList());
	}
	
	
	/**
	 * Computes the Carmaker Sustainability level
	 * 
	 * @return sustainability index
	 */
	public float getCarMakerSustainability() {
	 double d=listInsideModels.stream().collect(Collectors.summingDouble(Model::getIs));return (float)d/listInsideModels.size();
	}
	
	// **************** R5 ********************************* //

	/**
	 * Generates an allocation production plan
	 * 
	 * @param request allocation request string
	 * @return {@code true} is planning was successful
	 */
	public boolean plan(String request) {
		String r[]=request.split(",");
		for(String s:r)
		{
			String ss[]=s.split(":");Model m=models.get(ss[0]);
			int x;int q=Integer.parseInt(ss[1]);
			for (ProductionLines l:produzione.keySet())
				if(l.getMotorizzazione()==m.getMotorizzazione()) 
				{ l.setCapacity(x = Math.min(q, l.remaining));q -=x;}
		        if(q!=0) return false;
		}
		return true;
	    }
	
	
	
	/**
	 * Returns the capacity of a line
	 * 
	 * @param fname factory name
	 * @param lname line name
	 * @return total capacity of the line
	 */
	public int getLineCapacity(String fname, String lname) {
		return produzione.keySet().stream().filter(x -> x.getName().equals(lname)).findFirst().get().getCapacity();
	}
	
	/**
	 * Returns the allocated capacity of a line
	 * @param fname factory name
	 * @param lname line name
	 * @return already allocated capacity for the line
	 */
	public int getLineAllocatedCapacity(String fname, String lname) {
		return produzione.keySet().stream().filter(x -> x.getName().equals(lname)).findFirst().get().livecapacity;
	}
	
	
	
	// **************** R6 ********************************* //
	
	/**
	 * compute the proportion of lines that are fully allocated
	 * (i.e. allocated capacity == total capacity) as a result
	 * of previous calls to method {@link #plan}
	 * 
	 * @return proportion of lines fully allocated
	 */
	public float linesfullyAllocated() {
		return (float) produzione.keySet().stream().filter(x -> x.remaining==0).collect(Collectors.counting())/produzione.size();
	}

	/**
	 * compute the proportion of lines that are unused
	 * (i.e. allocated capacity == 0) as a result
	 * of previous calls to method {@link #plan}
	 * 
	 * @return proportion of unused lines
	 */
	public float unusuedLines() {
		return (float)produzione.keySet().stream().filter(x -> x.remaining==x.getCapacity()).collect(Collectors.counting())/produzione.size();
	}
}
