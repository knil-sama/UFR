package fr.areaX.authentication;

public interface AuthenticationInterface {

	public boolean verifySmartCardIdentity(byte[] userData1, byte[] userData2);
	
	public boolean identityUpdate(byte[] userData1);

	public byte[][] getNewIdentity(byte[] userData1);
	
}
