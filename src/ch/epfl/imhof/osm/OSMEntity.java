/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.osm;
import ch.epfl.imhof.Attributes;

/**
 * Représente une entité OSM.
 */
public abstract class OSMEntity {
	private final long id;
	private final Attributes attributes;
	
	/**
	 * Construit une entité OSM dotée de l'identifiant et des attributs donnés.
	 * @param id
	 * 		Un long, l'identifiant unique de l'entité.
	 * @param attributes
	 * 		Un Attributes, liste d'attributs propres à l'entité.
	 */
	public OSMEntity(long id, Attributes attributes){
		this.id = id;
		this.attributes = attributes;
	}
	
	/**
	 * @return long
	 * 		l'identifiant de l'entité. 
	 */
	public long id(){
		return id;
	}
	
	/**
	 * @return Attributes
	 * 		la liste d'attributs de l'entité.
	 */
	public Attributes attributes(){
		return attributes;
	}
	
	/**
	 * @param key
	 * 		une String donnée.
	 * @return true
	 * 		ssi la clé donnée est contenue dans la liste d'attributs.
	 */
	public boolean hasAttribute(String key){
		return attributes.contains(key);
	}
	
	/**
	 * @param key
	 * 		une String donnée.
	 * @return String
	 * 		la valeur associée à la clé donnée dans la liste d'attributs.
	 */
	public String attributeValue(String key){
		return attributes.get(key);
	}
	
	public static abstract class Builder{
		protected long id;
		protected Attributes.Builder attributes = new Attributes.Builder();
		protected boolean incomplete = false;
		
		/**
		 * Construit un bâtisseur pour une entité OSM avec l'identifiant donné.
		 * @param id
		 * 		Un long, l'identifiant unique de l'entité.
		 */
		public Builder(long id){
			this.id = id;
		}
		
		/**
		 * Associe la clé à la valeur dans la liste d'attributs.
		 * @param key
		 * 		une String donnée.
		 * @param value
		 * 		une String, valeur associé à la clé dans la liste d'attributs.
		 */ 
		public void setAttribute(String key, String value){
			attributes.put(key, value);
		}
		
		/**
		 * déclare que l'entité en cours de construction est incomplète.
		 */
		public void setIncomplete(){
			incomplete = true;
		}
		
		/**
		 * @return true
		 * 		ssi l'entité est cours de construction est incomplète.
		 */
		public boolean isIncomplete(){
			return incomplete;
		}
	}
}

