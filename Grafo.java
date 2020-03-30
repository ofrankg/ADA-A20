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

public class Grafo {
    HashMap <String, Nodo> V;
    HashMap <String, Arista> E;

    public Grafo(HashMap <String, Nodo> newV, HashMap <String, Arista> newE) {
        this.V = newV;
        this.E = newE;
    }

    public static Grafo grafo_erdos_renyi(int n, int m, boolean dir, boolean auto) {
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
    
    public static Grafo grafo_gilbert(int n, float p, boolean dir, boolean auto) {
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

    public static Grafo grafo_geografico(int n, float d, boolean dir, boolean auto) {
        HashMap <String, Nodo> V = new HashMap <String, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();
        int nodea, nodeb, Nodos=0;
		float distance, posx, posy;
        long seed = 20;
        int edges = 0;

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
				if (distance <= d) {
					E.put(Integer.toString(newValue)+"_"+Integer.toString(Nodos), new Arista(V.get(value), V.get("nodo_"+Nodos)) );
				}
			}
			Nodos++;
		}
        Grafo temp = new Grafo(V,E);
        return temp;
    }


    public static Grafo grafo_barabasi(int n, int d, boolean dir, boolean auto) {
        HashMap <String, Nodo> V = new HashMap <String, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();
        
		float p, pv;

        Random ranp = new Random();

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            v.setData("deg",Integer.toString(0));
            V.put("nodo_"+i, v);
        }
        
		for (int newValue = 0; newValue < n; newValue++) {
		
			String value = "nodo_"+Integer.toString(newValue);
//			System.out.println(value);
			
			for (int Nodos = 0; Nodos <= newValue ; Nodos++) {
		        if(!auto){
		        	if (newValue == Nodos)
						continue;
		        }
		        
		        if(!dir){ 
		        	if ( E.containsKey(Integer.toString(newValue)+ "_" + Integer.toString(Nodos)) || E.containsKey(Integer.toString(Nodos) +"_"+ Integer.toString(newValue)) )
		                continue;
		        }

				p = ranp.nextFloat();
		        pv = 1 - ( (float) Integer.parseInt(V.get("nodo_"+Integer.toString(Nodos)).getData("deg")) ) / d;
		        
//		        System.out.println("nodo_"+Nodos +  " deg = " + V.get("nodo_"+Integer.toString(Nodos)).getData("deg") + " p ="+ p + " pv = "+ pv);
		        
				if (p <= pv) {
					E.put(Integer.toString(newValue)+"_"+Integer.toString(Nodos), new Arista(V.get(value), V.get("nodo_"+Nodos)) );
					V.get(value).setData("deg",Integer.toString(Integer.parseInt(V.get(value).getData("deg"))+1));
					V.get("nodo_"+Integer.toString(Nodos)).setData("deg",Integer.toString(Integer.parseInt(V.get("nodo_"+Integer.toString(Nodos)).getData("deg"))+1));
				}
			}
		}
		
//		System.out.println(E);
			
