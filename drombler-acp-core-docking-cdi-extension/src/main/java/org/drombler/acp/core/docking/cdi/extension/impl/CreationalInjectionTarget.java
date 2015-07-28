/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2014 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.docking.cdi.extension.impl;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 *
 * @author puce
 */
public class CreationalInjectionTarget<T> implements AutoCloseable {

    private final InjectionTarget<T> injectionTarget;
    private final CreationalContext<T> context;

    public CreationalInjectionTarget(InjectionTarget<T> injectionTarget, CreationalContext<T> context) {
        this.injectionTarget = injectionTarget;
        this.context = context;
    }

    /**
     * @return the injectionTarget
     */
    public InjectionTarget<T> getInjectionTarget() {
        return injectionTarget;
    }

    /**
     * @return the context
     */
    public CreationalContext<T> getContext() {
        return context;
    }

    public T produce() {
        return injectionTarget.produce(context);
    }

    public void inject(T instance) {
        injectionTarget.inject(instance, context);
    }

    public void postConstruct(T instance) {
        injectionTarget.postConstruct(instance);
    }

    public void preDestroy(T instance) {
        injectionTarget.preDestroy(instance);
    }

    public void dispose(T instance) {
        injectionTarget.dispose(instance);
    }

    @Override
    public void close() {
        context.release();
    }

}
