package fr.exercice1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Paire<T, U> {
    T fst;
    U snd;

    public Paire(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T getFst() {
        return fst;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public U getSnd() {
        return snd;
    }

    public void setSnd(U snd) {
        this.snd = snd;
    }

    public List<T> filtragePredicatif(List<Predicate<T>> predicat, List<T> elements) {
        List<T> lesElements = new ArrayList<>();
        boolean test;
        for (T elem : elements) {
            test = false;
            for (Predicate<T> tPredicate : predicat) {
                if (tPredicate.test(elem)) {
                    test = true;
                }
            }
            if (test) {
                lesElements.add(elem);
            }
        }
        return lesElements;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", fst.toString(), snd.toString());
    }
}
