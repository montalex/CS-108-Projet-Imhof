/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof;

/**
 * Représente un vecteur tridimensionnel.
 */
public final class Vector3 {
    private final double x, y, z;
    
    /**
     * Construit un vecteur tridimensionnel avec ses trois composantes données.
     * @param x
     *      un double, la composante x.
     * @param y
     *      un double, la composante y.
     * @param z
     *      un double, la composante z.
     */
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Permet d'obtenir la norme du vecteur.
     * @return double
     *      la norme du vecteur.
     */
    public double norm(){
        return Math.sqrt(x*x + y*y + z*z);
    }
    
    /**
     * Permet d'obtenir une version normalisée du vecteur.
     * @return Vector3
     *      le nouveau vecteur de longueur unitaire.
     */
    public Vector3 normalized(){
        return new Vector3((x / this.norm()), (y / this.norm()), (z / this.norm()));
    }
    
    /**
     * Permet d'obtenir le produit scalaire de deux vecteurs.
     * @param that
     *      un Vector3, le deuxième vecteur.
     * @return double
     *      le produit scalaire des deux vecteurs.
     */
    public double scalarProduct(Vector3 that){
        return (this.x * that.x) + (this.y * that.y) + (this.z * that.z);
    }
    
    /**
     * Permet d'obtenir la composante x du vecteur.
     * @return double
     *      la composante x du vecteur.
     */
    public double x(){
        return x;
    }
    
    /**
     * Permet d'obtenir la composante y du vecteur.
     * @return double
     *      la composante y du vecteur.
     */
    public double y(){
        return y;
    }
    
    /**
     * Permet d'obtenir la composante z du vecteur.
     * @return double
     *      la composante z du vecteur.
     */
    public double z(){
        return z;
    }
}
