/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Représente un point en coordonnées cartésiennes.
 */
public final class Point {
    private final double x;
    private final double y;
    
    /** 
     * Crée un point à deux coordonnées cartésiennes.
     * @param x
     * 		La valeur de x en double.
     * @param y
     * 		La valeur de y en double.
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * @return double 
     * 		la coordonée x
     */
    public double x(){
        return x;
    }
   
    /**
     * @return double
     * 		la coordonée y
     */
    public double y(){
        return y;
    }
    
    /**
     * Permet d'avoir une fonction de changement de repère. 
     * Etant donnés deux paires de points (donc un total de quatre points) 
     * retourne le changement de repère correspondant.
     * @param p11
     *      un Point, le premier point dans le premier repère.
     * @param p12
     *      un Point, le premier point dans le deuxième repère.
     * @param p21
     *      un Point, le deuxième point dans le premier repère.
     * @param p22
     *      un Point, le deuxième point dans le deuxième repère.
     * @return Function<Point, Point>
     *      la fonction permettant le passage du premier au deuxième repère.
     * @throws IllegalArgumentException
     *      ssi les deux points sont situés sur une même ligne horizontale ou verticale.
     */
    public static Function<Point, Point> alignedCoordinateChange(Point p11, Point p12, Point p21, Point p22) throws IllegalArgumentException{
        if(p11.x() == p21.x() || p11.y() == p21.y()){
            throw new IllegalArgumentException("Les points sont sur une même ligne verticale.");
        }
        return x -> {
            double b1 = (p22.x() * p11.x() - p12.x() * p21.x()) / (p11.x() - p21.x());
            double b2 = (p22.y() * p11.y() - p12.y() * p21.y()) / (p11.y() - p21.y());
            double a1 = (p12.x() - b1) / p11.x();
            double a2 = (p12.y() - b2) / p11.y();
            return new Point(a1 * x.x() + b1, a2 * x.y() + b2);
        };
    }
}
