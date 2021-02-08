package org.japper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Mapping<T, S, V> {

    private BiConsumer<T, V> setterResolver;
    private Function<S, V> valueResolver;

    public static <T, S, V> Mapping<T, S, V> createMapping(BiConsumer<T, V> setterResolver, Function<S, V> valueResolver) {
        return new Mapping<>() {{
            setSetterResolver(setterResolver);
            setValueResolver(valueResolver);
        }};
    }

    public BiConsumer<T, V> getSetterResolver() {
        return setterResolver;
    }

    public void setSetterResolver(BiConsumer<T, V> setterResolver) {
        this.setterResolver = setterResolver;
    }

    public Function<S, V> getValueResolver() {
        return valueResolver;
    }

    public void setValueResolver(Function<S, V> valueResolver) {
        this.valueResolver = valueResolver;
    }
}
