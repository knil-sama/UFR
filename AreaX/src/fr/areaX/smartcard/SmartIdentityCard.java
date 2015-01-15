package fr.areaX.smartcard;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import fr.areaX.crypto.XCryptoKeys;


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
			 
			 
		 byte[] writeStream = new byte[64+5];
		 for(int i=0; i<5; i++){
			 writeStream[i] = header[i];
		 }
		 
		 int wi = 5;
		 for(int i=0; i<stream.length; i++){
			 writeStream[wi++] = stream[i];
		 }
		 
		 for(int i = stream.length; i<64; i++){
			 writeStream[5+i] = (byte)0x00;
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
		int i = (int) (Math.random()*1000) % 50;
		byte b = (byte)i;
		return b;
	}
	
	public static int getRandomIntId(){
		int i = 10000;
		i += (int) Math.random()*1000;
		return i%10000;
	}
	public static int getRandomToken(){
		int i = (int) Math.random()*100000;
		return i%1000000;
	}

	public static void initiliazeCard() throws Exception{
		SmartIdentityCard sm = new SmartIdentityCard();

		XCryptoKeys keys = new XCryptoKeys();
		keys.loadPrivateKey("private.key");
		keys.loadPublicKey("public.key");
		
		byte[] cardContent = new byte[12];
		
		int randomIntId = getRandomIntId();
		int randomIntToken = getRandomToken();
	
		byte[] byteId = SmartCardHelper.fromIntToByte(randomIntId);
		byte[] byteToken = SmartCardHelper.fromIntToByte(randomIntToken);
		int i;
		for(i=0; i<byteId.length; i++){
			cardContent[i] = byteId[i];
		}
		
		int ii=0;
		for(i=byteId.length; i<12; i++){
			cardContent[i] = byteToken[ii++];
		}
		
		System.out.println("Id generated : " + randomIntId + " " +randomIntToken);
		System.out.println("Id in byte" + SmartCardHelper.fromByteToInt(cardContent));

		byte[] signature = keys.generateSignature(cardContent);

		if(!sm.hasCard()){
			System.err.println("No card present in the reader");
			return;
		}
		
		
		System.out.println("The size of content is :" + cardContent.length);
		System.out.println("The size of the signature is " + signature.length);
		
		System.out.println("Initializing data to the card");
		
		sm.write(cardContent, 1);
		sm.write(signature, 2);

		System.out.println("Writing to the card user area 1/2 terminated");
		
		System.out.println("Verifying the written content");
		

		byte[] read1 = sm.read(1);
		byte[] read2 = sm.read(2);

		System.out.println("Card content 1 o : " + SmartCardDrive.toString(cardContent));
		System.out.println("Card content 1 r : " + SmartCardDrive.toString(read1));

		System.out.println("Card content 2 o : " + SmartCardDrive.toString(signature));
		System.out.println("Card content 2 r : " + SmartCardDrive.toString(read2));

		boolean equal = true;
		for(i = 0; i<cardContent.length; i++){

			System.out.print(cardContent[i] + " " +read1[i]+"\t");
			if (cardContent[i]!=read1[i]){
				equal = false;
				break;
			}
		}
		System.out.println("");
		
		if (!equal){
			System.out.println("Read 1 and cardContent not equal");
		}

		equal = true;
		for(i = 0; i<signature.length; i++){

			System.out.print(signature[i] + " " +read2[i]+"\t");

			if (signature[i]!=read2[i]){
				equal = false;
				break;
			}
		}
		System.out.println("");
		
		if (!equal){
			System.out.println("Read 2 and signature not equal");
		}
		
		
		
	}
	
	public static void main(String[] args) throws Exception {

		initiliazeCard();
		System.exit(0);

		SmartIdentityCard sm = new SmartIdentityCard();
		
		
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
