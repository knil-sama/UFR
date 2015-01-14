package fr.areaX.gui;

public interface XNode {
	
	public static final int TO_PIN_SCREEN = 0;
	public static final int TO_CAMERA_SCREEN = 1;

	public void onEvent(String source, int eventType, Object args);
	
}
