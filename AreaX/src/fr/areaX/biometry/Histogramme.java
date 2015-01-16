package fr.areaX.biometry;

import org.json.JSONArray;
import org.json.JSONObject;

public class Histogramme {
	JSONArray histo;
	public Histogramme(String json_string){
		histo = new JSONArray(json_string);
	}
	public Histogramme(JSONArray json){
		histo = json;	
	}
	/**
	 * 
	 * @param histo_compare
	 * @return
	 */
	public double compare_histo(Histogramme histo_compare){
		double bhatta = 0.0;
		for (int gray =0; gray < histo.length(); gray++){ 
			bhatta += Math.sqrt((this.histo.getDouble(gray)) * histo_compare.histo.getDouble(gray));
		}
		System.out.println("bhatta" + bhatta);
		return -Math.log(bhatta);
	}
}