package fr.areaX.authentication;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.javafx.geom.Area;

import fr.areaX.biometry.IrisScanInterface;
import fr.areaX.biometry.IrisScanProcessor;
import fr.areaX.controller.AreaX;
import fr.areaX.crypto.XCryptoKeys;
import fr.areaX.dao.PostgreSQLJDBC;
import fr.areaX.smartcard.SmartCardHelper;

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
	public int authenticate(byte[] userData1, byte[] userData2,String histogram){
		if(verifySmartCardIdentity(userData1, userData2)){
			PostgreSQLJDBC server = new PostgreSQLJDBC();
			//Second step: query the database for the data
			//prune the tail
			byte[] cardContent = new byte[12];
			
			for(int i =0; i<cardContent.length; i++	){
				cardContent[i] = userData1[i];
			}
			int identityCard = convertIdentityCard(cardContent);
			int tokenCard = convertTokenCard(cardContent); 
			
			
			try {
				Integer tokenSessionActive = server.authenticate(identityCard,tokenCard, histogram);
				if(tokenSessionActive != 0){
					return tokenSessionActive;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	private int convertTokenCard(byte[] cardContent) {
		byte[] cardTokenByte = new byte[7];
		int ii = 5;
		for(int i=0; i<7; i++){
			cardTokenByte[i] = cardContent[ii++];
		}
		return SmartCardHelper.fromByteToInt(cardTokenByte);
	}
	private int convertIdentityCard(byte[] cardContent) {
		byte[] cardIdentityByte = new byte[5];
		
		for(int i=0; i<5; i++){
			cardIdentityByte[i] = cardContent[i];
		}
		return SmartCardHelper.fromByteToInt(cardIdentityByte);
	}
	/**
	 * must be done after user
	 */
	@Override
	public boolean verifySmartCardIdentity(byte[] userData1, byte[] userData2) {
		
		boolean result;
		
		//prune the tail
		byte[] cardContent = new byte[12];
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
			JSONObject jsonReply = iris.parseImage(imgUrl, 0);

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
