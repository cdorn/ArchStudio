package org.archstudio.bna.things.utility;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import org.archstudio.bna.IBNAView;
import org.archstudio.bna.ICoordinate;
import org.archstudio.bna.ICoordinateMapper;
import org.archstudio.bna.IResources;
import org.archstudio.bna.IThingPeer;
import org.archstudio.bna.constants.GridDisplayType;
import org.archstudio.bna.facets.IHasColor;
import org.archstudio.bna.things.AbstractThingPeer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class GridThingPeer<T extends GridThing> extends AbstractThingPeer<T> implements IThingPeer<T> {

	public GridThingPeer(T thing) {
		super(thing);
	}

	@Override
	public void draw(IBNAView view, ICoordinateMapper cm, GL2 gl, Rectangle clip, IResources r) {

		// only draw for the top level things
		if (view.getParentView() != null) {
			return;
		}

		int worldGridStep = t.getGridSpacing();
		if (worldGridStep == 0) {
			return;
		}

		GridDisplayType gdt = t.getGridDisplayType();
		if (gdt == null || gdt == GridDisplayType.NONE) {
			return;
		}

		if (r.setColor(t, IHasColor.COLOR_KEY)) {
			
			gl.glLineWidth(0.5f);

			while (worldGridStep * cm.getLocalScale() <= 8) {
				worldGridStep *= 2;
			}

			Rectangle lClip = new Rectangle(clip.x, clip.y, clip.width, clip.height);
			Rectangle wClip = cm.localToWorld(lClip);
			int wx = wClip.x;
			int wy = wClip.y;
			int wx2 = wClip.x + wClip.width;
			int wy2 = wClip.y + wClip.height;

			int dx = wx % worldGridStep;
			int dy = wy % worldGridStep;

			if (gdt == GridDisplayType.SOLID_LINES || gdt == GridDisplayType.DOTTED_LINES) {
				int dashLength = 1;
				if (gdt == GridDisplayType.DOTTED_LINES) {
					gl.glLineStipple(1, (short) 0xaaaa);
					dashLength = 6;
				}
				gl.glBegin(GL.GL_LINES);
				for (int i = wx - dx; i <= wx2; i += worldGridStep) {
					int gx = cm.worldToLocal(new Point(i, wy)).x;
					gl.glVertex2f(gx + 0.5f, lClip.y / dashLength * dashLength + 0.5f);
					gl.glVertex2f(gx + 0.5f, lClip.y + lClip.height + 2 + 0.5f);
				}
				for (int i = wy - dy; i <= wy2; i += worldGridStep) {
					int gy = cm.worldToLocal(new Point(wx, i)).y;
					gl.glVertex2f(lClip.x / dashLength * dashLength + 0.5f, gy + 0.5f);
					gl.glVertex2f(lClip.x + lClip.width + 2 + 0.5f, gy + 0.5f);
				}
				gl.glEnd();
			}
			else if (gdt == GridDisplayType.DOTS_AT_CORNERS) {
				gl.glBegin(GL.GL_POINTS);
				for (int i = wx - dx; i <= wx2; i += worldGridStep) {
					int gx = cm.worldToLocal(new Point(i, wy)).x;
					for (int j = wy - dy; j <= wy2; j += worldGridStep) {
						int gy = cm.worldToLocal(new Point(wx, j)).y;
						gl.glVertex2f(gx + 0.5f, gy + 0.5f);
					}
				}
				gl.glEnd();
			}
			else if (gdt == GridDisplayType.CROSSES_AT_CORNERS) {
				gl.glBegin(GL.GL_LINES);
				for (int i = wx - dx; i <= wx2; i += worldGridStep) {
					int gx = cm.worldToLocal(new Point(i, wy)).x;
					for (int j = wy - dy; j <= wy2; j += worldGridStep) {
						int gy = cm.worldToLocal(new Point(wx, j)).y;
						gl.glVertex2f(gx - 3 + 0.5f, gy + 0.5f);
						gl.glVertex2f(gx + 3 + 0.5f, gy + 0.5f);
						gl.glVertex2f(gx + 0.5f, gy - 3 + 0.5f);
						gl.glVertex2f(gx + 0.5f, gy + 3 + 0.5f);
					}
				}
				gl.glEnd();
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, ICoordinateMapper cm, ICoordinate location) {
		return false;
	}
}
