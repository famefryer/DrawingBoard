package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class CompositeGObject extends GObject {

	private List<GObject> gObjects;

	public CompositeGObject() {
		super(0, 0, 0, 0);
		gObjects = new ArrayList<GObject>();
	}

	public void add(GObject gObject) {
		gObjects.add(gObject);
	}

	public void remove(GObject gObject) {
		gObjects.remove(gObject);
	}

	@Override
	public void move(int dX, int dY) {
		super.move(dX, dY);
		for (GObject obj : gObjects) {
			obj.move(dX, dY);
		}
	}

	public void recalculateRegion() {
		int maxX = gObjects.get(0).x;
		int maxY = gObjects.get(0).y;
		int minX = gObjects.get(0).x;
		int minY = gObjects.get(0).y;

		for (GObject g : gObjects) {
			if (g.x + g.width > maxX) {
				maxX = g.x + g.width;
			}
			if (g.y + g.height > maxY) {
				maxY = g.y + g.height;
			}
			if (g.x < minX) {
				minX = g.x;
			}
			if (g.y < minY) {
				minY = g.y;
			}
		}

		this.x = minX;
		this.y = minY;

		this.width = maxX - minX;
		this.height = maxY - minY;
	}

	@Override
	public void paintObject(Graphics g) {
		recalculateRegion();
		for (GObject obj : gObjects) {
			obj.paint(g);
		}
	}

	@Override
	public void paintLabel(Graphics g) {
		g.drawString("Component", x, y);
	}

}
