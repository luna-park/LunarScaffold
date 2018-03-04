package org.lunapark.dev.scaffold.layout;

public interface EventListener {
    void complete();

    void onEvent(String message);
}
