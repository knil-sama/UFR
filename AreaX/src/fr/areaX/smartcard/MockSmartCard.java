package fr.areaX.smartcard;

import javax.smartcardio.CardException;

public class MockSmartCard implements SmartCardInterface {

	private int cardCheck = 0;
	private byte[] area1 = null;
	private byte[] area2 = null;
	
	public MockSmartCard() throws CardException{
		area1 = new byte[64];
		area2 = new byte[64];
		
		for(int i=0; i<64; i++){
			area1[i] = SmartIdentityCard.getRandomByte();
			area2[i] = SmartIdentityCard.getRandomByte();
		}
	}
	
	@Override
	public boolean hasCard() {
		int i = (++cardCheck % 11);
		return (i == 10);
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
