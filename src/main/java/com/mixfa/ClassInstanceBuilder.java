package com.mixfa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassInstanceBuilder<T> {
    private final Class<T> clazz;
    private final Map<String, Objects> fieldsToSet = new HashMap<>();
    private Constructor<T> constructor = null;

    private ClassInstanceBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ClassInstanceBuilder(Class<T> clazz, Class<?>... constructorArgs) throws NoSuchMethodException {
        this.clazz = clazz;
        this.constructor = clazz.getConstructor(constructorArgs);
    }

    public ClassInstanceBuilder<T> selectConstructor(Class<?> argsTypes) throws NoSuchMethodException {
        this.constructor = clazz.getConstructor(argsTypes);
        return this;
    }

    public ClassInstanceBuilder<T> withField(String fieldName, Objects value) {
        fieldsToSet.put(fieldName, value);
        return this;
    }

    public void clearFields() {
        fieldsToSet.clear();
    }

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
