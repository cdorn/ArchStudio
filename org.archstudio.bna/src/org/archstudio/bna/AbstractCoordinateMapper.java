package org.archstudio.bna;

import static org.archstudio.sysutils.SystemUtils.simpleName;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.archstudio.bna.CoordinateMapperEvent.EventType;
import org.archstudio.bna.utils.BNAUtils;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public abstract class AbstractCoordinateMapper implements IMutableCoordinateMapper, Cloneable {

	public static final Rectangle getDefaultBounds() {
		return new Rectangle(-100000, -100000, 200000, 200000);
	}

	protected Rectangle worldBounds = getDefaultBounds();
	protected double localScale = 1.0d;
	protected Point localOrigin = new Point(worldBounds.x + worldBounds.width / 2, worldBounds.y + worldBounds.height
			/ 2);

	protected List<ICoordinateMapperListener> listeners = new CopyOnWriteArrayList<ICoordinateMapperListener>();

	public AbstractCoordinateMapper() {
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		AbstractCoordinateMapper t = (AbstractCoordinateMapper) super.clone();
		t.worldBounds = new Rectangle(t.worldBounds.x, t.worldBounds.y, t.worldBounds.width, t.worldBounds.height);
		t.localOrigin = new Point(t.localOrigin.x, t.localOrigin.y);
		t.listeners = new CopyOnWriteArrayList<ICoordinateMapperListener>();
		return t;
	}

	@Override
	public void addCoordinateMapperListener(ICoordinateMapperListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCoordinateMapperListener(ICoordinateMapperListener l) {
		listeners.remove(l);
	}

	protected synchronized void fireCoordinateMapperEvent(EventType eventtType) {
		fireCoordinateMapperEvent(new CoordinateMapperEvent(this, eventtType, worldBounds, getLocalOrigin(),
				getLocalScale()));
	}

	protected synchronized void fireCoordinateMapperEvent(CoordinateMapperEvent evt) {
		for (ICoordinateMapperListener l : listeners) {
			l.coordinateMappingsChanged(evt);
		}
	}

	@Override
	public synchronized Rectangle getWorldBounds() {
		return BNAUtils.clone(worldBounds);
	}

	@Override
	public synchronized void setWorldBounds(Rectangle worldBounds) {
		this.worldBounds = BNAUtils.clone(worldBounds);
		fireCoordinateMapperEvent(EventType.WORLD_BOUNDS);
	}

	@Override
	public Point getLocalOrigin() {
		return BNAUtils.clone(localOrigin);
	}

	@Override
	public void setLocalOrigin(Point localOrigin) {
		this.localOrigin = BNAUtils.clone(localOrigin);
		fireCoordinateMapperEvent(EventType.LOCAL_ORIGIN);
	}

	@Override
	public double getLocalScale() {
		return localScale;
	}

	@Override
	public void setLocalScale(double localScale) {
		this.localScale = localScale;
		fireCoordinateMapperEvent(EventType.LOCAL_SCALE);
	}

	@Override
	public void align(Point localPoint, Point worldPoint) {
		Point oldLocalPoint = worldToLocal(new Point(worldPoint.x, worldPoint.y));
		Point localDelta = new Point(localPoint.x - oldLocalPoint.x, localPoint.y - oldLocalPoint.y);
		this.localOrigin.x -= localDelta.x;
		this.localOrigin.y -= localDelta.y;
		fireCoordinateMapperEvent(EventType.LOCAL_ORIGIN);
	}

	@Override
	public void setLocalScaleAndAlign(final double localScale, final Point localPoint, final Point worldPoint) {
		setLocalScale(localScale);
		align(localPoint, worldPoint);
	}

	@Override
	public String toString() {
		return simpleName(this.getClass()) + "["//
				+ "worldBounds=" + worldBounds + ","//
				+ "localScale=" + localScale + ","//
				+ "localOrigin=" + localOrigin + "]";
	}

	@Override
	public Rectangle localToWorld(Rectangle localRectangle) {
		Point topLeft = localToWorld(new Point(localRectangle.x, localRectangle.y));
		Point bottomRight = localToWorld(new Point(//
				localRectangle.x + localRectangle.width, //
				localRectangle.y + localRectangle.height));
		return new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
	}

	@Override
	public Rectangle worldToLocal(Rectangle worldRectangle) {
		Point topLeft = worldToLocal(new Point(worldRectangle.x, worldRectangle.y));
		Point bottomRight = worldToLocal(new Point(//
				worldRectangle.x + worldRectangle.width, //
				worldRectangle.y + worldRectangle.height));
		return new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
	}
}