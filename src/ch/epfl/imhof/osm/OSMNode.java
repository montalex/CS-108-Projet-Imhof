/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * Représente un noeud OSM.
 */
public final class OSMNode extends OSMEntity {
	private final PointGeo position;
	
	/**
	 * Construit un noeud OSM avec l'identifiant, la position et les attributs donnés.
	 * @param id
	 * 		Un long, l'identifiant unique du noeud.
	 * @param position
	 * 		Un PointGeo, la position du noeud.
	 * @param attributes
	 * 		Un Attributes, liste des attributs du noeud.
	 */
	public OSMNode(long id, PointGeo position, Attributes attributes){
		super(id, attributes);
		this.position = position;
	}
	
	/**
	 * @return PointGeo
	 * 		la position du noeud.
	 */
	public PointGeo position(){
		return position;
	}
	
	public static final class Builder extends OSMEntity.Builder{
		private final PointGeo position;
		
		/**
		 * Permet de construire un noeud OSM en plusieurs étapes.
		 * @param id
		 * 		Un long, l'identifiant unique du noeud.
		 * @param position
		 * 		Un PointGeo, la position du noeud.
		 */
		public Builder(long id, PointGeo position){
			super(id);
			this.position = position;
		}
		
		/**
		 * Construit le noeud avec l'identifiant et la position passée au constructeur et 
		 * les éventuels attributs ajoutés.
		 * @return OSMNode
		 * 		le noeud construit.
		 * @throws IllegalStateException
		 * 		si le noeud en cours de construction est incomplet.
		 */
		public OSMNode build() throws IllegalStateException{
			if(this.isIncomplete()){
				throw new IllegalStateException("Noeud incomplet");
			}
			return new OSMNode(id, position, attributes.build());
		}
	}
}
