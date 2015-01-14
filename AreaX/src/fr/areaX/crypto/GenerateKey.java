package fr.areaX.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class GenerateKey {

	public GenerateKey(){
		
	}
	
	public static KeyPair generateKeys(){
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("DSA", "SUN");
		
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			
			gen.initialize(1024, random);
			
			return gen.generateKeyPair();
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	public static byte[] getRandomBytes(){
		byte[] buffer = new byte[256];
		
		for(int i=0; i<256; i++){
			buffer[i] = (byte) ((int)(Math.random()*100)%125);
		}
		
		return buffer;
	}

}
