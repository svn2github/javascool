

import java.awt.Color;

import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class Editor extends JTextPane 
    {
    
   
	private static final long serialVersionUID = 1L;

    public Editor() {
        super();
        this.setFont(new Font("Courier", this.getFont().getStyle(), this.getFont().getSize()));
        this.setCaretColor(Color.BLUE);
        
        try {
        doc = this.getStyledDocument();
        
        //Style "normal":
        normal = doc.addStyle("normal", null);
        StyleConstants.setForeground(normal, Color.black);
        StyleConstants.setBold(normal, false);
        
        //Style Code:
        CodeStyle = doc.addStyle("Code", null);
        StyleConstants.setForeground(CodeStyle, new Color(254,136,0));
        StyleConstants.setBold(CodeStyle, true);
        
      
        StringStyle = doc.addStyle("String", null);
        StyleConstants.setForeground(StringStyle, Color.BLUE);
        StyleConstants.setBold(StringStyle, false);
        
        //Style operateur:
        OperatorStyle = doc.addStyle("Operateur", null);
        StyleConstants.setForeground(OperatorStyle, Color.black);
        StyleConstants.setBold(OperatorStyle, true);
       
        
      
        //Style Boucles:
        BouclesStyle = doc.addStyle("Boucles", null);
        StyleConstants.setForeground(BouclesStyle, new Color(0, 120, 80));
        StyleConstants.setBold(BouclesStyle, true);
        
       
      
        
        
        this.colorise(0, doc.getLength(), doc.getText(0, doc.getLength()));
        
        courant = doc.getText(0, doc.getLength());
      
        
        } catch (BadLocationException ble) {
                ble.printStackTrace();
        }
    }
    
    public void colorise(int length) {
        try {
            String text = doc.getText(0, doc.getLength());
            int curs, i, j, k;
            
                if(!courant.equals(text)) {
                
                    curs = this.getCaretPosition();
                    k = length;
                    if((i=curs-50)<0) i=0;
                    if((j=curs+50)>text.length()) j=text.length();
                    
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
                }
                
                courant = doc.getText(0, doc.getLength());
            } catch (BadLocationException ble) {
                ble.printStackTrace();
            }
    }
    
    public void colorise(boolean all) {
        try {
            String text = doc.getText(0, doc.getLength());
            int curs, i, j, k;
            String tmp = courant;
            courant = text;
            
                if(!tmp.equals(text) && !all) {
                
                    curs = this.getCaretPosition();
                    k = text.length()-tmp.length();
                    if((i=curs-50)<0) i=0;
                    if((j=curs+50)>text.length()) j=text.length();
                    
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
    
    public void colorise(int start, int end, String text) {
    	  int i=0, j;
    	Matcher matcher;
        doc.setCharacterAttributes (start, end-start, normal, true);
        
        colorise(Operator, OperatorStyle, text, start);
      
       
        colorise(Boucles, BouclesStyle, text, start);
       
      
        
        colorise(Code, CodeStyle, text, start);
        matcher = Pattern.compile("(\".*?\")", Pattern.MULTILINE).matcher(text);
        i=j=0;
        while(matcher.find(i)) {
            i = matcher.end();
            j = matcher.start();
            doc.setCharacterAttributes (j+start, i-j, StringStyle, true);
        }
   
    }
    
    public void colorise(String[] tab, Style style, String text, int start) {
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
            	if(j!=0)
            	{
            	c=	text.charAt(j-1);
            	}
            	c1=text.charAt(j+mot.length());
            	
            	System.out.println(c1);
            if (((c1=='.')||(c1=='(')||(c1==')')||(c1=='{')||(c1=='{')||(c1=='}')||(c1=='\n')||(c1=='\r')||(c1=='\b')||(c1=='\f')||(c1=='\t')||(c1==' '))&&((c==')')||(c=='{')||(c=='{')||(c=='}')||(c=='\n')||(c=='\r')||(c=='\b')||(c=='\f')||(c=='\t')||(c==' ')||(c=='.'))) 
            	{ i = j;
                
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
    
   
    
    
   
    
   
    
    
    
    Style CodeStyle;
    Style normal;
    Style OperatorStyle;
    Style BouclesStyle;
    Style ConditionsStyle;
    Style  StringStyle;
   
    
    
    StyledDocument doc;
    String courant;
  
    String[] Code = {  "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", " const", " continue", "default", "double", "do", 
    	    "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", 
    	    "long", "native", "new ", " null ","main", " package ", "private", "protected", " public ","print", "return", "short", "static", "strictfp", "super", "switch", "synchronized", 
    	    "this", " throw", "throws", "transient", "true", "try", "void", "volatile", "while",  "echo", "equal", "sqrt", "pow", "random", "now", "sleep", "show",
    	    "clear", "println", "readString", "readInt", "readInteger", "readDouble", "readFloat", "readBoolean", 
    	    "dichoLength","dichoCompare",
    	    "smileyReset", "smileyLoad", "smileySet", "smileyGet",  
    	    "scopeReset ", "scopeSet", "scopeAdd",  "scopeAddLine", "scopeAddRectangle", "scopeAddCircle", "scopeX", "scopeY",
    	    "convaOut",  "convaCompare","Math",
    	    "synthePlay", "syntheSet", "@tone"};
 
    String[] Operator = {"^", "(", ")", "+", "-", "*", "/", "|","{","}"};
    String[] Boucles = { "for" ,"while" };
    String[] Separateur={"\n","\r","\b","\f","\t"};
     
     
    
}
