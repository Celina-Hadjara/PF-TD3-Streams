package fr.exercice2.main;

import fr.exercice2.universite.Annee;
import fr.exercice2.universite.Etudiant;
import fr.exercice2.universite.Matiere;
import fr.exercice2.universite.UE;
import fr.exercice2.universite.refactorer.MesMethodes;
import fr.exercice2.universite.refactorer.MesPrediates;

import java.util.Map;

import java.util.Set;

/**
 * Main class to implement predicates of Somme interface and ToString interface
 *
 * @author Célina Hadjara
 */
public class AppTd2 {

    public static void main(String[] args) {

        Matiere m1 = new Matiere("MAT1");
        Matiere m2 = new Matiere("MAT2");
        UE ue1 = new UE("UE1", Map.of(m1, 2, m2, 2));
        Matiere m3 = new Matiere("MAT3");
        UE ue2 = new UE("UE2", Map.of(m3, 1));
        Annee a1 = new Annee(Set.of(ue1, ue2));
        Etudiant e1 = new Etudiant("39001", "Alice", "Merveille", a1);
        e1.noter(m1, 12.0);
        e1.noter(m2, 14.0);
        e1.noter(m3, 10.0);
        String.valueOf(e1);
        Etudiant e2 = new Etudiant("39002", "Bob", "Eponge", a1);
        e2.noter(m1, 14.0);
        e2.noter(m3, 14.0);
        Etudiant e3 = new Etudiant("39003", "Charles", "Chaplin", a1);
        e3.noter(m1, 18.0);
        e3.noter(m2, 5.0);
        e3.noter(m3, 14.0);
        Etudiant e4 = new Etudiant("39004", "Toto", "Dupont", a1);
        e4.noter(m1, 8.0);
        e4.noter(m2, 5.0);
        e4.noter(m3, 14.0);

        MesMethodes.afficheSi("**TOUS LES ETUDIANTS", a -> true, a1);

        MesMethodes.afficheSi("**TOUS LES ETUDIANTS DEFAILLANTS", MesPrediates.aDEF, a1);

        System.out.println("\n **NOTES PONDEREES \n "+ e2.nom() + MesPrediates.notesPonderees.apply(e2));

        System.out.println("\n **NOTES PONDEREES Indicatives \n "+ e2.nom() + MesPrediates.notesPondereesIndicatives.apply(e2));

        //MesMethodes.afficheSi("**ETUDIANTS AVEC NOTE ELIMINATOIRE", MesPrediates.aNoteEliminatoire, a1);

        for (Etudiant etudiant : a1.etudiants()) {
            String.format(
                    "%n **LA MOYENNE DE: %s %s %s",
                    etudiant.nom(),
                    etudiant.prenom(),
                    MesMethodes.moyenne(etudiant)
            );
        }

        MesMethodes.afficheSi(
                "\n\n**ETUDIANTS SOUS LA MOYENNE (v1)", MesPrediates.naPasLaMoyennev1, a1
        );

        MesMethodes.afficheSi(
                "\n\n**ETUDIANTS SOUS LA MOYENNE (v2)", MesPrediates.naPasLaMoyennev2, a1
        );

        MesMethodes.afficheSi(
                "\n\n**ETUDIANTS EN SESSION 2 (v2)", MesPrediates.session2v1, a1
        );

        MesMethodes.afficheSi2("\n**TOUS LES ETUDIANTS",
                a2 -> true, a1, MesPrediates.functionMoyenne);

        MesMethodes.afficheSi2("\n**TOUS LES ETUDIANTS AVEC MOYENNE INDICATIVE", MesPrediates.naPasLaMoyenneGeneralise,
                a1, MesPrediates.functionMoyenneIndicative);

    }
}
