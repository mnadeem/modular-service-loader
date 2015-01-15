package com.prokarma.app;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServiceSupport {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceSupport.class);

    protected final AtomicBoolean started = new AtomicBoolean(false);
    protected final AtomicBoolean starting = new AtomicBoolean(false);
    protected final AtomicBoolean stopping = new AtomicBoolean(false);
    protected final AtomicBoolean stopped = new AtomicBoolean(false);
    protected final AtomicBoolean suspending = new AtomicBoolean(false);
    protected final AtomicBoolean suspended = new AtomicBoolean(false);
    protected final AtomicBoolean shuttingdown = new AtomicBoolean(false);
    protected final AtomicBoolean shutdown = new AtomicBoolean(false);

    public void start() throws Exception {
        if (isStarting() || isStarted()) {
            // only start service if not already started
            LOG.trace("Service already started");
            return;
        }
        if (starting.compareAndSet(false, true)) {
            LOG.trace("Starting service");
            try {
                doStart();
                started.set(true);
                starting.set(false);
                stopping.set(false);
                stopped.set(false);
                suspending.set(false);
                suspended.set(false);
                shutdown.set(false);
                shuttingdown.set(false);
            } catch (Exception e) {
                try {
                    stop();
                } catch (Exception e2) {
                    // Ignore exceptions as we want to show the original exception
                } finally {
                    // ensure flags get reset to stopped as we failed during starting
                    stopping.set(false);
                    stopped.set(true);
                    starting.set(false);
                    started.set(false);
                    suspending.set(false);
                    suspended.set(false);
                    shutdown.set(false);
                    shuttingdown.set(false);
                }
                throw e;
            } 
        }
    }
    
    public void stop() throws Exception {
        if (isStopped()) {
            LOG.trace("Service already stopped");
            return;
        }
        if (isStopping()) {
            LOG.trace("Service already stopping");
            return;
        }
        stopping.set(true);
        try {
            doStop();
        } finally {
            stopping.set(false);
            stopped.set(true);
            starting.set(false);
            started.set(false);
            suspending.set(false);
            suspended.set(false);
            shutdown.set(false);
            shuttingdown.set(false);            
        }
    }

    public void suspend() throws Exception {
        if (!suspended.get()) {
            if (suspending.compareAndSet(false, true)) {
                try {
                    starting.set(false);
                    stopping.set(false);
                    doSuspend();
                } finally {
                    stopped.set(false);
                    stopping.set(false);
                    starting.set(false);
                    started.set(false);
                    suspending.set(false);
                    suspended.set(true);
                    shutdown.set(false);
                    shuttingdown.set(false);
                }
            }
        }
    }

    public void resume() throws Exception {
        if (suspended.get()) {
            if (starting.compareAndSet(false, true)) {
                try {
                    doResume();
                } finally {
                    started.set(true);
                    starting.set(false);
                    stopping.set(false);
                    stopped.set(false);
                    suspending.set(false);
                    suspended.set(false);
                    shutdown.set(false);
                    shuttingdown.set(false);
                }
            }
        }
    }

    public void shutdown() throws Exception {
        if (shutdown.get()) {
            LOG.trace("Service already shut down");
            return;
        }
        // ensure we are stopped first
        stop();

        if (shuttingdown.compareAndSet(false, true)) {
            try {
                doShutdown();
            } finally {
                // shutdown is also stopped so only set shutdown flags
                shutdown.set(true);
                shuttingdown.set(false);
            }
        }
    }

    public ServiceStatus getStatus() {
        // we should check the ---ing states first, as this indicate the state is in the middle of doing that
        if (isStarting()) {
            return ServiceStatus.Starting;
        }
        if (isStopping()) {
            return ServiceStatus.Stopping;
        }
        if (isSuspending()) {
            return ServiceStatus.Suspending;
        }

        // then check for the regular states
        if (isStarted()) {
            return ServiceStatus.Started;
        }
        if (isStopped()) {
            return ServiceStatus.Stopped;
        }
        if (isSuspended()) {
            return ServiceStatus.Suspended;
        }

        // use stopped as fallback
        return ServiceStatus.Stopped;
    }
    
    public boolean isStarted() {
        return started.get();
    }

    public boolean isStarting() {
        return starting.get();
    }

    public boolean isStopping() {
        return stopping.get();
    }

    public boolean isStopped() {
        return stopped.get();
    }

    public boolean isSuspending() {
        return suspending.get();
    }

    public boolean isSuspended() {
        return suspended.get();
    }

    public boolean isRunAllowed() {
        return !isStoppingOrStopped();
    }

    public boolean isStoppingOrStopped() {
        return stopping.get() || stopped.get();
    }

    /**
     * Implementations override this method to support customized start/stop.
     * <p/>
     * <b>Important: </b> See {@link #doStop()} for more details.
     * 
     * @see #doStop()
     */
    protected abstract void doStart() throws Exception;

    /**
     * Implementations override this method to support customized start/stop.
     * <p/>
     * <b>Important:</b> Camel will invoke this {@link #doStop()} method when
     * the service is being stopped. This method will <b>also</b> be invoked
     * if the service is still in <i>uninitialized</i> state (eg has not
     * been started). The method is <b>always</b> called to allow the service
     * to do custom logic when the service is being stopped, such as when
     * {@link org.apache.camel.CamelContext} is shutting down.
     * 
     * @see #doStart() 
     */
    protected abstract void doStop() throws Exception;

    /**
     * Implementations override this method to support customized suspend/resume.
     */
    protected void doSuspend() throws Exception {
        // noop
    }

    /**
     * Implementations override this method to support customized suspend/resume.
     */
    protected void doResume() throws Exception {
        // noop
    }

    /**
     * Implementations override this method to perform customized shutdown.
     */
    protected void doShutdown() throws Exception {
        // noop
    }
    
    public static enum ServiceStatus implements Serializable {
        Starting, Started, Stopping, Stopped, Suspending, Suspended;

        public boolean isStartable() {
            return this == Stopped || this == Suspended;
        }

        public boolean isStoppable() {
            return this == Started || this == Suspended;
        }

        public boolean isSuspendable() {
            return this == Started;
        }

        public boolean isStarting() {
            return this == Starting;
        }

        public boolean isStarted() {
            return this == Started;
        }

        public boolean isStopping() {
            return this == Stopping;
        }

        public boolean isStopped() {
            return this == Stopped;
        }

        public boolean isSuspending() {
            return this == Suspending;
        }

        public boolean isSuspended() {
            return this == Suspended;
        }

    }
}
