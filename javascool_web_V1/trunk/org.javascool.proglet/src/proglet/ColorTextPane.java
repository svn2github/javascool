/*******************************************************************************
 * Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
 *******************************************************************************/

package proglet;

// Used to define the widget
import javax.swing.JTextPane;

// Used to define the styles
import java.awt.Color;
import java.awt.Font;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

// Used to manage the colorization
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/** This widget defines a colored editor for the proglet source editor.
 * @see <a href="ColorTextPane.java">source code</a>
 */
public class ColorTextPane extends JTextPane implements KeyListener {
  private static final long serialVersionUID = 1L;

  public ColorTextPane() {
    this.setFont(new Font("Courier", this.getFont().getStyle(), this.getFont().getSize()));
    this.setCaretColor(Color.BLUE);
        
    doc = this.getStyledDocument();
        
    //Style Normal
    NormalStyle = doc.addStyle("Normal", null);
    StyleConstants.setForeground(NormalStyle, Color.BLACK);
    StyleConstants.setBold(NormalStyle, false);
    
    //Style Code:
    CodeStyle = doc.addStyle("Code", null);
    StyleConstants.setForeground(CodeStyle, Color.ORANGE);
    StyleConstants.setBold(CodeStyle, true);
    
    //Style String:
    StringStyle = doc.addStyle("String", null);
    StyleConstants.setForeground(StringStyle, Color.BLUE);
    StyleConstants.setBold(StringStyle, false);
    
    //Style operateur:
    OperatorStyle = doc.addStyle("Operateur", null);
    StyleConstants.setForeground(OperatorStyle, Color.BLACK);
    StyleConstants.setBold(OperatorStyle, true);
    
    //Style Boucles:
    BouclesStyle = doc.addStyle("Boucles", null);
    StyleConstants.setForeground(BouclesStyle, new Color(0, 120, 80));
    StyleConstants.setBold(BouclesStyle, true);
    
    try {
      colorise(0, doc.getLength(), doc.getText(0, doc.getLength()));
      
      courant = doc.getText(0, doc.getLength());
      
    } catch (BadLocationException ble) {
      ble.printStackTrace();
    }
    
    addKeyListener(this); 
  }

  public void setText(String text) {
    super.setText(text);
    colorise(true);
  }

  public void setCaretPosition(int position) {
    super.setCaretPosition(position);
    colorise(false);
  }
    
  private void colorise(int length) {
    try {
      String text = doc.getText(0, doc.getLength());
      int curs, i, j, k;
            
      if(!courant.equals(text)) {
                
	curs = this.getCaretPosition();
	k = length;
	if((i = curs-50) < 0) i=0;
	if((j = curs+50) > text.length()) j=text.length();
                    
	if(k > 0) if((i -= k) < 0) i=0;

	for(; i > 0 ;i--) {
	  if(text.charAt(i) == '\n') break;
	  else if(text.charAt(i) == '\r') break;
	}
	k = text.indexOf("\n", j);
	if(k==-1) k=text.indexOf("\r", j);
	if(k==-1) k=text.length();
	j = k;
                    
	this.colorise(i, j, text.substring(i, j));
                    
	this.setStyledDocument(doc);
	this.setCaretPosition(curs);
      }
                
      courant = doc.getText(0, doc.getLength());
    } catch (BadLocationException ble) {
      ble.printStackTrace();
    }
  }
    
  private void colorise(boolean all) {
    try {
      String text = doc.getText(0, doc.getLength());
      int curs, i, j, k;
      String tmp = courant;
      courant = text;
            
      if(!tmp.equals(text) && !all) {
                
	curs = this.getCaretPosition();
	k = text.length()-tmp.length();
	if((i = curs - 50) < 0)  i = 0;
	if((j = curs + 50)>text.length()) j = text.length();
                    
	if(k>0) if((i-=k)<0) i=0;

	for(;i>0;i--) {
	  if(text.charAt(i) == '\n') break;
	  else if(text.charAt(i) == '\r') break;
	}
	k = text.indexOf("\n", j);
	if(k==-1) k=text.indexOf("\r", j);
	if(k==-1) k=text.length();
	j = k;
                    
                    
	this.colorise(i, j, text.substring(i, j));
                    
	this.setStyledDocument(doc);
	this.setCaretPosition(curs);
      } else if(all)
	this.colorise(0, doc.getLength(), text);
                
    } catch (BadLocationException ble) {
      ble.printStackTrace();
    }
  }
    
