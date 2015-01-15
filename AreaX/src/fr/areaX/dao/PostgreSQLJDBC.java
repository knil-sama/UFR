package fr.areaX.dao;
import java.sql.Connection;

import org.json.*;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQLJDBC {

	private Connection connectToDatabase() {
		Connection c = null;
		try {
			// Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/test", "test", "test");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		return c;
	}

	public boolean emptyDatabase() {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			String sql = "TRUNCATE users, log, cards,session, card_owner CASCADE";
			commande.executeUpdate(sql);
			commande.close();
			// c.commit();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean createCard() {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			String sql = "INSERT INTO cards (id_card) VALUES (DEFAULT)";
			commande.executeUpdate(sql);
			commande.close();
			// c.commit();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			return false;
		}
		return true;
	}
	public boolean createCard(int id) {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			String sql = "INSERT INTO cards (id_card) VALUES ("+id+")";
			commande.executeUpdate(sql);
			commande.close();
			// c.commit();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			return false;
		}
		return true;
	}

	public boolean createUser(int id) {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			String sql = "INSERT INTO cards (id_card) VALUES ("+id+")";
			commande.executeUpdate(sql);
			commande.close();
			// c.commit();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			return false;
		}
		return true;
	}
/**
 * 
 * @param first_name
 * @param last_name
 * @param birthdate
 * @param histo
 * @return
 */
	public boolean createUser(String first_name, String last_name, Date birthdate,
			JSONObject histo) {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			// date format YYYY-MM-DD
			String sql = "INSERT INTO users (id_user, first_name, last_name,birthdate, histogram) VALUES (DEFAULT,'"+first_name+"','"+last_name+"','"+ birthdate.toString() +"','"+histo.toString()+"')";
			commande.executeUpdate(sql);
			commande.close();
			// c.commit();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			return false;
		}
		return true;
	}


}
