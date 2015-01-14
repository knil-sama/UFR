package fr.areaX.smartcard;

import javax.smartcardio.CardException;

import fr.areaX.crypto.XCryptoKeys;

public class MockSmartCard implements SmartCardInterface {

	private int c = 0;
	private byte[] area1 = null;
	private byte[] area2 = null;
	
	public MockSmartCard() throws CardException{
		area1 = new byte[30];
		
		XCryptoKeys crytoKeys = new XCryptoKeys();
		crytoKeys.loadPrivateKey("private.key");
		
		for(int i=0; i<30; i++){
			area1[i] = SmartIdentityCard.getRandomByte();
		}
		
		try {
			area2 = crytoKeys.generateSignature(area1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean hasCard() {
		
		if (++c < 10){
			return false;
		} else if (++c < 100) {
			c = 0;
			return true;
		} else
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
