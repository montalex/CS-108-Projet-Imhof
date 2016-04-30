/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.CH1903Projection;

public class ReliefShaderTest {

   @Test
    public void test() throws Exception {
        CH1903Projection p = new CH1903Projection();
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(getClass().getResource("/N46E007.hgt").getFile()));
        Vector3 lightVector = new Vector3(-1, 1, 1);
        ReliefShader rs = new ReliefShader(p, dem, lightVector);
        Point bl = p.project(new PointGeo(Math.toRadians(7.8122), Math.toRadians(46.6645)));
        Point tr = p.project(new PointGeo(Math.toRadians(7.9049), Math.toRadians(46.7061)));
        BufferedImage image = rs.shadedRelief(bl, tr, 3316, 2188, 19);
        dem.close();
        ImageIO.write(image, "png", new File("testReliefShader.png"));
    }
}
