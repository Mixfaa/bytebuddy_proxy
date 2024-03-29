package com.mixfa.bytebuddy_proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for producing instances
 * @param <T> Class type
 */
public class ClassInstanceBuilder<T> {
    private final Class<T> clazz;
    private final Map<String, Object> fieldsToSet = new HashMap<>();
    private Constructor<T> constructor;

    public ClassInstanceBuilder(Class<T> clazz, Class<?>... constructorArgs) {
        this.clazz = clazz;
        try {
            this.constructor = clazz.getConstructor(constructorArgs);
        }
        catch (Exception ignored) {
            this.constructor = null;
        }
    }

    public ClassInstanceBuilder<T> selectConstructor(Class<?>... argsTypes) throws NoSuchMethodException {
        this.constructor = clazz.getConstructor(argsTypes);
        return this;
    }

    public ClassInstanceBuilder<T> withField(String fieldName, Object value) {
        fieldsToSet.put(fieldName, value);
        return this;
    }

    public ClassInstanceBuilder<T> clearFields() {
        fieldsToSet.clear();
        return this;
    }

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
