/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;

/**
 * Représente un convertisseur de données OSM en carte.
 */
public final class OSMToGeoTransformer {
	private final Projection projection;
	private static final Set<String> setForPolyLine  = new HashSet<String>(Arrays.asList("bridge", "highway", "layer", "man_made", "railway", "tunnel", "waterway"));
	private static final Set<String> setForPolygon = new HashSet<String>(Arrays.asList("building", "landuse", "layer", "leisure", "natural", "waterway"));
	private static final Set<String> setArea = new HashSet<String>(Arrays.asList("aeroway", "amenity", "building", "harbour", "historic","landuse",
	        "leisure", "man_made", "military", "natural", "office", "place", "power", "public_transport", "shop", "sport", "tourism", "water", 
	        "waterway", "wetland"));
	
	/**
	 * Construit un OSMToGeoTransformer avec la projection donnée.
	 * @param projection
	 * 		un Projection, la projection qui sera utilisé pour la tranformation.
	 */
	public OSMToGeoTransformer(Projection projection){
		this.projection = projection;
	}
	
	/**
	 * Transforme une CarteOSM en Carte géographique.
	 * @param map
	 * 		un OSMMap, la CarteOSM à transformer.
	 * @return Map
	 * 		la Carte géographique.
	 */
	public Map transform(OSMMap map){
		Map.Builder mapGeoTemp = new Map.Builder();
		/**
		 * Transforme les cheminOSM, en filtrant les attributs à garder, en Polygon sans trou, s'il est fermé et que ses attributs indiquent qu'il décrit une surface.
		 * Sinon, transforme le chamin en Polyligne.
		 * Ensuite ajoute ce Polygon ou cette Polyligne attribué(e) au constructeur de carte.
		 */
		for(OSMWay w: map.ways()){
		    boolean test = false;
		    for(String s : setArea){
		        if(w.hasAttribute(s)){
		            test = true;
		        }
		    }
            if(w.isClosed()){
                if(((w.hasAttribute("area")) && (w.attributeValue("area").equals("yes") || w.attributeValue("area").equals("1")
                        || w.attributeValue("area").equals("true"))) || test){
					PolyLine.Builder polyLineTemp = new PolyLine.Builder();
					Attributes attsToKeep = w.attributes().keepOnlyKeys(setForPolygon);
					if(!(attsToKeep.isEmpty())){
						for(int i = 0; i < (w.nodes().size() - 1); i++){
							polyLineTemp.addPoint(projection.project(w.nodes().get(i).position()));
						}
						mapGeoTemp.addPolygon(new Attributed<Polygon>(new Polygon(polyLineTemp.buildClosed()), attsToKeep));
					}
				} else {
					PolyLine.Builder polyLineTemp = new PolyLine.Builder();
					Attributes attsToKeep = w.attributes().keepOnlyKeys(setForPolyLine);
					if(!(attsToKeep.isEmpty())){
						for(OSMNode n: w.nodes()){
							polyLineTemp.addPoint(projection.project(n.position()));
						}
						mapGeoTemp.addPolyLine(new Attributed<PolyLine>(polyLineTemp.buildClosed(), attsToKeep));
					}
				}
			} else {
				PolyLine.Builder polyLineTemp = new PolyLine.Builder();
				Attributes attsToKeep = w.attributes().keepOnlyKeys(setForPolyLine);
				if(!(attsToKeep.isEmpty())){
						for(OSMNode n: w.nodes()){
							polyLineTemp.addPoint(projection.project(n.position()));
						}
						mapGeoTemp.addPolyLine(new Attributed<PolyLine>(polyLineTemp.buildOpen(), attsToKeep));
				}
			}
		}
		
		/**
		 * Transforme les relationsOSM, en filtrant les attributs à garder, en Polygon attribué.
		 * Puis ajoute ce Polygon à la carte en construction
		 */
		for(OSMRelation r: map.relations()){
		    
			if(r.hasAttribute("type") && r.attributes().get("type").equals("multipolygon")){
				Attributes attsToKeep = r.attributes().keepOnlyKeys(setForPolygon);
				if(!(attsToKeep.isEmpty())){
					List<Attributed<Polygon>> attributedPolygons = assemblePolygon(r, attsToKeep);
					for(int i = 0; i < attributedPolygons.size(); i++){
						mapGeoTemp.addPolygon(attributedPolygons.get(i));
					}
				}
			}
		}
		return mapGeoTemp.build();
	}
	
