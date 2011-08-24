package org.archstudio.bna;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.archstudio.bna.BNAModelEvent.EventType;
import org.archstudio.bna.IThing.IThingKey;
import org.archstudio.bna.utils.BNARenderingSettings;
import org.archstudio.bna.utils.BNAUtils;
import org.archstudio.bna.utils.DefaultBNAView;
import org.archstudio.swtutils.SWTWidgetUtils;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

public class BNACanvas extends Canvas implements IBNAModelListener, PaintListener {

	protected static int DEBUG = 0;
	private static final String STARTED_RENDERING_EVENT = "Started rendering " + BNACanvas.class;

	protected final IBNAView bnaView;
	protected final ScrollBar hBar = getHorizontalBar();
	protected final ScrollBar vBar = getVerticalBar();
	protected final BNASWTEventHandler eventHandler;
	protected final IResources resources;

	public BNACanvas(Composite parent, int style, IBNAWorld bnaWorld) {
		super(parent, style | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED);
		checkNotNull(bnaWorld);
		checkNotNull(bnaWorld.getBNAModel());
		
		this.bnaView = new DefaultBNAView(this, null, bnaWorld, new LinearCoordinateMapper());
		this.eventHandler = new BNASWTEventHandler(this, bnaView);
		this.resources = new Resources(this);

		this.renderMCM = new LinearCoordinateMapper();
		renderMCM.setTranslate(false);

		final IMutableCoordinateMapper mcm = getCoordinateMapper();
		
		this.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				doUpdateRender(mcm.getLocalScale(), mcm.getLocalOrigin(new Point()));
				doUpdateBars();
			}
		});

		// coordinate mapping
		hBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isScrollBarUpdating) {
					mcm.synchronizedUpdate(new Runnable() {
						@Override
						public void run() {
							Rectangle localBounds = mcm.getLocalBounds(new Rectangle());
							Point localOrigin = mcm.getLocalOrigin(new Point());
							mcm.setLocalOrigin(new Point(hBar.getSelection() + localBounds.x, localOrigin.y));
						}
					});
				}
			}
		});
		vBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isScrollBarUpdating) {
					mcm.synchronizedUpdate(new Runnable() {
						@Override
						public void run() {
							Rectangle localBounds = mcm.getLocalBounds(new Rectangle());
							Point localOrigin = mcm.getLocalOrigin(new Point());
							mcm.setLocalOrigin(new Point(localOrigin.x, vBar.getSelection() + localBounds.y));
						}
					});
				}
			}
		});
		getCoordinateMapper().addCoordinateMapperListener(new ICoordinateMapperListener() {
			@Override
			public void coordinateMappingsChanged(final CoordinateMapperEvent evt) {
				SWTWidgetUtils.sync(BNACanvas.this, new Runnable() {
					@Override
					public void run() {
						doUpdateBars();
						doUpdateRender(evt.getNewLocalScale(), evt.getNewLocalOrigin());
					}
				});
			}
		});
		
		doUpdateRender(mcm.getLocalScale(), mcm.getLocalOrigin(new Point()));
		doUpdateBars();

		// prevent the mouse wheel from scrolling the canvas
		addListener(SWT.MouseWheel, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// prevent the mouse wheel from scrolling the canvas
				event.doit = false;
			}
		});

		// select canvas on click
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				BNACanvas.this.forceFocus();
			}
		});

		// handle events
		getBNAView().getBNAWorld().getBNAModel().addBNAModelListener(this);
		addPaintListener(this);
	}

	@Override
	public void dispose() {
		getBNAView().getBNAWorld().getBNAModel().removeBNAModelListener(this);
		eventHandler.dispose();
		resources.dispose();
		super.dispose();
	}

	//protected final IMutableCoordinateMapper mcm;
	/*
	 * the renderMCM ICoordinateMapper is special in two ways: First, it is only
	 * updated in the SWT thread so that painting is consistent with scrolling.
	 * If this is not done, rapidly sliding a scroll bar back and forth causes
	 * artifacts on the display. Second, it does not translate coordinates so
	 * that the translation may be performed within the graphics object during
	 * rendering. If this is not done, the local cache values for each thing
	 * become inconsistent with the rendered things once the canvas is scrolled.
	 */
	/*
	 * Coordinate mapper relationships: mcm <-> scrollbars -> renderMCM
	 */
	protected final IScrollableCoordinateMapper renderMCM;
	boolean isScrollBarUpdating = false;
	int lastBoundsRelevantCount = 0;

	protected void doUpdateRender(double localScale, Point localOrigin) {
		ICoordinateMapper mcm = getCoordinateMapper();
		final double newLocalScale = mcm.getLocalScale();
		final Point newLocalOrigin = mcm.getLocalOrigin(new Point());

		if (Display.getCurrent() == null) {
			SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}

		if (renderMCM.getLocalScale() != newLocalScale) {
			// the scale changed, force a redraw of everything
			renderMCM.setLocalScale(newLocalScale);
			renderMCM.setLocalOrigin(newLocalOrigin);
			lastBoundsRelevantCount++;
			redraw();
			return;
		}
		Point renderLocalOrigin = renderMCM.getLocalOrigin(new Point());
		if (!renderLocalOrigin.equals(newLocalOrigin)) {
			// scroll the screen to align with the desired offset
			int dx = newLocalOrigin.x - renderLocalOrigin.x;
			int dy = newLocalOrigin.y - renderLocalOrigin.y;
			org.eclipse.swt.graphics.Rectangle client = getClientArea();
			/*
			 * scroll before updating the renderMCM, as the old painting events
			 * (with the old renderMCM settings) will be flushed... then the new
			 * renderMCM settings should be put into effect
			 */
			scroll(-dx, -dy, 0, 0, client.width, client.height, true);
			renderMCM.setLocalOrigin(newLocalOrigin);
			// scroll automatically causes a redraw of the newly revealed area
			return;
		}
	}

	protected void doUpdateBars() {
		final IMutableCoordinateMapper mcm = getCoordinateMapper();

		org.eclipse.swt.graphics.Rectangle client = getClientArea();
		Rectangle localBounds = mcm.getLocalBounds(new Rectangle());
		Point localOrigin = mcm.getLocalOrigin(new Point());
		doUpdateBar(hBar, localOrigin.x - localBounds.x, client.width, localBounds.width);
		doUpdateBar(vBar, localOrigin.y - localBounds.y, client.height, localBounds.height);
	}

	private void doUpdateBar(ScrollBar bar, int selection, int thumb, int total) {
		isScrollBarUpdating = true;
		try {
			// ScrollBar silently fails when certain constraints are violated
			thumb = Math.min(thumb, total);
			bar.setValues(Math.max(0, selection), 0, Math.max(1, total - thumb), Math.max(1, thumb),
					Math.max(1, thumb / 10), Math.max(1, thumb / 3));
		}
		finally {
			isScrollBarUpdating = false;
		}
	}

	// ignore the flurry of events that occur before rendering the BNA World for the first time
	boolean startedRendering = false;
	boolean ignoreModelEvents = true;

	@Override
	public void paintControl(PaintEvent e) {
		IBNAModel bnaModel = getBNAView().getBNAWorld().getBNAModel();

		if (!startedRendering) {
			startedRendering = true;
			bnaModel.fireStreamNotificationEvent(STARTED_RENDERING_EVENT);
			redraw();
			return;
		}

		Graphics g = null;
		Region localClipRegion = null;
		IResources resources = null;
		try {
			g = new SWTGraphics(e.gc);
			resources = new Resources(this, e.gc.getDevice());
			g.setAdvanced(true);
			g.setAntialias(BNARenderingSettings.getAntialiasGraphics(this) ? SWT.ON : SWT.OFF);
			g.setTextAntialias(BNARenderingSettings.getAntialiasText(this) ? SWT.ON : SWT.OFF);

			localClipRegion = new Region(e.display);
			e.gc.getClipping(localClipRegion);

			Point renderLocalOrigin = renderMCM.getLocalOrigin(new Point());
			g.translate(-renderLocalOrigin.x, -renderLocalOrigin.y);
			localClipRegion.translate(renderLocalOrigin.x, renderLocalOrigin.y);

			if (DEBUG >= 2) {
				int debugColor = new Random().nextInt(256);
				e.gc.setBackground(e.display.getSystemColor(debugColor & 0x0F));
				e.gc.fillRectangle(e.x, e.y, e.width, e.height);
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
				e.gc.drawRectangle(e.x, e.y, e.width - 1, e.height - 1);
			}

			// update thing caches
			List<IThing> thingsToRender = Lists.newArrayListWithExpectedSize(bnaModel.getNumThings());
			for (IThing thing : bnaModel.getThings()) {
				IThingPeer<?> peer = getBNAView().getThingPeer(thing);
				RenderData renderData = getRenderData(thing);
				
				if (renderData.lastBoundsRelevantCount != lastBoundsRelevantCount) {
					renderData.lastLocalBounds.setSize(0, 0);
					peer.getLocalBounds(getBNAView(), renderMCM, resources, renderData.lastLocalBounds);
					renderData.lastBoundsRelevantCount = lastBoundsRelevantCount;
					renderData.needsCacheUpdate = true;
				}

				if (!renderData.lastLocalBounds.isEmpty()) {
					if (renderData.needsCacheUpdate) {
						renderData.needsCacheUpdate = false;
						peer.updateCache(getBNAView(), renderMCM);
					}

					Rectangle r = renderData.lastLocalBounds;
					if (localClipRegion.intersects(r.x, r.y, r.width, r.height)) {
						thingsToRender.add(thing);
					}
				}
			}

			// render things
			g.pushState();
			try {
				IThingPeer<?> lastThingPeer = null;
				for (IThing thingToRender : thingsToRender) {
					IThingPeer<?> peer = getBNAView().getThingPeer(thingToRender);
					RenderData renderData = getRenderData(thingToRender);

					try {
						if (DEBUG >= 1) {
							g.setBackgroundColor(resources.getColor(SWT.COLOR_RED));
							g.setAlpha(10);
							g.fillRectangle(renderData.lastLocalBounds);
							g.restoreState();
						}
						g.clipRect(renderData.lastLocalBounds);
						peer.draw(getBNAView(), renderMCM, g, resources);
						g.restoreState();
					}
					catch (Exception e2) {
						System.err.println(lastThingPeer != null ? lastThingPeer.getClass() : "null");
						System.err.println(peer.getClass());
						e2.printStackTrace();
					}
					lastThingPeer = peer;
				}
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			finally {
				g.popState();
			}
		}
		finally {
			localClipRegion = SWTWidgetUtils.quietlyDispose(localClipRegion);
			if (resources != null) {
				resources.dispose();
			}
			if (g != null) {
				g.dispose();
			}
		}
	}

	// collect events into a queue and process them in bulk within the paint thread
	@SuppressWarnings("rawtypes")
	private final Queue<BNAModelEvent> eventQueue = new LinkedList<BNAModelEvent>();
	private boolean eventQueueBeingProcessed = false;

	@Override
	public <ET extends IThing, EK extends IThingKey<EV>, EV> void bnaModelChanged(final BNAModelEvent<ET, EK, EV> evt) {
		if (ignoreModelEvents) {
			if (evt.getEventType() == EventType.STREAM_NOTIFICATION_EVENT
					&& STARTED_RENDERING_EVENT.equals(evt.getStreamNotification())) {
				ignoreModelEvents = false;
			}
			return;
		}
		synchronized (eventQueue) {
			eventQueue.add(evt);
			if (!eventQueueBeingProcessed && !evt.inBulkChange) {
				eventQueueBeingProcessed = true;
				SWTWidgetUtils.async(this, new Runnable() {

					Rectangle allRedrawRect = new Rectangle();

					@Override
					public void run() {
						List<IThing> removedThingsToDestroy = Lists.newArrayListWithExpectedSize(eventQueue.size());
						Set<IThing> thingsToProcess = Sets.newHashSetWithExpectedSize(eventQueue.size());
						while (true) {
							BNAModelEvent<?, ?, ?> evt;
							synchronized (eventQueue) {
								evt = eventQueue.poll();
								if (evt == null) {
									eventQueueBeingProcessed = false;
									break;
								}
							}
							final IThing t = evt.getTargetThing();
							if (t != null) {
								RenderData cacheData = getRenderData(t);
								switch (evt.getEventType()) {
								case THING_REMOVED:
									removedThingsToDestroy.add(t);
									cacheData.needsLastRenderCleanup = true;
									break;
								case THING_ADDED:
									cacheData.needsRenderUpdate = true;
									break;
								case THING_CHANGED:
								case THING_RESTACKED:
									cacheData.needsLastRenderCleanup = true;
									cacheData.needsRenderUpdate = true;
									cacheData.needsCacheUpdate = true;
									break;
								}
								thingsToProcess.add(t);
							}
						}
						for (IThing thingToProcess : thingsToProcess) {
							processRedrawRect(thingToProcess);
						}
						for (IThing removedThingToDestroy : removedThingsToDestroy) {
							getBNAView().disposePeer(removedThingToDestroy);
						}
						if (!allRedrawRect.isEmpty()) {
							Point renderLocalOrigin = renderMCM.getLocalOrigin(new Point());
							allRedrawRect.translate(-renderLocalOrigin.x, -renderLocalOrigin.y);
							redraw(allRedrawRect.x, allRedrawRect.y, allRedrawRect.width, allRedrawRect.height, false);
						}
					}

					private void processRedrawRect(IThing t) {
						IThingPeer<?> peer = getBNAView().getThingPeer(t);
						RenderData cacheData = getRenderData(t);
						if (cacheData.needsLastRenderCleanup) {
							if (cacheData.lastBoundsRelevantCount == lastBoundsRelevantCount) {
								BNAUtils.union(allRedrawRect, cacheData.lastLocalBounds);
							}
							cacheData.needsLastRenderCleanup = false;
						}
						if (cacheData.needsRenderUpdate) {
							peer.getLocalBounds(getBNAView(), renderMCM, resources, cacheData.lastLocalBounds);
							BNAUtils.union(allRedrawRect, cacheData.lastLocalBounds);
							cacheData.needsRenderUpdate = false;
						}
					}
				});
			}
		}
	}

	public IBNAView getBNAView() {
		return bnaView;
	}
	
	private IMutableCoordinateMapper getCoordinateMapper() {
		return (IMutableCoordinateMapper)getBNAView().getCoordinateMapper();
	}
	
	private static class RenderData implements IThingPeerData {
		protected Rectangle lastLocalBounds = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
		protected int lastBoundsRelevantCount = -1;
		protected int lastThingModCount = -1;
		protected boolean needsLastRenderCleanup = false;
		protected boolean needsRenderUpdate = true;
		protected boolean needsCacheUpdate = true;
	}
	
	private static Map<IThing,RenderData> autoRenderData = new MapMaker().weakKeys().makeComputingMap(
			new Function<IThing,RenderData>() {
				@Override
				public RenderData apply(IThing input) {
					return new RenderData();
				}
			});
	
	private <T extends IThing> RenderData getRenderData(T thing) {
		return autoRenderData.get(thing);
	}

}