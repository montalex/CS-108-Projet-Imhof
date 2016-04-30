/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

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

public class TestFinal {

   @Test
    public void testInterlakenFinal() throws Exception {
        CH1903Projection projection = new CH1903Projection();
        OSMMap osmMap = OSMMapReader.readOSMFile(getClass().getResource("/interlaken.osm.gz").getFile(), true);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projection);
        Map map = transformer.transform(osmMap);
        PointGeo geobl = new PointGeo(Math.toRadians(Double.parseDouble("7.8122")), Math.toRadians(Double.parseDouble("46.6645")));
        PointGeo geotr = new PointGeo(Math.toRadians(Double.parseDouble("7.9049")), Math.toRadians(Double.parseDouble("46.7061")));
        Point bl = projection.project(geobl);
        Point tr = projection.project(geotr);
        int resolution = Integer.parseInt("300");
        int height = (int)(Math.round((resolution / 2.54 * 100) * 1/25000 * (geotr.latitude() - geobl.latitude()) * Earth.RADIUS));
        int width = (int)(Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height));
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, resolution, Color.WHITE);
        Painter painter = SwissPainter.painter();
        painter.drawMap(map, canvas);
        BufferedImage imageMap = canvas.image();
        
        Vector3 lightVector = new Vector3(-1, 1, 1);
        double blurRadius = 0.17 * (resolution / 2.54);
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(getClass().getResource("/N46E007.hgt").getFile()));
        ReliefShader rs = new ReliefShader(projection, dem, lightVector);
        BufferedImage imageRelief = rs.shadedRelief(bl, tr, width, height, blurRadius);
        dem.close();
       
        BufferedImage imageFinal = new BufferedImage(width, height, TYPE_INT_RGB);
        for(int i = 0; i < imageFinal.getWidth(); i++){
            for(int j = 0; j < imageFinal.getHeight(); j++){
                Color cMap = Color.rgb(imageMap.getRGB(i, j));
                Color cRelief = Color.rgb(imageRelief.getRGB(i, j));
                Color cFinal = cMap.multiply(cRelief);
                imageFinal.setRGB(i, j, cFinal.toAWTColor().getRGB());
            }
        }
        
        ImageIO.write(imageFinal, "png", new File("InterlakenFinalTest.png"));
    }
    
    @Test
    public void testLausanneFinal() throws Exception {
        CH1903Projection projection = new CH1903Projection();
        OSMMap osmMap = OSMMapReader.readOSMFile(getClass().getResource("/lausanne.osm.gz").getFile(), true);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projection);
        Map map = transformer.transform(osmMap);
        PointGeo geobl = new PointGeo(Math.toRadians(Double.parseDouble("6.5594")), Math.toRadians(Double.parseDouble("46.5032")));
        PointGeo geotr = new PointGeo(Math.toRadians(Double.parseDouble("6.6508")), Math.toRadians(Double.parseDouble("46.5459")));
        Point bl = projection.project(geobl);
        Point tr = projection.project(geotr);
        int resolution = Integer.parseInt("300");
        int height = (int)(Math.round((resolution / 2.54 * 100) * 1/25000 * (geotr.latitude() - geobl.latitude()) * Earth.RADIUS));
        int width = (int)Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, resolution, Color.WHITE);
        Painter painter = SwissPainter.painter();
        painter.drawMap(map, canvas);
        BufferedImage imageMap = canvas.image();

        
        Vector3 lightVector = new Vector3(-1, 1, 1);
        double blurRadius = 0.17 * (resolution / 2.54);
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(getClass().getResource("/N46E006.hgt").getFile()));
        ReliefShader rs = new ReliefShader(projection, dem, lightVector);
        BufferedImage imageRelief = rs.shadedRelief(bl, tr, width, height, blurRadius);
        dem.close();
        
        BufferedImage imageFinal = new BufferedImage(width, height, TYPE_INT_RGB);
        for(int i = 0; i < imageFinal.getWidth(); i++){
            for(int j = 0; j < imageFinal.getHeight(); j ++){
                Color cMap = Color.rgb(imageMap.getRGB(i, j));
                Color cRelief = Color.rgb(imageRelief.getRGB(i, j));
                Color cFinal = cMap.multiply(cRelief);
                imageFinal.setRGB(i, j, cFinal.toAWTColor().getRGB());
            }
        }
        
        ImageIO.write(imageFinal, "png", new File("LausanneFinalTest.png"));
    }
    
    @Test
    public void testBerneFinal() throws Exception {
        CH1903Projection projection = new CH1903Projection();
        OSMMap osmMap = OSMMapReader.readOSMFile(getClass().getResource("/berne.osm.gz").getFile(), true);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projection);
        Map map = transformer.transform(osmMap);
        PointGeo geobl = new PointGeo(Math.toRadians(Double.parseDouble("7.3912")), Math.toRadians(Double.parseDouble("46.9322")));
        PointGeo geotr = new PointGeo(Math.toRadians(Double.parseDouble("7.4841")), Math.toRadians(Double.parseDouble("46.9742")));
        Point bl = projection.project(geobl);
        Point tr = projection.project(geotr);
        int resolution = Integer.parseInt("300");
        int height = (int)(Math.round((resolution / 2.54 * 100) * 1/25000 * (geotr.latitude() - geobl.latitude()) * Earth.RADIUS));
        int width = (int)Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, resolution, Color.WHITE);
        Painter painter = SwissPainter.painter();
        painter.drawMap(map, canvas);
        BufferedImage imageMap = canvas.image();
        
       
        Vector3 lightVector = new Vector3(-1, 1, 1);
        double blurRadius = 0.17 * (resolution / 2.54);
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(getClass().getResource("/N46E007.hgt").getFile()));
        ReliefShader rs = new ReliefShader(projection, dem, lightVector);
        BufferedImage imageRelief = rs.shadedRelief(bl, tr, width, height, blurRadius);
        dem.close();
        
        
        BufferedImage imageFinal = new BufferedImage(width, height, TYPE_INT_RGB);
        for(int i = 0; i < imageFinal.getWidth(); i++){
            for(int j = 0; j < imageFinal.getHeight(); j ++){
                Color cMap = Color.rgb(imageMap.getRGB(i, j));
                Color cRelief = Color.rgb(imageRelief.getRGB(i, j));
                Color cFinal = cMap.multiply(cRelief);
                imageFinal.setRGB(i, j, cFinal.toAWTColor().getRGB());
            }
        }
        
        ImageIO.write(imageFinal, "png", new File("BerneFinalTest.png"));
    }
    
    @Test
    public void testNelsonFinal() throws Exception {
        CH1903Projection projection = new CH1903Projection();
        OSMMap osmMap = OSMMapReader.readOSMFile(getClass().getResource("/Nelson.osm.xml").getFile(), false);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projection);
        Map map = transformer.transform(osmMap);
        PointGeo geobl = new PointGeo(Math.toRadians(Double.parseDouble("173.2560")),-Math.toRadians(Double.parseDouble("41.2492")));
        PointGeo geotr = new PointGeo(Math.toRadians(Double.parseDouble("173.3345")),-Math.toRadians(Double.parseDouble("41.2997")));
        Point bl = projection.project(geobl);
        Point tr = projection.project(geotr);
        int resolution = Integer.parseInt("300");
        int height = (int)Math.abs((Math.round((resolution / 2.54 * 100) * 1/25000 * (geotr.latitude() - geobl.latitude()) * Earth.RADIUS)));
        int width = (int)Math.abs(Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height));
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, resolution, Color.WHITE);
        Painter painter = SwissPainter.painter();
        painter.drawMap(map, canvas);
        BufferedImage imageMap = canvas.image();
        
       
        Vector3 lightVector = new Vector3(-1, 1, 1);
        double blurRadius = 0.17 * (resolution / 2.54);
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(getClass().getResource("/S41E173.hgt").getFile()));
        ReliefShader rs = new ReliefShader(projection, dem, lightVector);
        BufferedImage imageRelief = rs.shadedRelief(bl, tr, width, height, blurRadius);
        dem.close();
        
        
        BufferedImage imageFinal = new BufferedImage(width, height, TYPE_INT_RGB);
        for(int i = 0; i < imageFinal.getWidth(); i++){
            for(int j = 0; j < imageFinal.getHeight(); j ++){
                Color cMap = Color.rgb(imageMap.getRGB(i, j));
                Color cRelief = Color.rgb(imageRelief.getRGB(i, j));
                Color cFinal = cMap.multiply(cRelief);
                imageFinal.setRGB(i, j, cFinal.toAWTColor().getRGB());
            }
        }
        
        ImageIO.write(imageFinal, "png", new File("NelsonFinalTest.png"));
    }
}
