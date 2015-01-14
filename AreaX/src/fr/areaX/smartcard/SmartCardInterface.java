package fr.areaX.smartcard;

import javax.smartcardio.CardException;

public interface SmartCardInterface {

	public boolean hasCard();
	
	public byte[] read(int userArea) throws CardException;
	
	public void write(byte[] stream, int userArea) throws CardException;
	
}