	/**
	 * Sépare les cheminsOSM selon leur role et ensuite regroupe le nouvel ensemble de chemins 
	 * en fonction des noeuds à leur extrémités pour former des Polylignes fermées.
	 * @param relation
	 * 		un OSMRelation, la relation donnée.
	 * @param role
	 * 		un String, le role que l'on veut isoler.
	 * @return List<ClosedPolyLine>
	 * 		la liste des PolyLines fermées qu'on a transformé. La liste est vide si le calcul échoue.
	 */
	private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){
		List<ClosedPolyLine> listPolyLines = new ArrayList<ClosedPolyLine>();
		
		List<OSMWay> listWay = new ArrayList<OSMWay>();
		for(Member m: relation.members()){
			if(m.type().equals(Type.WAY) && m.role().equals(role)){
				listWay.add((OSMWay)m.member());
			}
		}
		if(listWay.isEmpty()){
			return listPolyLines;
		}
		
		Graph.Builder<OSMNode> graphTemp = new Graph.Builder<OSMNode>();
		for(int i = 0; i < listWay.size(); i++){
			for(int j = 0; j < listWay.get(i).nodesCount(); j++){
				graphTemp.addNode(listWay.get(i).nodes().get(j));
			}
			for(int j = 0; j < (listWay.get(i).nodesCount() - 1); j++){
					graphTemp.addEdge(listWay.get(i).nodes().get(j), listWay.get(i).nodes().get(j + 1));
			}
		}
		Graph<OSMNode> graph = graphTemp.build();
		if(graph.nodes().isEmpty() || graph.nodes().size() == 1){
			return listPolyLines;
		}
		
		List<OSMNode> listNodes = new ArrayList<OSMNode>(graph.nodes());
		for(OSMNode n: listNodes){
			if(graph.neighborsOf(n).size() != 2){
				return listPolyLines;
			}
		}
		if(!(listNodes.isEmpty())){
			List<OSMNode> visited = new ArrayList<OSMNode>();
			List<Point> points = new ArrayList<Point>();
			int size = listNodes.size();
			OSMNode n = listNodes.get(0);
			OSMNode first = listNodes.get(0);
			List<OSMNode> voisins = new ArrayList<OSMNode>(graph.neighborsOf(n));
			while(visited.size() != size){
				points.add(projection.project(n.position()));
				visited.add(n);
				OSMNode v = voisins.get(0);
				OSMNode m = n;
				n = v;
				if(v.equals(first)){
					listPolyLines.add(new ClosedPolyLine(points));
					points.clear();
					listNodes.removeAll(visited);
					if(!(listNodes.isEmpty())){
						first = listNodes.get(0);
						n = listNodes.get(0);
					} else return listPolyLines;
				}
				voisins = new ArrayList<OSMNode>(graph.neighborsOf(n));
				voisins.remove(m);
			}
			return listPolyLines;
		} else {
			return listPolyLines;
		}
	}
	
	/**
	 * Calcule et retourne la liste des polygones attribués de la relation donnée, 
	 * en leur attachant les attributs donnés.
	 * @param relation
	 * 		un OSMRelation, la relation donnée avec laquelle on calcule la liste de polygones.
	 * @param attributes
	 * 		un Attributes, la liste d'attributs à attacher à aux polygones.
	 * @return List<Attributed<Polygon>>
	 * 		la liste de polygones attribués. La liste ne contient que des polygons extérieur si le calcul des anneaux intérieur à échouer.
	 * 		La liste est complétement vide si le calcul des anneaux extérieur à échouer.
	 */
	private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes){
		List<Attributed<Polygon>> listAttributedPolygon = new ArrayList<Attributed<Polygon>>();	
		List<ClosedPolyLine> outerPolyLines = ringsForRole(relation, "outer");
		List<ClosedPolyLine> innerPolyLines = ringsForRole(relation, "inner");
		
		if(outerPolyLines.isEmpty()){
			return listAttributedPolygon;
		} else if(innerPolyLines.isEmpty()){
			for(ClosedPolyLine op: outerPolyLines){
				listAttributedPolygon.add(new Attributed<Polygon>(new Polygon(op), attributes));
			}
			return listAttributedPolygon;
		} else {
			Collections.sort(outerPolyLines, (ClosedPolyLine cp1, ClosedPolyLine cp2) -> {
				Double aire1 = cp1.area();
				Double aire2 = cp2.area();
				return aire1.compareTo(aire2);
			});
			for(ClosedPolyLine op: outerPolyLines){
				List<ClosedPolyLine> containedList = new ArrayList<ClosedPolyLine>();
				for(ClosedPolyLine ip: innerPolyLines){
					if(op.containsPoint(ip.firstPoint())){
						containedList.add(ip);
					}
				}
				innerPolyLines.removeAll(containedList);
				if(containedList.isEmpty()){
					listAttributedPolygon.add(new Attributed<Polygon>(new Polygon(op), attributes));
				} else {
					listAttributedPolygon.add(new Attributed<Polygon>(new Polygon(op, containedList), attributes));
				}
			}
			return listAttributedPolygon;
		}
	}
}
