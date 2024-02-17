package com.mixfa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for producing instances
 *
 * @param <T> Class type
 */
public class ClassInstanceBuilder<T> {
    private final Class<T> clazz;
    private final Map<String, Object> fieldsToSet = new HashMap<>();
    private Constructor<T> constructor;

    /**
     * Constructor
     *
     * @param clazz           java class of target type
     * @param constructorArgs constructor args types
     */
    public ClassInstanceBuilder(Class<T> clazz, Class<?>... constructorArgs) {
        this.clazz = clazz;
        try {
            this.constructor = clazz.getConstructor(constructorArgs);
        }
        catch (Exception ignored) {
            this.constructor = null;
        }
    }

    /**
     * selects constructor
     *
     * @param argsTypes constructor args types
     */
    public ClassInstanceBuilder<T> selectConstructor(Class<?>... argsTypes) throws NoSuchMethodException {
        this.constructor = clazz.getConstructor(argsTypes);
        return this;
    }

    /**
     * set field value after instance creation
     *
     * @param fieldName field name
     * @param value     field value
     */
    public ClassInstanceBuilder<T> withField(String fieldName, Object value) {
        fieldsToSet.put(fieldName, value);
        return this;
    }

    /**
     * cleares fields
     */
    public void clearFields() {
        fieldsToSet.clear();
    }

    /**
     * creates new instance
     *
     * @param args constructor args
     * @return instance
     */
    public T newInstance(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T instance = constructor.newInstance(args);

        for (Map.Entry<String, Object> entry : fieldsToSet.entrySet()) {
            var fieldName = entry.getKey();
            var value = entry.getValue();

            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        }

        return instance;
    }
}
