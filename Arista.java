import java.util.HashMap;
import java.io.*;

class Arista{
    //String Id;
    Nodo n1, n2;
	HashMap <String, String> datos;//weight, directed, autoconection

    public Arista(Nodo newn1, Nodo newn2){
        //this.Id = newId;
        this.n1 = newn1;
        this.n2 = newn2;
        this.datos = new HashMap<String, String>();
    }
    
    public void setData(String key, String value){
    	this.datos.put(key,value);
    }

    public String getData(String key){
		for (String value : this.datos.keySet()) {
			if (value == key)
				return this.datos.get(key);
		}
		return "invalid";
    }
    
    public Nodo[] getNodos(){
    	Nodo[] temp = {this.n1, this.n2};
    	return temp; 
    }

   	public static void main(String args[]) throws IOException,ClassNotFoundException {
   		Nodo j = new Nodo("nj");
   		Nodo k = new Nodo("nk");
   		Arista jk = new Arista(j,k);
   		jk.setData("weight","20");
   		System.out.println(jk.getData("weightw"));
   		System.out.println(jk.getData("weight"));
   		System.out.println(jk.getNodos()[0].getName());
   		System.out.println(jk.getNodos()[1].getName());
	}  
   
}
  
    
    

