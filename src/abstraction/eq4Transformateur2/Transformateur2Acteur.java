package abstraction.eq4Transformateur2; 

import abstraction.eq8Romu.appelsOffres.FiliereTestAO;
import abstraction.eq8Romu.appelsOffres.IVendeurAO;
import abstraction.eq8Romu.appelsOffres.PropositionAchatAO;
import abstraction.eq8Romu.appelsOffres.SuperviseurVentesAO;
import abstraction.eq8Romu.bourseCacao.FiliereTestBourse;
import abstraction.eq8Romu.clients.FiliereTestClientFinal;
import abstraction.eq8Romu.contratsCadres.FiliereTestContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.filiere.IFabricantChocolatDeMarque;
import abstraction.eq8Romu.filiere.IMarqueChocolat;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Transformateur2Acteur implements IActeur,IMarqueChocolat, IFabricantChocolatDeMarque {
	
	
	
	protected Variable prixSeuil; // au dela duquel nous n'achetons pas
	//private Variable capaciteStockageFixe;// stock que l'on souhaite en permanence
	
	private Stock<Feve> stockReferenceFeve; //Le stock referent de feve, celui vers lequel on essaye de retourner à chaque etape
	private Stock<ChocolatDeMarque> stockReferenceChocolat;//Idem pour choco
	private double marge;
	
	protected SuperviseurVentesAO superviseur;
	protected Journal journal;
	protected int cryptogramme;
	protected double NewCap;//à réinitialiser=cpacité de production au début de chaque tour
	



	

	
	
	//Nawfel
	public Transformateur2Acteur() {
	
		this.prixSeuil = new Variable("prix seuil", "<html>Prix Seuil</html>",this, 0.0, 10000000, 10);
		//this.capaciteStockageFixe=new Variable("stock theorique desire", "<html>Stock Theorique désiré en permanence</html>",this, 0.0, 1000000.0, 8000);
		this.marge = 1.1;
		this.journal=new Journal("Opti'Cacao activités", this);
		//On crée notre stock referent, qui servira juste de guide pour savoir combien acheter/transformer à chaque tour.
		this.stockReferenceFeve=new Stock();
		this.stockReferenceFeve.ajouter(Feve.FEVE_BASSE, 8000);
		this.stockReferenceFeve.ajouter(Feve.FEVE_MOYENNE, 5000);
		ChocolatDeMarque c1=new ChocolatDeMarque(Chocolat.MQ,this.getMarquesChocolat().get(1));
		ChocolatDeMarque c0=new ChocolatDeMarque(Chocolat.BQ,this.getMarquesChocolat().get(0));
		this.stockReferenceChocolat=new Stock();
		this.stockReferenceChocolat.ajouter(c1, 5000);
		this.stockReferenceChocolat.ajouter(c0, 8000);
		
		
		
		



	}
	

	public void initialiser() {
		
		
	}
	
	public String getNom() {
		return "Opti'Cacao";
	}

	public String getDescription() {
		return "Aux petits soins pour vous";
	}

	public Color getColor() {
		return new Color(230, 126, 34);
	}
	

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}
	
	public void next() {
			
	}
	
	
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filiere = new ArrayList<String>();
		filiere.add("TESTAOOPTI'CACAO");  
		filiere.add("TESTCCOPTI'CACAO"); 
		return filiere;
	}

	public Filiere getFiliere(String nom) {
		switch (nom) { 
		case "TESTAOOPTI'CACAO" : return new CopieFiliereTestAO();
		case "TESTCCOPTI'CACAO" : return new CopieFiliereTestContratCadre();
	    default : return null;
		}
	}
	
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
		
	}
	
	public List<Variable> getParametres() { 
		List<Variable> p= new ArrayList<Variable>();
//		p.add(this.expirationChoco);
		return p;
	} 
	

	public List<Journal> getJournaux() {
		List<Journal> j= new ArrayList<Journal>();
		j.add(this.journal);
		return j;
	}
	

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
		System.out.println("I'll be back... or not... "+this.getNom());
		} else {
			System.out.println("Poor "+acteur.getNom()+"... We will miss you. "+this.getNom());
		}
	}
	
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
	}


	public double getMarge() {
		return this.marge;
	}


	public int getCryptogramme() {
		return cryptogramme;
	}



	public Variable getPrixSeuil() {
		return prixSeuil;
	}









	@Override
	public LinkedList<String> getMarquesChocolat() {
//		LinkedList<String> res = new LinkedList<String>();
//		res.add("O'ptella");
////		res.add("O'ptibon");
//		res.add("O'max");
		return this.getMarquesChocolat();
	}


	@Override
	public LinkedList<ChocolatDeMarque> getChocolatsProduits() {
		LinkedList<ChocolatDeMarque> res= new LinkedList<ChocolatDeMarque>();
		ChocolatDeMarque c1=new ChocolatDeMarque(Chocolat.MQ,this.getMarquesChocolat().get(1));
		ChocolatDeMarque c0=new ChocolatDeMarque(Chocolat.BQ,this.getMarquesChocolat().get(0));
		res.add(c0);
		res.add(c1);
		return res;
	}


	public Stock<Feve> getStockReferenceFeve() {
		return stockReferenceFeve;
	}


	public Stock<ChocolatDeMarque> getStockReferenceChocolat() {
		return stockReferenceChocolat;
	}

}