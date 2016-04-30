/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.osm;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

/**
 * Représente un lecteur de fichier OSM.
 */
public final class OSMMapReader {
	/**
	 * Constructeur privé car cette classe ne doit pas être instansiable.
	 */
	private OSMMapReader(){}
	
	private final static String NODE = "node";
	private final static String WAY = "way";
	private final static String RELATION = "relation";
	private final static String TAG = "tag";
	private final static String MEMBER = "member";
	private final static String ROLE = "role";
	private final static String TYPE = "type";
	private final static String ID = "id";
	private final static String K = "k";
	private final static String V = "v";
	private final static String LONGITUDE = "lon";
	private final static String LATITUDE = "lat";
	private final static String REF = "ref";
	private final static String ND = "nd";
	
	/**
	 * lit la carte OSM contenue dans le fichier de nom donné, 
	 * 	en le décompressant avec gzip si et seulement si le second argument est vrai
	 * @param fileName
	 * 		un String, le nom du fichier à lire.
	 * @param unGZip
	 * 		un boolean, true si le fichier est compréssé par GZip.
	 * @return OSMMap
	 * 		un OSMMap, la carte OSM avec les données contenues dans le fichier.
	 * @throws SAXException
	 * 		En cas d'erreur dans le format du fichier XML contenant la carte
	 * @throws IOException
	 * 		En cas d'autre erreur d'entrée/sortie, p.ex. si le fichier n'existe pas.
	 */
	public static OSMMap readOSMFile(String fileName, boolean unGZip) throws SAXException, IOException{
		OSMMap.Builder mapTemp = new OSMMap.Builder();
		if(unGZip){
			try (InputStream i = new GZIPInputStream(new FileInputStream(fileName))) {
				return mapMaker(mapTemp, i);
			}
		} else {
			try (InputStream i = new FileInputStream(fileName)) {
			    return mapMaker(mapTemp, i);
			}
		}
	}
	
	private static OSMMap mapMaker(OSMMap.Builder mapTemp, InputStream i) throws SAXException, IOException{
	    XMLReader r = XMLReaderFactory.createXMLReader();
        r.setContentHandler(new DefaultHandler() {
            /**
             * @param OSMNode.Builder, OSMWay.Builder, OSMRelation.Builder
             *      Ces builders servent à stocker les valeurs contenues dans le fichier OSM à fin de créer la carte OSM.
             * 
             * @param counter
             *      un int, ce paramètre sert à localiser si on se trouve dans un node, way ou relation.
             */
            private OSMNode.Builder nodeTemp;
            private OSMWay.Builder wayTemp;
            private OSMRelation.Builder relationTemp;
            private int counter = 0;
            
            /**
             * L'override de startElement permet de traiter chaque balise ouvrante pour le cas:
             *  node, way, relation, tag, nd et member. Ceci permet d'ajouter ces données aux constructeurs
             *  à fin de créer la carte OSM.
             */
            @Override
              public void startElement(String uri, String lName, String qName, Attributes atts) throws SAXException {
                switch(qName){
                case NODE :
                    counter = 1;
                    PointGeo pointTemp = new PointGeo(Math.toRadians(Double.parseDouble(atts.getValue(LONGITUDE))), 
                                                        Math.toRadians(Double.parseDouble(atts.getValue(LATITUDE))));
                    nodeTemp = new OSMNode.Builder(Long.parseLong(atts.getValue(ID)), pointTemp);
                    break;
                case WAY :
                    counter = 2;
                    wayTemp = new OSMWay.Builder(Long.parseLong(atts.getValue(ID)));
                    break;
                case RELATION :
                    counter = 3;
                    relationTemp = new OSMRelation.Builder(Long.parseLong(atts.getValue(ID)));
                    break;
                case TAG :
                    switch(counter){
                    case 1 :
                        nodeTemp.setAttribute(atts.getValue(K), atts.getValue(V));
                        break;
                    case 2 :
                        wayTemp.setAttribute(atts.getValue(K), atts.getValue(V));
                        break;
                    case 3 :
                        relationTemp.setAttribute(atts.getValue(K), atts.getValue(V));
                        break;
                    default : break; //n'arrive jamais.
                    }
                    break;
                case ND :
                    if(!(mapTemp.nodeForId(Long.parseLong(atts.getValue(REF))) == null)){
                        wayTemp.addNode(mapTemp.nodeForId(Long.parseLong(atts.getValue(REF))));
                    } else{
                        wayTemp.setIncomplete();
                    }
                    break;
                case MEMBER :
                    switch(atts.getValue(TYPE)){
                    case NODE :
                        if(!(mapTemp.nodeForId(Long.parseLong(atts.getValue(REF))) == null)){
                            relationTemp.addMember(Type.NODE, atts.getValue(ROLE), mapTemp.nodeForId(Long.parseLong(atts.getValue(REF))));
                        } else{
                            relationTemp.setIncomplete();
                        }
                        break;
                    case WAY :
                        if(!(mapTemp.wayForId(Long.parseLong(atts.getValue(REF))) == null)){
                            relationTemp.addMember(Type.WAY, atts.getValue(ROLE), mapTemp.wayForId(Long.parseLong(atts.getValue(REF))));
                        } else{
                            relationTemp.setIncomplete();
                        }
                        break;
                    case RELATION :
                        if(!(mapTemp.relationForId(Long.parseLong(atts.getValue(REF))) == null)){
                            relationTemp.addMember(Type.RELATION, atts.getValue(ROLE), mapTemp.relationForId(Long.parseLong(atts.getValue(REF))));
                        } else{
                            relationTemp.setIncomplete();
                        }
                        break;
                    default : break; //n'arrive jamais.
                    }
                    break;
                default : break;//n'arrive jamais
                }
            }
            
            /**
             * L'override de endElement sert simplement à construite les divers éléments à partir
             *  des builders, s'ils ne sont pas incomplets.
             */
             @Override
              public void endElement(String uri, String lName, String qName) {
                switch(qName){
                case NODE :
                    if(!nodeTemp.isIncomplete()){
                        mapTemp.addNode(nodeTemp.build());
                    }
                    break;
                case WAY :
                    if(!wayTemp.isIncomplete()){
                        mapTemp.addWay(wayTemp.build());
                    }
                    break;
                case RELATION :
                    if(!relationTemp.isIncomplete()){
                        mapTemp.addRelation(relationTemp.build());
                    }
                    break;
                default : break; //n'arrive jamais.
                }
              }
        });
        r.parse(new InputSource(i));
	return mapTemp.build();
    }
}