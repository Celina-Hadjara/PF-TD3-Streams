package fr.exercice1;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.HashMap;

public class Commande {
    private List<Paire<Produit, Integer>> lignes;

    public Commande() {
        this.lignes = new ArrayList<>();
    }

    public Commande ajouter(Produit p, int q) {
        lignes.add(new Paire<>(p, q));
        return this;
    }

    public List<Paire<Produit, Integer>> lignes() {
        return lignes;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Commande\n");
        for (Paire<Produit, Integer> ligne : lignes) {
            str.append(String.format("%s x%d\n", ligne.getFst(), ligne.getSnd()));
        }
        return str.toString();
    }

    /**
     * cumule les lignes en fonction des produits
     */
    public Commande normaliser() {
        Map<Produit,Integer> lignesCumulees = new HashMap<>();
        for(Paire<Produit,Integer> ligne : lignes) {
            Produit p = ligne.getFst();
            int qte = ligne.getSnd();
            if(lignesCumulees.containsKey(ligne.getFst())) {
               lignesCumulees.put(p, lignesCumulees.get(p)+qte);
            }
            else {
                lignesCumulees.put(p, qte);
            }
        }
        Commande commandeNormalisee = new Commande();
        for(Produit p : lignesCumulees.keySet()) {
            commandeNormalisee.ajouter(p, lignesCumulees.get(p));
        }
        return commandeNormalisee;
    }

    public Double cout(Function<Paire<Produit,Integer>,Double> calculLigne) {
        return normaliser().lignes.stream()
                .map(l -> calculLigne.apply(l))
                .reduce(0.0, (x,y) -> x+y);       
    }

    public String affiche(Function<Paire<Produit, Integer>, Double> calculLigne) {
        Commande c = this.normaliser();
        final String HLINE = "+------------+------------+-----+------------+--------+------------+\n";
        StringBuilder str = new StringBuilder();
        str.append("\n\nCommande\n");
        str.append(HLINE);
        str.append("+ nom        + prix       + qté + prix ht    + tva    + prix ttc   +\n");
        str.append(HLINE);
        for (Paire<Produit, Integer> ligne : c.lignes) {
            str.append(String.format("+ %10s + %10.2f + %3d + %10.2f + %5.2f%% + %10.2f +\n", ligne.getFst(), // nom
                    ligne.getFst().prix(), // prix unitaire
                    ligne.getSnd(), // qté
                    ligne.getFst().prix() * ligne.getSnd(), // prix ht
                    ligne.getFst().cat().tva() * 100, // tva
                    calculLigne.apply(ligne)));
        }
        str.append(HLINE);
        str.append(String.format("Total : %10.2f", c.cout(calculLigne)));
        return str.toString();
    }

}
