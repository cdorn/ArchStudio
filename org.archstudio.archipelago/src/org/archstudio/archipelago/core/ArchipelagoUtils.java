package org.archstudio.archipelago.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.archstudio.bna.AbstractCoordinateMapper;
import org.archstudio.bna.BNACanvas;
import org.archstudio.bna.IBNAModel;
import org.archstudio.bna.IBNAView;
import org.archstudio.bna.IBNAWorld;
import org.archstudio.bna.IThing;
import org.archstudio.bna.IThing.IThingKey;
import org.archstudio.bna.constants.GridDisplayType;
import org.archstudio.bna.facets.IHasAnchorPoint;
import org.archstudio.bna.facets.IHasBoundingBox;
import org.archstudio.bna.keys.ThingKey;
import org.archstudio.bna.logics.background.LifeSapperLogic;
import org.archstudio.bna.things.borders.PulsingBorderThing;
import org.archstudio.bna.things.labels.UserNotificationThing;
import org.archstudio.bna.things.utility.EnvironmentPropertiesThing;
import org.archstudio.bna.things.utility.NoThing;
import org.archstudio.bna.utils.BNAUtils;
import org.archstudio.bna.utils.ZoomUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ArchipelagoUtils {
	public static final IThingKey<String> XARCH_ID_KEY = ThingKey.create("xArchID");

	public static List<Object> combine(List<? extends Object> list1, Object o) {
		List<Object> newList = new ArrayList<Object>(list1.size() + 1);
		newList.addAll(list1);
		newList.add(o);
		return newList;
	}

	public static List<Object> combine(List<? extends Object> list1, List<? extends Object> list2) {
		List<Object> newList = new ArrayList<Object>(list1.size() + list2.size());
		newList.addAll(list2);
		return newList;
	}

	public static IThing findThing(IBNAModel m, String xArchID) {
		for (IThing t : m.getAllThings()) {
			if (!(t instanceof EnvironmentPropertiesThing)) {
				String id = t.get(XARCH_ID_KEY);
				if (id != null && id.equals(xArchID)) {
					return t;
				}
			}
		}
		return null;
	}

	public static IThing[] findAllThings(IBNAModel m, String xArchID) {
		List<IThing> results = new ArrayList<IThing>();
		for (IThing t : m.getAllThings()) {
			String id = t.get(XARCH_ID_KEY);
			if (id != null && id.equals(xArchID)) {
				results.add(t);
			}
		}
		return results.toArray(new IThing[results.size()]);
	}

	public static final String BNA_BOTTOM_ROOT_THING_ID = "$BOTTOM";
	public static final String BNA_NORMAL_ROOT_THING_ID = "$NORMAL";
	public static final String BNA_TOP_ROOT_THING_ID = "$TOP";

	protected static void initBNAModel(IBNAModel m) {
		if (m.getThing(BNA_BOTTOM_ROOT_THING_ID) == null) {
			IThing bottomRootThing = new NoThing(BNA_BOTTOM_ROOT_THING_ID);
			m.addThing(bottomRootThing);
		}

		if (m.getThing(BNA_NORMAL_ROOT_THING_ID) == null) {
			IThing normalRootThing = new NoThing(BNA_NORMAL_ROOT_THING_ID);
			m.addThing(normalRootThing);
		}

		if (m.getThing(BNA_TOP_ROOT_THING_ID) == null) {
			IThing topRootThing = new NoThing(BNA_TOP_ROOT_THING_ID);
			m.addThing(topRootThing);
		}
	}

	protected static IThing getRootThing(IBNAModel m, String id) {
		IThing rootThing = m.getThing(id);
		if (rootThing == null) {
			initBNAModel(m);
			return m.getThing(id);
		}
		return rootThing;
	}

	public static IThing getNormalRootThing(IBNAModel m) {
		return getRootThing(m, BNA_NORMAL_ROOT_THING_ID);
	}

	public static IThing getBottomRootThing(IBNAModel m) {
		return getRootThing(m, BNA_BOTTOM_ROOT_THING_ID);
	}

	public static IThing getTopRootThing(IBNAModel m) {
		return getRootThing(m, BNA_TOP_ROOT_THING_ID);
	}

	public static void setNewThingSpot(IBNAModel m, int worldX, int worldY) {
		EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(m);
		ept.setNewThingSpot(new Point(worldX, worldY));
	}

	public static Point findOpenSpotForNewThing(IBNAModel m) {
		EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(m);
		try {
			if (ept.has(EnvironmentPropertiesThing.NEW_THING_SPOT_KEY)) {
				return ept.getNewThingSpot();
			}
		}
		catch (Exception e) {
		}

		Point p;
		if (ept.has(EnvironmentPropertiesThing.LAST_OPEN_SPOT_KEY)) {
			p = ept.getLastOpenSpot();
			p.x += 10;
			p.y += 10;
		}
		else {
			Rectangle bounds = AbstractCoordinateMapper.getDefaultBounds();
			p = new Point(bounds.x + bounds.width / 2 + 30, bounds.y + bounds.height / 2 + 30);
			// first find all things along the points of interest
			int dyx = p.y - p.x;
			Set<Integer> xValues = new HashSet<Integer>();
			for (IThing t : m.getAllThings()) {
				if (t instanceof IHasBoundingBox) {
					Rectangle r = ((IHasBoundingBox) t).getBoundingBox();
					if (r.y - r.x == dyx) {
						xValues.add(r.x);
					}
				}
				if (t instanceof IHasAnchorPoint) {
					Point ap = ((IHasAnchorPoint) t).getAnchorPoint();
					if (ap.y - ap.x == dyx) {
						xValues.add(ap.x);
					}
				}
			}
			// now find an open point
			while (xValues.contains(p.x)) {
				p.x += 10;
				p.y += 10;
			}
		}

		ept.setLastOpenSpot(p);
		return p;
	}

	public static final String EDITOR_PANE_PROPERTY_BNA_COMPOSITE = "bnaCanvas";

	public static void setBNACanvas(IArchipelagoEditorPane editorPane, Canvas bnaCanvas) {
		editorPane.setProperty(EDITOR_PANE_PROPERTY_BNA_COMPOSITE, bnaCanvas);
	}

	public static BNACanvas getBNACanvas(IArchipelagoEditorPane editorPane) {
		return (BNACanvas) editorPane.getProperty(EDITOR_PANE_PROPERTY_BNA_COMPOSITE);
	}

	//public static void sendEventToInnerViews(XArchADTModelEvent evt, IBNAWorld world,
	//		TypedThingSetTrackingLogic<IHasWorld> ttstlView) {
	//	//Ship the event to subviews
	//	IHasWorld[] worldThings = ttstlView.getThings();
	//	if (worldThings != null) {
	//		for (IHasWorld vt : worldThings) {
	//			IBNAWorld innerWorld = vt.getWorld();
	//			if (innerWorld != null) {
	//				IThingLogicManager innerThingLogicManager = innerWorld.getThingLogicManager();
	//				if (innerThingLogicManager != null) {
	//					for (IThingLogic innerThingLogic : innerThingLogicManager.getAllThingLogics()) {
	//						if (innerThingLogic instanceof IXArchEventHandlerLogic) {
	//							((IXArchEventHandlerLogic) innerThingLogic).handleXArchADTModelEvent(evt, innerWorld);
	//						}
	//					}
	//				}
	//			}
	//		}
	//	}
	//}

	public static void showUserNotification(IBNAWorld w, String text, int worldX, int worldY) {
		w.getThingLogicManager().addThingLogic(LifeSapperLogic.class);

		UserNotificationThing unt = w.getBNAModel().addThing(new UserNotificationThing(null));
		unt.setText(text);
		unt.setAnchorPoint(new Point(worldX, worldY));
	}

	public static void beginTreeCellEditing(TreeViewer viewer, Object allowEditing) {
		viewer.setData("allowCellEditing", allowEditing);
		//System.err.println("allowing cell editing on: " + allowEditing);
		viewer.editElement(allowEditing, 0);
		viewer.setData("allowCellEditing", null);
	}

	public static String getClassName(Class<?> c) {
		if (c.isArray()) {
			Class<?> cc = c.getComponentType();
			return getClassName(cc) + "[]";
		}
		return c.getName();
	}

	public static void applyGridPreferences(IPreferenceStore prefs, IBNAModel bnaModel) {
		int gridSpacing = prefs.getInt(ArchipelagoConstants.PREF_GRID_SPACING);
		BNAUtils.setGridSpacing(bnaModel, gridSpacing);

		String gridDisplayTypeString = prefs.getString(ArchipelagoConstants.PREF_GRID_DISPLAY_TYPE);
		if (gridDisplayTypeString == null || gridDisplayTypeString.length() == 0) {
			gridDisplayTypeString = GridDisplayType.NONE.name();
		}
		try {
			GridDisplayType gdt = GridDisplayType.valueOf(gridDisplayTypeString);
			if (gdt != null) {
				BNAUtils.setGridDisplayType(bnaModel, gdt);
			}
		}
		catch (Exception e) {
			BNAUtils.setGridDisplayType(bnaModel, GridDisplayType.NONE);
		}
	}

	public static void pulseNotify(final IBNAModel m, final IThing t) {
		if (t != null && t instanceof IHasBoundingBox) {
			final Rectangle bb = ((IHasBoundingBox) t).getBoundingBox();
			if (bb != null) {
				final PulsingBorderThing pbt = new PulsingBorderThing(null);
				pbt.setBoundingBox(bb);
				m.addThing(pbt);
				Thread pulserThread = new Thread() {

					@Override
					public void run() {
						try {
							Thread.sleep(6000);
						}
						catch (InterruptedException ie) {
						}
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								m.removeThing(pbt);
							}
						});
					}
				};
				pulserThread.start();
			}
		}
	}

	//	public static IThing getGlassThing(IBNAModel m, IThing rootThing) {
	//		if (rootThing != null && rootThing instanceof IHasAssemblyData) {
	//			IHasAssemblyData assemblyRootThing = (IHasAssemblyData) rootThing;
	//			String assemblyKind = assemblyRootThing.getAssemblyKind();
	//			if (assemblyKind != null) {
	//				if (assemblyKind.equals(BoxAssembly.ASSEMBLY_KIND)) {
	//					BoxAssembly a = BoxAssembly.attach(m, assemblyRootThing);
	//					return a.getBoxGlassThing();
	//				}
	//				else if (assemblyKind.equals(StickySplineAssembly.ASSEMBLY_KIND)) {
	//					StickySplineAssembly a = StickySplineAssembly.attach(m, assemblyRootThing);
	//					return a.getSplineGlassThing();
	//				}
	//				else if (assemblyKind.equals(SplineAssembly.ASSEMBLY_KIND)) {
	//					SplineAssembly a = SplineAssembly.attach(m, assemblyRootThing);
	//					return a.getSplineGlassThing();
	//				}
	//				else if (assemblyKind.equals(EndpointAssembly.ASSEMBLY_KIND)) {
	//					EndpointAssembly a = EndpointAssembly.attach(m, assemblyRootThing);
	//					return a.getEndpointGlassThing();
	//				}
	//			}
	//		}
	//		return null;
	//	}

	public static IStructuredSelection addToSelection(ISelection os, Object[] thingsToAdd) {
		Set<Object> newSelections = new HashSet<Object>();
		if (os != null) {
			if (os instanceof IStructuredSelection) {
				for (Object element : ((IStructuredSelection) os).toList()) {
					newSelections.add(element);
				}
			}
		}
		for (Object o : thingsToAdd) {
			newSelections.add(o);
		}
		return new StructuredSelection(newSelections.toArray());
	}

	public static void addZoomWidget(final BNACanvas bnaCanvas, final IBNAView bnaView) {
		Listener l = new Listener() {

			@Override
			public void handleEvent(Event e) {
				bnaCanvas.forceFocus();
			}
		};

		bnaCanvas.addListener(SWT.Activate, l);
		bnaCanvas.addListener(SWT.MouseDown, l);

		final Control zoomWidget = ZoomUtils.createZoomWidget(bnaCanvas, bnaCanvas, bnaView.getCoordinateMapper());
		zoomWidget.setSize(zoomWidget.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		zoomWidget.setLocation(bnaCanvas.getClientArea().x + bnaCanvas.getClientArea().width - zoomWidget.getSize().x
				- 1, 1);

		bnaCanvas.addControlListener(new ControlListener() {

			@Override
			public void controlMoved(ControlEvent e) {
			}

			@Override
			public void controlResized(ControlEvent e) {
				zoomWidget.setLocation(
						bnaCanvas.getClientArea().x + bnaCanvas.getClientArea().width - zoomWidget.getSize().x - 1, 1);
			}
		});

		/*
		 * final IToolBarManager tb = AS.editor.getActionBars().getToolBarManager();
		 * 
		 * final ControlContribution cc = new ControlContribution("ZOOM"){ protected Control createControl(Canvas
		 * parent){ final Control zoomWidget = ZoomUtils.createZoomWidget(parent, bnaCanvas,
		 * bnaView.getCoordinateMapper()); return zoomWidget; } }; tb.add(cc); tb.markDirty(); tb.update(true);
		 * 
		 * bnaCanvas.addDisposeListener(new DisposeListener(){ public void widgetDisposed(DisposeEvent e){
		 * tb.remove(cc); tb.update(true); } });
		 */
	}
}
