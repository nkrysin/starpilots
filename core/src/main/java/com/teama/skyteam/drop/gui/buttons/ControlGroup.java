/**
 * Repräsentiert eine Gruppe von `ControlField`-Elementen für das GUI.
 *
 * Die Klasse `ControlGroup` wird verwendet, um mehrere `ControlField`-Objekte zu verwalten,
 * die im GUI zusammengehören. Jede Gruppe hat einen Namen, eine Liste von Spielfeldern und
 * Methoden zur Verwaltung der Felder, wie das Abrufen verfügbarer Felder oder das Freigeben von Ressourcen.
 */
package com.teama.skyteam.drop.gui.buttons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlGroup {
    private String name;
    private Map<String, ControlField> fields;

    /**
     * Konstruktor zur Erstellung einer Gruppe von `ControlField`-Elementen.
     * @param name der Name der Gruppe.
     * @param fieldList eine Liste der Spielfelder, die zur Gruppe gehören.
     */
    public ControlGroup(String name, List<ControlField> fieldList) {
        this.name = name;
        this.fields = new HashMap<>();
        for (ControlField field : fieldList) {
            fields.put(field.getName(), field);
        }
    }

    public String getName() {
        return name;
    }

    public ControlField getFieldByName(String fieldName) {
        return fields.get(fieldName);
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
