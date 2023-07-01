import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;

public class Icons {
	
	public static ImageIcon mineIcon() {
		return new ImageIcon("mine.png");
	}
	
	public static ImageIcon flagIcon() {
		return new ImageIcon("flag.png");
	}
	
	public static ImageIcon numberIcon(int n) {
		switch (n) {
			case 1: return new ImageIcon("one.png"); 
			case 2: return new ImageIcon("two.png"); 
			case 3: return new ImageIcon("three.png"); 
			case 4: return new ImageIcon("four.png"); 
			case 5: return new ImageIcon("five.png"); 
			case 6: return new ImageIcon("six.png"); 
			case 7: return new ImageIcon("seven.png"); 
			case 8: return new ImageIcon("eight.png"); 
		}
		return null;
	}
	
	public static ImageIcon blueIcon() {
		BufferedImage result = new BufferedImage(25, 25, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphic = (Graphics2D)result.getGraphics();
		graphic.setColor(Color.BLUE);
		graphic.fillRect(0, 0, 25, 25);
		return new ImageIcon(result);
	}
}
