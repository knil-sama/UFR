package fr.areaX.unit_test;
import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.areaX.biometry.Histogramme;
import fr.areaX.dao.PostgreSQLJDBC;

public class TestDAO{
	@Before
	public void resetTables(){
		PostgreSQLJDBC database = new PostgreSQLJDBC();
		//database.emptyDatabase();
		database.resetTable();
	}
	@Test
	public void createCardFirstTime(){
		PostgreSQLJDBC database = new PostgreSQLJDBC();
		assertTrue(database.createCard()  > 0);
	}
	@Test
	public void createCardWithTheSameIdTime(){
		try{
		PostgreSQLJDBC database = new PostgreSQLJDBC();
		assertTrue(database.createCard(10));
		assertFalse(database.createCard(10));
		}catch(Exception e){
			assertTrue(true);
		}
	}
	@Test
   public void createUserValid() { 
	PostgreSQLJDBC database = new PostgreSQLJDBC();
	String json_string = "[1,2,3]";
   // assertTrue(database.createUser("clement","demonchy",new Date(1421278232), new JSONArray(json_string)));
     
   }
	@Test
	public void findSameUserByBiometrics(){
		PostgreSQLJDBC server = new PostgreSQLJDBC();
		int id_card = 68;
		int id_user = 42;
		server.createCard(id_card);
		String json_string = "[";
		for(int i = 0; i < 255; i++){
			json_string += 1.0/255.0;
			if(i < 254)
				json_string += ",";
		}
		json_string += "]";
		JSONArray json_histo = new JSONArray(json_string);
	
		java.util.Date date = new java.util.Date();
		Timestamp currentTimestamp = new Timestamp(date.getTime());
		server.createUser(id_user, "clement", "demonchy", new Date(currentTimestamp.getTime()),json_histo);
		server.attachCardToUser(id_card, id_user);
		assertEquals(1,server.findUsersByBiometric(json_histo).size());
	}
	@Test
	public void DidntFindUserByBiometrics(){
		PostgreSQLJDBC server = new PostgreSQLJDBC();
		int id_card = 68;
		int id_user = 42;
		server.createCard(id_card);
		String json_string = "[0.8,0.1,0.05,0.05]";
		JSONArray json_histo = new JSONArray(json_string);
		java.util.Date date = new java.util.Date();
		Timestamp currentTimestamp = new Timestamp(date.getTime());
		server.createUser(id_user, "clement", "demonchy", new Date(currentTimestamp.getTime()),json_histo);
		server.attachCardToUser(id_card, id_user);
		
		String json_string_bis = "[0.25,0.25,0.25,0.25]";
		JSONArray json_histo_bis = new JSONArray(json_string_bis);
		assertEquals(0,server.findUsersByBiometric(json_histo_bis).size());
	}
	@Test
	public void batamachin(){
		JSONArray json_histo = new JSONArray();
		json_histo.put(0,0.5);
		json_histo.put(1,0.5);
		Histogramme histo1 = new Histogramme(json_histo);
		Histogramme histo2 = new Histogramme(json_histo);
		System.out.println(histo1.compare_histo(histo2));
		assertTrue(true);
		
	}
//calcul histo
	// table test (id serial primary key, c0 integer);
	// insert into test(name) select name from json_populate_record(NULL::test, '{"155": 155544}');
}