package com.lalaalal.droni;

import java.util.concurrent.Executor;

public class CallbackExecutor implements Executor {

    @Override
    public void execute(final Runnable r) {
        final Thread runner = new Thread(r);
        runner.start();

        if (r instanceof CallbackRunnable) {
            Thread callerbacker = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        runner.join();
                        ((CallbackRunnable)r).callBack();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            callerbacker.start();
        }
    }
}
