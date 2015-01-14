package fr.areaX.smartcard;

import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class SmartCardDrive {
    private CardTerminal currentTerminal;
    private Card currentCard;
    
    public List<CardTerminal> getTerminals() throws CardException {
        return TerminalFactory.getDefault().terminals().list();
    }
    
    public void selectFirstTerminal() throws CardException {
    	if(getTerminals().size() > 0) {
    		currentTerminal = getTerminals().get(0);
    		
    		System.out.println("Terminal at index 0 selected as current terminal");
    	} else {
    		System.err.println("FATAL: No Card Terminal detected");
    		throw new CardException("FATAL: No Card Terminal detected");
    	}
    }
    
    public void connectCard() throws CardException {
    	currentCard = currentTerminal.connect("T=0");
        System.out.println(toString(currentCard.getATR().getBytes()));
        System.out.println(currentCard.getProtocol());
    }

    public Card getCurrentCard(){
    	return currentCard;
    }

    public static String toString(byte[] byteTab){
        String texte="";
        String hexNombre;
        for(int i=0;i<byteTab.length;i++){
                hexNombre="";
                hexNombre=Integer.toHexString(byteTab[i]);
                if(hexNombre.length()==1){
                    texte+=" 0"+hexNombre;
                }
                else{
                    texte+=" "+hexNombre;
                }
        }
        return texte;
    }
    
  public static void main(String[] args) throws CardException {
	  
    	SmartCardDrive cardDriver = new SmartCardDrive();    	
    	
    	TerminalFactory factory = TerminalFactory.getDefault();
    	    	
    	List<CardTerminal> terminauxDispos = cardDriver.getTerminals();
        //Premier terminal dispo

    	System.out.println(terminauxDispos);
    	CardTerminal terminal = terminauxDispos.get(0);
        System.out.println(terminal.toString());
        
        
        //Connexion à la carte
        Card card = terminal.connect("T=0");
        //ATR (answer To Reset)
        
        CardChannel channel = card.getBasicChannel();
        CommandAPDU commande ;
        ResponseAPDU r;
        
        commande = new CommandAPDU(0x80,0xBE,0x00,0x10,0x40);//lecture
        r = channel.transmit(commande);
        System.out.println(cardDriver.toString(r.getData()));
        System.out.println("la premiere");
        
        String texte=new String();

        
        byte apdu[]={(byte)0x00,(byte) 0x20,(byte)0x00,(byte)0x07,(byte)0x04,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA};
        commande = new CommandAPDU(apdu);//test code pin
        r = channel.transmit(commande);
        System.out.println("reponse : " + "SW1 : 0x"+Integer.toHexString(r.getSW1())+" SW2 : 0x"+Integer.toHexString(r.getSW2())+" data : "+cardDriver.toString(r.getData()));
        System.out.println(texte);
        System.out.println("fin lecture");
        System.out.println("debut ecriture");
        byte apdu2[]={(byte)0x80,(byte) 0xDE,(byte)0x00,(byte)0x10,(byte)0x04,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01};
        commande = new CommandAPDU(apdu2);//test code pin
        r = channel.transmit(commande);
        System.out.println("reponse2 : " + "SW1 : 0x"+Integer.toHexString(r.getSW1())+" SW2 : 0x"+Integer.toHexString(r.getSW2())+" data : "+cardDriver.toString(r.getData()));
        System.out.println(texte);
        System.out.println("lecture");
        commande = new CommandAPDU(0x80,0xBE,0x00,0x10,0x40);//lecture
        r = channel.transmit(commande);
        System.out.println(cardDriver.toString(r.getData()));
        card.disconnect(false);

    }
    

}
