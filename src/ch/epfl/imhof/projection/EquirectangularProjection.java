/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Représente une projection équirectangulaire.
 */
public final class EquirectangularProjection implements Projection {

 /**
 * Projete un PointGeo sur un plan cartésien avec la méthode de Projection equirectangulaire.
 * 
 * @param point
 * 		Un PointGeo à projeter sur le plan.
 * 
 * @return Point
 * 		le PointGeo projeté sur le plan.	
 */
    @Override
    public Point project(PointGeo point){
        return new Point(point.longitude(), point.latitude());
    }

/**
 * Transforme un Point donnée coordonée cartésienne en PointGeo avec la méthode de 
 * projection equirectangulaire.
 *    
 * @param point
 * 		Un Point, à transformer en PointGeo.
 * 
 * @return PointGeo
 * 		le Point transformé.
 */
    @Override
    public PointGeo inverse(Point point){
        return new PointGeo(point.x(), point.y());
    }
}
