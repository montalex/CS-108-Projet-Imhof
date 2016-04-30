/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.EndLine;
import ch.epfl.imhof.painting.LineStyle.SegmentStyle;

/**
 * Représente un peintre.
 */
public interface Painter {
    
    /**
     * Permet au peintre de dessiner la carte donnée sur la toile donnée.
     * @param map
     *      un Map, la carte à dessiner.
     * @param canvas
     *      un Java2DCanevas, la toile.
     */
    abstract void drawMap(Map map, Java2DCanvas canvas);
    
    /**
     * Retourne un peintre dessinant l'intérieur de tous les polygones de la carte avec la couleur donnée.
     * @param c
     *      un Color, la couleur voulue.
     * @return Painter
     *      le peintre.
     */
    public static Painter polygon(Color c){
        return (map, canvas) -> {
          for(Attributed<Polygon> p: map.polygons()){
              canvas.drawPolygon(p.value(), c);
          }
        };
    }
    
    /**
     * Retourne un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec le style correspondant.
     * @param ls
     *      un LineStyle, le style de ligne pour le dessin.
     * @return Painter
     *      le peintre.
     */
    public static Painter line(LineStyle ls){
        return (map, canvas) -> {
            for(Attributed<PolyLine> p: map.polyLines()){
                canvas.drawPolyLine(p.value(), ls);
            }
        };
    }
    
    /**
     * Retourne un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec le style correspondant.
     * @param width
     *      un float, la largeur du trait.
     * @param c
     *      un Color, la couleur du trait.
     * @param end
     *      un EndLine, La terminaison de la ligne.
     * @param seg
     *      un SegmentStyle, La jointure des segments.
     * @param sections
     *      un float[], L'alternance des sections opaques et transparentes de la lignes (pour traitillés).
     * @return Painter
     *      le peintre.
     */
    public static Painter line(float width, Color c, EndLine end, SegmentStyle seg, float... sections){
        return line(new LineStyle(width, c, end, seg, sections));
    }
    
    /**
     * Retourne un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec le style correspondant.
     * @param width
     *      un float, la largeur du trait.
     * @param c
     *      un Color, la couleur du trait.
     * @return Painter
     *      le peintre.
     */
    public static Painter line(float width, Color c){
        return line(new LineStyle(width, c));
    }
    
    /**
     * Retourne un peintre qui dessine les pourtours de l'enveloppe et des trous de tous les polygones 
     * de la carte qu'on lui fournit.
     * @param ls
     *      un LineStyle, le style de ligne pour le dessin.
     * @return Painter
     *      le peintre.
     */
    public static Painter outline(LineStyle ls){
        return (map, canvas) -> {
            for(Attributed<Polygon> p: map.polygons()){
                canvas.drawPolyLine(p.value().shell(), ls);
                for(ClosedPolyLine cp: p.value().holes()){
                    canvas.drawPolyLine(cp, ls);
                }
            }
        };
    }
    
    /**
     * Retourne un peintre qui dessine les pourtours de l'enveloppe et des trous de tous les polygones 
     * de la carte qu'on lui fournit.
     * @param width
     *      un float, la largeur du trait.
     * @param c
     *      un Color, la couleur du trait.
     * @param end
     *      un EndLine, La terminaison de la ligne.
     * @param seg
     *      un SegmentStyle, La jointure des segments.
     * @param sections
     *      un float[], L'alternance des sections opaques et transparentes de la lignes (pour traitillés).
     * @return Painter
     *      le peintre.
     */
    public static Painter outline(float width, Color c, EndLine end, SegmentStyle seg, float... sections){
        return outline(new LineStyle(width, c, end, seg, sections));
    }
    
    /**
     * Retourne un peintre qui dessine les pourtours de l'enveloppe et des trous de tous les polygones 
     * de la carte qu'on lui fournit.
     * @param width
     *      un float, la largeur du trait.
     * @param c
     *      un Color, la couleur du trait.
     * @return Painter
     *      le peintre.
     */
    public static Painter outline(float width, Color c){
        return outline(new LineStyle(width, c));
    }
    
    /**
     * Retourne un peintre se comportant comme celui auquel on l'applique, 
     * si ce n'est qu'il ne considère que les éléments de la carte satisfaisant le prédicat.
     * @param x
     *      un Predicate<Attributed<?>>, le predicate qui doit être satisfait.
     * @return Painter
     *      le peintre.
     */
    public default Painter when(Predicate<Attributed<?>> x){
        return (map, canvas) -> {
           Map.Builder mapTemp = new Map.Builder();
           for(Attributed<Polygon> ap: map.polygons()){
               if(x.test(ap)){
                   mapTemp.addPolygon(ap);
               }
           }
           for(Attributed<PolyLine> apl: map.polyLines()){
               if(x.test(apl)){
                   mapTemp.addPolyLine(apl);
               }
           }
           Map filteredMap = mapTemp.build();
           this.drawMap(filteredMap, canvas);
        };
    }
    
    /**
     * Retourne un peintre dessinant d'abord la carte produite par le peintre donné, puis, par dessus,
     * la carte produite par le peintre sur lequel on applique la méthode.
     * @param that
     *      le peintre donné.
     * @return Painter
     *      le nouveau peintre, cumul des deux autres.
     */
    public default Painter above(Painter that){
        return (map, canvas) -> {
          that.drawMap(map, canvas);
          this.drawMap(map, canvas);
        };
    }
    
    /**
     * Retourne un peintre qui dessine la carte par couches (de -5 à +5).
     * @return Painter
     *      le peintre.
     */
    public default Painter layered(){
       return (map, canvas) -> {
            for(int i = -5; i < 6; i++){
                this.when(Filters.onLayer(i)).drawMap(map, canvas);
             }
        };
    }
}
