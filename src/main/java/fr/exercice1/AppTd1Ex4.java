package fr.exercice1;

import fr.Paire;

public class AppTd1Ex4 {
    public static void main(String[] args) {
        DAO data = DAO.instance();
        // commandes (non normalisées puis normalisées)
        for (Commande c : data.commandes()) {
            System.out.println(c);
        }
        for (Commande c : data.commandes()) {
            System.out.println(c.normaliser());
        }
        // produits
        System.out.println(data.produits());
        // produits à TVA réduite (3 versions)
        System.out.println(data.selectionProduits(p -> p.cat() == Categorie.REDUIT));
        // produits à TVA réduite coûtant plus de 5 EUR
        System.out.println(data.selectionProduits(p -> p.cat() == Categorie.REDUIT && p.prix() > 5.0));
        // commandes de plus de 2 items
        System.out.println(data.selectionCommande(c -> c.lignes().size() > 2));
        // commandes contenant au moins un produit à TVA réduite commandé en plus de 2
        // exemplaires
        System.out.println(
                data.selectionCommandeSurExistanceLigne(l -> l.fst().cat() == Categorie.REDUIT && l.snd() > 2));
        System.out.println(
                data.selectionCommandeSurExistanceLigne(l -> l.fst().cat() == Categorie.REDUIT && l.snd() > 2));
        // affichage des commandes sans réduction
        for (Commande c : data.commandes()) {
            System.out.println(c.affiche(AppTd1Ex4::calculSimple));
        }
        // affichage des commandes avec réduction
        for (Commande c : data.commandes()) {
            System.out.println(c.affiche(AppTd1Ex4::calculReduction));
        }
    }

    public static Boolean aTauxReduit(Produit p) {
        return p.cat() == Categorie.REDUIT;
    }

    /**
     * prix TTC = prix unitaire * (1 + taux TVA) * qté
     */
    public static Double calculSimple(Paire<Produit, Integer> ligne) {
        Produit p = ligne.fst();
        int qte = ligne.snd();
        return p.prix() * (1 + p.cat().tva()) * qte;
    }

    /**
     * réduction = si qté > 2 alors prix unitaire sinon 0 prix TTC = prix unitaire *
     * (1 + taux TVA) * qté - réduction
     */
    public static Double calculReduction(Paire<Produit, Integer> ligne) {
        Double reduction = (ligne.snd() > 2) ? ligne.fst().prix() : 0.0;
        return calculSimple(ligne) - reduction;
    }
}