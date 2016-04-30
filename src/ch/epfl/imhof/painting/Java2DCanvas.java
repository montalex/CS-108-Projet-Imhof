/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.function.Function;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Représente une Toile Java2D.
 */
public final class Java2DCanvas implements Canvas {
    private final BufferedImage image;
    private final Graphics2D graphics;
    private final Function<Point, Point> coordinateChanger;
    
    /**
     * Construit une Toile Java2D
     * @param p1
     *      un Point, le point en bas à gauche de la carte.
     * @param p2
     *      un Point, le point en haut à droite de la carte.
     * @param width
     *      un int, la largeur de la toile.
     * @param height
     *      un int, la hauteur de la toile.
     * @param resolution
     *      un int, la résolution en point par pouce de l'image.
     * @param c
     *      un Color, la couleur de fond de le toile.
     */
    public Java2DCanvas(Point p1, Point p2, int width, int height, int resolution, Color c){
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = image.createGraphics();
        graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        graphics.scale((resolution / 72.0), (resolution / 72.0));
        graphics.setColor(c.toAWTColor());
        graphics.fillRect(0, 0, width, height);
        double x = width * 72.0 / resolution;
        double y = height * 72.0 / resolution;
        coordinateChanger = Point.alignedCoordinateChange(p1, new Point(0,y), p2, new Point(x, 0));
    }
    
    @Override
    public void drawPolyLine(PolyLine p, LineStyle ls) {
        graphics.setColor(ls.getColor().toAWTColor());      
        Path2D path = new Path2D.Double();
        Point firstPoint = coordinateChanger.apply(p.firstPoint());
        path.moveTo(firstPoint.x(), firstPoint.y());
        Iterator<Point> pointIt = p.points().iterator();
        pointIt.next();
        while(pointIt.hasNext()){
            Point newPoint = coordinateChanger.apply(pointIt.next());
            path.lineTo(newPoint.x(), newPoint.y());
        }
        if(p.isClosed()){
            path.closePath();
        }
        graphics.setStroke(new BasicStroke(ls.getWidth(), ls.getEndLine().ordinal(), ls.getSegmentStyle().ordinal(),
                            10f, ls.getSections().length == 0 ? null : ls.getSections(), 0f));
        graphics.draw(path);
    }

    @Override
    public void drawPolygon(Polygon p, Color c) {
        graphics.setColor(c.toAWTColor());
        
        Path2D shape = new Path2D.Double();
        Point firstPoint = coordinateChanger.apply(p.shell().firstPoint());
        shape.moveTo(firstPoint.x(), firstPoint.y());
        Iterator<Point> pointIt = p.shell().points().iterator();
        pointIt.next();
        while(pointIt.hasNext()){
            Point newPoint = coordinateChanger.apply(pointIt.next());
            shape.lineTo(newPoint.x(), newPoint.y());
        }
        shape.closePath();
        Area polygon = new Area(shape);
        
        for(ClosedPolyLine cp: p.holes()){
            Path2D path = new Path2D.Double();
            Point firstPoint2 = coordinateChanger.apply(cp.firstPoint());
            path.moveTo(firstPoint2.x(), firstPoint2.y());
            Iterator<Point> pointIt2 = cp.points().iterator();
            pointIt2.next();
            while(pointIt2.hasNext()){
                Point point = pointIt2.next();
                Point newPoint = coordinateChanger.apply(point);
                path.lineTo(newPoint.x(), newPoint.y());
            }
            path.closePath();
            Area hole = new Area(path);
            polygon.subtract(hole);
        }
        graphics.fill(polygon);
    }
    
    /**
     * Permet d'obtenir l'image de la toile.
     * @return BufferedImage
     *      l'image de la toile.
     */
    public BufferedImage image(){
        return image;
    }
}
