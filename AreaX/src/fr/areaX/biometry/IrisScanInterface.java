package fr.areaX.biometry;

import org.json.JSONObject;

public interface IrisScanInterface {

	public JSONObject parseImage(String imageUrl, int threshold);
	
}
