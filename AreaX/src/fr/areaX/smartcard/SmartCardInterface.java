package fr.areaX.smartcard;

public interface SmartCardInterface {

	public boolean hasCard();
	
	public byte[] read();
	
	public void write(byte[] stream);
	
}
