package fr.exercice2.universite.refactorer;

import fr.Paire;
import fr.exercice2.universite.Annee;
import fr.exercice2.universite.Etudiant;
import fr.exercice2.universite.Matiere;
import fr.exercice2.universite.UE;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class of Predicates and Functions
 *
 * @author Celina HADJARA
 */
public class MesPrediates {
    /**
     * Somme de deux entiers
     */
    Somme<Integer> sommeInt =
            (Integer x, Integer y) -> x + y;

    /**
     * Concaténation de deux chaines de caractères
     */
    Somme<String> sommeString =
            (String x, String y) -> x + y;

    /**
     * Somme de deux doubles
     */
    Somme<Double> sommeDouble =
            (Double x, Double y) -> x + y;

    /**
     * Somme de deux longs
     */
    Somme<Long> sommeLong =
            (Long x, Long y) -> x + y;

    //Avec les streams
    ToString<List<String>> listToString = list ->
            list.stream().reduce("", (String a, String b) -> a + " ," + b);

    //Sans les streams
    ToString<List<Object>> listToString1 = x -> {
        String total = "";
        for (int i = 0; i < x.size(); i++) {
            total = sommeString.somme(total, x.get(i).toString());
            //virgule entre les elements
            if (i < x.size() - 1) {
                total = sommeString.somme(total, ",");
            }
            //Dernier element fini par un "."
            if (i == x.size() - 1) {
                total = sommeString.somme(total, ".");
            }
        }
        return total;
    };

    Map<String, Integer> map = Map.of("L3", 23, "M1", 25, "M2", 30);

    //Avec les streams
    ToString<Map<Integer, String>> mapToString = x ->
            map.keySet()
                    .stream()
                    .map(key -> key + ": " + map.get(key)).collect(Collectors.joining(","));

    //Sans les streams
    ToString<Map<Object, Object>> mapToString1 = x -> {
        StringBuilder total = new StringBuilder(" ");
        x.forEach((key, value) -> total.append(total)
                .append(key
                        .toString()).append(":")
                .append(value.toString())
                .append(","));
        return total.toString();

    };

    /**
     * Vérifier si un étudiant est défaillant
     */
    public static final Predicate<Etudiant> aDEF = etudiant -> {
        for (UE ue : etudiant.annee().ues()) {
            for (Map.Entry<Matiere, Integer> ects : ue.ects().entrySet()) {
                if (!etudiant.notes().containsKey(ects.getKey())) {
                    return true;
                }
            }
        }
        return false;
    };

    /*
      Question 3
      Function: Prends un argument (T) et retourne un (R), c'est à dire T convertie en R
      Predicate: Prends un argument (T) et retourne un boolean
      Consumer: Prends un argument (T) et ne retourne aucune valeur
      Supplier: Ne prends rien en argument et retourne (T)
     */
    /**
     * Vérifier si un étudiant a une note éliminatoire cad <6
     */
    public static final Predicate<Etudiant> aNoteEliminatoire = a -> {
        for (Double note : a.notes().values()) {
            if (note < 6.0)
                return true;
        }

        return false;
    };

    /**
     * Without try catch, this predicate make a null pointer exception
     * Because we can't compare null with double
     */
    public static final Predicate<Etudiant> naPasLaMoyennev1 = etudiant -> {
        Double aDouble = MesMethodes.moyenne(etudiant) == null ? null : MesMethodes.moyenne(etudiant);
        if (aDouble != null) {
            return aDouble < 10;
        }
        return false;
    };

    /**
     * Verifier si un étudiant n'a pas de moyenne cad défaillant ou a une note <6
     */
    public static final Predicate<Etudiant> naPasLaMoyennev2 = etudiant -> aDEF.or(naPasLaMoyennev1).test(etudiant);

    /**
     * On observe que l'ordre des test (d'excecution) correspond aux mêmes ordres
     * dans la disjonction logique des predicats
     */
    public static final Predicate<Etudiant> session2v1 = etudiant -> aDEF.or(aNoteEliminatoire).or(naPasLaMoyennev1).test(etudiant);

    /**
     * Function qui affiche la moyenne en tenant compte de défaillant
     */
    public static final Function<Etudiant, String> functionMoyenne = etudiant -> {
        if (aDEF.negate().test(etudiant))
            return etudiant.prenom() + " " + etudiant.nom() + " : " + MesMethodes.moyenne(etudiant);
        return etudiant.prenom() + " " + etudiant.nom() + " : " + "défaillant";
    };

    /**
     * Function qui affiche la moyenne, 0.0 dans la matière si l'étudiant est défaillant
     */
    public static final Function<Etudiant, String> functionMoyenneIndicative = etudiant ->
            etudiant.prenom() + " " + etudiant.nom() + " : " + MesMethodes.moyenneIndicative(etudiant);

    /**
     * En dessous de 10
     */
    public static final Predicate<Etudiant> naPasLaMoyenneGeneralise = etudiant -> {
        Double aDoubleIndic = MesMethodes.moyenneIndicative(etudiant) == null ? null : MesMethodes.moyenneIndicative(etudiant);
        Double aDouble = MesMethodes.moyenne(etudiant) == null ? null : MesMethodes.moyenne(etudiant);

        if (aDoubleIndic != null && MesPrediates.aDEF.test(etudiant)) return aDoubleIndic < 10;
        if (aDouble != null) return aDouble < 10;

        return false;
    };

