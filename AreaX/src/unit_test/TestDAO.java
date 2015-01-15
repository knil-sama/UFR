package unit_test;
import static org.junit.Assert.*;

import java.sql.Date;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dao.PostgreSQLJDBC;

public class TestDAO{
	@Before
	public void resetTables(){
		PostgreSQLJDBC database = new PostgreSQLJDBC();
		database.emptyDatabase();
	}
	@Test
	public void createCardFirstTime(){
		PostgreSQLJDBC database = new PostgreSQLJDBC();
		assertTrue(database.createCard());
	}
	@Test
	public void createCardWithTheSameIdTime(){
		PostgreSQLJDBC database = new PostgreSQLJDBC();
		assertTrue(database.createCard(10));
		assertFalse(database.createCard(10));
	}
	@Test
   public void createUserValid() { 
	PostgreSQLJDBC database = new PostgreSQLJDBC();
	String json_string = "{\"0\": 5529632,\"1\": 4431841,\"2\": 3026793,\"3\": 2356835,\"4\": 1736520}";
    assertTrue(database.createUser("clement","demonchy",new Date(1421278232), new JSONObject(json_string)));
     
   }
//calcul histo
	// table test (id serial primary key, c0 integer);
	// insert into test(name) select name from json_populate_record(NULL::test, '{"155": 155544}');
}