/*
*
*	Oswaldo Franco
*	CIC-IPN
*	ADA - A20
*
*/


import java.util.Random;
import java.util.HashMap;
import java.lang.Math.*;
import java.io.*;

class Nodo {
    String nombre;
	float x=0, y=0;
	int degree = 0;
    
    public Nodo(String newnombre){
        this.nombre = newnombre;	
    }

    public Nodo(String newnombre, float newx, float newy){
        this.nombre = newnombre;	
        this.x = newx;	
        this.y = newy;	
    }
	
	public void add_degree(){
		this.degree++;
	}

	public int getdegree(){
		return this.degree;
	}

    public String toString(){
        return this.nombre;
    }

	public float[] getpos(){
		float tmp [] = new float [2];
		tmp[0] = this.x; 
		tmp[1] = this.y ;
		return tmp;
	}

}

class Arista{
    Nodo n1, n2;
    boolean directed = false;

    public Arista(Nodo newn1, Nodo newn2){
        this.n1 = newn1;
        this.n2 = newn2;
    }

    public void direction(boolean a){
        this.directed = a;
    }    

    public String toString(){
        if (this.directed){
			return this.n1.nombre + "->" + this.n2.nombre;
        }
        else	return this.n1.nombre + "--" + this.n2.nombre;
    }

}

class Grafo {
    HashMap <Integer, Nodo> V;
    HashMap <String, Arista> E;

    public Grafo(){
    }

    public Grafo(HashMap <Integer, Nodo> newV, HashMap <String, Arista> newE){
        this.V = newV;
        this.E = newE;
    }


    public static Grafo grafo_erdos_renyi(int n, int m, boolean dir, boolean auto){
        HashMap <Integer, Nodo> V = new HashMap <Integer, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();

        int nodea, nodeb;
        int edges = 0;
		int seed = 1;

        Random ran = new Random();

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            V.put(i, v);
        }

