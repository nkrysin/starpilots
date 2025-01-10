package com.teama.skyteam.drop.gui.buttons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlGroup {
    private String name;
    private Map<String, ControlField> fields;

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

    public void dispose() {
        for (ControlField field : fields.values()) {
            if (field.getButton() != null) {
                field.getButton().remove();
            }
        }
        fields.clear();
    }



}
