package fr.areaX.authentication;

public class AuthenticationBureau implements AuthenticationInterface{

	@Override
	public boolean verifySmartCardIdentity(byte[] userData1, byte[] userData2) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

}
