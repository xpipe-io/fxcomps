package io.xpipe.fxcomps.util;

import javafx.beans.WeakListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

public class StrongBindings {

    public static <E> Object bindContent(List<E> list1, ObservableList<? extends E> list2) {
        final StrongListContentBinding<E> contentBinding = new StrongListContentBinding<E>(list1);
        if (list1 instanceof ObservableList) {
            ((ObservableList<E>) list1).setAll(list2);
        } else {
            list1.clear();
            list1.addAll(list2);
        }
        list2.removeListener(contentBinding);
        list2.addListener(contentBinding);
        return contentBinding;
    }

    private static class StrongListContentBinding<E> implements ListChangeListener<E>, WeakListener {

        private final List<E> listRef;

        public StrongListContentBinding(List<E> list) {
            this.listRef = list;
        }

        @Override
        public void onChanged(ListChangeListener.Change<? extends E> change) {
            final List<E> list = listRef;
            if (list == null) {
                change.getList().removeListener(this);
            } else {
                while (change.next()) {
                    if (change.wasPermutated()) {
                        list.subList(change.getFrom(), change.getTo()).clear();
                        list.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo()));
                    } else {
                        if (change.wasRemoved()) {
                            list.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                        }
                        if (change.wasAdded()) {
                            list.addAll(change.getFrom(), change.getAddedSubList());
                        }
                    }
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return listRef == null;
        }

        @Override
        public int hashCode() {
            final List<E> list = listRef;
            return (list == null)? 0 : list.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final List<E> list1 = listRef;
            if (list1 == null) {
                return false;
            }

            if (obj instanceof StrongListContentBinding) {
                final StrongListContentBinding<?> other = (StrongListContentBinding<?>) obj;
                final List<?> list2 = other.listRef;
                return list1 == list2;
            }
            return false;
        }
    }

    public static <T> ChangeListener<T> bind(Property<T> property1, Property<T> property2) {
        final var binding = new StrongGenericBidirectionalBinding<T>(property1, property2);
        property1.setValue(property2.getValue());
        property1.addListener(binding);
        property2.addListener(binding);
        return binding;
    }

    private static class StrongGenericBidirectionalBinding<T> implements ChangeListener<T> {
        private final int cachedHashCode;
        private final Property<T> propertyRef1;
        private final Property<T> propertyRef2;
        private boolean updating = false;

        private StrongGenericBidirectionalBinding(Property<T> property1, Property<T> property2) {
            cachedHashCode = property1.hashCode() * property2.hashCode();
            propertyRef1 = property1;
            propertyRef2 = property2;
        }

        protected Property<T> getProperty1() {
            return propertyRef1;
        }

        protected Property<T> getProperty2() {
            return propertyRef2;
        }

        @Override
        public int hashCode() {
            return cachedHashCode;
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

            if (obj instanceof final StrongGenericBidirectionalBinding otherBinding) {
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
        public void changed(ObservableValue<? extends T> sourceProperty, T oldValue, T newValue) {
            if (!updating) {
                final Property<T> property1 = propertyRef1;
                final Property<T> property2 = propertyRef2;
                if ((property1 == null) || (property2 == null)) {
                    if (property1 != null) {
                        property1.removeListener(this);
                    }
                    if (property2 != null) {
                        property2.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        if (property1 == sourceProperty) {
                            property2.setValue(newValue);
                        } else {
                            property1.setValue(newValue);
                        }
                    } catch (RuntimeException e) {
                        try {
                            if (property1 == sourceProperty) {
                                property1.setValue(oldValue);
                            } else {
                                property2.setValue(oldValue);
                            }
                        } catch (Exception e2) {
                            e2.addSuppressed(e);
                            //unbind(property1, property2);
                            throw new RuntimeException(
                                    "Bidirectional binding failed together with an attempt"
                                            + " to restore the source property to the previous value."
                                            + " Removing the bidirectional binding from properties " +
                                            property1 + " and " + property2, e2);
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
}
