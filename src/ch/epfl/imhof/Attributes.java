/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * Représente un ensemble d'attributs et la valeur qui leur est associée.
 */
public final class Attributes {
	private final Map<String, String> mapAttributes;
	
	/**
	 * Construit un objet Attributes avec une table donnée.
	 * @param attributes
	 * 			Une Map<String, String>, table qui associe un attribut avec sa valeur.
	 */
	public Attributes(Map<String, String> attributes){
		mapAttributes = Collections.unmodifiableMap(new HashMap<String, String>(attributes));
		}
	
	/**
	 * @return true
	 * 			ssi l'ensemble d'attributs est vide.
	 */
	public boolean isEmpty(){
		return mapAttributes.isEmpty();
	}
	
	/**
	 * @param key
	 * 			Une chaîne de caractères clé qui est sensé être associée à une valeur dans la table.
	 * @return true
	 * 			ssi l'ensemble d'attributs contient key.
	 */
	public boolean contains(String key){
		return mapAttributes.containsKey(key);
	}
	
	/**
	 * @param key
	 * 			Une chaîne de caractères clé qui est sensé être associée à une valeur dans la table.
	 * @return String
	 * 			la valeur associée à "key" dans la table.
	 */
	public String get(String key){
		return mapAttributes.get(key);
	}
	
	/**
	 * @param key
	 * 			Une chaîne de caractères clé qui est sensé être associée à une valeur dans la table.
	 * @param defaultValue
	 * 			Une chaîne de caractères valeur qui sert de valeur par défaut si "key" n'est pas dans la table.
	 * @return String
	 * 			la valeur associée à "key" dans la table, ou defaultValue si aucune valeur ne lui est associée.
	 */
	public String get(String key, String defaultValue){
		if(this.contains(key)){
			return this.get(key);
		} else return defaultValue;
	}
	
	/**
	 * @param key
	 * 			Une chaîne de caractères clé qui est sensé être associée à une valeur dans la table.
	 * @param defaultValue
	 * 			Une valeur int qui sert de valeur par défaut si "key" n'est pas dans la table.
	 * @return int
	 * 			la valeur associée à "key" dans la table en Integer, ou defaultValue si aucune valeur ne lui est associée.
	 */
	public int get(String key, int defaultValue){
		if(this.contains(key)){
			try{
				return Integer.parseInt(this.get(key));
			} catch(NumberFormatException e){
				return defaultValue;
			}
		} else return defaultValue;
	}
	
	/**
	 * @param keysToKeep
	 * 			Un ensemble de chaîne de caractères clés voulant être garder (le filtre).
	 * @return Attributes
	 * 			Un nouvel objet Attributes, version filtrée des attributs ne contenant que ceux dont le nom figure dans l'ensemble passé.
	 */
	public Attributes keepOnlyKeys(Set<String> keysToKeep){
		Attributes.Builder temp = new Attributes.Builder();
		for(String s: keysToKeep){
			if(mapAttributes.containsKey(s)){
				temp.put(s, this.get(s));
			}
		}
		return temp.build();
	}
	
	public final static class Builder{
		private Map<String, String> mapAttributes = new HashMap<String, String>();
		
		/**
		 * Ajoute une association clé - valeur dans la table mapAttributes.
		 * 
		 * @param key
		 * 			Une chaîne de caractères clé.
		 * @param value
		 * 			Une chaîne de caractères valeur associée à la clé.
		 */
		public void put(String key, String value){
			mapAttributes.put(key, value);
		}
		
		/**
		 * @return Attributes
		 * 			Construit et retourne un nouvel objet Attributes avec les associations clé - valeur ajoutées.
		 */
		public Attributes build(){
			return new Attributes(mapAttributes);
		}
	}
}
