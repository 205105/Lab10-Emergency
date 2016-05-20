package it.polito.tdp.emergency.model;

import it.polito.tdp.emergency.db.FieldHospitalDAO;
import it.polito.tdp.emergency.simulation.Core;
import it.polito.tdp.emergency.simulation.Dottore;
import it.polito.tdp.emergency.simulation.Evento;


public class Model {

	Core simulatore;

	public static void main(String[] args) {
		Model m = new Model();
		m.stub();
	}

	protected void stub() {
		simulatore = new Core();
		//FieldHospitalDAO dao=new FieldHospitalDAO();

		//simulatore.setMediciDisponibili(1);

		/*simulatore.aggiungiPaziente(new Paziente(1, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(2, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(3, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(4, Paziente.StatoPaziente.ROSSO));

		simulatore.aggiungiEvento(new Evento(10, Evento.TipoEvento.PAZIENTE_ARRIVA, 1));
		simulatore.aggiungiEvento(new Evento(11, Evento.TipoEvento.PAZIENTE_ARRIVA, 2));
		simulatore.aggiungiEvento(new Evento(12, Evento.TipoEvento.PAZIENTE_ARRIVA, 3));
		simulatore.aggiungiEvento(new Evento(13, Evento.TipoEvento.PAZIENTE_ARRIVA, 4));*/
		
		simulatore.aggiungiDottore(new Dottore(1, false));
		simulatore.aggiungiDottore(new Dottore(2, false));
		simulatore.aggiungiDottore(new Dottore(3, false));
		simulatore.aggiungiDottore(new Dottore(4, false));
		
		simulatore.aggiungiEvento(new Evento(simulatore.getListaEventi().element().getTempo(), Evento.TipoEvento.DOCTOR_INIZIA_TURNO, 2001));
		simulatore.aggiungiEvento(new Evento(simulatore.getListaEventi().element().getTempo()+120, Evento.TipoEvento.DOCTOR_INIZIA_TURNO, 2002));
		simulatore.aggiungiEvento(new Evento(simulatore.getListaEventi().element().getTempo()+2*120, Evento.TipoEvento.DOCTOR_INIZIA_TURNO, 2003));
		simulatore.aggiungiEvento(new Evento(simulatore.getListaEventi().element().getTempo()+4*120, Evento.TipoEvento.DOCTOR_INIZIA_TURNO, 2004));
		//System.out.println(new Evento(simulatore.getListaEventi().element().getTempo(), Evento.TipoEvento.DOCTOR_INIZIA_TURNO, 2008));
		simulatore.simula();

		System.err.println("Persi:" + simulatore.getPazientiPersi());
		System.err.println("Salvati:" + simulatore.getPazientiSalvati());
	}

}
