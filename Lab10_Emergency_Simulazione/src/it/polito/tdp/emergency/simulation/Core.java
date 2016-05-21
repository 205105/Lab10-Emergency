//////////////////////////////////////////////////////////////////-*-java-*-//
//             // Classroom code for "Tecniche di Programmazione"           //
//   #####     // (!) Giovanni Squillero <giovanni.squillero@polito.it>     //
//  ######     //                                                           //
//  ###   \    // Copying and distribution of this file, with or without    //
//   ##G  c\   // modification, are permitted in any medium without royalty //
//   #     _\  // provided this notice is preserved.                        //
//   |   _/    // This file is offered as-is, without any warranty.         //
//   |  _/     //                                                           //
//             // See: http://bit.ly/tecn-progr                             //
//////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import it.polito.tdp.emergency.db.FieldHospitalDAO;

public class Core {
	public int getPazientiSalvati() {
		return pazientiSalvati;
	}

	public int getPazientiPersi() {
		return pazientiPersi;
	}

	public int getPazientiArrivati() {
		return pazientiArrivati;
	}

	int pazientiSalvati = 0;
	int pazientiPersi = 0;
	int pazientiArrivati = 0;

	FieldHospitalDAO dao=new FieldHospitalDAO();
	
	Queue<Evento> listaEventi = new PriorityQueue<Evento>(dao.getAllEventi());
	Map<Integer, Paziente> pazienti = new HashMap<Integer, Paziente>(dao.getAllPazienti());
	int mediciDisponibili = 0;
	Map<Integer, Assistente> assistenti = new HashMap<Integer, Assistente>();
	int assDisp=0;
	Queue<Paziente> pazientiInAttesa = new PriorityQueue<Paziente>();
	Map<Integer, Dottore> dottori = new HashMap<Integer, Dottore>();
	
	public int getMediciDisponibili() {
		return mediciDisponibili;
	}

	public void setMediciDisponibili(int mediciDisponibili) {
		this.mediciDisponibili = mediciDisponibili;
	}

	public void aggiungiEvento(Evento e) {
		listaEventi.add(e);
	}

	public void aggiungiPaziente(Paziente p) {
		pazienti.put(p.getId(), p);
	}
	
	public void aggiungiDottore(Dottore d) {
		dottori.put(d.getId(), d);
	}
	
	public void aggiungiAssistente(Assistente a) {
		assistenti.put(a.getId(), a);
	}
	public long passo() {
		Evento e = listaEventi.remove();
		switch (e.getTipo()) {
		case PAZIENTE_ARRIVA:
			++pazientiArrivati;
			System.out.println("Arrivo paziente:" + e);
			pazientiInAttesa.add(pazienti.get(e.getDato()));
			switch (pazienti.get(e.getDato()).getStato()) {
			case BIANCO:
				//this.aggiungiEvento(new Evento(e.getTempo() + 20 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case GIALLO:
				this.aggiungiEvento(new Evento(e.getTempo() + 6 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case ROSSO:
				this.aggiungiEvento(new Evento(e.getTempo() + 1 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case VERDE:
				this.aggiungiEvento(new Evento(e.getTempo() + 12 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			default:
				System.err.println("Panik!");
			}
			break;
		case PAZIENTE_GUARISCE:
			if (pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.NERO) {
				System.out.println("Paziente salvato: " + e);
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.SALVO);
				++mediciDisponibili;
				++pazientiSalvati;
			}
			break;
		case PAZIENTE_GUARISCE_ASS:
			if (pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.NERO) {
				System.out.println("Paziente salvato dall'assistente: " + e);
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.SALVO);
				++assDisp;
				++pazientiSalvati;
			}
			break;
		case PAZIENTE_MUORE:
			if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.SALVO) {
				//System.out.println("Paziente già salvato: " + e);
			} else { 
				if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.IN_CURA) {
					++mediciDisponibili;
				}
				if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.IN_CURA_ASS) {
					++assDisp;
				}
				++pazientiPersi;
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.NERO);
				System.out.println("Paziente morto: " + e);
			}
			break;
		case DOCTOR_INIZIA_TURNO:
			++mediciDisponibili;
			dottori.get(e.getDato()).setSDisponibile(true);
			System.out.println("Dottore inizia turno: " +e);
			this.aggiungiEvento(new Evento(e.getTempo() + 480, Evento.TipoEvento.DOCTOR_FINE_TURNO, e.getDato()));
			break;
		case DOCTOR_FINE_TURNO:
			--mediciDisponibili;
			System.out.println("Dottore finisce turno: " +e);
			this.aggiungiEvento(new Evento(e.getTempo() + 960, Evento.TipoEvento.DOCTOR_INIZIA_TURNO, e.getDato()));
			dottori.get(e.getDato()).setSDisponibile(false);
			break;
		case ASS_INIZIA_TURNO:
			++assDisp;
			assistenti.get(e.getDato()).setDisponibile(true);
			System.out.println("Assistente inizia turno: " +e);
			this.aggiungiEvento(new Evento(e.getTempo() + 480, Evento.TipoEvento.ASS_FINE_TURNO, e.getDato()));
			if(!pazientiInAttesa.isEmpty()){
				
			}
			break;
		case ASS_FINE_TURNO:
			--assDisp;
			assistenti.get(e.getDato()).setDisponibile(false);
			System.out.println("Assistente finisce turno: " +e);
			this.aggiungiEvento(new Evento(e.getTempo() + 960, Evento.TipoEvento.ASS_INIZIA_TURNO, e.getDato()));
			break;
		default:
			System.err.println("Panik!");
		}

		while (cura(e.getTempo()));
			
		return e.getTempo();
	}

	protected boolean cura(long adesso) {
		if (pazientiInAttesa.isEmpty())
			return false;
		
		if(mediciDisponibili==0 && assDisp==0)
			return false;
		
		Paziente p = pazientiInAttesa.remove();
		if(assDisp!=0 && p.getStato()!= Paziente.StatoPaziente.ROSSO && p.getStato() != Paziente.StatoPaziente.NERO){
			--assDisp;
			pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA_ASS);
			this.aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE_ASS, p.getId()));
			System.out.println("Inizia a curare assistente: " + p);
			return true;
		}
		if (mediciDisponibili == 0)
			return false;
		
		if (p.getStato() != Paziente.StatoPaziente.NERO) {
			--mediciDisponibili;
			pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA);
			this.aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE, p.getId()));
			System.out.println("Inizio a curare: " + p);
		}
		return true;
	}
	

	public Queue<Evento> getListaEventi() {
		return listaEventi;
	}

	public void simula() {
		long prova=this.getListaEventi().element().getTempo()+60*150;
		while (prova>passo()) { 
			passo();
		}
	}
}
