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
		int id_card = 65,id_user = 67, id_session = 21;
		server.createCard(65);
		String json_string = "[0.5,0.5]";
		JSONArray json_histo = new JSONArray(json_string);
		java.util.Date date = new java.util.Date();
		Timestamp currentTimestamp = new Timestamp(date.getTime());
		server.createUser(id_user, "clement", "demonchy", new Date(currentTimestamp.getTime()),json_histo);
		server.attachCardToUser(id_card, id_user);
		Integer token = server.generateSession(id_session,id_user,1);
		try {
			Integer tokenSessionActive = server.authenticate(id_card, token, json_string);
			assertEquals(token,tokenSessionActive);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
