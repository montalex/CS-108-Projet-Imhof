/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Représente une toile.
 */
public interface Canvas {
    /**
     * Dessine une polyligne donnée.
     * @param p
     *      un PolyLine, la polyligne à dessiner.
     * @param ls
     *      un LigneStyle, le style de ligne pour le dessin.
     */
    abstract void drawPolyLine(PolyLine p, LineStyle ls);
    
    /**
     * Dessine un polygon donné.
     * @param p
     *      un Polygon, le polygon à dessiner.
     * @param c
     *      un Color, la couleur de fond du polygon.
     */
    abstract void drawPolygon(Polygon p, Color c);
}
