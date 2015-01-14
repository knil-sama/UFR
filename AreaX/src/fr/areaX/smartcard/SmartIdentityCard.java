package fr.areaX.smartcard;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;


public class SmartIdentityCard implements SmartCardInterface {

	private SmartCardDrive drive;

	
	public SmartIdentityCard() throws CardException {
		drive = new SmartCardDrive();
		drive.selectFirstTerminal();
	}
	
	@Override
	public boolean hasCard() {
		try {
			drive.connectCard();
		} catch (CardException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public byte[] read(int userArea) throws CardException {
		CardChannel channel = drive.getCurrentCard().getBasicChannel();
		
		CommandAPDU commande;
		
		if (userArea == 1)
			commande = new CommandAPDU(0x80,0xBE,0x00,0x10,0x40);
		else
			commande = new CommandAPDU(0x80,0xBE,0x00,0x28,0x40);
		
		ResponseAPDU r = channel.transmit(commande);
		return r.getData();
	}

	@Override
	public void write(byte[] stream, int userArea)  throws CardException {
		 CardChannel channel = drive.getCurrentCard().getBasicChannel();
		 CommandAPDU commande ;

		 byte[] header;
		 if (userArea == 1)
			 header = new byte[]{(byte)0x80,(byte) 0xDE,(byte)0x00,(byte)0x10,(byte)0x40};
		 else
			 header = new byte[]{(byte)0x80,(byte) 0xDE,(byte)0x00,(byte)0x28,(byte)0x40};
			 
			 
		 byte[] writeStream = new byte[5 + stream.length];
		 for(int i=0; i<5; i++){
			 writeStream[i] = header[i];
		 }
		 
		 int wi = 5;
		 for(int i=0; i<64; i++){
			 writeStream[wi++] = stream[i];
		 }
		
		 ResponseAPDU r;
		byte apdu[]={(byte)0x00,(byte) 0x20,(byte)0x00,(byte)0x07,(byte)0x04,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA};
		commande = new CommandAPDU(apdu);//test code pin
		r = channel.transmit(commande);
	       System.out.println("pre reponse : " + "SW1 : 0x"+Integer.toHexString(r.getSW1())+" SW2 : 0x"+Integer.toHexString(r.getSW2())+" data : "+drive.toString(r.getData()));

		//byte apdu2[]=,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01};
       commande = new CommandAPDU(writeStream);//test code pin
       r = channel.transmit(commande);
       System.out.println("reponse : " + "SW1 : 0x"+Integer.toHexString(r.getSW1())+" SW2 : 0x"+Integer.toHexString(r.getSW2())+" data : "+drive.toString(r.getData()));
	
	}

	public static byte getRandomByte(){
		int i = (int) (Math.random()*1000) % 250;
		byte b = (byte)i;
		return b;
	}
	
	public static void test(){
		
	}
	
	public static void main(String[] args) throws Exception {

		SmartIdentityCard sm = new SmartIdentityCard();
		
		System.exit(0);
		
		if (!sm.hasCard()){
			System.out.println("No card in the drive");
			return;
		}

		byte writeData[] = new byte[64];
		for (int i=0; i<64; i++){
			writeData[i] = getRandomByte();
		}
		
		sm.write(writeData, 1);
		
		byte[] read = sm.read(1);
		String readStr = SmartCardDrive.toString(read);
		String toWriteStr = SmartCardDrive.toString(writeData);
		System.out.println("The size of readBuffer is: " + read.length);
		System.out.println("Read content: " + readStr);
		System.out.println("To write content: " + toWriteStr);
		
	}
	
	
	

}
