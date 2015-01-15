package fr.areaX.controller;

import javax.smartcardio.CardException;

import fr.areaX.authentication.AuthenticationBureau;
import fr.areaX.authentication.AuthenticationInterface;
import fr.areaX.gui.XNode;
import fr.areaX.smartcard.MockSmartCard;
import fr.areaX.smartcard.SmartCardInterface;
import fr.areaX.smartcard.SmartIdentityCard;

public class AreaX {

	private SmartCardInterface smartCard;
	private XNode gui = null;
	private AuthenticationInterface authentication;
	
	private final String className = this.getClass().getSimpleName();

	
	private static AreaX self = null;
	public static AreaX getInstance(){
		if (self == null){
			self = new AreaX();
		}
		return self;
	}
	
	private AreaX() {

	}
	
	public void initialise() {
		try {
			authentication = new AuthenticationBureau();
			smartCard = new MockSmartCard();
	//		smartCard = new SmartIdentityCard();
			scanForSmartCardPermanently();

		} catch (CardException e) {
			gui.onEvent(className, XNode.INITIALISATION_ERROR, null);
			e.printStackTrace();
		}
		gui.onEvent(className, XNode.NO_SMART_CARD, null);
	}
	
	public void setStartScreenGUI(XNode gui){
		this.gui = gui;
	}
	
	public void scanForSmartCardPermanently(){
		Thread th = (new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean rejected = false;
				boolean accepted = false;
				boolean hasCard = false;
				while(true){
					while(rejected || accepted || smartCard==null || !hasCard) {
						
						if (!hasCard){
							rejected = false;
							accepted = false;
							gui.onEvent(null, XNode.NO_SMART_CARD, null);
						}
						
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						hasCard = smartCard.hasCard();
						//System.out.println("[VERBOSE] Card in reader: " + hasCard );

					}
					
					try {

						byte[] userData1 = smartCard.read(1);
						byte[] userData2 = smartCard.read(2);

						//userData1[0]=(byte)100;
						
						boolean verified = authentication
										.verifySmartCardIdentity(userData1, userData2);
						
						if (!verified){
							gui.onEvent(null, XNode.SMART_CARD_REFUSED, null);
							rejected = true;
							continue;
						}

						gui.onEvent(null, XNode.SMART_CARD_VERIFIED, null);
						accepted = true;
						
						
						boolean needsUpdate = authentication.identityUpdate(userData1);
						if (needsUpdate){
							byte[][] userData = authentication.getNewIdentity(userData1);
							smartCard.write(userData[0], 1);
							smartCard.write(userData[1], 2);
						}

						gui.onEvent(null, XNode.SMART_CARD_UPDATED, null);
						
						
					} catch (CardException e) {
						gui.onEvent(null, XNode.SMART_CARD_IO_ERROR, null);
						e.printStackTrace();
					}
					
					
				}
				
			}
		}));
		th.setDaemon(true);
		th.start();
	}
	
	public void runNewThread(Runnable runnable){
		Thread th = new Thread(runnable);
		th.setDaemon(true);
		th.start();
	}
	
	public void initBiometry(){
		runNewThread(new Runnable() {
			
			@Override
			public void run() {
				boolean result = 
						authentication.authenticateByBiometry("image_snap.jpg");
				
				if (result){
					gui.onEvent(className, XNode.BIOMETRY_ACCEPTED, null);
				} else {
					gui.onEvent(className, XNode.BIOMETRY_REJECTED, null);
				}
			}
		});
	}
	
	public void imageProcessingError(String msg){
		gui.onEvent(className, XNode.IMAGE_PROCESSING_ERROR, msg);
	}
	
	public void onBiometricResult(boolean result){
		if(result){
			gui.onEvent(className, XNode.BIOMETRY_ACCEPTED,null);
		} else {
			gui.onEvent(className, XNode.BIOMETRY_REJECTED,null);
		}
	}	
}
