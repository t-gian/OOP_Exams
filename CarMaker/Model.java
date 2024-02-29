package it.polito.oop.production;

public class Model {
	
	private String id, name;
	private float cilindrata,ismin,ismax;
	private int motorizzazione,year;
	private float is = (float) (motorizzazione*100 / (java.time.LocalDate.now().getYear()-year));
	public float getIs() {
		return is;
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getYear() {
		return year;
	}
	public float getCilindrata() {
		return cilindrata;
	}
	public int getMotorizzazione() {
		return motorizzazione;
	}
    
    public Model (String id,String name,int year,float cilindrata,int motorizzazione)
    {
    	this.id=id;this.name=name;this.year=year;this.cilindrata=cilindrata;this.motorizzazione=motorizzazione;is = (motorizzazione*100) / (float) (java.time.LocalDate.now().getYear()-year);
    }
     @Override
     public String toString()
     {
    	 return id + "," + name + "," + year + "," + cilindrata + "," + motorizzazione;
     }
     public int calcolaFascia()
     {    	 if (is<ismin) 
    		 return 0;
    	 if (ismin<= is && is<=ismax) 
    		 {
    		 
    		 return 1;
    		 }
    	 if (is > ismax) return 2;
    	 
    	 return -1;
     }
     public void setIs(float ismin,float ismax) {
			this.ismin = ismin;this.ismax=ismax;
		}
     
}
