package fr.exercice1;

import fr.Paire;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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
        /*StringBuilder str = new StringBuilder();
        str.append("Commande\n");
        for (Paire<Produit, Integer> ligne : lignes) {
            str.append(String.format("%s x%d\n", ligne.getFst(), ligne.getSnd()));
        }
        return str.toString();*/

        return lignes.stream()
                .map(ligne -> formatteurLigne.apply(ligne))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Cumule les lignes en fonction des produits
     */
    public Commande normaliser() {
        Map<Produit,Integer> lignesCumulees = new HashMap<>();
        for(Paire<Produit,Integer> ligne : lignes) {
            Produit p = ligne.fst();
            int qte = ligne.snd();
            if(lignesCumulees.containsKey(ligne.fst())) {
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

    public <T, U> Map<T, List<U>> regrouper(List<Paire<T, U>> ligne) {
        Map<T, List<U>> resultat = new HashMap<>();
        ligne.forEach(
                l -> resultat.computeIfAbsent(l.fst(), a -> new ArrayList<>()).add(l.snd())
        );
        return resultat;
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
            str.append(String.format("+ %10s + %10.2f + %3d + %10.2f + %5.2f%% + %10.2f +\n", ligne.fst(), // nom
                    ligne.fst().prix(), // prix unitaire
                    ligne.snd(), // qté
                    ligne.fst().prix() * ligne.snd(), // prix ht
                    ligne.fst().cat().tva() * 100, // tva
                    calculLigne.apply(ligne)));
        }
        str.append(HLINE);
        str.append(String.format("Total : %10.2f", c.cout(calculLigne)));
        return str.toString();
    }

    /**
     * Une ligne de commande
     */
    public Function<Paire<Produit, Integer>, String> formatteurLigne = ligne ->
            String.format("%s %d", ligne.fst(), ligne.snd());
}
