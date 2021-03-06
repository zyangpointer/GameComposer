package de.mirkosertic.gameengine.teavm.json;

import org.teavm.jso.JS;
import org.teavm.jso.JSArray;
import org.teavm.jso.JSObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class JSONMap implements Map<String, Object> {

    private final JSODelegate root;

    public JSONMap(JSObject aRoot) {
        root = (JSODelegate) aRoot;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    private Object unwrap(JSODelegate aDelegate) {
        switch(JS.getType(aDelegate)) {
        case BOOLEAN:
            return JS.unwrapBoolean(aDelegate);
        case FUNCTION:
            throw new IllegalArgumentException();
        case NUMBER:
            return JS.unwrapInt(aDelegate);
        case OBJECT: {
            if (aDelegate.getConstructor() == ((JSOConstructors) JS.getGlobal()).getArray()) {
                // We are dealing with an Array
                JSArray theArray = (JSArray) aDelegate;
                List<Object> theResult = new ArrayList<>();
                for (int i=0;i<theArray.getLength();i++) {
                    theResult.add(unwrap((JSODelegate) theArray.get(i)));
                }
                return theResult;
            }
            return new JSONMap(aDelegate);
        }
        case STRING:
            return JS.unwrapString(aDelegate);
        case UNDEFINED:
            return null;
        }
        throw new IllegalStateException();
    }

    @Override
    public Object get(Object aKey) {
        return unwrap((JSODelegate) root.get((String) aKey));
    }

    @Override
    public Object put(String key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object remove(Object key) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void clear() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object replace(String key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }
}
