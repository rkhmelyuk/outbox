package outbox.subscriber.field

/**
 * Represents subscriber's dynamic field values.
 * 
 * @author Ruslan Khmelyuk
 */
class DynamicFieldValues {

    final List<DynamicField> fields
    final List<DynamicFieldValue> values

    DynamicFieldValues(List<DynamicField> fields, List<DynamicFieldValue> values) {
        this.fields = fields
        this.values = values
    }

    List<DynamicField> getFields() {
        Collections.unmodifiableList(fields)
    }

    List<DynamicField> getValues() {
        Collections.unmodifiableList(values)
    }

    List<String> getNames() {
        fields.collect { it.name }
    }

    List<String> getLabels() {
        fields.collect { it.label }
    }

    boolean addValue(DynamicFieldValue value) {
        if (value && value.dynamicField) {
            if (!hasValue(value.dynamicField)) {
                values << value
                return true
            }
        }
        return false
    }

    DynamicFieldValue get(DynamicField field) {
        for (DynamicFieldValue each : values) {
            if (each.dynamicField?.id == field.id) {
                return each
            }
        }
        return null
    }

    DynamicFieldValue get(String name) {
        for (DynamicFieldValue each : values) {
            if (each.dynamicField?.name == name) {
                return each
            }
        }
        return null
    }

    def value(DynamicField field) {
        for (DynamicFieldValue each : values) {
            if (each.dynamicField?.id == field.id) {
                return each.value
            }
        }
        return null
    }

    def value(String name) {
        for (DynamicFieldValue each : values) {
            if (each.dynamicField?.name == name) {
                return each.value
            }
        }
        return null
    }

    boolean hasValue(String name) {
        for (DynamicFieldValue each : values) {
            if (each.dynamicField?.name == name) {
                return true
            }
        }
        return false
    }

    boolean hasValue(DynamicField field) {
        for (DynamicFieldValue each : values) {
            if (each.dynamicField?.id == field.id) {
                return true
            }
        }
        return false
    }

}
