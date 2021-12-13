package fr.exercice2.universite.refactorer;


/**
 * Interface that somme two variables
 *
 * @param <T>
 * @author Célina Hadjara
 */
@FunctionalInterface
public interface Somme<T> {
    T somme(T t1, T t2);

}
