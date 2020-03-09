/*
*
*	Oswaldo Franco
*	CIC-IPN
*	ADA - A20
*
*/

import java.util.Random;
import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;
import java.lang.Math.*;
import java.io.*;

class Grafo {
    HashMap <String, Nodo> V;
    HashMap <String, Arista> E;

    public Grafo(HashMap <String, Nodo> newV, HashMap <String, Arista> newE){
        this.V = newV;
        this.E = newE;
    }

    public static Grafo grafo_erdos_renyi(int n, int m, boolean dir, boolean auto){
        HashMap <String, Nodo> V = new HashMap <String, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();
        
        int nodea, nodeb;
        int edges = 0;
		int seed = 1;

        Random ran = new Random();

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            V.put("nodo_"+i, v);
        }
 
        while (edges < m){
            nodea = ran.nextInt(n);
            nodeb = ran.nextInt(n);
            if (!auto)
                if(nodea == nodeb)	continue;
                
            if (!dir)
            	if ( E.containsKey(Integer.toString(nodea) + "_"+Integer.toString(nodeb)) || E.containsKey(Integer.toString(nodeb) +"_"+Integer.toString(nodea)) ) {
                continue;
            }
            
			E.put(Integer.toString(nodea) +"_"+ Integer.toString(nodeb), new Arista(V.get("nodo_"+nodea), V.get("nodo_"+nodeb)));
			edges ++;
        }
        Grafo temp = new Grafo(V,E);
        return temp;
    }
    
    public static Grafo grafo_gilbert(int n, float p, boolean dir, boolean auto){
        HashMap <String, Nodo> V = new HashMap <String, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();

		float edgep;
        int Nodos=0;

        Random ranf = new Random();

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            V.put("nodo_"+Integer.toString(i), v);
        }
        while (Nodos < n){
			for (String value : V.keySet()) {
				String[] separated = value.split("_");
				int newValue = Integer.parseInt(separated[1]);
				
				if(!auto){
					if (newValue == Nodos)
						continue;
                }
    
                if(!dir){
                	if ( E.containsKey(Integer.toString(newValue) +"_"+ Integer.toString(Nodos)) || E.containsKey(Integer.toString(Nodos) +"_"+ Integer.toString(newValue)) )
	                    continue;
                }
	
				edgep = ranf.nextFloat();
				if (edgep >= p) {
					E.put(Integer.toString(newValue)+"_"+Integer.toString(Nodos), new Arista(V.get(value), V.get("nodo_"+Nodos)) );
				}
			}
			Nodos++;
		}
        Grafo temp = new Grafo(V,E);
        return temp;
	}   

    public static Grafo grafo_geografico(int n, float d, boolean dir, boolean auto){
        HashMap <String, Nodo> V = new HashMap <String, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();
        int nodea, nodeb, Nodos=0;
		float distance, posx, posy;
        long seed = 20;
        int edges = 0;

        Random ran = new Random();
        Random ranx = new Random();
        Random rany = new Random();

        for (int i=0; i<n; i++){
			posx = ranx.nextFloat();
			posy = rany.nextFloat();
            Nodo v = new Nodo("nodo_" + i);
            v.setData("posx",Float.toString(posx));
            v.setData("posy",Float.toString(posy));
            V.put("nodo_"+i, v);
        }
        

        while (Nodos < n){
			for (String value : V.keySet()) {
				String[] separated = value.split("_");
				int newValue = Integer.parseInt(separated[1]);
                if(!auto){
                	if (newValue == Nodos)
						continue;
                }
                
                if(!dir){ 
                	if ( E.containsKey(Integer.toString(newValue)+ "_" + Integer.toString(Nodos)) || E.containsKey(Integer.toString(Nodos) +"_"+ Integer.toString(newValue)) )
	                    continue;
                }

                distance = (float) Math.sqrt( Math.pow( ( Float.parseFloat(V.get(value).getData("posx")) - Float.parseFloat(V.get("nodo_"+Nodos).getData("posx")) ),2 ) +  Math.pow( ( Float.parseFloat(V.get(value).getData("posy")) - Float.parseFloat(V.get("nodo_"+Nodos).getData("posy")) ),2 ) );
//				System.out.println(V.get(value).getName() + " x: " + V.get(value).getData("posx") + " y: "+ V.get(value).getData("posy"));
//				System.out.println(V.get("nodo_"+Nodos).nombre + " x: " + V.get("nodo_"+Nodos).getData("posx") + " y: "+ V.get("nodo_"+Nodos).getData("posx"));
//				System.out.println("distance " + distance + " comp " + d + "\n");
				if (distance <= d) {
					E.put(Integer.toString(newValue)+"_"+Integer.toString(Nodos), new Arista(V.get(value), V.get("nodo_"+Nodos)) );
				}
			}
			Nodos++;
		}
        Grafo temp = new Grafo(V,E);
        return temp;
    }

	public Grafo BFS(Nodo s) 
    { 
        HashMap <String, Arista> F = new HashMap <String, Arista>();
		
		for(String value : this.V.keySet()){
			this.V.get(value).setData("discovered","false");
		}
		
		s.setData("discovered","true");
		
		LinkedList<Nodo> queue = new LinkedList<Nodo>();
		
		queue.add(s);
  
        while (queue.size() != 0) 
        { 
			s = queue.poll();
			//System.out.print(s.getName()+" ");
			
			for (String value: this.E.keySet()){
				if (this.E.get(value).getNodos()[0].getName() == s.getName() && this.E.get(value).getNodos()[1].getData("discovered") == "false"){
					this.E.get(value).getNodos()[1].setData("discovered","true");
					F.put(value, this.E.get(value));
					queue.add(this.E.get(value).getNodos()[1]);
				}
				else if (this.E.get(value).getNodos()[1].getName() == s.getName() && this.E.get(value).getNodos()[0].getData("discovered") == "false"){
					this.E.get(value).getNodos()[0].setData("discovered","true");
					F.put(value, this.E.get(value));
					queue.add(this.E.get(value).getNodos()[0]);
				}
			}
		}
        Grafo temp = new Grafo(this.V,F);
        return temp;
	}
	

	public void logFile(String filePath, String fileName,  Grafo grafo) throws IOException{
		FileWriter fileWriter = new FileWriter(filePath + fileName + ".DOT");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.printf("graph " + fileName + " {");
		printWriter.println();
        HashMap <String, Nodo> Vi = new HashMap <String, Nodo>();
        Vi = grafo.V;
		for(String value : grafo.E.keySet()){
			printWriter.printf("%s;",grafo.E.get(value).getNodos()[0].getName() + "--" + grafo.E.get(value).getNodos()[1].getName());
			printWriter.println();
			Vi.remove(grafo.E.get(value).getNodos()[0].getName());
			Vi.remove(grafo.E.get(value).getNodos()[1].getName());
		}
        
		for(String value : Vi.keySet()){
			printWriter.printf("%s;", Vi.get(value).getName());
			printWriter.println();
		}
		
		printWriter.printf("}");
		printWriter.println();
		printWriter.close();
		
	}
	
	public static void main(String args[]) throws IOException,ClassNotFoundException {
		String filepath = "/home/oswald/Documents/grafos/";
		String filename;
	    int n=0;
	    int m=0;
	    float p=(float)0.0;
        
//        Grafo graph, tree;
        
		boolean execution = false;
		
		if (args.length != 3){
			System.out.println("Ejecutar java Grafo <erdos, gilbert, geo, bara> <n> <m/p/d> "  );
			return;
		}
		else {		
			if (args[0].equals("erdos") || args[0].equals("gilbert") || args[0].equals("geo") || args[0].equals("bara")){
				n = Integer.parseInt(args[1]);
				
				if (args[0].equals("erdos")) m = Integer.parseInt(args[2]);
				else p = Float.parseFloat(args[2]);

				if (args[0].equals("erdos")){
			        Grafo graph, tree;
					graph = Grafo.grafo_erdos_renyi(n, m, false, false);
					tree = graph.BFS(graph.V.get("nodo_4"));
					filename = "grafo_"+ args[0] +"_" + n;
					graph.logFile(filepath, filename, graph);
					filename = args[0]+ "_bfs_" + n;
					graph.logFile(filepath, filename, tree);		

				}
				else if (args[0].equals("gilbert")){      
			        Grafo graph, tree;
					graph = Grafo.grafo_gilbert(n, p, false, false);

					tree = graph.BFS(graph.V.get("nodo_4"));
					filename = "grafo_"+ args[0] +"_" + n;
					graph.logFile(filepath, filename, graph);
					filename = args[0]+ "_bfs_" + n;
					graph.logFile(filepath, filename, tree);

				}
				else if (args[0].equals("geo")){
			        Grafo graph, tree;
					graph = Grafo.grafo_geografico(n, p, false, false);

					tree = graph.BFS(graph.V.get("nodo_4"));
					filename = "grafo_"+ args[0] +"_" + n;
					graph.logFile(filepath, filename, graph);
					filename = args[0]+ "_bfs_" + n;
					graph.logFile(filepath, filename, tree);
				}
				else if(args[0].equals("bara")){
			        Grafo graph, tree;
					System.out.println("No implementado");
					execution = false;
				}
				else	return;
			}
			
			else{
				System.out.println("Error en argumento 0"  );
				System.out.println("Ejecutar java Grafo <erdos, gilbert, geo, bara> <n> <m/p/d> "  );
				return;
			}
			System.out.println("Done! Output directory "+ filepath);
		}		
		
    }

}

