package fr.areaX.dao;

import java.sql.Connection;

import org.json.*;

import fr.areaX.biometry.Histogramme;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

	public boolean resetTable() {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			// CARDS
			String sql = "DROP TABLE IF EXISTS cards CASCADE";
			commande.executeUpdate(sql);
			sql = "CREATE TABLE cards(" + "id_card serial NOT NULL,"
					+ "active boolean NOT NULL DEFAULT true,"
					+ "CONSTRAINT cards_pkey PRIMARY KEY (id_card))"
					+ "WITH (OIDS=FALSE);";
			commande.execute(sql);
			// USERS
			sql = "DROP TABLE IF EXISTS users CASCADE";
			commande.executeUpdate(sql);
			sql = "CREATE TABLE users(" + "id_user serial NOT NULL,"
					+ "first_name text," + "last_name text,"
					+ "birthdate date," + "histogram json,"
					+ "CONSTRAINT users_pkey PRIMARY KEY (id_user))"
					+ "WITH (OIDS=FALSE);";
			commande.executeUpdate(sql);
			// SESSION
			sql = "DROP TABLE IF EXISTS session CASCADE";
			commande.executeUpdate(sql);
			sql = "CREATE TABLE session(" + "id_session serial NOT NULL,"
					+ "token text," + "time_creation timestamp with time zone,"
					+ "time_end timestamp with time zone,"
					+ "\"user\" integer," + "salt text,"
					+ "CONSTRAINT session_pkey PRIMARY KEY (id_session))"
					+ "WITH (OIDS=FALSE);";
			commande.executeUpdate(sql);
			// CARTE OWNER
			sql = "DROP TABLE IF EXISTS card_owner CASCADE";
			commande.executeUpdate(sql);
			sql = "CREATE TABLE card_owner(id_card_owner serial NOT NULL,"
					+ "card integer," + "\"user\" integer,"
					+ "\"time\" timestamp with time zone,"
					+ "CONSTRAINT card_owner_pkey PRIMARY KEY (id_card_owner),"
					+ "CONSTRAINT card_owner_card_fkey FOREIGN KEY (card)"
					+ "REFERENCES cards (id_card) MATCH SIMPLE "
					+ "ON UPDATE NO ACTION ON DELETE NO ACTION)"
					+ "WITH (OIDS=FALSE);";
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
			String sql = "INSERT INTO cards (id_card) VALUES (" + id + ")";
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
			String sql = "INSERT INTO cards (id_card) VALUES (" + id + ")";
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
	public boolean createUser(String first_name, String last_name,
			Date birthdate, JSONObject histo) {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			// date format YYYY-MM-DD
			String sql = "INSERT INTO users (id_user, first_name, last_name,birthdate, histogram) VALUES (DEFAULT,'"
					+ first_name
					+ "','"
					+ last_name
					+ "','"
					+ birthdate.toString() + "','" + histo.toString() + "')";
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

	public boolean verifyToken(String identifiant_card, String token) {
		Connection c = connectToDatabase();
		try {
			/*
			 * Statement commande = c.createStatement(); String[] content =
			 * cardContent.toString().split("#"); String identifiant =
			 * content[0]; String Token = content[1]; //String sql =
			 * "SELECT time_creation, time_end from session where token="
			 * +Byte.valueOf(Token)+"; if(commande.execute(sql)){
			 * commande.getResultSet(). } commande.close();
			 */
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param histogram
	 * @return a list of user id because the comparison can't be 100% accurate
	 */
	@SuppressWarnings("resource")
	public ArrayList<Integer> findUsersByBiometric(JSONObject histogram) {
		ArrayList<Integer> users = new ArrayList<Integer>();
		Histogramme histo_tested = new Histogramme(histogram.toString());
		Connection c = connectToDatabase();
		int id_user;
		String histo_string;
		try {
			Statement commande = c.createStatement();
			String sql = "SELECT id_user, histogram FROM users";
			// String sql =
			// "SELECT time_creation, time_end from session where token="+Byte.valueOf(Token)+";
			if (commande.execute(sql)) {
				ResultSet result = commande.getResultSet();
				while (result != null) {
					id_user = result.getInt(0);
					histo_string = result.getString(1);
					if (histo_tested
							.compare_histo(new Histogramme(histo_string)) < 0.5) {
						users.add(id_user);
					}
					// move cursor
					commande.getMoreResults();
					result = commande.getResultSet();
				}
				commande.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	public boolean authenticate(byte[] userData1, JSONObject histogram)
			throws Exception {
		ArrayList<Integer> user_matched = findUsersByBiometric(histogram);
		if (user_matched.size() <= 0) {
			throw new Exception("user not find by biometrics");
		}
		if (cardIsValid(userData1)) {
			return true;
		}
		return false;
	}

	/**
	 * verify if a card exist in the database and is still activated
	 * 
	 * @param userData1
	 * @return
	 * @throws Exception
	 */
	private boolean cardIsValid(byte[] userData1) throws Exception {
		Connection c = connectToDatabase();
		String id_card_tested = null;
		Statement commande = null;
		try {
			commande = c.createStatement();
			String sql = "SELECT active FROM cards WHEN id_card="
					+ id_card_tested;
			if (commande.execute(sql)) {
				ResultSet result = commande.getResultSet();
				if (result != null) {
					boolean card_active = result.getBoolean(0);
					if (card_active) {
						/*if(verifyToken(identifiant_card, token)){
						commande.close();
						}*/
						return true;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			commande.close();
		}
		return false;
	}

}
