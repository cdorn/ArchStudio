package org.archstudio.bna.things.glass;

import org.archstudio.bna.IBNAView;
import org.archstudio.bna.ICoordinateMapper;
import org.archstudio.bna.IResources;
import org.archstudio.bna.IThingPeer;
import org.archstudio.bna.things.AbstractSplineThingPeer;
import org.archstudio.bna.utils.BNAUtils;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.swt.graphics.RGB;

public class SplineGlassThingPeer<T extends SplineGlassThing> extends AbstractSplineThingPeer<T> implements
		IThingPeer<T> {

	public SplineGlassThingPeer(T thing) {
		super(thing);
	}

	@Override
	public void draw(IBNAView view, ICoordinateMapper cm, IResources r, Graphics g) {
		if (t.isSelected()) {
			int[] xyArray = BNAUtils.toXYArray(BNAUtils.worldToLocal(cm, t.getPoints()));
			g.setForegroundColor(r.getColor(new RGB(255, 255, 255)));
			g.drawPolyline(xyArray);
			g.setForegroundColor(r.getColor(new RGB(0, 0, 0)));
			g.setLineDash(new int[]{4,4});
			g.setLineWidth(1);
			if (g instanceof SWTGraphics) {
				((SWTGraphics) g).setLineDashOffset(t.getRotatingOffset());
			}
			g.drawPolyline(xyArray);
		}
	}
}
