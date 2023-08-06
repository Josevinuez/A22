package MVC;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * A custom OutputStream that directs its output to a JTextArea.
 * This allows for easy redirection of standard output streams to
 * a GUI component.
 */
public class CustomOutputStream extends OutputStream {
    /** The JTextArea to which the output stream will be redirected. */
    private JTextArea textArea;

    /**
     * Constructs a new CustomOutputStream that will redirect its output to the given JTextArea.
     * @param textArea The JTextArea to which output will be redirected.
     */
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * Writes the specified byte to the JTextArea. This method appends the
     * character associated with the byte to the JTextArea and ensures the
     * JTextArea scrolls to show the newly appended data.
     * @param b The byte to be written as a character to the JTextArea.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char) b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
