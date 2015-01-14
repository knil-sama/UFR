package fr.areaX.crypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class XCryptoKeys {

	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public XCryptoKeys(){
		
	}
	
	public boolean loadPrivateKey(String keyFile) {
		try {
			FileInputStream fi = new FileInputStream(keyFile);
			byte[] encPrivateKey = new byte[fi.available()];
			
			fi.read(encPrivateKey);
			fi.close();
			
			PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(encPrivateKey);
			
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			privateKey = keyFactory.generatePrivate(priKeySpec);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| InvalidKeySpecException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean loadPublicKey(String keyFile) {
		try {
			FileInputStream fi = new FileInputStream(keyFile);
			byte[] encPubKey = new byte[fi.available()];
			
			fi.read(encPubKey);
			fi.close();
			
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encPubKey);

			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			publicKey = keyFactory.generatePublic(pubKeySpec);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| InvalidKeySpecException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void savePublicKey(String keyFilename) throws Exception{
		FileOutputStream fs = new FileOutputStream(keyFilename);
		fs.write(publicKey.getEncoded());
		fs.close();
	}

	public void savePrivateKey(String keyFilename) throws Exception{
		FileOutputStream fs = new FileOutputStream(keyFilename);
		fs.write(privateKey.getEncoded());
		fs.close();
	}
	
	public boolean verifySignature(byte[] signature, byte[] data) throws Exception {
		Signature sign = Signature.getInstance("SHA1withDSA", "SUN");
		sign.initVerify(publicKey);
		sign.update(data);
		return (sign.verify(signature));
	}
	
	public byte[] generateSignature(byte[] data) throws Exception {
		Signature sign = Signature.getInstance("SHA1withDSA", "SUN");
		sign.initSign(privateKey);
		sign.update(data);
		return sign.sign();
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	public static void test() throws Exception{
		XCryptoKeys cryptokeys = new XCryptoKeys();

		cryptokeys.loadPrivateKey("private.key");
		
		byte[] data = GenerateKey.getRandomBytes();
		
		byte[] signData = cryptokeys.generateSignature(data);
		
		cryptokeys.loadPublicKey("public.key");
		
		boolean verify = cryptokeys.verifySignature(signData, data);
		System.out.println(verify);
	}
	
}
