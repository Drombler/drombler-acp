package org.drombler.acp.core.commons.util.concurrent;

import java.util.function.Consumer;

/**
 * Executes a {@link Consumer} on the GUI-toolkit specific application thread.
 *
 * @param <T> the input type
 *
 * @author puce
 */
public class ApplicationThreadConsumer<T> implements Consumer<T> {

    private final ApplicationExecutorProvider applicationExecutorProvider;
    private final Consumer<T> consumer;

    /**
     * Creates a new instance of this class.
     *
     * @param applicationExecutorProvider the application executor provider
     * @param consumer the consumer to call
     */
    public ApplicationThreadConsumer(ApplicationExecutorProvider applicationExecutorProvider, Consumer<T> consumer) {
        this.applicationExecutorProvider = applicationExecutorProvider;
        this.consumer = consumer;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void accept(T t) {
        applicationExecutorProvider.getApplicationExecutor().execute(() -> consumer.accept(t));
    }

}
