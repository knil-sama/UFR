package fr.areaX.authentication;

import fr.areaX.crypto.XCryptoKeys;

public class AuthenticationBureau implements AuthenticationInterface{

	private XCryptoKeys crytoKeys;
	
	public AuthenticationBureau() {
		crytoKeys = new XCryptoKeys();
		
		crytoKeys.loadPrivateKey("private.key");
		crytoKeys.loadPublicKey("public.key");
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

}
