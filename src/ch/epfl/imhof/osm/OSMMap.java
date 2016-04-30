/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.osm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente une carte OSM.
 */
public final class OSMMap {
	private final List<OSMWay> ways;
	private final List<OSMRelation> relations;
	
	/**
	 * Construit une carte OSM avec les chemins et relations données.
	 * @param ways
	 * 		un Collection<OSMWay>, liste de chemins.
	 * @param relations
	 * 		un Collection<OSMRelation>, liste de relations.
	 */
	public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations){
		this.ways = Collections.unmodifiableList(new ArrayList<OSMWay>(ways)); 
		this.relations = Collections.unmodifiableList(new ArrayList<OSMRelation>(relations));
	}
	
	/**
	 * @return List<OSMWay>
	 * 		la liste de chemins OSM de la carte.
	 */
	public List<OSMWay> ways(){
		return ways;
	}
	
	/**
	 * @return List<OSMRelation>
	 * 		la liste des relations de la carte.
	 */
	public List<OSMRelation> relations(){
		return relations;
	}
	/**
	 * Permet de construire une cartes en plusieurs étapes.
	 */
	public static final class Builder{
		private Map<Long, OSMNode> mapNode = new HashMap<Long, OSMNode>();
		private Map<Long, OSMWay> mapWay = new HashMap<Long, OSMWay>();
		private Map<Long, OSMRelation> mapRelation = new HashMap<Long, OSMRelation>();
		
		/**
		 * Ajoute un noeud donné au bâtisseur.
		 * @param newNode
		 * 		un OSMNode, noeud à ajouter au bâtisseur.
		 */
		public void addNode(OSMNode newNode){
			mapNode.put(newNode.id(), newNode);
		}
		
		/**
		 * @param id
		 * 		un long, l'identifiant unique du noeud recherché.
		 * @return OSMNode
		 * 		le noeud recherché s'il est dans la liste, sinon retourne null.
		 */
		public OSMNode nodeForId(long id){
			return mapNode.getOrDefault(id, null);
		}
		
		/**
		 * Ajoute un chemin donné au bâtisseur.
		 * @param newWay
		 * 		un OSMWay, le chemin à ajouter au bâtisseur.
		 */
		public void addWay(OSMWay newWay){
			mapWay.put(newWay.id(), newWay);
		}
		
		/**
		 * @param id
		 * 		un long, l'identifiant unique du chemin recherché.
		 * @return OSMWay
		 * 		le chemin recherché s'il est dans la liste, sinon retourne null.
		 */
		public OSMWay wayForId(long id){
			return mapWay.getOrDefault(id, null);
		}
		
		/**
		 * Ajoute une relation donnée au bâtisseur.
		 * @param newRelation
		 * 		un OSMRelation, la relation à ajouter au bâtisseur.
		 */
		public void addRelation(OSMRelation newRelation){
			mapRelation.put(newRelation.id(), newRelation);
		}
		
		/**
		 * @param id
		 * 		un long, l'identifiant unique de la relation recherchée.
		 * @return OSMRelation
		 * 		la relation recherchée si elle est dans la liste, sinon retourne null.
		 */
		public OSMRelation relationForId(long id){
			return mapRelation.getOrDefault(id, null);
		}
		
		/**
		 * Construit une carte OSM avec les chemins et relations éventuellement ajoutées précédements.
		 * @return OSMMap
		 * 		La nouvelle carte OSM.
		 */
		public OSMMap build(){
			return new OSMMap(mapWay.values(), mapRelation.values());
		}
	}
}