package org.japper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Japper<S, T> {

    @SuppressWarnings("rawtypes")
    private final List<Mapping> mappings = new ArrayList<>();
    private Consumer<T> thenFunction;
    private final Class<T> targetClass;

    @SuppressWarnings("rawtypes")
    private static final Map<String, Japper> Mappers = new HashMap<>();

    private static String srcTgtPairToHashCode(Class<?> srcClass, Class<?> tgtClass) {
        var srcHashCode = String.valueOf(srcClass.hashCode());
        var tgtHashCode = String.valueOf(tgtClass.hashCode());
        return srcHashCode + '$' + tgtHashCode;
    }

    public Japper (Class<S> sourceClass, Class<T> targetClass) {
        this.targetClass = targetClass;
        var key = srcTgtPairToHashCode(sourceClass, targetClass);
        Mappers.put(key, this);
    }

    @SuppressWarnings("all")
    public static <S, T> Japper<S, T> getFor(Class<S> sourceClass, Class<T> targetClass) {
        return (Japper<S, T>) Mappers.get(srcTgtPairToHashCode(sourceClass, targetClass));
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
