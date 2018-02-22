package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import objects.*;

public class DrawingBoard extends JPanel {

	private MouseAdapter mouseAdapter;
	private List<GObject> gObjects;
	private GObject target;

	private int gridSize = 10;

	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setPreferredSize(new Dimension(800, 600));
	}

	public void addGObject(GObject gObject) {
		gObjects.add(gObject);
		repaint();
	}

	public void groupAll() {
		CompositeGObject component = new CompositeGObject();
		for (int i = 0; i < gObjects.size(); i++) {
			GObject obj = gObjects.get(i);
			obj.deselected();
			component.add(obj);
		}
		gObjects.removeAll(gObjects);
		gObjects.add(component);
		component.selected();
		repaint();
	}

	public void deleteSelected() {
		for (int i = 0; i < gObjects.size(); i++) {
			GObject obj = gObjects.get(i);
			if (obj.isSelected()) {
				gObjects.remove(obj);
			}
		}
		repaint();
	}

	public void clear() {
		for (int i = 0; i < gObjects.size(); i++) {
			GObject obj = gObjects.get(i);
			obj.deselected();
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBackground(g);
		paintGrids(g);
		paintObjects(g);
	}

	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintGrids(Graphics g) {
		g.setColor(Color.lightGray);
		int gridCountX = getWidth() / gridSize;
		int gridCountY = getHeight() / gridSize;
		for (int i = 0; i < gridCountX; i++) {
			g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
		}
		for (int i = 0; i < gridCountY; i++) {
			g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
		}
	}

	private void paintObjects(Graphics g) {
		for (GObject go : gObjects) {
			go.paint(g);
		}
	}

	class MAdapter extends MouseAdapter {

		int nowX;
		int nowY;

		private void deselectAll() {
			clear();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			boolean isInArea = false;
			for (GObject obj : gObjects) {
				if (obj.pointerHit(e.getX(), e.getY())) {
					obj.selected();
					target = obj;
					isInArea = true;
					nowX = e.getX();
					nowY = e.getY();
				}
			}
			if (!isInArea) {
				deselectAll();
				target = null;
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (target != null) {
				target.move(nowX - e.getX(), nowY - e.getY());
				repaint();
				nowX = e.getX();
				nowY = e.getY();
			}
		}
	}
}
