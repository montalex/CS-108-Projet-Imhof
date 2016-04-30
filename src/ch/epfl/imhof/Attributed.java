/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof;

/**
 * Représente une entité de type T dotée d'attributs.
 * @param <T>
 *      Le type de l'entité.
 */
public final class Attributed<T> {
	private final T value;
	private final Attributes attributes;
	
	/**
	 * Construit une valeur attribuée dont la valeur et les attributs sont ceux donnés
	 * @param value
	 * 			Valeur donnée
	 * @param attributes
	 * 			Table associatives d'attributs.
	 */
	public Attributed(T value, Attributes attributes){
		this.value = value;
		this.attributes = attributes;
	}
	
	/**
	 * @return T
	 * 		La valeur de l'objet.
	 */
	public T value(){
		return value;
	}
	
	/**
	 * @return Attributes
	 * 			La table associative d'attributs.
	 */
	public Attributes attributes(){
		return attributes;
	}
	
	/**
	 * @param attributeName
	 * 			String, clé sensée être contenue dans la table associative.
	 * @return true
	 * 			ssi La table associative d'attributs contient la clé.
	 */
	public boolean hasAttribute(String attributeName){
		return attributes.contains(attributeName);
	}
	
	/**
	 * @param attributeName
	 * 			String, clé sensée être contenue dans la table associative.
	 * @return String
	 * 			La valeur associée à la clé dans la table.
	 */
	public String attributeValue(String attributeName){
		return attributes.get(attributeName);
	}
	
	/**
	 * @param attributeName
	 * 			String, clé sensée être contenue dans la table associative.
	 * @param defaultValue
	 * 			Valeur par défaut associée à la clé si rien n'était associé.
	 * @return String
	 * 			La valeur associée à la clé.
	 */
	public String attributeValue(String attributeName, String defaultValue){
		return attributes.get(attributeName, defaultValue);
	}
	
	/**
	 * @param attributeName
	 * 			String, clé sensé être contenue dans la table associative.
	 * @param defaultValue
	 * 			Valeur par défaut associé à la clé si rien n'était associé.
	 * @return int
	 * 			La valeur associée à la clé en Integer, ou la valeur par défaut si rien n'était associé.
	 */
	public int attributeValue(String attributeName, int defaultValue){
		return attributes.get(attributeName, defaultValue);
	}
}
