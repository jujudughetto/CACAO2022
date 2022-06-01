package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.IVendeurContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;

public class Producteur1ContratCadre extends Producteur1Transfo implements IVendeurContratCadre {
	
	protected List<ExemplaireContratCadre> mesContratEnTantQueVendeur;
	private Journal contratCadre;

	//Auteur : Khéo (sur toute la partie)
	// Pour l'instant selon ma seule décision, le but d'un condrat cadre sera d'écouler les stocks non vendu. Donc on va partir sur la vente
	//d'une quantité de 100000 tonnes à un prix pouvant aller jusuqu'a 25% inférieur au prix moyen de la bourse
	public Producteur1ContratCadre() {	
		super();
		this.mesContratEnTantQueVendeur=new LinkedList<ExemplaireContratCadre>();
		this.contratCadre = new Journal(this.getNom()+" ContratCadre", this);
	}
	
	@Override
	//Auteur : Khéo
	public boolean vend(Object produit) {
		
		//FEVE
		if(produit instanceof Feve) {
			if(this.getStock((Feve)produit, false)>100000) { //On peut initier la vente si on a les bonnes quantités
				return true;
			}
		}
		
		if(produit instanceof Chocolat) {
			if(this.getStock((Chocolat)produit)>0) { //On peut initier la vente si on a les bonnes quantités
				return true;
			}
		}
	

		return false;
	}

	@Override
	//Auteur : Khéo
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		this.getContratCadre().ajouter("============================================");
		this.getContratCadre().ajouter("ACHETEUR " + contrat.getAcheteur().getNom().toString() + " // PRODUIT " + contrat.getProduit().toString());
		this.getContratCadre().ajouter("Premier échéancier proposée " + contrat.getEcheancier());
		
		
		
		if(contrat.getProduit() instanceof Feve) {
		if (contrat.getEcheancier().getQuantiteTotale()<this.getStock((Feve)contrat.getProduit(), false)) {
			if (contrat.getEcheancier().getQuantiteTotale()<100000) {
				Echeancier newcontrat = contrat.getEcheancier();
				newcontrat.ajouter(100000-newcontrat.getQuantiteTotale());
				this.getContratCadre().ajouter("On ajoute une quantité pour un nouveau contrat");
				this.getContratCadre().ajouter("Nouvelle échéancier " + newcontrat.toString());
				return newcontrat;
			} else { //Quantité demandé acceptable
				this.getContratCadre().ajouter("Quantité demandé acceptable");
				this.getContratCadre().ajouter("L'échéancier du contrat est " + contrat.getEcheancier().toString());
				return contrat.getEcheancier(); 
			}
			
		} else { //Pas assez de quantité dans le stock présent
			this.getContratCadre().ajouter("Pas assez de quantité dans le stock présent de " + contrat.getProduit().toString());
			return null;
		}
		}
		
		if(contrat.getProduit() instanceof Chocolat) {
			if (contrat.getEcheancier().getQuantiteTotale()<this.getStock((Chocolat)contrat.getProduit())) {
				if (contrat.getEcheancier().getQuantiteTotale()<1000) {
					Echeancier newcontrat = contrat.getEcheancier();
					newcontrat.ajouter(1000-newcontrat.getQuantiteTotale());
					this.getContratCadre().ajouter("On ajoute une quantité pour un nouveau contrat");
					this.getContratCadre().ajouter("Nouvelle échéancier " + newcontrat.toString());
					return newcontrat;
				} else { //Quantité demandé acceptable
					this.getContratCadre().ajouter("Quantité demandé acceptable");
					this.getContratCadre().ajouter("L'échéancier du contrat est " + contrat.getEcheancier().toString());
					return contrat.getEcheancier(); 
				}
				
			} else { //Pas assez de quantité dans le stock présent
				this.getContratCadre().ajouter("Pas assez de quantité dans le stock présent de " + contrat.getProduit().toString());
				return null;
			}
			}
		
