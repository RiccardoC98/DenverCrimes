package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getAllCategories() {
		String sql = "SELECT DISTINCT offense_category_id FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
		
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add( res.getString("offense_category_id"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getOffenseTypes(String categoria, Month mese) {
		String sql = "SELECT DISTINCT offense_type_id FROM events WHERE offense_category_id = ? " +
				"AND MONTH(reported_date) = ? ORDER BY offense_type_id ASC ";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
		
			st.setString(1, categoria);
			st.setInt(2, mese.getValue());
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add( res.getString("offense_type_id"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Arco> getEdges(List<String> offense_types) {
		List<Arco> list = new ArrayList<>() ;
		for (String t1 : offense_types) {
			// evito di prendere le coppie due volte
			for (String t2 : offense_types) {
				if (t2.compareTo(t1) != 0) {
					String sql = " SELECT count( DISTINCT neighborhood_id ) AS weight " +
						" FROM events e  " +
						" WHERE e.offense_type_id = ? " +
						" AND neighborhood_id IN " +
						" ( " +
						" SELECT neighborhood_id " +
						" FROM events e " +
						" WHERE e.offense_type_id = ? " +
						" ) ";
					try {
						Connection conn = DBConnect.getConnection() ;
						PreparedStatement st = conn.prepareStatement(sql) ;
						
						st.setString(1, t1);
						st.setString(2, t2);
						
						
						
						ResultSet res = st.executeQuery() ;
						
						while(res.next()) {
							list.add( new Arco(t1, t2, res.getInt("weight")) );
						}
					
						conn.close();
		
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null ;
					}
				}
			}
		}
		return list ;
	}

}
