package blister.input;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import ext.csharp.Event;

/**
 * Static Class That Sends Events Generated By A Keyboard To A Set Of Listeners
 * @author Cristian
 *
 */
public class KeyboardEventDispatcher {
	/**
	 * Clipboard For Copy-Pasting
	 */
	private static final Clipboard sysCB = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	/**
	 * Event Called When A Character Is Received
	 */
	public static final Event<KeyPressEventArgs> OnReceiveChar = new Event<>(null);
	/**
	 * Event Called When A Key Is Pressed
	 */
    public static final Event<KeyboardKeyEventArgs> OnKeyPressed = new Event<>(null);
    /**
     * Event Called When A Key Is Released
     */
    public static final Event<KeyboardKeyEventArgs> OnKeyReleased = new Event<>(null);
    
    /**
     * Current Clipboard
     */
    static String clipboard = "";
    /**
     * Retrieve The Last Queried System Clipboard
     * @return Clipboard Data As A String 
     */
    public static String getClipboard() {
        return clipboard;
    }
    
    /**
     * Call The OnKeyPressed Event
     * @param e Event
     */
    public static void eventInput_KeyDown(KeyboardKeyEventArgs e) {
        OnKeyPressed.Invoke(e);
    }
    /**
     * Call The OnKeyReleased Event
     * @param e Event
     */
    public static void eventInput_KeyUp(KeyboardKeyEventArgs e) {
    	OnKeyReleased.Invoke(e);
    }
    /**
     * Call The OnReceiveChar Event
     * @param e Event
     */
    public static void eventInput_CharEntered(KeyPressEventArgs e) {
    	OnReceiveChar.Invoke(e);
    }
    
    /**
     * Set A String To The System Clipboard
     * @param s New Data
     */
    public static void setToClipboard(String s) {
    	clipboard = s;
    	StringSelection data = new StringSelection(clipboard);
        sysCB.setContents(data, data);
    }
    /**
     * Query The System For The Current Clipboard Data 
     * @return The New Data
     */
    public static String getNewClipboard() {
    	Transferable t = sysCB.getContents(null);
        if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
	            clipboard = (String)t.getTransferData(DataFlavor.stringFlavor);
			}
            catch (UnsupportedFlavorException e) {
			}
            catch (IOException e) {
			}
        }
    	return clipboard;
    }
}