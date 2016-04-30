/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Représente un model numérique de terrain (MNT).
 */
public interface DigitalElevationModel extends AutoCloseable{
    
    /**
     * La résolution angulaire d'un MNT.
     */
    public final static double ANGULAR_RESOLUTION = Math.toRadians(1/3600d);
    
    /**
     * L'espace, en mètres, séparant les points du MNT.
     */
    public final static double S = ANGULAR_RESOLUTION * Earth.RADIUS;
    
    /**
     * Permet de calculer le vecteur normal à la Terre en un point donné.
     * @param p
     *      un PointGeo, le point donné.
     * @return Vector3
     *      le vecteur normal à la Terre en ce point donné.
     * @throws IllegalArgumentException
     *      ssi le point pour lequel la normale est demandé ne fait pas partie de la zone couverte par le MNT
     */
    abstract Vector3 normalAt(PointGeo p) throws IllegalArgumentException;
}
