/**
 *  @Author:      Alexis Montavon (245433)
 *  @Author:      Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * Représente un filtre.
 */
public final class Filters {
    
    /**
     * Constructeur privé car classe non instanciable.
     */
    private Filters(){}
    
    /**
     * @param key
     *      un String, le nom de l'attribut.
     * @return Predicate<Attributed<?>>
     *      retourne un prédicat qui n'est vrai que si la valeur attribuée à laquelle on l'applique 
     *      possède un attribut portant ce nom.
     */
    public static Predicate<Attributed<?>> tagged(String key){
        return x -> x.hasAttribute(key);
    }
    
    /**
     * @param key
     *      un String, le nom de l'attribut.
     * @param values
     *      des Strings, les valeurs voulues de l'attribut.
     * @return Predicate<Attributed<?>>
     *      retourne un prédicat qui n'est vrai que si la valeur attribuée à laquelle on l'applique 
     *      possède un attribut portant le nom donné et si de plus la valeur associée à cet attribut 
     *      fait partie de celles données.
     */
    public static Predicate<Attributed<?>> tagged(String key, String... values){
       return x -> {
           if(!(x.hasAttribute(key))){
               return false;
           } else {
               for(String s: values){
                   if(x.attributeValue(key).equals(s)){
                       return true;
                   }
               }
               return false;
           }
       };
    }
    
    /**
     * Permet de savoir si l'entité à laquelle on l'applique est sur la couche donnée.
     * Toute entité ne possédant pas cet attribut (ou le possédant mais avec une valeur non entière) 
     * se comporte comme si elle le possédait et sa valeur était 0.
     * @param i
     *      un int, La couche à laquelle l'entité doit appartenir.
     * @return Predicate<Attributed<?>>
     *      retourne vrai que lorsqu'on l'applique à une entité attribuée appartenant cette couche.
     */
    public static Predicate<Attributed<?>> onLayer(int i){
        return x -> {
            return x.attributeValue("layer", 0) == i; 
        };
    }
}
