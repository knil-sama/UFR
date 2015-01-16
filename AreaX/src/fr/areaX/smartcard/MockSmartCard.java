package fr.areaX.smartcard;

import java.security.Signature;

import javax.smartcardio.CardException;

import fr.areaX.crypto.XCryptoKeys;

public class MockSmartCard implements SmartCardInterface {

	private int c = 0;
	private byte[] area1 = null;
	private byte[] area2 = null;
	
	public MockSmartCard() throws CardException{
		XCryptoKeys crytoKeys = new XCryptoKeys();
		crytoKeys.loadPrivateKey("private.key");

		area1 = new byte[12];

		byte[] id = SmartCardHelper.fromIntToByte(12345);
		byte[] token = SmartCardHelper.fromIntToByte(1234567);
		
		for(int i=0; i<5; i++){
			area1[i] = id[i];
		}
		int ii=5;
		for(int i=0; i<7; i++){
			area1[ii++] = token[i];
		}
		
		try {
			byte[] signature = crytoKeys.generateSignature(area1);
			area2 = new byte[64];
			for(int i=0;i<signature.length; i++){
				area2[i] = signature[i];
			}
			for(int i=signature.length;i<64; i++){
				area2[i] = (byte)0x0;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean hasCard() {
		
		if (++c < 10){
			return false;
		} else if (++c < 100) {
			return true;
		} 
		c = 0;
		return false;
	}

	@Override
	public byte[] read(int userArea) throws CardException {
		return userArea==1? area1: area2;
	}

	@Override
	public void write(byte[] stream, int userArea) throws CardException {
		if (userArea==1)
			area1 = stream;
		else
			area2 = stream;
	}

}
