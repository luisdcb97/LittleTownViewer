package littletownviewer.interactive;

import processing.event.KeyEvent;

/**
 * <p>Interface for processing keyboard input events</p>
 *
 * @see MouseClick
 * @see MouseDrag
 * @see MouseScroll
 *
 * @author Luis David
 */
public interface KeyboardInput {
    /**
     * <p>The <code>keyTyped()</code> function is called once
     * every time a key is pressed, but action keys such as
     * Ctrl, Shift, and Alt are ignored.</p>
     *
     * <p>Because of how operating systems handle key repeats,
     * holding down a key may cause multiple calls to
     * <code>keyTyped()</code>. The rate of repeat is set by
     * the operating system, and may be configured differently
     * on each computer.</p>
     */
    void keyTyped();

    /**
     * <p>The <code>keyTyped()</code> function is called once
     * every time a key is pressed, but action keys such as
     * Ctrl, Shift, and Alt are ignored.</p>
     *
     * <p>Because of how operating systems handle key repeats,
     * holding down a key may cause multiple calls to
     * <code>keyTyped()</code>. The rate of repeat is set by
     * the operating system, and may be configured differently
     * on each computer.</p>
     *
     * @param event Holds the <code>keyCode</code> for the key typed
     */
    void keyTyped(KeyEvent event);

    /**
     * <p>The {@code keyPressed()} function is called once every time a key is
     * pressed. The key that was pressed is stored in the
     * {@link littletownviewer.MySketch#key key} variable.</p>
     *
     * <p>For non-ASCII keys, use the
     * {@link littletownviewer.MySketch#keyCode keyCode} variable. The keys
     * included in the ASCII specification (BACKSPACE, TAB, ENTER, RETURN,
     * ESC, and DELETE) do not require checking to see if the key is
     * coded; for those keys, you should simply use the {@code key} variable
     * directly (and not {@code keyCode}). If you're making cross-platform
     * projects, note that the ENTER key is commonly used on PCs and Unix,
     * while the RETURN key is used on Macs. Make sure your program will
     * work on all platforms by checking for both ENTER and RETURN.</p>
     *
     * <p>Because of how operating systems handle key repeats,
     * holding down a key may cause multiple calls to
     * <code>keyPressed()</code>. The rate of repeat is set by
     * the operating system, and may be configured differently
     * on each computer.</p>
     */
    void keyPressed();

    /**
     * <p>The {@code keyPressed()} function is called once every time a key is
     * pressed. The key that was pressed is stored in the
     * {@link littletownviewer.MySketch#key key} variable.</p>
     *
     * <p>For non-ASCII keys, use the
     * {@link littletownviewer.MySketch#keyCode keyCode} variable. The keys
     * included in the ASCII specification (BACKSPACE, TAB, ENTER, RETURN,
     * ESC, and DELETE) do not require checking to see if the key is
     * coded; for those keys, you should simply use the {@code key} variable
     * directly (and not {@code keyCode}). If you're making cross-platform
     * projects, note that the ENTER key is commonly used on PCs and Unix,
     * while the RETURN key is used on Macs. Make sure your program will
     * work on all platforms by checking for both ENTER and RETURN.</p>
     *
     * <p>Because of how operating systems handle key repeats,
     * holding down a key may cause multiple calls to
     * <code>keyPressed()</code>. The rate of repeat is set by
     * the operating system, and may be configured differently
     * on each computer.</p>
     *
     * @param event Holds the <code>keyCode</code> for the key released
     */
    void keyPressed(KeyEvent event);

    /**
     * <p>The <code>keyReleased()</code> function is called once
     * every time a key is released. The key that was released
     * will be stored in the {@link littletownviewer.MySketch#key key}
     * variable.</p>
     */
    void keyReleased();

    /**
     * <p>The <code>keyReleased()</code> function is called once
     * every time a key is released. The key that was released
     * will be stored in the {@link littletownviewer.MySketch#key key}
     * variable.</p>
     *
     * @param event Holds the <code>keyCode</code> for the key released
     */
    void keyReleased(KeyEvent event);
}
