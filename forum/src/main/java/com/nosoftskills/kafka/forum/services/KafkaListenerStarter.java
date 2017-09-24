package com.nosoftskills.kafka.forum.services;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaListenerStarter {

    @Resource
    private ManagedExecutorService executorService;

    @Inject
    private Instance<KafkaUpdatesListener> updatesListener;

    public void startListening(@Observes @Initialized(ApplicationScoped.class) Object init) {
        System.out.println("Initialized!!!");
        System.out.println("Update listener: " + updatesListener.get());
        executorService.execute(updatesListener.get());
    }
}
