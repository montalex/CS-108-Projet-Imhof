/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.xml.sax.SAXException;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public class PaintingTest {

   @Test
    public void testLausanne() throws SAXException, IOException{
     // Le peintre et ses filtres
       Painter painter = SwissPainter.painter();
        
        OSMMap map2 = OSMMapReader.readOSMFile(getClass().getResource("/lausanne.osm.gz").getFile(), true);
        OSMToGeoTransformer testTransformer = new OSMToGeoTransformer(new CH1903Projection());
        Map map = testTransformer.transform(map2);

        // La toile
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);
        Java2DCanvas canvas =
            new Java2DCanvas(bl, tr, 1600, 1060, 150, Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("loz.png"));
    }
    
    @Test
    public void testInterlaken() throws SAXException, IOException{
        // Le peintre et ses filtres
        Painter painter = SwissPainter.painter();
           
           OSMMap map2 = OSMMapReader.readOSMFile(getClass().getResource("/interlaken.osm.gz").getFile(), true);
           OSMToGeoTransformer testTransformer = new OSMToGeoTransformer(new CH1903Projection());
           Map map = testTransformer.transform(map2);

           // La toile
           Point bl = new Point(628590, 168210);
           Point tr = new Point(635660, 172870);
           Java2DCanvas canvas =
               new Java2DCanvas(bl, tr, 800, 530, 72, Color.WHITE);

           // Dessin de la carte et stockage dans un fichier
           painter.drawMap(map, canvas);
           ImageIO.write(canvas.image(), "png", new File("interlaken.png"));
       }
}
