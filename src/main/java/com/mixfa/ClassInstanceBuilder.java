package com.mixfa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class for producing instances
 * @param <T> Class type
 */
public class ClassInstanceBuilder<T> {
    private final Class<T> clazz;
    private final Map<String, Objects> fieldsToSet = new HashMap<>();
    private Constructor<T> constructor = null;

    /**
     * Constructor
     * @param clazz java class of target type
     */
    private ClassInstanceBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Constructor
     * @param clazz java class of target type
     * @param constructorArgs constructor args types
     */
    public ClassInstanceBuilder(Class<T> clazz, Class<?>... constructorArgs) throws NoSuchMethodException {
        this.clazz = clazz;
        this.constructor = clazz.getConstructor(constructorArgs);
    }

    /**
     * selects constructor
     * @param argsTypes constructor args types
     */
    public ClassInstanceBuilder<T> selectConstructor(Class<?> argsTypes) throws NoSuchMethodException {
        this.constructor = clazz.getConstructor(argsTypes);
        return this;
    }

    /**
     * set field value after instance creation
     * @param fieldName field name
     * @param value field value
     */
    public ClassInstanceBuilder<T> withField(String fieldName, Objects value) {
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
     * @param args constructor args
     * @return instance
     */
    public T newInstance(Objects... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T instance = constructor.newInstance((Object[]) args);

        for (Map.Entry<String, Objects> entry : fieldsToSet.entrySet()) {
            var fieldName = entry.getKey();
            var value = entry.getValue();

            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        }

        return instance;
    }
}
