package net.md_5.bungee;

import lombok.NonNull;
import lombok.val;
import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityStrategy;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

import static net.openhft.affinity.AffinityStrategies.DIFFERENT_CORE;

/**
 * Created on 17-5-9.
 */
public class AffinityTFactory implements ThreadFactory {

    private final String name;
    private final boolean d;
    private final AffinityStrategy[] st;
    private AffinityLock lastLock;
    private int next;
    private final Logger logger;

    public AffinityTFactory(@NonNull String name, boolean d, @NonNull Logger logger, AffinityStrategy... st) {
        this.name = name;
        this.d = d;
        this.st = st;
        this.logger = logger;
    }

    public AffinityTFactory(String name, Logger logger) {
        this(name, true, logger, DIFFERENT_CORE);
    }

    @Override
    public synchronized Thread newThread(final Runnable r) {
        final val n = this.name + "-" + (++next);
        val t = new Thread(new Runnable() {
            public void run() {
                val l = lastLock == null ? AffinityLock.acquireLock() : lastLock.acquireLock(st);
                try {
                    int i = l.cpuId();
                    if (i > -1) lastLock = l;
                    logger.info(n + " assigned to cpu " + i);
                    r.run();
                } finally {
                    l.release();
                }
            }
        }, n);
        t.setDaemon(d);
        return t;
    }
}
