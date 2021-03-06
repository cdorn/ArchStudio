package org.archstudio.bna.things.labels;

import java.awt.Font;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import org.archstudio.bna.IBNAView;
import org.archstudio.bna.ICoordinate;
import org.archstudio.bna.ICoordinateMapper;
import org.archstudio.bna.IResources;
import org.archstudio.bna.IThingPeer;
import org.archstudio.bna.facets.IHasColor;
import org.archstudio.bna.facets.IHasEdgeColor;
import org.archstudio.bna.facets.IHasSecondaryColor;
import org.archstudio.bna.things.AbstractAnchorPointThingPeer;
import org.archstudio.bna.utils.BNAUtils;
import org.archstudio.sysutils.SystemUtils;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.jogamp.opengl.util.awt.TextRenderer;

public class UserNotificationThingPeer<T extends UserNotificationThing> extends AbstractAnchorPointThingPeer<T>
		implements IThingPeer<T> {

	int SPACING = 4;

	public UserNotificationThingPeer(T thing) {
		super(thing);
	}

	protected static int calculateFontSize(ICoordinateMapper cm, int origFontSize, boolean dontIncreaseFontSize) {
		double scale = cm.getLocalScale();
		double nfs = origFontSize;
		if (dontIncreaseFontSize) {
			if (scale < 1.0d) {
				nfs = origFontSize * scale;
			}
		}
		else {
			nfs = origFontSize * scale;
		}
		return (int) nfs;
	}

	@Override
	public void draw(IBNAView view, ICoordinateMapper cm, GL2 gl, Rectangle clip, IResources r) {

		Point lap = cm.worldToLocal(t.getAnchorPoint());
		float alpha = SystemUtils.bound(0, (16 - Math.abs(t.getLife() - 16)) / 8f, 1);

		String text = t.getText();
		int fontSize = calculateFontSize(view.getCoordinateMapper(), t.getFontSize(), t.getDontIncreaseFontSize());
		Font font = r.getFont(t, fontSize);
		TextRenderer tr = r.getTextRenderer(font);

		Rectangle lbb = BNAUtils.toRectangle(tr.getBounds(text));
		lbb.x += lap.x;
		lbb.y += lap.y;
		lbb.x -= SPACING;
		lbb.y -= SPACING;
		lbb.width += 2 * SPACING;
		lbb.height += 2 * SPACING;
		Point p1 = new Point(lbb.x, lbb.y);
		Point p2 = new Point(lbb.x + lbb.width, lbb.y + lbb.height);

		if (r.setColor(t, IHasColor.COLOR_KEY, alpha)) {
			gl.glBegin(GL2.GL_QUADS);
			gl.glVertex2i(p1.x, p1.y);
			gl.glVertex2i(p2.x, p1.y);
			if (t.isGradientFilled()) {
				r.setColor(t, IHasSecondaryColor.SECONDARY_COLOR_KEY, alpha);
			}
			gl.glVertex2i(p2.x, p2.y);
			gl.glVertex2i(p1.x, p2.y);
			gl.glEnd();
		}

		if (r.setColor(t, IHasEdgeColor.EDGE_COLOR_KEY, alpha)) {
			gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex2f(p1.x + 0.5f, p1.y + 0.5f);
			gl.glVertex2f(p2.x - 0.5f, p1.y + 0.5f);
			gl.glVertex2f(p2.x - 0.5f, p2.y - 0.5f);
			gl.glVertex2f(p1.x + 0.5f, p2.y - 0.5f);
			gl.glEnd();

			Point canvasSize = view.getComposite().getSize();
			gl.glPushMatrix();
			tr.beginRendering(canvasSize.x, canvasSize.y);
			gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
			gl.glTranslated(lap.x, canvasSize.y - lap.y, 0);
			r.setColor(t, IHasEdgeColor.EDGE_COLOR_KEY, alpha, tr);
			tr.draw(text, 0, 0);
			tr.endRendering();
			gl.glPopMatrix();
		}
	}

	@Override
	public boolean isInThing(IBNAView view, ICoordinateMapper cm, ICoordinate location) {
		return false;
	}
}