        Grafo temp = new Grafo(V,E);
        return temp;
    }


	public Grafo BFS(Nodo s) { 
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

	public void DFSr(Grafo G, HashMap <String, Arista> F, Nodo s) { 	
    	
		for (String value: G.E.keySet()){
			if (G.E.get(value).getNodos()[0].getName() == s.getName() && G.E.get(value).getNodos()[1].getData("discovered") == "false"){
				G.E.get(value).getNodos()[1].setData("discovered","true");
				F.put(value, G.E.get(value));
				DFSr(G, F, G.E.get(value).getNodos()[1]);
			}
			else if (G.E.get(value).getNodos()[1].getName() == s.getName() && G.E.get(value).getNodos()[0].getData("discovered") == "false"){
				G.E.get(value).getNodos()[0].setData("discovered","true");
				F.put(value, G.E.get(value));
				DFSr(G, F, G.E.get(value).getNodos()[0]);
			}
		}
		
	}
	
	public Grafo DFS(Nodo s) { 
        HashMap <String, Arista> F = new HashMap <String, Arista>();
        
		for(String value : this.V.keySet()){
			this.V.get(value).setData("discovered","false");
		}
		
		s.setData("discovered","true");
		DFSr(this, F, s);
		Grafo temp = new Grafo(this.V,F);
        return temp;
	}


	public void logFile(String filePath, String fileName,  Grafo grafo, boolean DisNode) throws IOException {
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
        
        // nodos que no se encuentran en una arista
        if (DisNode) {
			for(String value : Vi.keySet()){
				printWriter.printf("%s;", Vi.get(value).getName());
				printWriter.println();
			}
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
		
		if (args.length != 4){
			System.out.println("Ejecutar java Grafo <erdos, gilbert, geo, bara> <n> <m/p/d> <x>"  );
			return;
		}
		else {		
			if (args[0].equals("erdos") || args[0].equals("gilbert") || args[0].equals("geo") || args[0].equals("bara")){
				n = Integer.parseInt(args[1]);
				
				if (args[0].equals("erdos") || args[0].equals("bara")) m = Integer.parseInt(args[2]);
				else p = Float.parseFloat(args[2]);
				
				String node = "nodo_" + Integer.toString(Integer.parseInt(args[3]));

				if (args[0].equals("erdos")){

			        Grafo graph, treeB, treeD;
					graph = Grafo.grafo_erdos_renyi(n, m, false, false);
					treeB = graph.BFS(graph.V.get(node));
					treeD = graph.DFS(graph.V.get(node));
					filename = "grafo_"+ args[0] +"_" + n;
					graph.logFile(filepath, filename, graph, true);
					filename = args[0]+ "_bfs_" + n;
					graph.logFile(filepath, filename, treeB, false);
					filename = args[0]+ "_dfs_" + n;
					graph.logFile(filepath, filename, treeD, false);
				}
				else if (args[0].equals("gilbert")){      
			        Grafo graph, treeB, treeD;
					graph = Grafo.grafo_gilbert(n, p, false, false);
					treeB = graph.BFS(graph.V.get(node));
					treeD = graph.BFS(graph.V.get(node));
					filename = "grafo_"+ args[0] +"_" + n;
					graph.logFile(filepath, filename, graph, true);
					filename = args[0]+ "_bfs_" + n;
					graph.logFile(filepath, filename, treeB, false);
					filename = args[0]+ "_dfs_" + n;
					graph.logFile(filepath, filename, treeD, false);

				}
				else if (args[0].equals("geo")){
			        Grafo graph, treeB, treeD;
					graph = Grafo.grafo_geografico(n, p, false, false);
					treeB = graph.BFS(graph.V.get(node));
					treeD = graph.BFS(graph.V.get(node));
					filename = "grafo_"+ args[0] +"_" + n;
					graph.logFile(filepath, filename, graph, true);
					filename = args[0]+ "_bfs_" + n;
					graph.logFile(filepath, filename, treeB, false);
					filename = args[0]+ "_dfs_" + n;
					graph.logFile(filepath, filename, treeD, false);
				}
				else if(args[0].equals("bara")){
			        Grafo graph, treeB, treeD;
					graph = Grafo.grafo_barabasi(n, m, false, false);
					treeB = graph.BFS(graph.V.get(node));
					treeD = graph.BFS(graph.V.get(node));
					filename = "grafo_"+ args[0] +"_" + n;
					graph.logFile(filepath, filename, graph, true);
					filename = args[0]+ "_bfs_" + n;
					graph.logFile(filepath, filename, treeB, false);
					filename = args[0]+ "_dfs_" + n;
					graph.logFile(filepath, filename, treeD, false);
				}
				else	return;
			}
			
			else{
				System.out.println("Error en argumento 0"  );
				System.out.println("Ejecutar java Grafo <erdos, gilbert, geo, bara> <n> <m/p/d> <x>"  );
				return;
			}
			System.out.println("Done! Output directory "+ filepath);
		}		
		
    }

}

