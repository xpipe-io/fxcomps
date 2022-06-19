package io.xpipe.fxcomps.util;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class PlatformThread {

    public static Observable sync(Observable o) {
        return new Observable() {

            private final Map<InvalidationListener, InvalidationListener> invListenerMap = new ConcurrentHashMap<>();

            @Override
            public void addListener(InvalidationListener listener) {
                InvalidationListener l = o -> {
                    PlatformThread.runLaterIfNeeded(() -> listener.invalidated(o));
                };

                invListenerMap.put(listener, l);
                o.addListener(l);
            }

            @Override
            public void removeListener(InvalidationListener listener) {
                o.removeListener(invListenerMap.getOrDefault(listener, listener));
            }
        };
    }

    public static <T> ObservableValue<T> sync(ObservableValue<T> ov) {
        return new ObservableValue<>() {

            private final Map<ChangeListener<? super T>, ChangeListener<? super T>> changeListenerMap = new ConcurrentHashMap<>();
            private final Map<InvalidationListener, InvalidationListener> invListenerMap = new ConcurrentHashMap<>();

            @Override
            public void addListener(ChangeListener<? super T> listener) {
                ChangeListener<? super T> l = (c, o, n) -> {
                    PlatformThread.runLaterIfNeeded(() -> listener.changed(c, o, n));
                };

                changeListenerMap.put(listener, l);
                ov.addListener(l);
            }

            @Override
            public void removeListener(ChangeListener<? super T> listener) {
                ov.removeListener(changeListenerMap.getOrDefault(listener, listener));
            }

            @Override
            public T getValue() {
                return ov.getValue();
            }

            @Override
            public void addListener(InvalidationListener listener) {
                InvalidationListener l = o -> {
                    PlatformThread.runLaterIfNeeded(() -> listener.invalidated(o));
                };

                invListenerMap.put(listener, l);
                ov.addListener(l);
            }

            @Override
            public void removeListener(InvalidationListener listener) {
                ov.removeListener(invListenerMap.getOrDefault(listener, listener));
            }
        };
    }

    public static <T> ObservableList<T> sync(ObservableList<T> ol) {
        return new ObservableList<>() {

            private final Map<ListChangeListener<? super T>, ListChangeListener<? super T>> listChangeListenerMap = new ConcurrentHashMap<>();
            private final Map<InvalidationListener, InvalidationListener> invListenerMap = new ConcurrentHashMap<>();

            @Override
            public void addListener(ListChangeListener<? super T> listener) {
                ListChangeListener<? super T> l = (lc) -> {
                    PlatformThread.runLaterIfNeeded(() -> listener.onChanged(lc));
                };

                listChangeListenerMap.put(listener, l);
                ol.addListener(l);
            }

            @Override
            public void removeListener(ListChangeListener<? super T> listener) {
                ol.removeListener(listChangeListenerMap.getOrDefault(listener, listener));
            }

            @Override
            public boolean addAll(T... elements) {
                return ol.addAll(elements);
            }

            @Override
            public boolean setAll(T... elements) {
                return ol.setAll(elements);
            }

            @Override
            public boolean setAll(Collection<? extends T> col) {
                return ol.setAll(col);
            }

            @Override
            public boolean removeAll(T... elements) {
                return ol.removeAll(elements);
            }

            @Override
            public boolean retainAll(T... elements) {
                return ol.retainAll(elements);
            }

            @Override
            public void remove(int from, int to) {
                ol.remove(from, to);
            }

            @Override
            public int size() {
                return ol.size();
            }

            @Override
            public boolean isEmpty() {
                return ol.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                return ol.contains(o);
            }

            @Override
            public Iterator<T> iterator() {
                return ol.iterator();
            }

            @Override
            public Object[] toArray() {
                return ol.toArray();
            }

            @Override
            public <T1> T1[] toArray(T1[] a) {
                return ol.toArray(a);
            }

            @Override
            public boolean add(T t) {
                return ol.add(t);
            }

            @Override
            public boolean remove(Object o) {
                return ol.remove(o);
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return ol.containsAll(c);
            }

            @Override
            public boolean addAll(Collection<? extends T> c) {
                return ol.addAll(c);
            }

            @Override
            public boolean addAll(int index, Collection<? extends T> c) {
                return ol.addAll(index, c);
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return ol.removeAll(c);
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return ol.retainAll(c);
            }

            @Override
            public void clear() {
                ol.clear();
            }

            @Override
            public T get(int index) {
                return ol.get(index);
            }

            @Override
            public T set(int index, T element) {
                return ol.set(index, element);
            }

            @Override
            public void add(int index, T element) {
                ol.add(index, element);
            }

            @Override
            public T remove(int index) {
                return ol.remove(index);
            }

            @Override
            public int indexOf(Object o) {
                return ol.indexOf(o);
            }

            @Override
            public int lastIndexOf(Object o) {
                return ol.lastIndexOf(o);
            }

            @Override
            public ListIterator<T> listIterator() {
                return ol.listIterator();
            }

            @Override
            public ListIterator<T> listIterator(int index) {
                return ol.listIterator(index);
            }

            @Override
            public List<T> subList(int fromIndex, int toIndex) {
                return ol.subList(fromIndex, toIndex);
            }

            @Override
            public void addListener(InvalidationListener listener) {
                InvalidationListener l = o -> {
                    PlatformThread.runLaterIfNeeded(() -> listener.invalidated(o));
                };

                invListenerMap.put(listener, l);
                ol.addListener(l);
            }

            @Override
            public void removeListener(InvalidationListener listener) {
                ol.removeListener(invListenerMap.getOrDefault(listener, listener));
            }
        };
    }

    public static <T> void connect(Property<T> outer, Property<T> platform) {
        final var binding = new PlatformGenericBidirectionalBinding<>(outer, platform);
        platform.setValue(outer.getValue());
        platform.getValue();
        outer.addListener(binding);
        platform.addListener(binding);
    }

    private static class PlatformGenericBidirectionalBinding<T> implements InvalidationListener, WeakListener {
        private final WeakReference<Property<T>> outerPropertyRef;
        private final WeakReference<Property<T>> platformPropertyRef;
        private T oldValue;
        private boolean updating = false;
        private final int cachedHashCode;

        private PlatformGenericBidirectionalBinding(Property<T> outerProperty, Property<T>  platformProperty) {
            cachedHashCode = outerProperty.hashCode() *  platformProperty.hashCode();
            oldValue = outerProperty.getValue();
            outerPropertyRef = new WeakReference<>(outerProperty);
            platformPropertyRef = new WeakReference<>(platformProperty);
        }

        protected Property<T> getProperty1() {
            return outerPropertyRef.get();
        }

        protected Property<T> getProperty2() {
            return platformPropertyRef.get();
        }

        @Override
        public int hashCode() {
            return cachedHashCode;
        }

        @Override
        public boolean wasGarbageCollected() {
            return (getProperty1() == null) || (getProperty2() == null);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final Object propertyA1 = getProperty1();
            final Object propertyA2 = getProperty2();
            if ((propertyA1 == null) || (propertyA2 == null)) {
                return false;
            }

            if (obj instanceof final PlatformGenericBidirectionalBinding<?> otherBinding) {
                final Object propertyB1 = otherBinding.getProperty1();
                final Object propertyB2 = otherBinding.getProperty2();
                if ((propertyB1 == null) || (propertyB2 == null)) {
                    return false;
                }

                if (propertyA1 == propertyB1 && propertyA2 == propertyB2) {
                    return true;
                }
                if (propertyA1 == propertyB2 && propertyA2 == propertyB1) {
                    return true;
                }
            }
            return false;
        }


        @Override
        public void invalidated(Observable sourceProperty) {
            if (!updating) {
                final Property<T> outerProperty = outerPropertyRef.get();
                final Property<T> platformProperty = platformPropertyRef.get();
                if ((outerProperty == null) || (platformProperty == null)) {
                    if (outerProperty != null) {
                        outerProperty.removeListener(this);
                    }
                    if (platformProperty != null) {
                        platformProperty.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        if (outerProperty == sourceProperty) {
                            PlatformThread.runLaterIfNeeded(() -> {
                                T newValue = outerProperty.getValue();
                                platformProperty.setValue(newValue);
                                platformProperty.getValue();
                                oldValue = newValue;
                            });
                        } else {
                            T newValue = platformProperty.getValue();
                            outerProperty.setValue(newValue);
                            outerProperty.getValue();
                            oldValue = newValue;
                        }
                    } catch (RuntimeException e) {
                        try {
                            if (outerProperty == sourceProperty) {
                                outerProperty.setValue(oldValue);
                                outerProperty.getValue();
                            } else {
                                PlatformThread.runLaterIfNeeded(() -> {
                                    platformProperty.setValue(oldValue);
                                    platformProperty.getValue();
                                });
                            }
                        } catch (Exception e2) {
                            e2.addSuppressed(e);
                            //unbind(outerProperty, platformProperty);
                            throw new RuntimeException(
                                    "Bidirectional binding failed together with an attempt"
                                            + " to restore the source property to the previous value."
                                            + " Removing the bidirectional binding from properties " +
                                            outerProperty + " and " + platformProperty, e2);
                        }
                        throw new RuntimeException(
                                "Bidirectional binding failed, setting to the previous value", e);
                    } finally {
                        updating = false;
                    }
                }
            }
        }
    }

    public static void runLaterIfNeeded(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }
}
