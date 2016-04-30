/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.geometry;
import java.util.List;

/**
 * Représente une polyligne ouverte.
 */
public final class OpenPolyLine extends PolyLine {
    /** 
     * Construit une polyligne ouverte avec les sommets donnés. 
     *  @param points
     * 		La liste des sommets (Points) qui constituent la Polyligne.
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }
    
    /** 
     * @return false
     * 		toujours, car la polyligne est ouverte.
     */
    public boolean isClosed(){
        return false;
    }
}
