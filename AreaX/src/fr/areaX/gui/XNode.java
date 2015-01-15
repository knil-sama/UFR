package fr.areaX.gui;

public interface XNode {
	
	public static final int TO_PIN_SCREEN = 0;
	public static final int TO_CAMERA_SCREEN = 1;
	public static final int SMART_CARD_TERMINAL_ERROR = 2;
	public static final int NO_SMART_CARD = 3;
	public static final int SMART_CARD_VERIFIED = 4;
	public static final int SMART_CARD_REFUSED = 5;
	public static final int INITIALISATION_ERROR = 6;
	public static final int SMART_CARD_IO_ERROR = 7;
	public static final int SMART_CARD_UPDATED = 8;
	public static final int IMAGE_PROCESSING_ERROR = 9;
	public static final int BIOMETRY_ACCEPTED = 10;
	public static final int BIOMETRY_REJECTED = 11;
	public static final int CREATE_NEW_USER = 12;
	public static final int NEW_USER_CREATED = 13;

	public void onEvent(String source, int eventType, Object args);
	
}
