/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length < 8){
            throw new IllegalArgumentException("Pas assez d'arguments");
        }
        
        /**
         * Dessine la carte.
         */
        CH1903Projection projection = new CH1903Projection();
        OSMMap osmMap = OSMMapReader.readOSMFile((args[0]), true);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projection);
        Map map = transformer.transform(osmMap);
        PointGeo geobl = new PointGeo(Math.toRadians(Double.parseDouble(args[2])), Math.toRadians(Double.parseDouble(args[3])));
        PointGeo geotr = new PointGeo(Math.toRadians(Double.parseDouble(args[4])), Math.toRadians(Double.parseDouble(args[5])));
        Point bl = projection.project(geobl);
        Point tr = projection.project(geotr);
        int resolution = Integer.parseInt(args[6]);
        int height = (int)(Math.round((resolution / 2.54 * 100) * 1/25000 * (geotr.latitude() - geobl.latitude()) * Earth.RADIUS));
        int width = (int)(Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height));
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, resolution, Color.WHITE);
        Painter painter = SwissPainter.painter();
        painter.drawMap(map, canvas);
        BufferedImage imageMap = canvas.image();
        
        /**
         * Dessine le relief.
         */
        Vector3 lightVector = new Vector3(-1, 1, 1);
        double blurRadius = 0.17 * (resolution / 2.54);
        try(HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(args[1]))){
            ReliefShader rs = new ReliefShader(projection, dem, lightVector);
            BufferedImage imageRelief = rs.shadedRelief(bl, tr, width, height, blurRadius);
            /**
             * Mix des images.
             */
            BufferedImage imageFinal = new BufferedImage(width, height, TYPE_INT_RGB);
            for(int i = 0; i < imageFinal.getWidth(); i++){
                for(int j = 0; j < imageFinal.getHeight(); j++){
                    Color cMap = Color.rgb(imageMap.getRGB(i, j));
                    Color cRelief = Color.rgb(imageRelief.getRGB(i, j));
                    Color cFinal = cMap.multiply(cRelief);
                    imageFinal.setRGB(i, j, cFinal.toAWTColor().getRGB());
                }
            }
        
            /**
             * Produit l'image finale.
             */
            ImageIO.write(imageFinal, "png", new File(args[7]));
        }
    }

}
