package org.holoeasy.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MutableState<T> {

    private List<Observer> observers = new ArrayList<Observer>();
    private T value;

    public MutableState(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T newValue) {
        value = newValue;
        this.notifyObservers();
    }

    public void update(Function<T, T> newFun) {
        set(newFun.apply(get()));
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.observerUpdate();
        }
    }
}
