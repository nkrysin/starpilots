package com.teama.skyteam.drop.spiel.action.command;

import com.teama.skyteam.drop.spiel.logik.events.GameEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionResult {
    private final boolean success;
    private final String errorMessage;
    private final List<GameEvent> events;

    private ActionResult(boolean success, String errorMessage,  List<GameEvent> events) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.events = new ArrayList<>(events);
    }

    public static ActionResult success() {
        return new ActionResult(true, null,  Collections.emptyList());
    }
    public static ActionResult fail(String errorMessage) {
        return new ActionResult(false, errorMessage,  Collections.emptyList());
    }
    public boolean isSuccess() {
        return success;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public List<GameEvent> getEvents() { return Collections.unmodifiableList(events);  }

    public ActionResult withEvent(GameEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        List<GameEvent> newEvents = new ArrayList<>(this.events);
        newEvents.add(event);
        return new ActionResult(this.success, this.errorMessage, newEvents);
    }

    public ActionResult withEvents(List<GameEvent> newEvents) {
        if (newEvents == null || newEvents.isEmpty()) {
            return this;
        }
        List<GameEvent> allEvents = new ArrayList<>(this.events);
        allEvents.addAll(newEvents);
        return new ActionResult(this.success, this.errorMessage, allEvents);
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }
}
