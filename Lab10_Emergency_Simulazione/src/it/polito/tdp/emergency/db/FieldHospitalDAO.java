////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Field Hospital DAO                                          //
//   #     _\  // Test with MariaDB 10 on win                                 //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import it.polito.tdp.emergency.simulation.Evento;
import it.polito.tdp.emergency.simulation.Evento.TipoEvento;
import it.polito.tdp.emergency.simulation.Paziente;
import it.polito.tdp.emergency.simulation.Paziente.StatoPaziente;

public class FieldHospitalDAO {

	public Map<Integer, Paziente> getAllPazienti() {
		final String sql = "SELECT id, triage, name FROM view_arrivals ";
		Map<Integer, Paziente> pazienti = new HashMap<Integer, Paziente>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String stato=rs.getString("triage");
				switch(stato){
				case "White":
					Paziente p = new Paziente(rs.getInt("id"), StatoPaziente.BIANCO, rs.getString("name"));
					pazienti.put(rs.getInt("id"), p);
					break;
				case "Green":
					Paziente p1 = new Paziente(rs.getInt("id"), StatoPaziente.VERDE, rs.getString("name"));
					pazienti.put(rs.getInt("id"), p1);
					break;
				case "Yellow":
					Paziente p2 = new Paziente(rs.getInt("id"), StatoPaziente.GIALLO, rs.getString("name"));
					pazienti.put(rs.getInt("id"), p2);
					break;
				case "Red":
					Paziente p3 = new Paziente(rs.getInt("id"), StatoPaziente.ROSSO, rs.getString("name"));
					pazienti.put(rs.getInt("id"), p3);
					break;
				default:
					System.err.println("Panik!");
			}
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return pazienti;
	}
	
	public Queue<Evento> getAllEventi() {
		final String sql = "SELECT timestamp, patient FROM arrivals";
		Queue<Evento> eventi = new PriorityQueue<Evento>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Timestamp t=rs.getTimestamp(1);
				Evento e=new Evento(t.getTime()/60000, TipoEvento.PAZIENTE_ARRIVA, rs.getInt("patient"));
				eventi.add(e);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return eventi;
	}
	
	public long inizioSimulazione(){
		
		final String sql = "select timestamp from arrivals where timestamp='1991-08-25 00:02:23'";
		Timestamp t=null;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				t=rs.getTimestamp(1);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return t.getTime()/60000;
	}
}
