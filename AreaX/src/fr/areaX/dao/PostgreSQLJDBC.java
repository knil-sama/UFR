package fr.areaX.dao;

import java.sql.Connection;

import org.json.*;
import org.postgresql.jdbc2.TimestampUtils;

import fr.areaX.biometry.Histogramme;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

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
					+ "token integer," + "time_creation timestamp with time zone,"
					+ "time_end timestamp with time zone,"
					+ "user_session integer," + "salt text,"
					+ "CONSTRAINT session_pkey PRIMARY KEY (id_session))"
					+ "WITH (OIDS=FALSE);";
			commande.executeUpdate(sql);
			// CARTE OWNER
			sql = "DROP TABLE IF EXISTS card_owner CASCADE";
			commande.executeUpdate(sql);
			sql = "CREATE TABLE card_owner(id_card_owner serial NOT NULL,"
					+ "card integer," + "owner integer,"
					+ "time_assignment timestamp with time zone,"
					+ "CONSTRAINT card_owner_pkey PRIMARY KEY (id_card_owner),"
					+ "CONSTRAINT card_owner_card_fkey FOREIGN KEY (card)"
					+ "REFERENCES cards (id_card) MATCH SIMPLE "
					+ "ON UPDATE NO ACTION ON DELETE NO ACTION)"
					+ "WITH (OIDS=FALSE);";
			commande.executeUpdate(sql);
			commande.close();
			// LOG
			sql = "DROP TABLE IF EXISTS logs CASCADE";
			commande.executeUpdate(sql);
			sql = "CREATE TABLE logs("
					+ "id_log serial NOT NULL,"
					+ "card_owner integer,"
					+ "user_recognize integer,"
					+ "card_used integer,"
					+ "time_access timestamp with time zone,"
					+ "current_session integer,"
					+ "message text,"
					+ "CONSTRAINT logs_pkey PRIMARY KEY (id_log),"
					+ "CONSTRAINT logs_card_owner_fkey FOREIGN KEY (card_owner)"
					+ "REFERENCES card_owner (id_card_owner) MATCH SIMPLE"
					+ "ON UPDATE NO ACTION ON DELETE NO ACTION,"
					+ "CONSTRAINT logs_card_used_fkey FOREIGN KEY (card_used)"
					+ "REFERENCES cards (id_card) MATCH SIMPLE"
					+ "ON UPDATE NO ACTION ON DELETE NO ACTION,"
					+ "CONSTRAINT logs_current_session_fkey FOREIGN KEY (current_session)"
					+ "REFERENCES session (id_session) MATCH SIMPLE"
					+ "ON UPDATE NO ACTION ON DELETE NO ACTION,"
					+ "CONSTRAINT logs_user_recognize_fkey FOREIGN KEY (user_recognize)"
					+ "REFERENCES users (id_user) MATCH SIMPLE"
					+ "ON UPDATE NO ACTION ON DELETE NO ACTION)"
					+ "WITH (OIDS=FALSE)";
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
			String sql = "TRUNCATE users, logs, cards,session, card_owner CASCADE";
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
			return false;
		}
		return true;
	}

	public boolean createUser(int id, String first_name, String last_name,
			Date birthdate, JSONArray histo) {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			// date format YYYY-MM-DD
			String sql = "INSERT INTO users (id_user, first_name, last_name,birthdate, histogram) VALUES ("
					+ id
					+ ",'"
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
	public ArrayList<Integer> findUsersByBiometric(JSONArray histogram) {
		double ACCEPTATION_TRESHOLD = 0.15;
		ArrayList<Integer> users = new ArrayList<Integer>();
		Histogramme histo_tested = new Histogramme(histogram);
		Connection c = connectToDatabase();
		Integer id_user;
		String histo_string;
		try {
			Statement commande = c.createStatement();
			String sql = "SELECT id_user, histogram FROM users";
			ResultSet result = commande.executeQuery(sql);
			double value_distance;
			ResultSetMetaData resultMeta = result.getMetaData();
			while (result.next()) {
				System.out.println("entre dans la boucle");
				id_user = result.getInt(1);
				histo_string = result.getString(2);
				System.out.println(histo_string);
				value_distance = histo_tested.compare_histo(new Histogramme(
						histo_string));
				System.out.println("valeur bhat" + value_distance);
				if (value_distance < ACCEPTATION_TRESHOLD) {
					users.add(id_user);
				}
			}
			commande.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	public Integer authenticate(int identityCard, int tokenSession,
			JSONArray histogram) throws Exception {
		ArrayList<Integer> user_matched = findUsersByBiometric(histogram);
		if (user_matched.size() <= 0) {
			//throw new Exception("user not find by biometrics");
			return 0;
		}
		Iterator<Integer> iter_user = user_matched.iterator();
		int user_id;
		do {
			if (cardIsValid(identityCard)) {
				user_id = iter_user.next();
				if (userIsCardOwner(user_id, identityCard)) {
					Integer tokenCurrentSession = sessionValid(user_id,
							tokenSession);
					return tokenCurrentSession;
				}
			}
		} while (iter_user.hasNext());

		return 0;
	}

	private Integer sessionValid(int user_id, int tokenSession) {
		Connection c = connectToDatabase();
		try {
			Statement commande = c.createStatement();
			// return la session la plus récente de cet utilisateur
			String sql = "SELECT token, time_creation, time_end FROM session WHERE user_session = "
					+ user_id + " ORDER BY time_creation DESC";
			ResultSet last_session = commande.executeQuery(sql);
			if (last_session.next()) {
				int tokenLastSession = last_session.getInt("token");
				if (tokenLastSession == tokenSession) {
					Timestamp time_end = last_session.getTimestamp("time_end");
					java.util.Date date = new java.util.Date();
					Timestamp currentTimestamp = new Timestamp(date.getTime());
					// expired timestamp
					if (currentTimestamp.compareTo(time_end) > 0) {
						return generateNewToken(user_id);
					} else {
						return tokenSession;
					}
				}
			}
			commande.close();
			// c.commit();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Integer generateNewToken(int user_id) {
		return 1;
	}

	private boolean userIsCardOwner(int id_user, int id_card) throws Exception {
		Connection c = connectToDatabase();
		Statement commande = null;
		try {
			commande = c.createStatement();
			// renvoye la dernier utilisateur associé à la carte;
			String sql = "SELECT owner,time_assignment FROM card_owner WHERE card="
					+ id_card + " ORDER BY time_assignment DESC";
			ResultSet result = commande.executeQuery(sql);
			if (result.next()) {
				int id_owner = result.getInt("owner");
				if (id_owner == id_user) {
					return true;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			commande.close();
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
	private boolean cardIsValid(int id_card_tested) throws Exception {
		Connection c = connectToDatabase();
		Statement commande = null;
		try {
			commande = c.createStatement();
			String sql = "SELECT active FROM cards WHERE id_card="
					+ id_card_tested;
			ResultSet result = commande.executeQuery(sql);
			if (result.next()) {
				boolean card_active = result.getBoolean(1);
				if (card_active) {
					return true;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			commande.close();
		}
		return false;
	}

	public void attachCardToUser(int id_card, int id_user) {
		Connection c = connectToDatabase();
		Statement commande = null;
		try {
			commande = c.createStatement();
			String sql = "INSERT INTO card_owner (id_card_owner,card,owner,time_assignment) VALUES(DEFAULT,"
					+ id_card + "," + id_user + ", CURRENT_TIMESTAMP)";
			commande.execute(sql);
			commande.close();
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
	}

	public Integer generateSession(int id_token, int id_user) {
		return generateSession(id_token, id_user, 0);
	}

	public Integer generateSession(int id_session, int id_user, int hour_delay) {
		Connection c = connectToDatabase();
		Statement commande = null;
		Integer token = null;
		try {
			commande = c.createStatement();
			// TO DO generate Token, repasser à bytea pour générer dans SQL ?
			token = 5;
			String sql = "INSERT INTO session (id_session,token,time_creation,time_end,user_session,salt) VALUES("
					+ id_session
					+ ","
					+ token
					+ ", current_timestamp, CURRENT_TIMESTAMP + interval '"
					+ hour_delay + " hours'"
					+ "," + id_user + ","
					+ "gen_salt('md5'))";
			commande.execute(sql);
			commande.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

}
