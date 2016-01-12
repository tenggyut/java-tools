package com.tenggyut.os;

import com.google.common.base.Optional;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.exception.RetryFailedException;
import org.apache.logging.log4j.Logger;

/**
 * Created by tenggyt on 2016/1/12.
 */
public abstract class RetryWorker<T> {
    private static final Logger LOG = LogFactory.getLogger(RetryWorker.class);
    private static final int DEFAULT_MAX_RETRY = 5;

    private int maxRetry;
    private String workName;
    private Optional<T> result;

    public RetryWorker(String workName, int maxRetry) {
        this.maxRetry = maxRetry;
        this.workName = workName;
    }

    public RetryWorker() {
        this("unnamed work", DEFAULT_MAX_RETRY);
    }

    public RetryWorker(String workName) {
        this(workName, DEFAULT_MAX_RETRY);
    }

    public T doWorkWithRetry() throws RetryFailedException {
        int retry = 0;
        do {
            Optional<T> t = doWork();
            if (t.isPresent()) {
                result = t;
                break;
            }
            retry++;
        } while (retry < maxRetry);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RetryFailedException(workName, maxRetry);
        }
    }

    protected abstract Optional<T> doWork();
}
