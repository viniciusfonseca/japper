package org.japper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Japper<S, T> {

    @SuppressWarnings("rawtypes")
    private final List<Mapping> mappings = new ArrayList<>();
    private Consumer<T> thenFunction;
    private final Class<T> targetClass;

    public Japper (Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public <V> Japper<S, T> map(BiConsumer<T, V> setterResolver, Function<S, V> valueResolver) {
        mappings.add(Mapping.createMapping(setterResolver, valueResolver));
        return this;
    }

    public Japper<S, T> then(Consumer<T> thenFunction) {
        this.thenFunction = thenFunction;
        return this;
    }

    @SuppressWarnings("all")
    public T parse(S source) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var result = targetClass.getDeclaredConstructor().newInstance();
        for (var mapping : mappings) {
            var value = mapping.getValueResolver().apply(source);
            mapping.getSetterResolver().accept(result, value);
        }
        if (thenFunction != null) {
            thenFunction.accept(result);
        }
        return result;
    }
}
