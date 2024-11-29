package bowling;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {
	TreeMap<String, PartieMonoJoueur> partieMonoJoueurs;

	/**
	 * Démarre une nouvelle partie pour un groupe de joueurs
	 *
	 * @param nomsDesJoueurs un tableau des noms de joueurs (il faut au moins un joueur)
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 1, boule n° 1"
	 * @throws java.lang.IllegalArgumentException si le tableau est vide ou null
	 */
	@Override
	public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
		partieMonoJoueurs = new TreeMap<String, PartieMonoJoueur>();
		if (nomsDesJoueurs.length == 0) {
			throw new IllegalArgumentException("Pas de joueur dans la partie");
		}
		for (String s : nomsDesJoueurs) {
			partieMonoJoueurs.put(s, new PartieMonoJoueur());
		}

		return this.getProchainLanceEtBoulePour(partieMonoJoueurs.firstKey());
	}

	/**
	 * Enregistre le nombre de quilles abattues pour le joueur courant, dans le tour courant, pour la boule courante
	 *
	 * @param nombreDeQuillesAbattues : nombre de quilles abattue à ce lancer
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 5, boule n° 2",
	 * ou bien "Partie terminée" si la partie est terminée.
	 * @throws java.lang.IllegalStateException si la partie n'est pas démarrée.
	 */
	@Override
	public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
		if (partieMonoJoueurs == null) {
			throw new IllegalArgumentException("La partie n'est pas démarrée");
		}
		if (lesPartiesSontTermine()) {
			return "Partie terminée";
		}
		partieMonoJoueurs.get(this.getProchainJoueur()).enregistreLancer(nombreDeQuillesAbattues);
		return getProchainLanceEtBoulePour(getProchainJoueur());
	}

	/**
	 * Donne le score pour le joueur playerName
	 *
	 * @param nomDuJoueur le nom du joueur recherché
	 * @return le score pour ce joueur
	 * @throws IllegalArgumentException si nomDuJoueur ne joue pas dans cette partie
	 */
	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		if (partieMonoJoueurs.get(nomDuJoueur) == null) {
			throw new IllegalArgumentException("Le joueur n'existe pas");
		}
		return partieMonoJoueurs.get(nomDuJoueur).score();
	}

	public String getProchainJoueur() {
		String nb = partieMonoJoueurs.firstKey();
		for (Map.Entry<String, PartieMonoJoueur> joueurEntry : partieMonoJoueurs.entrySet()) {
			if (joueurEntry.getValue().numeroTourCourant() < partieMonoJoueurs.get(nb).numeroTourCourant()) {
				nb = joueurEntry.getKey();
			}
		}
		return nb;
	}

	public String getProchainLanceEtBoulePour(String s) {
		return "Prochain tir : joueur " + s
			+ ", tour n° " + partieMonoJoueurs.get(s).numeroTourCourant()
			+ ", boule n° " + partieMonoJoueurs.get(s).numeroProchainLancer();
	}

	public Boolean lesPartiesSontTermine() {
		for (Map.Entry<String, PartieMonoJoueur> joueurEntry : partieMonoJoueurs.entrySet()) {
			if (joueurEntry.getValue().estTerminee()) {
				return true;
			}
		}
		return false;
	}
}