        while (edges < m){
			//nextInt((max - min) + 1) + min;
            nodea = ran.nextInt(n);
            nodeb = ran.nextInt(n);
            if (!auto)
                if(nodea == nodeb)  continue;
            if (!dir & ( E.containsKey(Integer.toString(nodea) + Integer.toString(nodeb)) || E.containsKey(Integer.toString(nodeb) + Integer.toString(nodea)) ) ) {
                //System.out.println("nodes already" + V.get(nodea) + "--" + V.get(nodeb));
                continue;
            }
				//Arista e = new Arista(V.get(nodea), V.get(nodeb));
				//System.out.println(nodea + V.get(nodea).nombre);
				//System.out.println(nodeb + V.get(nodeb).nombre);
				E.put(Integer.toString(nodea) + Integer.toString(nodeb), new Arista(V.get(nodea), V.get(nodeb)));
				edges ++;
			//}
        }
        //System.out.println(E);
        Grafo temp = new Grafo(V,E);
        return temp;
    }

    public static Grafo grafo_gilbert(int n, int m, float p, boolean dir, boolean auto){
        HashMap <Integer, Nodo> V = new HashMap <Integer, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();

		float edgep;
        int nodes=0;

        Random ranf = new Random();

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            V.put(i, v);
        }
        while (nodes < n){
			for (Integer value : V.keySet()) {
				if(value == nodes & !auto){
                    //System.out.println(value.nombre + " -- " + V.get(nodes));
					continue;
                }
                if(!dir & ( E.containsKey(Integer.toString(value) + Integer.toString(nodes)) || E.containsKey(Integer.toString(nodes) + Integer.toString(value)) ) ){
                    //System.out.println("nodes already" + V.get(value) + "--" + V.get(nodes));
                    continue;
                }
				edgep = ranf.nextFloat();
				if (edgep <= p) {
					//Arista e = new Arista(V.get(value), V.get(nodes));
					E.put(Integer.toString(value)+Integer.toString(nodes), new Arista(V.get(value), V.get(nodes)) );
				}
			}
			nodes++;
		}
        //System.out.println(E);
        Grafo temp = new Grafo(V,E);
        return temp;
	}

    public static Grafo grafo_geografico(int n, float d, boolean dir, boolean auto){
        HashMap <Integer, Nodo> V = new HashMap <Integer, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();
        int nodea, nodeb, nodes=0;
		float distance, posx, posy;
        long seed = 20;
        int edges = 0;

        Random ran = new Random();
        Random ranx = new Random();
        Random rany = new Random();

        for (int i=0; i<n; i++){
			posx = ranx.nextFloat();
			posy = rany.nextFloat();
            Nodo v = new Nodo("nodo_" + i, posx, posy);
            V.put(i, v);
        }

        while (nodes < n){
			for (Integer value : V.keySet()) {
                if(value == nodes & !auto){
                    //System.out.println(value.nombre + " -- " + V.get(nodes));
					continue;
                }
                if(!dir & ( E.containsKey(Integer.toString(value) + Integer.toString(nodes)) || E.containsKey(Integer.toString(nodes) + Integer.toString(value)) ) ){
                    //System.out.println("nodes already" + V.get(value) + "--" + V.get(nodes));
                    continue;
                }

                distance = (float) Math.sqrt( Math.pow( ( (double)V.get(value).x - (double)V.get(nodes).x ) ,2 ) + Math.pow( ( (double)V.get(value).y - (double)V.get(nodes).y ) ,2 ) );
				//System.out.println(value.nombre + " x: " + value.x + " y: "+ value.y);
				//System.out.println(V.get(nodes).nombre + " x: " + V.get(nodes).x + " y: "+ V.get(nodes).y);
				//System.out.println("distance " + distance + " comp " + d);
				if (distance <= d) {
					//System.out.println("hit");
					//System.out.println(value.nombre + "--" +V.get(nodes).nombre );
					//Arista e = new Arista(value, V.get(nodes));
					E.put(Integer.toString(value)+Integer.toString(nodes), new Arista(V.get(value), V.get(nodes)) );
				}
			}
			nodes++;
		}
		//System.out.println(E);
        Grafo temp = new Grafo(V,E);
        return temp;
    }

    public static Grafo grafo_barabasi_albert(int n, int d, boolean dir, boolean auto){
        HashMap <Integer, Nodo> V = new HashMap <Integer, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();
        int nodes;
		float edgep = 0;
		int value;

		float pedge;
        Random ranedge = new Random();

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            V.put(i, v);
        }

        Random ran = new Random();

		for (nodes = 0; nodes <= d; nodes++) {
			for (value=0; value < nodes; value++){
                if(value == nodes & !auto){ // no autoconexiones
                    continue;
                }
                
                if(!dir & ( E.containsKey(Integer.toString(value) + Integer.toString(nodes)) || E.containsKey(Integer.toString(nodes) + Integer.toString(value)) ) ){ // no nodo dirigido
		            continue;
            	}
				V.get(value).add_degree();
				V.get(nodes).add_degree();
				E.put(Integer.toString(nodes)+Integer.toString(value), new Arista(V.get(nodes), V.get(value)) );
            }
        }

		for (nodes = d+1; nodes < n; nodes++) {
			for (value=0; value < nodes; value++){
			//for (int k=0; k < nodes; k++){
                value = ran.nextInt(nodes);
                if(value == nodes & !auto){ // no autoconexiones
                    continue;
                }
                
                if(!dir & ( E.containsKey(Integer.toString(value) + Integer.toString(nodes)) || E.containsKey(Integer.toString(nodes) + Integer.toString(value)) ) ){ // no nodo dirigido
		            continue;
            	}

				edgep = 1-(float) V.get(nodes).getdegree()/ (float)d;
				pedge = ranedge.nextFloat();			

				if (edgep>pedge){
					V.get(value).add_degree(); 				
					V.get(nodes).add_degree();
					E.put(Integer.toString(nodes)+Integer.toString(value), new Arista(V.get(nodes), V.get(value)) );
				}
            }
        }

		//System.out.println(E);
        Grafo temp = new Grafo(V,E);
        return temp;
	}

	public void logFile(String filePath, String fileName,  Grafo grafo) throws IOException{
		//String dir = "c:/users/oswaldo.franco/desktop/samplefile3.txt";
		FileWriter fileWriter = new FileWriter(filePath + fileName + ".DOT");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		//System.out.println(grafo.E);
		printWriter.printf("graph " + fileName + " {");
		printWriter.println();
//		for(int i = 0; i<grafo.E.size(); i++){
//    		printWriter.printf("%s;", grafo.E.get(i));
//	    	printWriter.println();
//		}
		for(String value : grafo.E.keySet()){
			printWriter.printf("%s;", grafo.E.get(value));
			printWriter.println();
		}
		printWriter.printf("}");
		printWriter.println();
		printWriter.close();
		
	}

	public static void main(String args[]) throws IOException,ClassNotFoundException {
        Grafo graph;
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
		float p = Float.parseFloat(args[2]);

        String filepath = "/home/oswald/Documents/A20/ADA-15A7159/tareas/";
		String filename;

        graph = Grafo.grafo_erdos_renyi(n, m, false, false);
        filename = "grafo_erdos_renyi_" + n;
        graph.logFile(filepath, filename, graph);

        graph = Grafo.grafo_gilbert(n, m, p, false, false);
        filename = "grafo_gilbert_" + n;
        graph.logFile(filepath, filename, graph);

        graph = Grafo.grafo_geografico(n, p, false, false);
        filename = "grafo_geografico_" + n;
        graph.logFile(filepath, filename, graph);

        graph = Grafo.grafo_barabasi_albert(n, m, false, false);
        filename = "grafo_barabasi_albert_" + n;
        graph.logFile(filepath, filename, graph);

		System.out.println("Fin");
	}

}


