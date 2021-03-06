package org.archstudio.xadl.bna.logics;

import org.archstudio.bna.BNAModelEvent;
import org.archstudio.bna.IBNAModel;
import org.archstudio.bna.IBNAModelListener;
import org.archstudio.bna.IThing;
import org.archstudio.bna.IThing.IThingKey;
import org.archstudio.bna.ThingEvent;
import org.archstudio.bna.facets.IHasAlpha;
import org.archstudio.bna.facets.IHasBoundingBox;
import org.archstudio.bna.facets.IHasColor;
import org.archstudio.bna.facets.IHasEndpoints;
import org.archstudio.bna.facets.IHasMidpoints;
import org.archstudio.bna.facets.IHasPoints;
import org.archstudio.bna.facets.IHasSelected;
import org.archstudio.bna.facets.IIsBackground;
import org.archstudio.bna.keys.IThingRefKey;
import org.archstudio.bna.keys.ThingKey;
import org.archstudio.bna.keys.ThingRefKey;
import org.archstudio.bna.logics.AbstractThingLogic;
import org.archstudio.bna.logics.coordinating.MirrorValueLogic;
import org.archstudio.bna.logics.tracking.ThingValueTrackingLogic;
import org.archstudio.bna.things.borders.RectangleGlowThing;
import org.archstudio.bna.things.borders.SplineGlowThing;
import org.archstudio.bna.utils.Assemblies;
import org.archstudio.swtutils.SWTWidgetUtils;
import org.archstudio.xadl.bna.facets.IHasObjRef;
import org.archstudio.xarchadt.ObjRef;
import org.archstudio.xarchadt.variability.IXArchADTVariability;
import org.archstudio.xarchadt.variability.IXArchADTVariability.ChangeStatus;
import org.archstudio.xarchadt.variability.IXArchADTVariabilityListener;
import org.archstudio.xarchadt.variability.XArchADTVariabilityEvent;
import org.archstudio.xarchadt.variability.XArchADTVariabilityEvent.Type;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Sets;

@NonNullByDefault
public class DecorateChangesLogic extends AbstractThingLogic implements IXArchADTVariabilityListener, IBNAModelListener {

	private static final IThingKey<ChangeStatus> CHANGE_STATUS_KEY = ThingKey.create("ChangeStatus");
	private static final IThingRefKey<IThing> CHANGE_DECORATION_KEY = ThingRefKey.create("ChangeStatusDecoration");

	protected final IXArchADTVariability xarch;
	protected ThingValueTrackingLogic tvtl;
	protected MirrorValueLogic mvl;

	public DecorateChangesLogic(IXArchADTVariability xarch) {
		this.xarch = xarch;
	}

	@Override
	protected void init() {
		super.init();
		tvtl = addThingLogic(ThingValueTrackingLogic.class);
		mvl = addThingLogic(MirrorValueLogic.class);
	}

	@Override
	public void handleXArchADTVariabilityEvent(final XArchADTVariabilityEvent evt) {
		if (evt.getType() == Type.STATUS) {
			final IBNAModel model = getBNAModel();
			if (model != null) {
				SWTWidgetUtils.async(Display.getDefault(), new Runnable() {
					@Override
					public void run() {
						for (IThing t : model.getThingsByID(tvtl.getThingIDs(IHasObjRef.OBJREF_KEY,
								evt.getChangedObjRef()))) {
							updateDecoration(t, evt.getChangeStatus());
						}
					}
				});
			}
		}
	}

	@Override
	public void bnaModelChanged(BNAModelEvent evt) {
		switch (evt.getEventType()) {
		case THING_ADDED: {
			IThing t = evt.getTargetThing();
			if (t != null) {
				ObjRef objRef = t.get(IHasObjRef.OBJREF_KEY);
				if (objRef != null) {
					updateDecoration(t, xarch.getChangeStatus(objRef));
				}
			}
		}
		case THING_REMOVED: {
			IThing t = evt.getTargetThing();
			if (t != null) {
				removeDecoration(t);
			}
		}
		case THING_CHANGED: {
			ThingEvent te = evt.getThingEvent();
			if (te != null) {
				if (IHasObjRef.OBJREF_KEY.equals(te.getPropertyName())) {
					ObjRef objRef = (ObjRef) te.getNewPropertyValue();
					if (objRef != null) {
						updateDecoration(te.getTargetThing(), xarch.getChangeStatus(objRef));
					}
					else {
						removeDecoration(te.getTargetThing());
					}
				}
			}
		}
		default: // do nothing
		}
	}

