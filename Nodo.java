import java.util.HashMap;
import java.util.Comparator;
import java.io.*;
//
class Nodo {
    String nombre;
    HashMap <String, String> datos; //color, posx, posy

    public Nodo(String newNombre){
        this.nombre = newNombre;
        this.datos = new HashMap <String, String>();
    }

    public String getName(){
    	return this.nombre;
    }
    
    public void setData(String key,String value){
    	this.datos.put(key, value);
    }

    public String getData(String key){
		for (String value : this.datos.keySet()) {
			if (value == key)
				return this.datos.get(key);
		}
		return "invalid";
    }

	public static void main(String args[]) throws IOException,ClassNotFoundException {
        Nodo j = new Nodo("n1");
        j.getName();
        System.out.println(j.getName());
        j.setData("color","black");
        System.out.println(j.getData("color"));
        j.setData("color","blue");
        System.out.println(j.getData("color"));
		j.setData("x","0.15");
		j.setData("y","0.45");
        System.out.println(j.getData("x"));
        System.out.println(j.getData("y"));
	}

}


class NodoComparator implements Comparator<Nodo>{ 
	public int compare(Nodo n1, Nodo n2) { 
		float dn1 = Float.parseFloat(n1.getData("dist"));
		float dn2 = Float.parseFloat(n2.getData("dist"));
		
		if ( dn1 > dn2 )	return 1; 
		else if ( dn1 == dn2) return 0; 
		else	return -1;
	}                 
}
