package fr.exercice2.universite.refactorer;

import fr.exercice2.universite.Annee;
import fr.exercice2.universite.Etudiant;
import fr.exercice2.universite.Matiere;
import fr.exercice2.universite.UE;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class of functions and methods
 *
 * @author Celina HADJARA
 */
public class MesMethodes {

    private MesMethodes() {
    }

    /**
     * En utilisant un for : <br>
     * Afficher les étudiants et leurs notes dans chaque matière d'UE, selon le prédicat appliqué
     *
     * @param EnTete       Titre de l'affichage
     * @param PredEtudiant Un prédicat à vérifier
     * @param annee        Année concernée
     */
    public static void afficheSi(String EnTete,
                                 Predicate<Etudiant> PredEtudiant,
                                 Annee annee) {
        System.out.println(EnTete);
        for (Etudiant i : annee.etudiants()) {
            if (PredEtudiant.test(i)) {
                System.out.println(i.toString());
            }
        }
    }

    /**
     * En utilisant un foreach et un Consumer : <br>
     * Afficher les étudiants et leurs notes dans chaque matière d'UE, selon le prédicat appliqué
     *
     * @param EnTete       Titre de l'affichage
     * @param PredEtudiant Un prédicat à vérifier
     *                     * @param annee         Année concernée
     */
    public static void afficheSi1(String EnTete, Predicate<Etudiant> PredEtudiant, Annee annee) {
        System.out.println(EnTete);
        annee.etudiants().forEach(etudiant -> {
            if (PredEtudiant.test(etudiant)) {
                System.out.println(etudiant.toString());
            }
        });
    }

    /**
     * Calculer la moyenne des étudiants, les étudiants défaillants dans une matière auront null comme moyenne
     *
     * @param etudiant {@link Etudiant} pour calculer sa moyenne
     * @return la moyenne des etudiants sans tenir compte des défaillants
     */
    public static Double moyenne (Etudiant etudiant) {
        double laMoyenne = 0;
        int sommeEcts = 0;
        if (MesPrediates.aDEF.test(etudiant)) return null;
        for (UE ue :etudiant.annee().ues()) {
            for (Map.Entry<Matiere, Integer> ects : ue.ects().entrySet()) {
                laMoyenne += etudiant.notes().get(ects.getKey())*ects.getValue();
                sommeEcts+= ects.getValue();
            }
        }
        return laMoyenne/sommeEcts;
    }

    /**
     * Afficher les étudiants et leurs moyenne selon le prédicat et la fonction appliqués
     *
     * @param EnTete       Titre de l'affichage
     * @param PredEtudiant Un prédicat à vérifier
     * @param annee        Année concernée
     * @param function     Function {@link MesMethodes}
     */
    public static void afficheSi2(String EnTete, Predicate<Etudiant> PredEtudiant, Annee annee, Function function) {
        System.out.println(EnTete);
        for (Etudiant i : annee.etudiants()) {
            if (PredEtudiant.test(i)) {
                System.out.println(function.apply(i));
            }
        }
    }

    /**
     * Calculer la moyenne des étudiants, un étudiant défaillant dans une matière aura 0 à cette matière
     *
     * @param etudiant pour calculer sa moyenne
     * @return moyenne de l'étudiant en tenant compte de la mention défaillante qui devient un 0
     */
    public static Double moyenneIndicative(Etudiant etudiant){
        double laMoyenne = 0.0;
        int sommeEcts = 0;
        for (UE ue :etudiant.annee().ues()) {
            for (Map.Entry<Matiere, Integer> ects : ue.ects().entrySet()) {
                if (!etudiant.notes().containsKey(ects.getKey())){
                    etudiant.notes().put(ects.getKey(),0.0);
                }
                laMoyenne += etudiant.notes().get(ects.getKey())*ects.getValue();
                sommeEcts+= ects.getValue();
            }
        }
        if (sommeEcts != 0) return laMoyenne / sommeEcts;
        return null;
    }

    //public static Function<Function<Etudiant,Double>, Predicate<Etudiant>> naPasLaMoyenneGeneralise = etudiant -> {

    //return etudiant.prenom() + " " + etudiant.nom() + " : " + MesMethodes.moyenneIndicative(etudiant);
    //}
}
