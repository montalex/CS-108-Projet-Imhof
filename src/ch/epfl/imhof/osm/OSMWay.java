/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.osm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Représente un chemin OSM.
 */
public final class OSMWay extends OSMEntity {
	private final List<OSMNode> nodes;
	
	/**
	 * Construit un chemin OSM avec l'identifiant, la liste de noeud et les attributs donnés.
	 * @param id
	 * 		Un long, l'identifiant unique du chemin.
	 * @param nodes
	 * 		Une List<OSMNode>, la liste de noeuds qui forment le chemin.
	 * @param attributes
	 * 		Un Attributes, la liste des attributs du chemin.
	 * @throws IllegalArgumentException
	 * 		si la liste de noeuds données possèdent moins de deux éléments.
	 */
	public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException{
		super(id, attributes);
		if(nodes.size() < 2){
			throw new IllegalArgumentException("La liste de noeuds contient moins de deux noeuds");
		}
		this.nodes = Collections.unmodifiableList(new ArrayList<OSMNode>(nodes));
	}
	
	/**
	 * @return int
	 * 		le nombre de noeuds qui forment le chemin.
	 */
	public int nodesCount(){
		return nodes.size();
	}
	
	/**
	 * @return List<OSMNode>
	 * 		la liste des noeuds qui forment le chemin.
	 */
	public List<OSMNode> nodes(){
		return nodes;
	}
	
	/**
	 * @return List<OSMNode>
	 * 		la liste des noeuds qui forment le chemin sans répétitions.
	 * 		c-à-d que si le chemin est fermé il ne retourne pas le dernier noeud qui est égal au premier.
	 */
	public List<OSMNode> nonRepeatingNodes(){
		if(firstNode().equals(lastNode())){
			return nodes().subList(0, nodes().size() - 1);
		} else return nodes;
	}
	
	/**
	 * @return OSMNode
	 * 		le premier noeud du chemin.
	 */
	public OSMNode firstNode(){
		return nodes.get(0);
	}
	
	/**
	 * @return OSMNode
	 * 		le dernier noeud du chemin.
	 */
	public OSMNode lastNode(){
		return nodes.get(nodes.size() - 1);
	}
	
	/**
	 * @return true
	 * 		ssi le premier et le dernier noeud du chemin sont les mêmes.
	 */
	public boolean isClosed(){
		return firstNode().equals(lastNode());
	}
	
	public static final class Builder extends OSMEntity.Builder{
		private List<OSMNode> tempNodes = new ArrayList<OSMNode>();

		/**
		 * Permet de construire un chemin en plusieurs étapes.
		 * @param id
		 * 		Un long, l'identifiant unique du chemin.
		 */
		public Builder(long id){
			super(id);
		}
		
		/**
		 * Permet d'ajouter un noeud à la liste de noeuds du chemin.
		 * @param newNode
		 * 		Un Node, un noeud à ajouter au chemin.
		 */
		public void addNode(OSMNode newNode){
			tempNodes.add(newNode);
		}
		
		/**
		 * Construit un chemin avec l'identifiant passé au constructeur et les éventuels noeuds et attributs ajoutés.
		 * @return OSMWay
		 * 		le chemin construit.
		 * @throws IllegalStateException
		 * 		si le chemin en cours de construction est incomplet.
		 */
		public OSMWay build() throws IllegalStateException{
			if(this.isIncomplete()){
				throw new IllegalStateException("Chemin incomplet");
			}
			return new OSMWay(id, tempNodes, attributes.build());
		}
		
		/**
		 * @return true
		 * 		ssi le chemin est incomplet OU si la liste de noeuds contient moins de deux éléments.
		 */
		@Override
		public boolean isIncomplete(){
			if(incomplete || tempNodes.size() < 2){
				return true;
			} else return false;
		}
	}
}
