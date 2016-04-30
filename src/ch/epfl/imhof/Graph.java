/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Représente un graph non orienté.
 * @param <N>
 *      Le type d'entité du graph.
 */
public final class Graph<N> {
	private final Map<N, Set<N>> neighbors;
	
	/**
	 * Construit un graphe non orienté avec la table d'adjacence donnée.
	 * @param neighbors
	 * 		une Map<N, Set<N>>, la table d'adjacense du graph.
	 */
	public Graph(Map<N, Set<N>> neighbors){
		Map<N, Set<N>> copy = new HashMap<N, Set<N>>();
		for(N n: neighbors.keySet()){
			copy.put(n, Collections.unmodifiableSet(new HashSet<N>(neighbors.get(n))));
		}
		this.neighbors = Collections.unmodifiableMap(new HashMap<N, Set<N>>(copy));
	}
	
	/**
	 * @return Set<N>
	 * 		l'ensemble des nœuds du graphe.
	 */
	public Set<N> nodes(){
		return neighbors.keySet();
	}
	
	/**
	 * @param node
	 * 		un N, le noeud donné.
	 * @return Set<N>
	 * 		l'ensemble des noeuds voisins du noeuds donné en argument.
	 * @throws IllegalArgumentException
	 * 		ssi le noeud passé en argument n'est pas dans le graph.
	 * 
	 * Le test == null permet de ne pas retourner un set null, qui interférerai à l'utilisation.
	 */
	public Set<N> neighborsOf(N node) throws IllegalArgumentException{
		if(neighbors.containsKey(node)){
		    return neighbors.get(node);
		}
		throw new IllegalArgumentException("Le noeud ne fait pas partie du graph");
	}

	/**
	 * Permet de construire un Graph en plusieurs étapes.
	 */
	public static final class Builder<N>{
		private Map<N, Set<N>> neighborsTemp = new HashMap<N, Set<N>>();
		
		/**
		 * ajoute le nœud donné au graphe en cours de construction, s'il n'en faisait pas déjà partie.
		 * @param n
		 * 		un N, le noeud à ajouter.
		 */
		public void addNode(N node){
			if(!(neighborsTemp.containsKey(node))){
				neighborsTemp.put(node, new HashSet<N>());
			}
		}
		
		/**
		 * ajoute le premier nœud à l'ensemble des voisins du second, et vice versa.
		 * @param n1
		 * 		un N, le premier noeud.
		 * @param n2
		 * 		un N, le deuxième noeud.
		 * @throws IllegalArgumentException
		 * 		ssi l'un des nœuds n'appartient pas au graphe en cours de construction.
		 */
		public void addEdge(N n1, N n2) throws IllegalArgumentException{
			if(neighborsTemp.get(n1) == null || neighborsTemp.get(n2) == null){
				throw new IllegalArgumentException("un des noeuds n'appartient pas au graph en construction");
			}
			neighborsTemp.get(n1).add(n2);
			neighborsTemp.get(n2).add(n1);
		}
		
		/**
		 * Construit le graphe composé des nœuds et arêtes ajoutés jusqu'à présent au bâtisseur.
		 * @return Graph<N>
		 * 		le graph construit.
		 */
		public Graph<N> build(){
			return new Graph<N>(neighborsTemp);
		}
	}
}
