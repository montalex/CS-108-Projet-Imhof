/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.geometry;
import java.util.List;

/**
 * Représente une polyligne fermée.
 */
public final class ClosedPolyLine extends PolyLine {
	
    /** Construit une polyligne fermée avec les points donnés.
     *  @param points
     * 		La liste des sommets (Points) qui constituent la Polyligne.
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }
    
    /** 
     * @return true
     * 		toujours, car la polyligne est fermée.
     */
    public boolean isClosed(){
        return true;
    }
    
    /** 
     * @return double
     * 		l'aire de la Polyligne fermée.
     */
    public double area(){
        double area = 0.0;
        for(int i = 0; i < this.points().size(); i++){
            double x = this.points().get(i).x();
            
            if((i + 1) < this.points().size()){
                double yPlus = this.points().get(i + 1).y();
                if((i - 1) < 0){
                    double yMinus = this.points().get((this.points().size() - 1)).y();
                    area = area + (x*(yPlus - yMinus));
                } else {
                    double yMinus = this.points().get(i - 1).y();
                    area = area + (x*(yPlus - yMinus));
                }   
            } else {
                double yPlus = this.points().get(0).y();
                if((i - 1) < 0){
                    double yMinus = this.points().get((this.points().size() - 1)).y();
                    area = area + (x*(yPlus - yMinus));
                } else {
                    double yMinus = this.points().get(i - 1).y();
                    area = area + (x*(yPlus - yMinus));
                }
            }
        }
        return 0.5 * Math.abs(area);
    }
    
    /** 
     * @return true 
     * 		ssi le point p donné est dans la Polyligne fermée.
     * @param p
     * 		Un Point donné dont on cherche à savoir s'il est dans la Polyligne.
     */
    public boolean containsPoint(Point p){
        int counter = 0;
        for(int i = 0; i < this.points().size(); i++){
            double y = p.y();
            double y1 = this.points().get(i).y();
            
            if((i + 1) < this.points().size()){
                double y2 = this.points().get(i + 1).y();
                if(y1 <= y){
                    if(y2 > y && isLeft(p, this.points().get(i), this.points().get(i+1))){
                        counter = counter + 1;
                    }
                } else if(y2 <= y && isLeft(p, this.points().get(i + 1), this.points().get(i))){
                    counter = counter - 1;
                }
            } else {
                double y2 = this.points().get(0).y();
                if(y1 <= y){
                    if(y2 > y && isLeft(p, this.points().get(i), this.points().get(0))){
                        counter = counter + 1;
                    }
                } else if(y2 <= y && isLeft(p, this.points().get(0), this.points().get(i))){
                    counter = counter - 1;
                }
            }
        }
        return (counter != 0);
    }
    
    /** 
     * @return true 
     * 		ssi le point p est à gauche de la ligne tracée par q1 et q2.
     * @param q1, q2
     * 		Les points donnés qui "tracent" la ligne.
     * @param p
     * 		Le Point donné, on cherche à savoir s'il est à gauche de la ligne
     * 		tracée par les deux autres points.
     */
    public boolean isLeft(Point p, Point q1, Point q2){
        return ((q1.x() - p.x()) * (q2.y() - p.y()) > (q2.x() - p.x()) * (q1.y() - p.y()));
    }
}
