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
	private XNode startScreen = null;
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
			startScreen.onEvent(className, XNode.INITIALISATION_ERROR, null);
			e.printStackTrace();
		}
		startScreen.onEvent(className, XNode.NO_SMART_CARD, null);
	}
	
	public void setStartScreenGUI(XNode startScreen){
		this.startScreen = startScreen;
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
						}
						
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						System.out.println("[VERBOSE] Card in reader: " + hasCard );

						hasCard = smartCard.hasCard();
					}
					
					try {

						byte[] userData1 = smartCard.read(1);
						byte[] userData2 = smartCard.read(2);

						boolean verified = authentication
										.verifySmartCardIdentity(userData1, userData2);
						
						if (!verified){
							startScreen.onEvent(null, XNode.SMART_CARD_REFUSED, null);
							continue;
						}

						startScreen.onEvent(null, XNode.SMART_CARD_VERIFIED, null);
						accepted = true;
						
						
						boolean needsUpdate = authentication.identityUpdate(userData1);
						if (needsUpdate){
							byte[][] userData = authentication.getNewIdentity(userData1);
							smartCard.write(userData[0], 1);
							smartCard.write(userData[1], 2);
						}

						startScreen.onEvent(null, XNode.SMART_CARD_UPDATED, null);
						
						
					} catch (CardException e) {
						startScreen.onEvent(null, XNode.SMART_CARD_IO_ERROR, null);
						e.printStackTrace();
					}
					
					
				}
				
			}
		}));
		th.setDaemon(true);
		th.start();
	}
	
}