		this.getContratCadre().ajouter("On ne propose pas ce produit");
		return null;
		
		
	}

	@Override
	//Auteur : Khéo
	public double propositionPrix(ExemplaireContratCadre contrat) {
		//FEVE
		if(contrat.getProduit() instanceof Feve) {
		if (this.getPrixmoyenFeve().keySet().contains(contrat.getProduit())) {
			this.getContratCadre().ajouter("Prix proposé " + this.getPrixmoyenFeve().get(contrat.getProduit())/Filiere.LA_FILIERE.getEtape() );
			return this.getPrixmoyenFeve().get(contrat.getProduit())/Filiere.LA_FILIERE.getEtape();
		} else {

			return 0.0;
		}
		}
		
		
		//CHOCOLAT 
		
		if(contrat.getProduit() instanceof Chocolat) {
			
			if (this.getPrixmoyenFeve().keySet().contains(this.getFev((Chocolat)contrat.getProduit()))) {
				this.getContratCadre().ajouter("Prix proposé " +3*this.getPrixmoyenFeve().get(this.getFev((Chocolat)contrat.getProduit()))/Filiere.LA_FILIERE.getEtape() );
				return this.getPrixmoyenFeve().get(this.getFev((Chocolat)contrat.getProduit()))/Filiere.LA_FILIERE.getEtape();
			} else {
				return 0.0;
			}
			}
		
		return 0.0;
		
	}

	@Override
	//Auteur : Khéo
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		//FEVE
		if (contrat.getProduit() instanceof Feve) {
		double prixmoyen = this.getPrixmoyenFeve().get(contrat.getProduit())/Filiere.LA_FILIERE.getEtape();
		if(contrat.getPrix()<prixmoyen*0.75) {
			this.getContratCadre().ajouter("Prix qui passe pas " + contrat.getPrix().toString());
			this.getContratCadre().ajouter("Notre prix " + prixmoyen*0.75 );
			return prixmoyen*0.75;	
		}
		this.getContratCadre().ajouter("Prix qui passe " + contrat.getPrix().toString());
		return contrat.getPrix();
	}
		
		
		
		//CHOCOLAT
		if (contrat.getProduit() instanceof Chocolat) {
			double prixmoyen = this.getPrixmoyenFeve().get(contrat.getProduit())/Filiere.LA_FILIERE.getEtape();
			if(contrat.getPrix()<prixmoyen*0.75) {
				this.getContratCadre().ajouter("Prix qui passe pas " + contrat.getPrix().toString());
				this.getContratCadre().ajouter("Notre prix " + prixmoyen*0.75 );
				return prixmoyen*0.75;	
			}
			this.getContratCadre().ajouter("Prix qui passe " + contrat.getPrix().toString());
			return contrat.getPrix();
		}
		
		return 0.0;
	
	}
	

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.getContratCadre().ajouter( " NOUVEAU CONTRAT " + contrat.getProduit().toString()+ " "  + contrat.getAcheteur().getNom()
				+ " PRIX : " + contrat.getPrix());
	}



	@Override
	//Copié sur le code de l'équipe 8 on veut livrer le plus possible dans tout les cas 
	public double livrer(Object produit, double quantite, ExemplaireContratCadre contrat) {
		//FEVE 
		if (contrat.getProduit() instanceof Feve) {
		double livre = Math.min(this.getStock((Feve)contrat.getProduit(), false), quantite);
		if (livre>0.0) {
			this.retirerQuantite((Feve)contrat.getProduit(), livre);;
		}
		this.getContratCadre().ajouter("============================================");
		this.getContratCadre().ajouter("LIVRAISON " + contrat.getProduit() +" "+ contrat.getAcheteur().getNom()+ " Quantité demandée " + quantite +" Livré " + livre 
				);
		return livre;
		}
		
		//FEVE 
		if (contrat.getProduit() instanceof Feve) {
		double livre = Math.min(this.getStock((Chocolat)contrat.getProduit()), quantite);
		if (livre>0.0) {
			this.retirerQuantite((Chocolat)contrat.getProduit(), livre);;
		}
		this.getContratCadre().ajouter("============================================");
		this.getContratCadre().ajouter("LIVRAISON " + contrat.getProduit() +" "+contrat.getAcheteur().getNom()+ " Quantité demandée " + quantite +" Livré " + livre
				);
		return livre;
		}
		
		return 0.0;
		

		
	}
	
	/**
	 * @return the contratCadre
	 */
	public Journal getContratCadre() {
		return contratCadre;
	}
	

	
	
	public Feve getFev(Chocolat c) {
		if (c.getGamme() == Gamme.HAUTE) {
			if (c.isBioEquitable()) {
				return Feve.FEVE_HAUTE_BIO_EQUITABLE;
			}
			return Feve.FEVE_HAUTE;
		}
		
		else if (c.getGamme() == Gamme.MOYENNE) {
			if (c.isBioEquitable()) {
				return Feve.FEVE_MOYENNE_BIO_EQUITABLE;
			}
			return Feve.FEVE_MOYENNE;
		}
		
		else if (c.getGamme() == Gamme.BASSE) {
			return Feve.FEVE_BASSE;
	}
	
		return null;
}
}
