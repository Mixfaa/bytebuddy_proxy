package com.mixfa.bytebuddy_proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ProxyClassMaker {
    private ProxyClassMaker() {}

    public static <T> Class<? extends T> makeProxyClass(Class<T> clazz, List<MethodInterceptionDescription> interceptions) {
        return makeProxyClass(clazz, interceptions, null);
    }

    public static <T> Class<? extends T> makeProxyClass(
            Class<T> clazz,
            List<MethodInterceptionDescription> interceptions,
            Function<DynamicType.Builder<T>, DynamicType.Builder<T>> configuration
    ) {
        Map<MethodDescription, Implementation.Composable> methodInterceptions = new HashMap<>();
        for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            methodInterceptions.put(new MethodDescription.ForLoadedMethod(method), SuperMethodCall.INSTANCE);
        }

        for (MethodInterceptionDescription description : interceptions) {
            var implementation = description.implementation();
            var matcher = description.matcher();
            var isBeforeSuper = description.beforeSuper();

            for (Map.Entry<MethodDescription, Implementation.Composable> mapEntry : methodInterceptions.entrySet()) {
                if (matcher.matches(mapEntry.getKey())) {
                    if (isBeforeSuper)
                        mapEntry.setValue(implementation.andThen(mapEntry.getValue()));
                    else
                        mapEntry.setValue(mapEntry.getValue().andThen(implementation));
                }
            }
        }

        DynamicType.Builder<T> bbBuilder = new ByteBuddy().subclass(clazz);

        if (configuration != null)
            bbBuilder = configuration.apply(bbBuilder);

        for (Map.Entry<MethodDescription, Implementation.Composable> entry : methodInterceptions.entrySet()) {
            bbBuilder = bbBuilder.method(ElementMatchers.is(entry.getKey())).intercept(entry.getValue());
        }

        try (var unloadedClass = bbBuilder.make()) {
            return unloadedClass.load(clazz.getClassLoader()).getLoaded();
        }
    }
}


