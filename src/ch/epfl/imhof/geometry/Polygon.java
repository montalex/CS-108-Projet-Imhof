/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.geometry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente un polygon.
 */
public final class Polygon {
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;
    
    /** 
     * Construit un Polygon avec trous.
     * @param shell
     * 		Une ClosedPolyLine qui represente l'enveloppe du Polygon. 
     * @param holes
     * 		Une liste de ClosedPolyLine qui représente les trous dans l'enveloppe
     * 		du Polygon.
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes){
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }
    
    /** 
     * Construit un Polygon sans trous.
     * @param shell
     * 		Une ClosedPolyLine qui represente l'enveloppe du Polygon.
     */
    public Polygon(ClosedPolyLine shell){
        this(shell, Collections.emptyList());
    }
    
    /** 
     * @return ClosedPolyLine
     * 		l'enveloppe.
     */
    public ClosedPolyLine shell(){
        return shell;
    }
    
    /** 
     * @return List<ClosedPolyLine>
     * 		la liste de trous.
     */
    public List<ClosedPolyLine> holes(){
        return holes;
    }
    
}
