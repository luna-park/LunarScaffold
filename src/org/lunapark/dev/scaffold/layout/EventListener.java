package org.lunapark.dev.scaffold.layout;

import java.io.File;

public interface EventListener {
    void complete();

    void onEvent(String message);

    void setSource(File file);
}
