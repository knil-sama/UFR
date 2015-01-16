package fr.areaX.unit_test;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.areaX.dao.PostgreSQLJDBC;

public class TestAuthentification {
	@Before
	public void resetTables(){
		PostgreSQLJDBC database = new PostgreSQLJDBC();
		database.emptyDatabase();
		//database.resetTable();
	}
	
	@Ignore
	public void testVerifySmartCardIdentity() {
		/*
		 * if(verifySmartCardIdentity(userData1, userData2)){
		 * 
		 * }
		 */
		assertTrue(false);
	}

	@Test
	public void testAuthenticate() {
		PostgreSQLJDBC server = new PostgreSQLJDBC();
		int id_card = 65;
		int id_user = 67;
		server.createCard(65);
		JSONArray json_histo = new JSONArray();
		json_histo.put(0,0.5);
		json_histo.put(1,0.5);
		java.util.Date date = new java.util.Date();
		Timestamp currentTimestamp = new Timestamp(date.getTime());
		server.createUser(id_user, "clement", "demonchy", new Date(currentTimestamp.getTime()),json_histo);
		server.attachCardToUser(id_card, id_user);
		try {
			Integer tokenSessionActive = server.authenticate(id_card, id_user, json_histo);
			if (tokenSessionActive != null) {
				assertTrue(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