    /**
     * Matières d'une année
     */
    public static final Function<Annee, Stream<Matiere>> matieresA =
            annee -> annee.ues()
                    .stream()
                    .flatMap(ue -> ue.ects().keySet().stream());

    /**
     * Matières d'un étudiant
     */
    public static final Function<Etudiant, Stream<Matiere>> matieresE =
            etudiant -> matieresA.apply(etudiant.annee());

    /**
     * Matières coefficientées d'un étudiant (version Entry)
     */
    public static final Function<Etudiant, Stream<Map.Entry<Matiere, Integer>>> matieresCoefE_ =
            etudiant -> etudiant.annee().ues()
                    .stream()
                    .flatMap(ue -> ue.ects().entrySet().stream());

    /**
     * Transformation d'une Entry en une Paire
     */
    public static final Function<Map.Entry<Matiere, Integer>, Paire<Matiere, Integer>> entry2paire =
            matiereIntegerEntry -> new Paire<>(matiereIntegerEntry.getKey(), matiereIntegerEntry.getValue());

    /**
     * Matières coefficientées d'un étudiant (version Paire)
     */
    public static final Function<Etudiant, Stream<Paire<Matiere, Integer>>> matieresCoefE = etudiant ->
            matieresCoefE_.apply(etudiant).map(entry2paire);

    /**
     * Accumulateur pour calcul de la moyenne
     * ((asomme, acoefs), (note, coef)) -> (asomme+note*coef, acoef+coef)
     */
    public static final BinaryOperator<Paire<Double, Integer>> accumulateurMoyenne =
            (x, y) -> new Paire<>(x.fst() + y.fst() * y.snd(), x.snd() + y.snd());

    /**
     * Zero (valeur initiale pour l'accumulateur)
     */
    public static final Paire<Double, Integer> zero = new Paire(0.0, 0);

    //
    //
    /**
     * Obtention de la liste de (note, coef) pour les matières d'un étudiant
     * <ul>
     *     <li>
     *         1. Obtenir les (matière, coef)s
     *     </li>
     *     <li>
     *         2. Mapper pour obtenir les (note, coef)s, null pour la note si l'étudiant est DEF dans cette matière
     *     </li>
     * </ul>
     */
    public static final Function<Etudiant, List<Paire<Double, Integer>>> notesPonderees = etudiant ->
            matieresCoefE.apply(etudiant)
                    .map(
                            x -> new Paire<>(etudiant.notes().get(x.fst()), x.snd()))
                    .toList();

    /**
     * Obtention de la liste de (note, coef) pour les matières d'un étudiant
     * <ul>
     *     <li>
     *         1. Obtenir les (matière, coef)s
     *     </li>
     *     <li>
     *         2. Mapper pour obtenir les (note, coef)s, 0.0 pour la note si l'étudiant est DEF dans cette matière
     *     </li>
     * </ul>
     */
    public static final Function<Etudiant, List<Paire<Double, Integer>>> notesPondereesIndicatives = etudiant ->
            matieresCoefE.apply(etudiant)
                    .map(
                            y -> (aDEF.test(etudiant) && etudiant.notes().get(y.fst()) == null) ?
                                    new Paire<>(0.0, y.snd()) :
                                    new Paire<>(etudiant.notes().get(y.fst()), y.snd()))
                    .toList();

    /**
     * Replie avec l'accumulateur spécifique
     */
    public static final Function<List<Paire<Double, Integer>>, Paire<Double, Integer>> reduit = l ->
            l.stream().reduce(zero, accumulateurMoyenne);

    /**
     * Calcule la moyenne à partir d'un couple (somme pondérée, somme coefs)
     */
    public static final Function<Paire<Double, Integer>, Double> divise = p ->
            p.fst() / p.snd();

    /**
     * Calcul de moyenne fonctionnel
     * Composer notesPonderees etudiant renvois list , reduit liste renvoi paire et divise pair renvoi double
     * Exception en cas de matière DEF
     */
    public static final Function<Etudiant, Double> computeMoyenne = etudiant ->
            (aDEF.test(etudiant)) ?
                    null :
                    divise.apply(reduit.apply(notesPonderees.apply(etudiant)));

    /**
     * Calcul de moyenne fonctionnel
     * Composer notesPondereesIndicatives, reduit et divise
     * Pas d'exception en cas de matière DEF
     */
    public static final Function<Etudiant, Double> computeMoyenneIndicative = etudiant ->
            divise.apply(reduit.apply(notesPondereesIndicatives.apply(etudiant)));

    /**
     * Calcul de moyenne
     */
    public static final Function<Etudiant, Double> moyenne = e ->
            (e == null || aDEF.test(e)) ? null : computeMoyenne.apply(e);

    /**
     * Calcul de moyenne indicative
     */
    public static final Function<Etudiant, Double> moyenneIndicative =
            computeMoyenneIndicative;
}