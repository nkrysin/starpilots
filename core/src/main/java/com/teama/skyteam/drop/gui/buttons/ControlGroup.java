/**
 * Repräsentiert eine Gruppe von `ControlField`-Elementen für das GUI.
 *
 * Die Klasse `ControlGroup` wird verwendet, um mehrere `ControlField`-Objekte zu verwalten,
 * die im GUI zusammengehören. Jede Gruppe hat einen Namen, eine Liste von Spielfeldern und
 * Methoden zur Verwaltung der Felder, wie das Abrufen verfügbarer Felder oder das Freigeben von Ressourcen.
 */
package com.teama.skyteam.drop.gui.buttons;

import com.teama.skyteam.drop.spiel.action.type.ActionControlGroup;
import com.teama.skyteam.drop.spiel.action.type.ActionName;

import java.util.*;

public class ControlGroup {
    private ActionControlGroup controlGroup;
    private EnumMap<ActionName, ControlField> fields;

    /**
     * Konstruktor zur Erstellung einer Gruppe von `ControlField`-Elementen.
     * @param controlGroup der Name der Gruppe.
     * @param fieldList eine Liste der Spielfelder, die zur Gruppe gehören.
     */
    public ControlGroup(ActionControlGroup controlGroup, List<ControlField> fieldList) {
        this.controlGroup = controlGroup;
        this.fields = new EnumMap<>(ActionName.class);
        for (ControlField field : fieldList) {
            fields.put(field.getAction(), field);
        }
    }

    public ActionControlGroup getControlGroup() {
        return controlGroup;
    }

    public ControlField getFieldByAction(ActionName action) {
        return fields.get(action);
    }

    /**
     * Gibt eine Liste der verfügbaren Spielfelder zurück.
     * Ein Spielfeld ist verfügbar, wenn es nicht belegt ist.
     * @return eine Liste der verfügbaren Spielfelder.
     */
    public List<ControlField> getAvailableFields() {
        List<ControlField> availableFields = new ArrayList<ControlField>();
        for (ControlField field : fields.values()) {
            if (!field.isOccupied()) {
                availableFields.add(field);
            }
        }
        return availableFields;
    }

    public int size() {
        return fields.size();
    }

    public List<ControlField> getAllFields() {
        return new ArrayList<>(fields.values());
    }

    /**
     * Gibt die Ressourcen aller Spielfelder in der Gruppe frei und leert die Gruppe.
     * Entfernt alle Buttons aus der Benutzeroberfläche und löscht die Referenzen auf die Spielfelder.
     */
    public void dispose() {
        for (ControlField field : fields.values()) {
            if (field.getButton() != null) {
                field.getButton().remove();
            }
        }
        fields.clear();
    }
}
