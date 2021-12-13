package fr.exercice2.universite.refactorer;

/**
 * @param <T>
 * @author Célina Hadjara
 */
@FunctionalInterface
public interface ToString<T> {
    String convertToString(T a);
}
