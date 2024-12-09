package com.example.demo;

public interface Sujet {
    void addObserver(Observer observer);
    void deleteObserver(Observer observer);
    void notifyObservers(String message);

}
