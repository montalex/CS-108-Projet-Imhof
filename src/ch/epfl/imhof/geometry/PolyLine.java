/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.geometry;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Représente une polyligne.
 */
public abstract class PolyLine {
    private final List<Point> listPoints;
    
    /** Construit une polyligne avec les sommets donnés.
     * 
     * @param points
     * 		La liste des sommets (Points) qui constituent la Polyligne.
     * 
     * @throws IllegalArguementException
     * 		si la liste de sommets données en argument est vide.
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if(points.isEmpty()){
            throw new IllegalArgumentException("La liste donnée est vide.");
        }
       listPoints = Collections.unmodifiableList(new ArrayList<Point>(points)); 
    }
    
    /** 
     * Permet de savoir si la polyligne est fermée ou ouverte.
     */
    public abstract boolean isClosed();
    
    /** 
     * @return List<Point>
     * 		la liste des sommets. 
     */
    public List<Point> points(){
        return listPoints;
    }
    
    /** 
     * @return Point
     * 		le premier sommet de la liste.
     */
    public Point firstPoint(){
        return listPoints.get(0);
    }
    
    /** 
     * Permet de construire une Polyligne en plusieurs étapes.
     */
    public final static class Builder {
        private ArrayList<Point> listTemp = new ArrayList<Point>();
        
        /** 
         * Ajoute un point à la liste en cours de construction.
         * @param newPoint
         * 		un sommet (Point) qui est ajouté pour former la Polyligne.
         */
        public void addPoint(Point newPoint) {
            listTemp.add(newPoint);
        }
        
        /** 
         * Construit et retourne une polyligne ouverte avec la liste de Points.
         * @return OpenPolyLine
         * 		une nouvelle Polyligne ouverte, formée de la liste de Points ajoutés.
         */
        public OpenPolyLine buildOpen(){
            return new OpenPolyLine(listTemp);
        }
        
        /** 
         * Construit et retourne une polyligne fermée avec la liste de Points.
         * @return ClosedPolyLine
         * 		une nouvelle Polyligne fermée, formée de la liste de Points ajoutés.
         */
        public ClosedPolyLine buildClosed(){
            return new ClosedPolyLine(listTemp);
        }
    }
}
