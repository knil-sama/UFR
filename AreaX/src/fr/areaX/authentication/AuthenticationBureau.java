package fr.areaX.authentication;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.javafx.geom.Area;

import fr.areaX.biometry.IrisScanInterface;
import fr.areaX.biometry.IrisScanProcessor;
import fr.areaX.controller.AreaX;
import fr.areaX.crypto.XCryptoKeys;

public class AuthenticationBureau implements AuthenticationInterface{

	private XCryptoKeys crytoKeys;
	private IrisScanInterface iris;
	
	public AuthenticationBureau() {
		crytoKeys = new XCryptoKeys();
		
		crytoKeys.loadPrivateKey("private.key");
		crytoKeys.loadPublicKey("public.key");
		
		iris = new IrisScanProcessor();
	}
	
	@Override
	public boolean verifySmartCardIdentity(byte[] userData1, byte[] userData2) {
		
		boolean result;
		
		//prune the tail
		byte[] cardContent = new byte[30];
		byte[] signature46 = new byte[46];
		byte[] signature47 = new byte[47];
		
		for(int i =0; i<cardContent.length; i++	){
			cardContent[i] = userData1[i];
		}
		for(int i =0; i<signature46.length; i++	){
			signature46[i] = userData2[i];
			signature47[i] = userData2[i];
		}
		signature47[46] = userData2[46];
		
		//First Step 
		//verify if the signature (userData2) is valid for the data (userData1)
		try {
			result = crytoKeys.verifySignature(signature47, cardContent) || 
					crytoKeys.verifySignature(signature46, cardContent);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if (!result){
			//maybe we want to log this activity
			System.out.println("[INFO] Authentication attempted with false data");
			return false;
		}

		//Second step: query the database for the data
		
		return true;
	}

	@Override
	public boolean identityUpdate(byte[] userData1) {
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		return false;
	}

	@Override
	public byte[][] getNewIdentity(byte[] userData1) {
		return null;
	}

	@Override
	public boolean authenticateByBiometry(String imgUrl) {
		
		try {	
			JSONObject jsonReply = iris.parseImage(imgUrl);

			String processorStatus = (String)jsonReply.get("reply");

			if(!processorStatus.equalsIgnoreCase("OK")){
				//maybe we want to notify the concerned module for the processor's error
				return false;
			}

			String histogramStr = (String) jsonReply.getString("data");

			JSONObject histogramJson = new JSONObject(histogramStr);


		} catch (JSONException e) {
			AreaX.getInstance().imageProcessingError("JSONException while image processing");
			System.err.println("JSONException while image processing");
			e.printStackTrace();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		return true;
	}

}
