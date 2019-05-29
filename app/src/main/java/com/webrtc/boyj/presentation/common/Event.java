package com.webrtc.boyj.presentation.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Read more :
 * https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
public class Event<T> {

    @Nullable
    private T content;

    private boolean hasBeenHandled = false;

    public Event(@Nullable T content) {
        this.content = content;
    }

    @Nullable
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    @Nullable
    public T peekContent() {
        return content;
    }

    @NonNull
    public T peekSafeContent() {
        if (content == null) {
            throw new IllegalStateException("content is null");
        }
        return content;
    }
}
