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
import java.util.PriorityQueue;
import java.lang.Math.*;
import java.io.*;

public class Grafo {
    HashMap <String, Nodo> V;
    HashMap <String, Arista> E;

	/**************************************************************************************************************************************
	*
	*	Proyecto 1: Generacion de grafos
	*
	**************************************************************************************************************************************/



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
        int Nodos=0;
		float distance, posx, posy;
        long seed = 20;
        int edges = 0;
        
        Nodo nodeb, nodea;

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
			nodea = V.get("nodo_"+Nodos);
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
                
                nodeb = V.get(value);

                distance = (float) Math.sqrt( Math.pow( ( Float.parseFloat(nodeb.getData("posx")) - Float.parseFloat(nodea.getData("posx")) ),2 ) +  Math.pow( ( Float.parseFloat(nodeb.getData("posy")) - Float.parseFloat(nodea.getData("posy")) ),2 ) );
				if (distance <= d) {
					E.put(Integer.toString(newValue)+"_"+Integer.toString(Nodos), new Arista(nodeb, nodea) );
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
		Nodo nodov, nodo;

        Random ranp = new Random();

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            v.setData("deg",Integer.toString(0));
            V.put("nodo_"+i, v);
        }
        
		for (int newValue = 0; newValue < n; newValue++) {
		
			String value = "nodo_"+Integer.toString(newValue);
			nodo = V.get(value);
			
			for (int Nodos = 0; Nodos <= newValue ; Nodos++) {
		        if(!auto){
		        	if (newValue == Nodos)
						continue;
		        }
		        
		        if(!dir){ 
		        	if ( E.containsKey(Integer.toString(newValue)+ "_" + Integer.toString(Nodos)) || E.containsKey(Integer.toString(Nodos) +"_"+ Integer.toString(newValue)) )
		                continue;
		        }
		        
				nodov = V.get("nodo_"+Integer.toString(Nodos));

				p = ranp.nextFloat();
		        pv = 1 - ( (float) Integer.parseInt(nodov.getData("deg")) ) / d;
		        
		        
				if (p <= pv) {
					E.put(Integer.toString(newValue)+"_"+Integer.toString(Nodos), new Arista(nodo, nodov) );
					nodo.setData("deg",Integer.toString(Integer.parseInt(nodo.getData("deg"))+1));
					nodov.setData("deg",Integer.toString(Integer.parseInt(nodov.getData("deg"))+1));
				}
			}
		}
		
			
        Grafo temp = new Grafo(V,E);
        return temp;
    }

	/**************************************************************************************************************************************
	*
	*   Proyecto 2: Algoritmos BFS y DFS
	*
	**************************************************************************************************************************************/
	
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
	

	public static Grafo prueba() { 
		HashMap <String, Nodo> V = new HashMap <String, Nodo>();
        HashMap <String, Arista> E = new HashMap <String, Arista>();
        int n = 7;

        for (int i=0; i<n; i++){
            Nodo v = new Nodo("nodo_" + i);
            V.put("nodo_"+Integer.toString(i), v);
        }
        
        E.put("0_1" ,new Arista (V.get("nodo_0"), V.get("nodo_1") ));
        E.get("0_1").setData("weight","0.4");
        
        E.put("0_3" ,new Arista (V.get("nodo_0"), V.get("nodo_3") ));
        E.get("0_3").setData("weight","0.7");
        
        E.put("1_2" ,new Arista (V.get("nodo_1"), V.get("nodo_2") ));
        E.get("1_2").setData("weight","0.5");

        E.put("2_3" ,new Arista (V.get("nodo_2"), V.get("nodo_3") ));
        E.get("2_3").setData("weight","0.2");

        E.put("2_5" ,new Arista (V.get("nodo_2"), V.get("nodo_5") ));
        E.get("2_5").setData("weight","0.2");


        E.put("6_0" ,new Arista (V.get("nodo_6"), V.get("nodo_0") ));
        E.get("6_0").setData("weight","0.3");
        
        E.put("3_4" ,new Arista (V.get("nodo_3"), V.get("nodo_4") ));
        E.get("3_4").setData("weight","0.5");

        E.put("3_6" ,new Arista (V.get("nodo_3"), V.get("nodo_6") ));
        E.get("3_6").setData("weight","0.8");

        E.put("4_2" ,new Arista (V.get("nodo_4"), V.get("nodo_2") ));
        E.get("4_2").setData("weight","1.0");
        
        E.put("5_4" ,new Arista (V.get("nodo_5"), V.get("nodo_4") ));
        E.get("5_4").setData("weight","0.3");
        
        E.put("6_1" ,new Arista (V.get("nodo_6"), V.get("nodo_1") ));
        E.get("6_1").setData("weight","0.6");        
        
        E.put("6_2" ,new Arista (V.get("nodo_6"), V.get("nodo_2") ));
        E.get("6_2").setData("weight","1.1");

        return new Grafo(V,E);
        
	}




	/**************************************************************************************************************************************
	*
	*	Proyecto 3: Algorimto Dikjstra
	*
	**************************************************************************************************************************************/
	
	public void randomEdgesValues(float min, float max){
		float ranW;
		Random rand = new Random();
		
//		System.out.println(this.E);

		for(String value : this.E.keySet()){
			ranW = min + rand.nextFloat()*(max-min);
			Arista arista = this.E.get(value);
			arista.setData("weight", Float.toString(ranW));
//			System.out.println("edge "+ value + " weight " + Float.parseFloat(arista.getData("weight")));
		}
	}


	public Grafo dijkstra(Nodo s){
        HashMap <String, Arista> F = new HashMap <String, Arista>();
		Arista arista;
		Nodo nodoA, nodoB;
		float distance, w_arista, newdistance;
		float inf = (float)99.99;
		float tot_distance = (float)0.0;

		PriorityQueue<Nodo> pq = new PriorityQueue<Nodo>(this.V.size(), new NodoComparator());		
		Iterator itr = pq.iterator(); 
		s.setData("dist","0");
		s.setData("discovered","true");
		pq.add(s);
		
		for(int j=0; j<this.V.size(); j++) {
			String node = "nodo_" + j;
			if (  node.equals(s.getName()) ) continue;
			Nodo nodo = this.V.get(node);
			nodo.setData("dist",Float.toString(inf));
			nodo.setData("discovered","false");
		}
		
		while (!pq.isEmpty()) {
			s = pq.peek();
			tot_distance += Float.parseFloat(s.getData("dist"));
			s.setData("discovered","true");
			pq.poll();
//			System.out.println(s.getName());
			
			for (String value: this.E.keySet()){
				arista = this.E.get(value);
				nodoA = arista.getNodos()[0];
				nodoB = arista.getNodos()[1];
				w_arista = Float.parseFloat(arista.getData("weight"));
				
				if ( nodoA.getName() == s.getName() && !Boolean.parseBoolean(nodoB.getData("discovered")) ) {
					distance = Float.parseFloat(s.getData("dist"));
					newdistance = distance + w_arista;
					if ( newdistance < Float.parseFloat(nodoB.getData("dist")) )	{
						nodoB.setData("dist",Float.toString(newdistance));
						F.put(nodoB.getName() , arista);
					}
					if (pq.contains(nodoB)) { 
						pq.remove(nodoB);
						pq.add(nodoB);
					}
					else	pq.add(nodoB);

				}
				if ( nodoB.getName() == s.getName() && !Boolean.parseBoolean(nodoA.getData("discovered")) ) {
					distance = Float.parseFloat(s.getData("dist"));
					newdistance = distance + w_arista;
					if ( newdistance < Float.parseFloat(nodoA.getData("dist")) ) {
						nodoA.setData("dist",Float.toString(newdistance));
						F.put(nodoA.getName(), arista);
					}
					if (pq.contains(nodoA)) { 
						pq.remove(nodoA);
						pq.add(nodoA);
					}
					else	pq.add(nodoA);
				}
			}
			
//			itr = pq.iterator();
//			while (itr.hasNext()) { 
//				Nodo nodo = (Nodo) itr.next();
//				System.out.println("----" + nodo.getName() + " dist "+ nodo.getData("dist")); 
//			}
//			System.out.println("=================");
			
		}
//		System.out.println(this.E);//.size());
//		System.out.println(F);//.size());
//		System.out.println("distance: " + tot_distance);
		Grafo temp = new Grafo(V,F);
		return temp;
	}
	

	/**************************************************************************************************************************************
	*
	*	Creación de archivos
	*
	**************************************************************************************************************************************/

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
		Grafo graph, treeB, treeD;
		
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
					graph = Grafo.grafo_erdos_renyi(n, m, false, false);
				}
				else if (args[0].equals("gilbert")){      
					graph = Grafo.grafo_gilbert(n, p, false, false);

				}
				else if (args[0].equals("geo")){
					graph = Grafo.grafo_geografico(n, p, false, false);
				}
				else if(args[0].equals("bara")){
					graph = Grafo.grafo_barabasi(n, m, false, false);

				}
				else	return;
				treeB = graph.BFS(graph.V.get(node));
				treeD = graph.BFS(graph.V.get(node));
				filename = "grafo_"+ args[0] +"_" + n;
				graph.logFile(filepath, filename, graph, true);
				filename = args[0]+ "_bfs_" + n;
				graph.logFile(filepath, filename, treeB, false);
				filename = args[0]+ "_dfs_" + n;
				graph.logFile(filepath, filename, treeD, false);
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

