/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;


import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.EndLine;
import ch.epfl.imhof.painting.LineStyle.SegmentStyle;

/**
 * Représente un générateur de peintre de réseau routier.
 */
public final class RoadPainterGenerator {
   
    /**
     * Non instanciable
     */
    private RoadPainterGenerator(){};
    
    /**
     * Représente une spécification de route.
     */
    public static final class RoadSpec {
        private Predicate<Attributed<?>> predicate;
        private float wi, wc;
        private Color ci, cc;
        
        /**
         * Construit une spécification de route avec les attributs données.
         * @param predicate
         *      un Predicate<Attributed<?>>, un filtre.
         * @param wi
         *      un float, la largeur i.
         * @param ci
         *      un Color, la couleur i.
         * @param wc
         *      un float, la largeur c.
         * @param cc
         *      un Color, la couleur c.
         */
        public RoadSpec(Predicate<Attributed<?>> predicate, float wi, Color ci, float wc, Color cc){
            this.predicate = predicate;
            this.wi = wi;
            this.ci = ci;
            this.wc = wc;
            this.cc = cc;
        }
    }
    
    /**
     * Permet de générer un peintre de route.
     * @param rsList
     *      un List[RoadSpec], une liste d'un nombre variable de spécifications de route.
     * @return Painter
     *      le peintre.
     */
   public static Painter painterForRoads(RoadSpec... rsList){
        float[] f = new float[]{};
        Painter innerBridgePainter = Painter.line(rsList[0].wi, rsList[0].ci, EndLine.ROUND, SegmentStyle.ROUND, f).when(Filters.tagged("bridge")).when(rsList[0].predicate);
        for(int i = 1; i < rsList.length; i++){
            Painter newPainter = Painter.line(rsList[i].wi, rsList[i].ci, EndLine.ROUND, SegmentStyle.ROUND, f).when(Filters.tagged("bridge")).when(rsList[i].predicate);
            innerBridgePainter = innerBridgePainter.above(newPainter);
        }
        Painter outerBridgePainter = Painter.line((rsList[0].wi + (2*rsList[0].wc)), rsList[0].cc, EndLine.BUTT, SegmentStyle.ROUND, f).when(Filters.tagged("bridge")).when(rsList[0].predicate);
        for(int i = 1; i < rsList.length; i++){
            Painter newPainter = Painter.line((rsList[i].wi + (2*rsList[i].wc)), rsList[i].cc, EndLine.BUTT, SegmentStyle.ROUND, f).when(Filters.tagged("bridge")).when(rsList[i].predicate);
            outerBridgePainter = outerBridgePainter.above(newPainter);
        }
        Painter innerRoadPainter = Painter.line(rsList[0].wi, rsList[0].ci, EndLine.ROUND, SegmentStyle.ROUND, f).when((Filters.tagged("bridge").or(Filters.tagged("tunnel"))).negate()).when(rsList[0].predicate);
        for(int i = 1; i < rsList.length; i++){
            Painter newPainter = Painter.line(rsList[i].wi, rsList[i].ci, EndLine.ROUND, SegmentStyle.ROUND, f).when((Filters.tagged("bridge").or(Filters.tagged("tunnel"))).negate()).when(rsList[i].predicate);
            innerRoadPainter = innerRoadPainter.above(newPainter);
        }
        Painter outerRoadPainter = Painter.line((rsList[0].wi + (2*rsList[0].wc)), rsList[0].cc, EndLine.ROUND, SegmentStyle.ROUND, f).when((Filters.tagged("bridge").or(Filters.tagged("tunnel"))).negate()).when(rsList[0].predicate);
        for(int i = 1; i < rsList.length; i++){
            Painter newPainter =  Painter.line((rsList[i].wi + (2*rsList[i].wc)), rsList[i].cc, EndLine.ROUND, SegmentStyle.ROUND, f).when((Filters.tagged("bridge").or(Filters.tagged("tunnel"))).negate()).when(rsList[i].predicate);
            outerRoadPainter = outerRoadPainter.above(newPainter);
        }
        Painter tunnelPainter = Painter.line(rsList[0].wi/2, rsList[0].cc, EndLine.BUTT, SegmentStyle.ROUND, (2 * rsList[0].wi), (2 * rsList[0].wi)).when(Filters.tagged("tunnel")).when(rsList[0].predicate);
        for(int i = 1; i < rsList.length; i++){
            Painter newPainter = Painter.line(rsList[i].wi/2, rsList[i].cc, EndLine.BUTT, SegmentStyle.ROUND, (2 * rsList[i].wi), (2 * rsList[i].wi)).when(Filters.tagged("tunnel")).when(rsList[i].predicate);
            tunnelPainter = tunnelPainter.above(newPainter);
        }
        return (innerBridgePainter.above(outerBridgePainter).above(innerRoadPainter).above(outerRoadPainter).above(tunnelPainter)).layered();
    }
}