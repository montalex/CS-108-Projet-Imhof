/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof;

/**
 * Représente un point en coordonnées terrestres.
 */
public final class PointGeo {
    private final double longitude;
    private final double latitude;
    
    /**
     * Construit un point avec la latitude et la longitude
     * données.
     * @param longitude
     * 		la longitude du point, en radians
     * @param latitude
     *      la latitude du point, en radians
     * @throws IllegalArgumentException
     *      si la longitude est invalide, c-à-d hors de l'intervalle [-π; π]
     * @throws IllegalArgumentException
     *      si la latitude est invalide, c-à-d hors de l'intervalle [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude) throws IllegalArgumentException{
        if(longitude > Math.PI  || longitude < -Math.PI ){
            throw new IllegalArgumentException("La longitude est impossible: " + longitude);
        }
        if(latitude > (Math.PI / 2.0) || latitude < -(Math.PI /2.0)){
            throw new IllegalArgumentException("La latitude est impossible: " + latitude);
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    /** 
     * @return double 
     * 		la longitude.
     */
    public double longitude(){
        return longitude;
    }
    
    /** 
     * @return double
     * 		la latitude.
     */
    public double latitude(){
        return latitude;
    }
}