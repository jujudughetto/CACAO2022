package abstraction.eq7Distributeur2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.contratsCadres.ContratCadre;
import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.IAcheteurContratCadre;
import abstraction.eq8Romu.contratsCadres.IVendeurContratCadre;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.produits.ChocolatDeMarque;

public class Distributeur2Achat extends Distributeur2Acteur implements IAcheteurContratCadre{
	public static final int EPS_ECH_OK=2;
	public static final int ECH_MAX=5;
	public static final Double PRIX_MAX=100.0;
	public static final Double PRIX_OK=50.0;
	public static final Double EPSILON_PRIX=5.0;
	
	protected List<ExemplaireContratCadre> mesContratEnTantQuAcheteur;
	
	public Distributeur2Achat() {
		super();
		this.mesContratEnTantQuAcheteur=new LinkedList<ExemplaireContratCadre>();
	}
	
	
	//Edgar & Matteo
	//A chaque étape, on créer un contrat cadre pour acheter un produit dont le stock est inférieur au seuil
	//On réalise alors des contrats avec tous les vendeurs qui le propose afin de voir quel est leur prix
	//On compare ces prixs et on réalise finalement le contrat avec le meilleur vendeur.
	//On réalise une moyenne des achats du client final sur les 6 steps précédents pour determiner la quantité achetée par step
	//On compare notre stock au seuil limite pour determiner la quantité à acheter pour remettre au seuil
	//Pour qu'un produit soit en boolTeteGondole, il doit être bioéquitable
	//On réalise un contrat pour chacun des différents chocolatss produits afin de tous les avoir en stock
	//Finalement, on fait un contrat avec le vendeur le plus offrant
	@Override
	public void next() {
		//Initialisation du superviseur de vente
		SuperviseurVentesContratCadre SupVente = ((SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre")));
		
		
		//Pour chaque chocolat produit sur le marché
		for (ChocolatDeMarque chocProduit : Filiere.LA_FILIERE.getChocolatsProduits()) {
			
			//On pose le chocolat en tête de gondole si il est bio et équitable
			boolean boolTeteGondole = chocProduit.isBioEquitable();
			
			//On initialise une liste des prix
			List <Double> prix = new ArrayList<Double>();
			
			int currentEtape = Filiere.LA_FILIERE.getEtape();
			
			//Si la quantite du chocolat en question est inférieure au seuil auquel on a décidé d'en racheter, alors on va en racheter
			if (stock.getQuantite(chocProduit)<=stock.getSeuilRachat(chocProduit)) {
				
				int index = 0;
				
				//Retourne le volume le plus judicieux à acheter selon le nombre d'étape sur lequel on reparti le contrat
				double venteParStep = this.volumeParEtapeMoyenne(chocProduit, currentEtape, 10);
				int nbStepContrat = 10;
				
				//On créer un écheancier correspondant à nos besoin
				Echeancier echeancierAchat = new Echeancier(currentEtape,nbStepContrat,venteParStep);
				
				//On récupère tout les vendeurs actifs
				List<IVendeurContratCadre> vendeurs = SupVente.getVendeurs(chocProduit);
				
				//Pour chaque vendeur
				for (IVendeurContratCadre vendeur : vendeurs) {
					journal.ajouter("BioFour propose un CC avec "+ vendeur +" avec le produit: "+chocProduit);
					
				}
				
				IVendeurContratCadre vendeur= vendeurs.get(index);
				ContratCadre contrat = new ContratCadre((IAcheteurContratCadre)this, vendeur, chocProduit, echeancierAchat, this.cryptogramme, boolTeteGondole);
				journal.ajouter("-->aboutit au contrat "+contrat);
			}
			this.actualiserIndicateurs();
			}
		}
	
		//Matteo
		public double volumeParEtapeMoyenne(ChocolatDeMarque chocProduit,int currentEtape,int nbEtape) {
			double ventes = 0.0;
			//On ajoute les quantités vendues à chaque étape depuis nbStep
			for (int j=1; j<=10; j++) {
				ventes+=Filiere.LA_FILIERE.getVentes(chocProduit, currentEtape-j);
			}
			return ventes/nbEtape;
			
		}
		

		@Override
		//edgard
		public boolean achete(Object produit) {
			// TODO Auto-generated method stub
			if (produit instanceof ChocolatDeMarque && stock.getQuantite((ChocolatDeMarque)produit)<stock.getSeuilRachat((ChocolatDeMarque)produit)) {
				return true;
			}else {
				return false;
			}
		}

		@Override
		//Edgar & Matteo
		public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat){
			
			//On récupère le dernier écheancier négocié
			Echeancier lastEcheancier = contrat.getEcheancier();
			
			//On récupère le chocolat concerné
			ChocolatDeMarque chocProduit = (ChocolatDeMarque)contrat.getProduit();
			
			//L'étape actuelle
			int currentEtape = Filiere.LA_FILIERE.getEtape();
			

			//Retourne le volume le plus judicieux à acheter selon le nombre d'étape sur lequel on reparti le contrat
			double venteParStep = this.volumeParEtapeMoyenne(chocProduit, currentEtape, 10);
			int nbStepContrat = 10;
			
			//On créer un écheancier correspondant à nos besoin
			Echeancier echeancierAchat = new Echeancier(currentEtape,nbStepContrat,venteParStep);
			
			if (lastEcheancier.getStepFin()>ECH_MAX) {
				return null;
			}else {
				if (lastEcheancier.equals(echeancierAchat)) {
					return lastEcheancier;
				}else {
					if(lastEcheancier.getStepFin()>echeancierAchat.getStepFin()) {
						return new Echeancier(lastEcheancier.getStepDebut(),lastEcheancier.getNbEcheances()-EPS_ECH_OK,lastEcheancier.getQuantiteTotale()/(lastEcheancier.getNbEcheances()-EPS_ECH_OK));
					}else {
						return null;
					}
				}
			}
		}

		@Override
		public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
			double prix = contrat.getPrix();
			if (prix>PRIX_MAX) {
				return -1;
			}else {
				if(Math.abs(prix-EPSILON_PRIX)==PRIX_OK) {
					return prix;
				}else {
					return prix-EPSILON_PRIX;
				}
			}
		}

		@Override
		public void receptionner(Object produit, double quantite, ExemplaireContratCadre contrat) {
			// TODO Auto-generated method stub
			this.stock.addProduit((ChocolatDeMarque)produit, quantite);
		}

		@Override
		public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
			// TODO Auto-generated method stub
			IVendeurContratCadre v = contrat.getVendeur();
			IAcheteurContratCadre a = contrat.getAcheteur();
			Echeancier currentEtape = contrat.getEcheancier();
			ChocolatDeMarque chocProduit = (ChocolatDeMarque) contrat.getProduit();
			Double q = contrat.getQuantiteTotale();
			System.out.println("Nouveau contrat cadre entre "+ v + "et"+ a + "pour une quantitée" + q + "de" + chocProduit + "étalé sur " + currentEtape);
		}
}