package com.github.ittyflow.viz;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class PersistedLayout<V,E> extends StaticLayout<V,E>{
	
	boolean boundsDirty = true;
	double minX = 0.0;
	double maxX = 0.0;
	double minY = 0.0;
	double maxY = 0.0;

	List<Collection<V>> rankedVertices;
	
	public PersistedLayout(Graph<V, E> graph) {
		super(graph);
		
		rankedVertices = groupIntoRanks();
		
		int y = 100;
		for(Collection<V> rank : rankedVertices) {
			int x = 300;
			for(V v:rank) {
				setLocation(v, x, y);
				x+= 400;
			}
			y += 100;
		}
	}
	
	protected Collection<V> getRoots() {
		Set<V> roots = new HashSet<V>();

		roots.addAll(getGraph().getVertices());
		
		for(E edge : getGraph().getEdges()) {
			Pair<V> pair = getGraph().getEndpoints(edge);
			roots.remove(pair.getSecond());
		}
		
		return roots;
	}

	protected void addToRank(Set<V> seen, List<Collection<V>> ranks, int index, Collection<V>vs) {
		seen.addAll(vs);
		
		Collection<V> rank;
		if(ranks.size() <= index) {
			rank = new ArrayList<V>();
			ranks.add(rank);
		} else {
			rank = ranks.get(index);
		}
		rank.addAll(vs);
		
		for(V v : vs) {
			Set<V> next = new HashSet<V>();
			for(E oe : getGraph().getOutEdges(v)) {
				V ov = getGraph().getEndpoints(oe).getSecond();
				if(seen.contains(ov))
					continue;
				next.add(ov);
			}
			
			addToRank(seen, ranks, index+1, next);
		}
	}
	
	protected List<Collection<V>> groupIntoRanks() {
		Set<V> seen = new HashSet<V>();
		
		Collection<V> roots = getRoots();
		List<Collection<V>> ranks = new ArrayList<Collection<V>>();

		addToRank(seen, ranks, 0, roots);
		
		return ranks;
	}
	
	public String getFilename() {
		return "savedLayout.ser";
	}
	
	public void read() {
		Map<String, Location> positions;
		try {
			FileInputStream fis = new FileInputStream(getFilename());
			ObjectInputStream ois = new ObjectInputStream(fis);
			positions = (Map<String, Location>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
//			throw new RuntimeException(ex);
			return;
		}
		
		for(V vertex : getVertices()) {
			String v = vertex.toString();
			Location l = positions.get(v);
			if (l == null)
				continue;
			this.setLocation(vertex, l.getX(), l.getY());
		}
	}
	
	public static class Location implements Serializable {
		private final double x, y;

		public Location(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}
		
	}
	
	public void write() {
		Map<String, Location> positions = new HashMap<String, Location>();
		for(V vertex : getVertices()) {
			String v = vertex.toString();
			double x = getX(vertex);
			double y = getY(vertex);
			positions.put(v, new Location(x,y));
		}
		
		try{
			FileOutputStream fos = new FileOutputStream(getFilename());
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(positions);
			oos.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	
	
	@Override
	public void setLocation(V picked, double x, double y) {
		boundsDirty = true;
		// TODO Auto-generated method stub
		super.setLocation(picked, x, y);
	}

	@Override
	public void setLocation(V picked, Point2D p) {
		boundsDirty = true;
		// TODO Auto-generated method stub
		super.setLocation(picked, p);
	}

	@Override
	public void setSize(Dimension size) {
		// TODO Auto-generated method stub
		super.setSize(size);
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		if(boundsDirty) {
		minX = 0.0;
		maxX = 0.0;
		minY = 0.0;
		maxY = 0.0;
		
		for(V vertex : getGraph().getVertices() ) {
			double x = getX(vertex);
			double y = getY(vertex);
			if(x > maxX)
				maxX = x;
			if(y > maxY)
				maxY = y;
		}
		maxX += 200;
		maxY += 200;
		boundsDirty = false;
		}
		
		return new Dimension((int)maxX, (int)maxY);
	}
	
	
}
