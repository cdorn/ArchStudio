package org.archstudio.myxgen;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import org.archstudio.myx.fw.*;
import org.archstudio.myx.fw.IMyxDynamicBrick;
import org.archstudio.myx.fw.IMyxLifecycleProcessor;
import org.archstudio.myx.fw.IMyxBrickItems;
import org.archstudio.myxgen.IMyxStubGenerator;
import org.archstudio.myx.fw.IMyxProvidedServiceProvider;

/**
 * Abstract Myx brick: "Myx Component Stub Generator Impl"
 *
 * @generated
 */
@SuppressWarnings("unused")
/* package */abstract class MyxGenMyxComponentStub extends org.archstudio.myx.fw.AbstractMyxSimpleBrick implements
		org.archstudio.myx.fw.IMyxDynamicBrick, org.archstudio.myx.fw.IMyxLifecycleProcessor,
		org.archstudio.myx.fw.IMyxProvidedServiceProvider {

	/**
	 * @generated
	 */
	protected final MyxRegistry myxRegistry = MyxRegistry.getSharedInstance();

	/**
	 * @generated
	 */
	public void begin() {
		super.begin();
		myxRegistry.register(this);
	}

	/**
	 * @generated
	 */
	public void end() {
		myxRegistry.unregister(this);
		super.end();
	}

	/**
	 * Myx name for the <code>myxgen</code> interface.
	 *
	 * @generated
	 */
	// MyxGenInterface[name=myxgen,direction=in,single=true,serviceObjectDelegate=variable,generateGetter=true,className=org.archstudio.myxgen.IMyxStubGenerator,description=null]
	public static final IMyxName IN_MYXGEN = MyxUtils.createName("myxgen");

	/**
	 * Service object(s) for the myxgen interface.
	 *
	 * @see #IN_MYXGEN
	 * @generated
	 */
	protected org.archstudio.myxgen.IMyxStubGenerator myxgen = null;

	/**
	 * Returns the service object(s) for the <code>myxgen</code> interface.
	 *
	 * @see #IN_MYXGEN
	 * @generated
	 */
	public org.archstudio.myxgen.IMyxStubGenerator getMyxgen() {
		return myxgen;
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
		if (interfaceName.equals(IN_MYXGEN)) {
			if (myxgen == null) {
				throw new NullPointerException("myxgen");
			}
			return myxgen;
		}
		throw new IllegalArgumentException("Unhandled interface service object: " + interfaceName);
	}
}