  private void colorise(int start, int end, String text) {
    int i=0, j;
    Matcher matcher;
    doc.setCharacterAttributes (start, end-start, NormalStyle, true);
        
    colorise(Operator, OperatorStyle, text, start);
       
    colorise(Boucles, BouclesStyle, text, start);
        
    colorise(Code, CodeStyle, text, start);
    matcher = Pattern.compile("(\"([^\"]|\\\")*\")", Pattern.MULTILINE).matcher(text);
    i=j=0;
    while(matcher.find(i)) {
      i = matcher.end();
      j = matcher.start();
      doc.setCharacterAttributes (j+start, i-j, StringStyle, true);
    }
   
  }
    
  private void colorise(String[] tab, Style style, String text, int start) {
    String mot;
    int k,i,j,l;
    char c1;
    char c='A';
    if(tab == null) return;
    for(k=0; k < tab.length; k++) {
      mot = tab[k];
      i=j=0;
      while ((j = text.indexOf(mot, i))!=-1) {
	if(tab.length!=10){
	  if(j != 0) {
	    c = text.charAt(j - 1);
	  }
	  c1 = j + mot.length() < text.length() ? text.charAt(j + mot.length()) : '\0';
	  
	  //System.out.println(c1);
	  if (((c1=='.')||(c1=='(')||(c1==')')||(c1=='{')||(c1=='{')||(c1=='}')||(c1=='\n')||(c1=='\r')||(c1=='\b')||(c1=='\f')||(c1=='\t')||(c1==' '))&&((c==')')||(c=='{')||(c=='{')||(c=='}')||(c=='\n')||(c=='\r')||(c=='\b')||(c=='\f')||(c=='\t')||(c==' ')||(c=='.'))) 
	    { 
	      i = j;
	      doc.setCharacterAttributes (i+start, mot.length(), style, true);
	      //System.out.println(c1);
	      i += mot.length();
               
	    }
	  else{
	    i = j;
	    i += mot.length();
            	
	  }
	}
	else {
	  i = j;
                    
	  doc.setCharacterAttributes (i+start, mot.length(), style, true);
	  //System.out.println(c1);
	  i += mot.length();
                   
	}
      }
    }
  }
    
  public void keyPressed(KeyEvent e) {}
  
  public void keyTyped(KeyEvent e) {}

  public void keyReleased(KeyEvent e) {
    colorise(false);
  }
    
    
  private Style NormalStyle, CodeStyle, OperatorStyle, BouclesStyle, ConditionsStyle, StringStyle;
    
  private StyledDocument doc;
  private String courant;
  
  private static final String[] Code = {  
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", " const", " continue", "default", "double", "do", 
    "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", 
    "long", "native", "new ", " null ","main", " package ", "private", "protected", " public ","print", "return", 
    "short", "static", "strictfp", "super", "switch", "synchronized", 
    "this", " throw", "throws", "transient", "true", "try", "void", "volatile", "while",  "echo", "equal", "sqrt", "pow", "random", "now", "sleep", "show",
    "clear", "println", "readString", "readInt", "readInteger", "readDouble", "readFloat", "readBoolean", 
    "dichoLength","dichoCompare",
    "smileyReset", "smileyLoad", "smileySet", "smileyGet",  
    "scopeReset ", "scopeSet", "scopeAdd",  "scopeAddLine", "scopeAddRectangle", "scopeAddCircle", "scopeX", "scopeY",
    "convaOut",  "convaCompare","Math",
    "synthePlay", "syntheSet", "@tone"};
  
  private static final String[] Operator = {"^", "(", ")", "+", "-", "*", "/", "|","{","}"};
  private static final String[] Boucles = { "for" ,"while" };
  private static final String[] Separateur={"\n","\r","\b","\f","\t"};
}
