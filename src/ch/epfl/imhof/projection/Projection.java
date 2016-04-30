/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.projection;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Simple interface dans le but de faire implémenter les classes
 * project() et inverse() aux classes CH1903Projection et EquirectangularProjection.
 */
public interface Projection {
    abstract Point project(PointGeo point);
    abstract PointGeo inverse(Point point);
}
