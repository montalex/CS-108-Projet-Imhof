/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.dem;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * Représente un objet premettant d'ombré un relief.
 */
public final class ReliefShader {
    
    private final Projection p;
    private final HGTDigitalElevationModel dem;
    private final Vector3 lightVector;
    
    /**
     * Construit un ReliefShader, étant donnés une projection, un model numérique de terrain et un vecteur lumière.
     * @param p
     *      un Projection, la projection à utiliser.
     * @param dem
     *      un HGTDigitalElevationModel, le model numérique de terrain.
     * @param v
     *      un Vector3, le vecteur lumière.
     */
    public ReliefShader(Projection p, HGTDigitalElevationModel dem, Vector3 v){
        this.p = p;
        this.dem = dem;
        this.lightVector = v;
    }
    
    /**
     * Permet d'ombrer et flouter le relief (pour un meilleur rendu).
     * @param bl
     *      un Point, le point bas-gauche du relief à dessiner en coordonnées du plan.
     * @param tr
     *      un Point, le point haut-droit du relief à dessiner en coordonnées du plan.
     * @param width
     *      un int, la largeur de l'image en pixels de l'image à dessiner.
     * @param height
     *      un int, la hauteur de l'image en pixels de l'image à dessiner.
     * @param blurRadius
     *      un double, le rayon de floutage.
     * @return BufferedImage
     *      l'image avec le relief ombré flouté.
     */
    public BufferedImage shadedRelief(Point bl, Point tr, int width, int height, double blurRadius) {
        if(blurRadius == 0.0){
            int specialExtra = 2; //zone tampon arbitraire fixe car pas de floutage.
            Function<Point, Point> coordinateChanger = Point.alignedCoordinateChange(new Point(specialExtra/2, height + specialExtra/2), 
                                                        bl, new Point(width + specialExtra/2, specialExtra/2), tr);
            return grossShadedRelief(width + specialExtra, height + specialExtra, coordinateChanger)
                    .getSubimage((specialExtra / 2), (specialExtra / 2), width, height);
        } else {
            int extra = (int)(2 * Math.round(blurRadius)); //zone tampon.
            Function<Point, Point> coordinateChanger = Point.alignedCoordinateChange(new Point(extra/2, height + extra/2), 
                                                        bl, new Point(width + extra/2, extra/2), tr);
            return blurredImage(kernelCalculator(blurRadius), grossShadedRelief(width + extra, height + extra, coordinateChanger))
                    .getSubimage((extra / 2), (extra / 2), width, height);
        }  
    }
    
    /**
     * Permet de dessiner un relief ombré brut, étant donné sa taille en pixel 
     *  et une fonction permettant de passer du repère de l'image à celui du plan.
     * @param width
     *      un int, la largeur de l'image.
     * @param height
     *      un int, la hauteur de l'image.
     * @param f
     *      une Function<Point, Point>, fonction permettant de passer du repère de l'image à celui du plan.
     * @return BufferedImage
     *      l'image du relief ombré brut dessiné.
     */
    private BufferedImage grossShadedRelief(int width, int height, Function<Point, Point> coordinateChanger){
        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                PointGeo pointGeo = p.inverse(coordinateChanger.apply(new Point(i, j)));
                Vector3 normal = dem.normalAt(pointGeo).normalized();
                double cosAngle = (normal.scalarProduct(lightVector)) / (normal.norm() * lightVector.norm());
                java.awt.Color javaColor = Color.rgb((float)(0.5 * (cosAngle + 1)), (float)(0.5 * (cosAngle + 1)), 
                                                     (float)(0.5 * ((0.7 * cosAngle) + 1))).toAWTColor();
                image.setRGB(i, j, javaColor.getRGB());
            }
        }
        return image;
    }
    
    /**
     * Calcule le noyau du flou Gaussien étant donné son rayon.
     * @param blurRadius
     *      un double, le rayon de flou Gaussien.
     * @return Kernel
     *      le noyau de flou Gaussien.
     */
    private Kernel kernelCalculator(double blurRadius){
        int n = (int) ((2 * Math.ceil(blurRadius)) + 1);
        double sigma = blurRadius / 3;
        float[] values = new float[n];
        double sum = 0.0;
        int index = 0;
        for(int i = -Math.floorDiv(n, 2); i < Math.floorDiv(n, 2) + 1; i++){
            float value = (float) (Math.pow(Math.E, -((i*i)) / (2*sigma*sigma)));
            sum = sum + value;
            values[index] = value;
            index++;
        }
        float[] normalizedValues = new float[n];
        for(int i = 0; i < values.length; i++){
            normalizedValues[i] = (float)(values[i] / sum);
        }
        return new Kernel(n, 1, normalizedValues);
    }
    
    /**
     * Permet de flouter une image, avec un noyau et une image donnés.
     * @param kHorizontal
     *      un Kernel, le noyau Gaussien.
     * @param image
     *      un BufferedImage, l'image donnée.
     * @return Image
     *      l'image floutée.
     */
    private BufferedImage blurredImage(Kernel kHorizontal, BufferedImage image){
        Kernel kVertical = new Kernel(1, kHorizontal.getWidth(), kHorizontal.getKernelData(new float[kHorizontal.getWidth()]));
        ConvolveOp cHorizontal = new ConvolveOp(kHorizontal, ConvolveOp.EDGE_NO_OP, null);
        ConvolveOp cVertical = new ConvolveOp(kVertical, ConvolveOp.EDGE_NO_OP, null);
        return cVertical.filter(cHorizontal.filter(image, null), null);
    }
}