	protected void updateDecoration(IThing t, ChangeStatus changeStatus) {
		switch (changeStatus) {
		case ATTACHED:
			removeDecoration(t);
			break;
		case EXPLICITLY_ADDED:
			updateDecoration(t, new RGB(0, 255, 255), 1f, true);
			break;
		case EXPLICITLY_ADDED_BUT_REALLY_REMOVED:
			updateDecoration(t, new RGB(0, 255, 255), 0.5f, false);
			break;
		case EXPLICITLY_MODIFIED:
			updateDecoration(t, new RGB(255, 0, 255), 1f, true);
			break;
		case EXPLICITLY_MODIFIED_BUT_REALLY_REMOVED:
			updateDecoration(t, new RGB(255, 0, 255), 0.5f, false);
			break;
		case EXPLICITLY_REMOVED:
			updateDecoration(t, new RGB(255, 0, 0), 0.5f, false);
			break;
		case EXPLICITLY_REMOVED_BUT_REALLY_ADDED:
			updateDecoration(t, new RGB(255, 0, 0), 1f, true);
			break;
		case OVERVIEW:
			updateDecoration(t, null, 0.5f, false);
			break;
		default:
			// do nothing
		}
	}

	private void updateDecoration(IThing t, RGB rgb, float alpha, boolean editable) {
		removeDecoration(t);
		updateAttributes(t, alpha, editable);
		IBNAModel model = getBNAModel();
		if (model != null) {
			IThing decoration = null;
			if (t instanceof IHasPoints) {
				decoration = model.addThing(new SplineGlowThing(null), t);
				mvl.mirrorValue(t, IHasEndpoints.ENDPOINT_1_KEY, decoration);
				mvl.mirrorValue(t, IHasEndpoints.ENDPOINT_2_KEY, decoration);
				mvl.mirrorValue(t, IHasMidpoints.MIDPOINTS_KEY, decoration);
			}
			else if (t instanceof IHasBoundingBox) {
				decoration = model.addThing(new RectangleGlowThing(null), t);
				mvl.mirrorValue(t, IHasBoundingBox.BOUNDING_BOX_KEY, decoration);
			}
			if (decoration != null) {
				model.sendToBack(Sets.newHashSet(decoration));
				decoration.set(IHasColor.COLOR_KEY, rgb);
				CHANGE_DECORATION_KEY.set(t, decoration);
			}
		}
	}

	protected void removeDecoration(IThing t) {
		IBNAModel model = getBNAModel();
		if (model != null) {
			IThing d = CHANGE_DECORATION_KEY.get(t, model);
			if (d != null) {
				model.removeThingAndChildren(d);
			}
			t.remove(CHANGE_STATUS_KEY);
			t.remove(CHANGE_DECORATION_KEY);
		}
		updateAttributes(t, 1f, true);
	}

	private void updateAttributes(IThing t, float alpha, boolean editable) {
		t.set(IHasAlpha.ALPHA_KEY, alpha);
		t.set(IIsBackground.BACKGROUND_KEY, !editable);
		if (!editable && Boolean.TRUE.equals(t.get(IHasSelected.SELECTED_KEY))) {
			t.set(IHasSelected.SELECTED_KEY, false);
		}
		for (IThing p : Assemblies.getParts(getBNAModel(), t)) {
			if (!p.has(IHasObjRef.OBJREF_KEY) || p.has(IHasObjRef.OBJREF_KEY, t.get(IHasObjRef.OBJREF_KEY))) {
				updateAttributes(p, alpha, editable);
			}
		}
	}
}
