package com.muse.editor.event;

import com.muse.editor.event.model.AppEvent;
import com.muse.editor.event.model.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus extends Bus {
    private static final EventBus instance = new EventBus();
    private Map<Class<?>, List<Consumer>> listeners = new HashMap();

    public static EventBus getInstance() {
        return instance;
    }

    private EventBus() {}

    public <T extends AppEvent> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T extends AppEvent> void unsubscribe(Class<T> eventType, Consumer<T> listener) {
        List<Consumer> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    public <T extends AppEvent> void publish(T event) {
        Class<?> eventClass = event.getClass();
        List<Consumer> eventListeners = listeners.get(eventClass);
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.accept(event));
        }
    }
}
