/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Représente une projection CH1903 (utilisée en cartographie suisse).
 */
public final class CH1903Projection implements Projection {
	
    /**
     * Projete un PointGeo sur un plan cartésien avec la méthode CH1903.
     * @param point
     * 		Un PointGeo à projeter sur le plan.
     * @return Point
     * 		le PointGeo projeté sur le plan.	
     */
    @Override
    public Point project(PointGeo point){
        double longitude = Math.toDegrees(point.longitude());
        double latitude = Math.toDegrees(point.latitude());
        double longitude1 = ((longitude * 3600) - 26782.5) / 10000;
        double latitude1 = ((latitude * 3600) - 169028.66) / 10000;
        
        double x = 600072.37 + (211455.93 * longitude1) - (10938.51 * longitude1 * latitude1) - (0.36 * longitude1 * Math.pow(latitude1, 2))
                   - (44.54 * Math.pow(longitude1, 3));
        double y = 200147.07 + (308807.95 * latitude1) + (3745.25 * Math.pow(longitude1, 2)) + (76.63 * Math.pow(latitude1, 2))
                   - (194.56 * Math.pow(longitude1, 2) * latitude1) + (119.79 * Math.pow(latitude1, 3));
        
        return new Point(x, y);
    }
    
    /**
     * Transforme un Point donnée coordonée cartésienne en PointGeo avec la méthode CH1903. 
     * @param point
     * 		Un Point, à transformer en PointGeo.
     * @return PointGeo
     * 		le Point transformé.
     */
    @Override
    public PointGeo inverse(Point point){
        double x = point.x();
        double y = point.y();
        double x1 = (x - 600000)/1000000;
        double y1 = (y - 200000)/1000000;
        double longitude0 = 2.6779094 + (4.728982 * x1) + (0.791484 * x1 * y1) + (0.1306 * x1 * Math.pow(y1,2)) - (0.0436 * Math.pow(x1,3));
        double latitude0 = 16.9023892 + (3.238272 * y1) - (0.270978 * Math.pow(x1,2)) - (0.002528 * Math.pow(y1,2)) - (0.0447 * Math.pow(x1,2) * y1)
               - (0.0140 * Math.pow(y1,3));
        double longitude = Math.toRadians(longitude0 * 100 / 36);
        double latitude = Math.toRadians(latitude0 * 100 / 36);
        
        return new PointGeo(longitude, latitude);
    }
}
