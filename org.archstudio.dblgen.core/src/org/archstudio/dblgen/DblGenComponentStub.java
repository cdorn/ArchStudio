package org.archstudio.dblgen;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import org.archstudio.myx.fw.*;
import org.archstudio.myx.fw.IMyxDynamicBrick;
import org.archstudio.dblgen.IDataBindingGenerator;
import org.archstudio.myx.fw.IMyxLifecycleProcessor;
import org.archstudio.myx.fw.IMyxBrickItems;
import org.archstudio.myx.fw.IMyxProvidedServiceProvider;
import org.archstudio.myx.fw.IMyxName;
import org.archstudio.myx.fw.MyxRegistry;
import org.archstudio.myx.fw.MyxUtils;

/**
 * Abstract Myx brick: "xADL Data Binding Library (DBL) Generator Impl"
 * 
 * @generated
 */
@SuppressWarnings("unused")
abstract class DblGenComponentStub extends org.archstudio.myx.fw.AbstractMyxSimpleBrick implements
		org.archstudio.myx.fw.IMyxDynamicBrick, org.archstudio.myx.fw.IMyxLifecycleProcessor,
		org.archstudio.myx.fw.IMyxProvidedServiceProvider {

	/**
	 * @generated
	 */
	protected final MyxRegistry myxRegistry = MyxRegistry.getSharedInstance();

	/**
	 * @generated
	 */
	@Override
	public void begin() {
		super.begin();
		myxRegistry.register(this);
	}

	/**
	 * @generated
	 */
	@Override
	public void end() {
		myxRegistry.unregister(this);
		super.end();
	}

	/**
	 * Myx interface dblgen: <code>IN_DBLGEN</code>
	 * 
	 * @generated
	 */
	public static final IMyxName IN_DBLGEN = MyxUtils.createName("dblgen");

	/**
	 * Service object(s) for dblgen: <code>dblgen</code>
	 * 
	 * @see #IN_DBLGEN
	 * @generated
	 */
	protected org.archstudio.dblgen.IDataBindingGenerator dblgen = null;

	/**
	 * Returns the service object(s) for <code>dblgen</code>
	 * 
	 * @see #IN_DBLGEN
	 * @generated
	 */
	public org.archstudio.dblgen.IDataBindingGenerator getDblgen() {
		return dblgen;
	}

	/**
	 * @generated
	 */
	@Override
	public void interfaceConnected(IMyxName interfaceName, Object serviceObject) {
		if (serviceObject == null) {
			throw new NullPointerException(interfaceName.getName());
		}
		throw new IllegalArgumentException("Unhandled interface connection: " + interfaceName);
	}

	/**
	 * @generated
	 */
	@Override
	public void interfaceDisconnecting(IMyxName interfaceName, Object serviceObject) {
		if (serviceObject == null) {
			throw new NullPointerException(interfaceName.getName());
		}
		throw new IllegalArgumentException("Unhandled interface disconnection: " + interfaceName);
	}

	/**
	 * @generated
	 */
	@Override
	public void interfaceDisconnected(IMyxName interfaceName, Object serviceObject) {
	}

	/**
	 * @generated
	 */
	@Override
	public Object getServiceObject(IMyxName interfaceName) {
		if (interfaceName.equals(IN_DBLGEN)) {
			if (dblgen == null) {
				throw new NullPointerException("dblgen");
			}
			return dblgen;
		}
		throw new IllegalArgumentException("Unhandled interface service object: " + interfaceName);
	}
}