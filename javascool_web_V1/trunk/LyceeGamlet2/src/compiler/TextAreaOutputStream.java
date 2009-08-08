package compiler;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

class TextAreaOutputStream extends OutputStream {

   private JTextArea ta;

   public TextAreaOutputStream(JTextArea ta) {
      this.ta = ta;
   }

   public synchronized void write(int b) throws IOException {
      ta.append(String.valueOf((char) b));
   }
}