package org.drombler.acp.core.commons.util.concurrent;

import java.util.function.Consumer;

/**
 * Executes the wrapped {@link Consumer} on the GUI-toolkit specific application thread.
 *
 * @param <T> the input type
 *
 * @author puce
 */
public class ApplicationThreadConsumer<T> implements Consumer<T> {

    private final ApplicationThreadExecutorProvider applicationThreadExecutorProvider;
    private final Consumer<T> consumer;

    /**
     * Creates a new instance of this class.
     *
     * @param applicationThreadExecutorProvider the application thread executor provider
     * @param consumer the consumer to call
     */
    public ApplicationThreadConsumer(ApplicationThreadExecutorProvider applicationThreadExecutorProvider, Consumer<T> consumer) {
        this.applicationThreadExecutorProvider = applicationThreadExecutorProvider;
        this.consumer = consumer;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void accept(T t) {
        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> consumer.accept(t));
    }

}
