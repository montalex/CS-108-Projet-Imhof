/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Représente une carte au sens géographique.
 */
public final class Map {
	private final List<Attributed<PolyLine>> polyLines;
	private final List<Attributed<Polygon>> polygons;
	
	/**
	 * Construit une carte à partir des listes de polylignes et polygones attribués donnés.
	 * @param polyLines
	 * 		un List<Attributed<PolyLine>>, liste de polylignes attribuées données.
	 * @param polygons
	 * 		un List<Attributed<Polygon>>, liste de polygons attribués donnés.
	 */
	public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons){
		this.polyLines = Collections.unmodifiableList(new ArrayList<Attributed<PolyLine>>(polyLines));
		this.polygons = Collections.unmodifiableList(new ArrayList<Attributed<Polygon>>(polygons));
	}
	
	/**
	 * @return List<Attributed<PolyLine>>
	 * 		la liste des polylignes attribuées de la carte.
	 */
	public List<Attributed<PolyLine>> polyLines(){
		return polyLines;
	}
	
	/**
	 * @return List<Attributed<Polygon>>
	 * 		la liste des polygons attribués de la carte.
	 */
	public List<Attributed<Polygon>> polygons(){
		return polygons;
	}
	
	/**
	 * Permet de construire une carte par étapes.
	 */
	public static final class Builder{
		private List<Attributed<PolyLine>> polyLinesTemp = new ArrayList<Attributed<PolyLine>>();
		private List<Attributed<Polygon>> polygonsTemp = new ArrayList<Attributed<Polygon>>();
		
		/**
		 * Ajoute une polyligne attribuée à la carte en cours de construction
		 * @param newPolyLine
		 * 		un Attributed<PolyLine>, la polyligne attribuée à ajouter.
		 */
		public void addPolyLine(Attributed<PolyLine> newPolyLine){
			polyLinesTemp.add(newPolyLine);
		}
		
		/**
		 * Ajoute un polygone attribué à la carte en cours de construction
		 * @param newPolygon
		 * 		un Attributed<Polygon>, le polygon attribué à ajouter.
		 */
		public void addPolygon(Attributed<Polygon> newPolygon){
			polygonsTemp.add(newPolygon);
		}
		
		/**
		 * Construit une carte avec les polylignes et polygones ajoutés jusqu'à présent au bâtisseur.
		 * @return Map
		 * 		la nouvelle carte construite à l'aide du bâtisseur.
		 */
		public Map build(){
			return new Map(polyLinesTemp, polygonsTemp);
		}
	}
}
