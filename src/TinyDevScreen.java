/**
 * TinyDev - Programming language for kids
 *
 * Copyright (C) 2017-2022 Miguel I. Garc�a L�pez (FloppySoftware).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Read the file `LICENSE.MD` for more details.
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

/**
 * Clase que implementa la entrada / salida de teclado, rat�n y pantalla.
 * 
 * Esta clase es totalmente dependiente del entorno (Swing, Android, etc.).
 * 
 * IMPORTANTE
 * ----------
 * 
 * Swing no es thread-safe, por lo que el constructor de la clase, ha de ser
 * llamado desde el EDT (Event Dispatch Thread) de Swing.
 * 
 * Como todos los m�todos gr�ficos de esta clase hacen uso del m�todo show(),
 * que a su vez hace uso del m�todo repaint(), que s� es thread-safe, el int�rprete
 * puede llamar a dichos m�todos desde otros threads, sin problemas.
 * 
 * @author Miguel
 */
public class TinyDevScreen implements KeyListener {
	
	public static final int MIN_SCREEN_WIDTH = 256;
	public static final int MIN_SCREEN_HEIGHT = 256;
	public static final int MAX_SCREEN_WIDTH = 800;
	public static final int MAX_SCREEN_HEIGHT = 800;

	public static final int DEFAULT_SCREEN_WIDTH = 300;	// Ancho de pantalla en pixels por defecto
	public static final int DEFAULT_SCREEN_HEIGHT = 300;   // Alto  de pantalla en pixels por defecto
	
	private Color pen;         // Color de pluma actual
	private Color paper;       // Color de papel (fondo) actual
	
	private boolean fontTransp;  // Fondo transparente para texto?
	
	private int screen_width;  // Ancho de pantalla en pixels
	private int screen_height; // Alto  de pantalla en pixels
	
	private String key;        // Buffer de teclado
	
	private int ypos;          // Posici�n Y actual del cursor de texto
	private int xpos;          // Posici�n X actual del cursor de texto
	
	private int row_pixels;    // Alto  de caracter en pixels
	private int col_pixels;    // Ancho de caracter en pixels
	
	private int offset_y;      // Desplazamiento Y de caracter
	
	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16); // Fuente por defecto
	
	private JFrame frame;      // Ventana
	
	private JLabel label;      // Componente donde ir� el buffer de pantalla
	
	private int mouseButton;
	
	private Graphics2D graphics = null;   // Manejador de gr�ficos
	
	private BufferedImage screen;  // Buffer de pantalla (bitmap)
	
	/**
	 * Constructor.
	 * 
	 * Crea e inicializa la ventana.
	 * 
	 * @param title T�tulo de la ventana
	 */
	public TinyDevScreen() {
		
		frame = new JFrame();          // Nueva ventana
		frame.setTitle("");            // T�tulo por defecto
		frame.setIconImage(new ImageIcon(getClass().getResource("/AppIcon.png")).getImage());
		//frame.setSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);   // Tama�o inicial
		frame.setResizable(false);     // No redimensionable
		frame.addKeyListener(this);    // Escuchador de teclado
		frame.setFocusable(true);      // Es necesario que pueda tener el foco para el escuchador de teclado
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // Estado al cerrar (dejar en el limbo)
		frame.requestFocusInWindow();  // Tomar el foco
		
		label = new JLabel();          // Componente que contendr� el bitmap
		
		label.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	mouseButton = e.getButton();
		    }
		});
		
        frame.setContentPane(label);   // A�adir el componente
        
        reset(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);                       // Finalizar inicializaci�n
	}
	
	/**
	 * Inicializaci�n de la pantalla.
	 * 
	 * @param width   ancho del �rea de trabajo en pixels
	 * @param height  alto  del �rea de trabajo en pixels
	 * @return        true en caso de error, false en caso contrario
	 */
	public boolean reset(int width, int height) {
		
		boolean result = false;
	
		if(width < MIN_SCREEN_WIDTH || width > MAX_SCREEN_WIDTH
				|| height < MIN_SCREEN_HEIGHT || height > MAX_SCREEN_HEIGHT) {
			
			result = true;
			
			width = DEFAULT_SCREEN_WIDTH;
			height = DEFAULT_SCREEN_HEIGHT;
		}
		
		screen_width = width;           // Ancho del bitmap en pixels
		screen_height = height;         // Alto  del bitmap en pixels
		
		frame.setSize(width, height);   // Fijar tama�o de la ventana
		
		// Crear buffer para el bitmap
		
		//screen = new BufferedImage(screen_width, screen_height, BufferedImage.TYPE_INT_ARGB); // 8-bit RGBA
		screen = new BufferedImage(screen_width, screen_height, BufferedImage.TYPE_INT_RGB);
		
		if(graphics != null)
			graphics.dispose();
		
		graphics = screen.createGraphics(); // Manejador de gr�ficos
		graphics.setFont(font);             // Fuente por defecto
		
		//FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());  // Tomar m�trica de la fuente
		//
		//row_pixels = metrics.getHeight();      // Tomar alto  en pixels
		//col_pixels = metrics.stringWidth("W"); // Tomar ancho en pixels
		//
		//offset_y = metrics.getAscent() + metrics.getLeading(); // Offset vertical
		
		// Antialiasing
		
        //RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        //graphics.addRenderingHints(hints);

        label.setIcon(new ImageIcon(screen));  // Agregar el bitmap al componente que lo mostrar�
        
		pen = Color.BLACK;   // Color de pluma
		paper = Color.WHITE; // Color de papel
		
		fontTransp = false;
		
		ypos = 0;            // Posici�n Y actual del cursor
		xpos = 0;            // Posici�n X actual del cursor
		
		key = null;
		
		mouseButton = MouseEvent.NOBUTTON;
        
        frame.setVisible(true);  // Mostrar ventana
		
		frame.pack();            // Adaptar el tama�o de la ventana a su contenido
		
		clear();                 // Borrar el bitmap
		
		return result;
	}

	/**
	 * Cerrar la terminal.
	 */
	public void quit() {

		// Enviar evento de cierre de la ventana
		
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	public boolean isClosed() {
		return !frame.isShowing();
	}
	
	/**
	 * Cambiar el t�tulo de la ventana.
	 * 
	 * @param title  T�tulo
	 */
	public void setTitle(String title) {
		
		frame.setTitle(title);
	}
	
	/**
	 * Leer una l�nea de texto desde el teclado.
	 * 
	 * @return Texto le�do
	 */
	public String readLine(String prompt, String title) {
		
		String input = (String)JOptionPane.showInputDialog(frame, prompt, title, JOptionPane.QUESTION_MESSAGE);

		if(input == null)
			input = "";
		
		// Devolver cadena introducida
		
		return input;
	}
	
	public void dialogInformation(String message, String title) {
		
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void dialogWarning(String message, String title) {
		
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	public void dialogError(String message, String title) {
		
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	public String dialogYesNo(String message, String title) {
		
		int reply = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		return (reply == JOptionPane.YES_OPTION ? "yes" : "no");
			
	}
	
	public String dialogOkCancel(String message, String title) {
		
		int reply = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		return (reply == JOptionPane.OK_OPTION ? "ok" : "cancel");
			
	}
	
	
	/**
	 * Imprimir una cadena de texto en la pantalla.
	 * 
	 * @param txt  Texto
	 */
	public void print(String txt) {

		// Recorrer la cadena de texto
		
		for(char ch : txt.toCharArray()) {
			if(ch == '\n')
				printLn();
			else
				printChar(ch);  // Imprimir car�cter
		}
		
		show();
	}
	
	public int getTextWidth(String txt) {
		
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());  // Tomar m�trica de la fuente
		
		return metrics.stringWidth(txt); // Tomar ancho en pixels
	}
	
	public int getTextHeight() {
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());  // Tomar m�trica de la fuente
		
		return metrics.getHeight();
	}

	/**
	 * Mover la posici�n actual, a la siguiente fila,
	 * columna 0.
	 */
	private void printLn() {
		
		// Columna 0
		
		xpos = 0;
		
		// Siguiente fila
		
		ypos += row_pixels;
		
		if(ypos >= screen_height) {
			
			ypos = 0; //
		}
	}

	/**
	 * Borrar la pantalla, y situar el cursor
	 * en la fila 0, columna 0.
	 */
	public void clear() {
		
		// Borrar la pantalla
		
		graphics.setColor(paper);
		graphics.fillRect(0, 0, screen_width, screen_height);
		
		// Actualizar
		
		show();
		
		// Fila 0, columna 0
		
		ypos = 0;
		xpos = 0;
	}
	

	public boolean setPen(String colorName) {
		
		return setColor(colorName, true);
	}
	

	public boolean setPaper(String colorName) {
		
		return setColor(colorName, false);
	}
	
	private boolean setColor(String colorName, boolean forPen) {
		
		Color color;
		
		if(colorName.equalsIgnoreCase("black"))
			color = Color.BLACK;
		else if(colorName.equalsIgnoreCase("blue"))
			color = Color.BLUE;
		else if(colorName.equalsIgnoreCase("cyan"))
			color = Color.CYAN;
		else if(colorName.equalsIgnoreCase("light_gray"))
			color = Color.LIGHT_GRAY;
		else if(colorName.equalsIgnoreCase("gray"))
			color = Color.GRAY;
		else if(colorName.equalsIgnoreCase("dark_gray"))
			color = Color.DARK_GRAY;
		else if(colorName.equalsIgnoreCase("magenta"))
			color = Color.MAGENTA;
		else if(colorName.equalsIgnoreCase("orange"))
			color = Color.ORANGE;
		else if(colorName.equalsIgnoreCase("pink"))
			color = Color.PINK;
		else if(colorName.equalsIgnoreCase("red"))
			color = Color.RED;
		else if(colorName.equalsIgnoreCase("white"))
			color = Color.WHITE;
		else if(colorName.equalsIgnoreCase("yellow"))
			color = Color.YELLOW;
		else if(colorName.equalsIgnoreCase("green"))
			color = Color.GREEN;
		else
			return true;
		
		if(forPen)
			pen = color;
		else
			paper = color;
		
		return false;
	}
	
	/**
	 * Imprimir un car�cter en la pantalla.
	 * 
	 * @param ch  Car�cter
	 */
	private void printChar(char ch) {

		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());  // Tomar m�trica de la fuente
		
		row_pixels = metrics.getHeight();      // Tomar alto  en pixels
		col_pixels = metrics.charWidth(ch); // Tomar ancho en pixels

		offset_y = metrics.getAscent() + metrics.getLeading(); // Offset vertical
		
		if(xpos + col_pixels > screen_width || ypos + row_pixels > screen_height)
			printLn();
		
		// Borrar la celda
		
		if(!fontTransp) {
			graphics.setColor(paper);
			graphics.fillRect(xpos, ypos, col_pixels, row_pixels);
		}
		
		// Imprimir el car�cter
		
		// Nota: Como ypos se refiere a la base del texto (baseline), es
		//       preciso a�adir offset_y.
		
		// http://www.javaworld.com/article/2076869/learn-java/drawing-text-is-easy-with-three-java-classes.html
		
		graphics.setColor(pen);
		graphics.drawString("" + ch, xpos, ypos + offset_y);
		
		// Actualizar la pantalla
		
		//show();
		
		// Actualizar el n� de columna actual, y
		// mover la posici�n actual a la siguiente fila, columna 0, si procede
		
		xpos += col_pixels;
		
		if(xpos >= screen_width)
			printLn();
	}
	

	
	public boolean cursorTo(int x, int y) {
		
		if(isValidPos(x, y)) {
		
			xpos = x;
			ypos = y;
			
			return false;
		}
		
		return true;
	}
	
	public boolean ellipse(int x, int y, int width, int height, TinyDevLang.Style style) {
		
		if(isValidRect(x, y, width, height)) {
			graphics.setColor(pen);
			
			switch(style) {
				case FILL :
					graphics.fillOval(x, y, width, height);
					graphics.setColor(paper);
					graphics.fillOval(x + 1, y + 1, width - 2, height - 2);
					break;
				case SOLID :
					graphics.fillOval(x, y, width, height);
					break;
				default :
					graphics.drawOval(x, y, width - 1, height - 1); // Ver API
					break;
			}
			
			show();
			
			return false;
		}
		
		return true;
	}
	
	public boolean line(int x, int y, int x2, int y2) {
		
		if(isValidPos(x, y) && isValidPos(x2, y2)) {
		
			graphics.setColor(pen);
			graphics.drawLine(x, y, x2, y2);
			
			show();
			
			return false;
		}
		
		return true;
	}
	
	public boolean rectangle(int x, int y, int width, int height, TinyDevLang.Style style) {
		
		// Ajustar valores (ver API, drawRect y fillRect difieren)
		
		//--width;
		//--height;
		
		if(isValidRect(x, y, width, height)) {
			graphics.setColor(pen);
			
			switch(style) {
				case FILL :
					graphics.drawRect(x, y, width - 1, height - 1);
					graphics.setColor(paper);
					graphics.fillRect(x + 1, y + 1, width - 2, height - 2);
					break;
				case SOLID :
					graphics.fillRect(x, y, width, height);
					break;
				default :
					graphics.drawRect(x, y, width - 1, height - 1);
					break;
			}
			
			show();
			
			return false;
		}
		
		return true;
	}
	
	public boolean point(int x, int y) {
		
		if(isValidPos(x, y)) {
			graphics.setColor(pen);
			graphics.drawLine(x, y, x, y);
			
			show();
			
			return false;
		}
		
		return true;
	}
	
	public void setFontFace(String face) {
		
		graphics.setFont(new Font(face, graphics.getFont().getStyle(), graphics.getFont().getSize()));
	}
	
	//public void setFontStyle(int style) {
	//	graphics.setFont(new Font(graphics.getFont().getFamily(), style, graphics.getFont().getSize()));
	//}
	
	public void setFontSize(int size) {
		graphics.setFont(new Font(graphics.getFont().getFamily(), graphics.getFont().getStyle(), size));
	}
	
	public void setFontStyle(int style) {
		int fontStyle = Font.PLAIN;
		
		if((style & TinyDevLang.FONT_STYLE_BOLD) == TinyDevLang.FONT_STYLE_BOLD) // Java no es como C...
			fontStyle |= Font.BOLD;
		
		if((style & TinyDevLang.FONT_STYLE_ITALIC) == TinyDevLang.FONT_STYLE_ITALIC)
			fontStyle |= Font.ITALIC;
		
		graphics.setFont(new Font(graphics.getFont().getFamily(), fontStyle, graphics.getFont().getSize()));
		
		if((style & TinyDevLang.FONT_STYLE_TRANSPARENT) == TinyDevLang.FONT_STYLE_TRANSPARENT)
			fontTransp = true;
		else
			fontTransp = false;
	}
	
	public Object loadImage(String filename) {
		
		BufferedImage img = null;
		
		try {
		    img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			//
		}
		
		return img;		
	}
	
	public boolean saveImage(String filename, Object img) {
		String format;
		int dotPos = filename.indexOf('.');
		
		if(dotPos >= 0) {
			format = filename.substring(dotPos + 1).toLowerCase();
		}
		else {
			format = "png";
			filename = filename.concat("." + format);
		}
		
		try {
			ImageIO.write((BufferedImage) img, format, new File(filename));
		} catch (IOException e) {
			//
			return true;
		}
		
		return false;
	}
	
	public boolean drawImage(int x, int y, Object img) {
		
		BufferedImage image = (BufferedImage) img;
		
		if(isValidRect(x, y, image.getWidth(), image.getHeight())) {
			graphics.drawImage(image, x, y, null);
		
			show();
			
			return false;
		}
		
		return true;
		
	}
	
	public Object getImage(int x, int y, int width, int height) {
		
		BufferedImage img = null;
		
		if(width <= 0 || height <= 0) {
			x = y = 0;
			width = screen_width;
			height = screen_height;
		}
		
		if(isValidRect(x, y, width, height)) {
			try {
				img = screen.getSubimage(x, y, width, height);
			} catch (RasterFormatException e) {
				//
			}
		}
		
		return img;		
	}
	
	public Object loadSound(String filename) {
		
		// FIXME: Implementar carga de fichero de sonido y almacenamiento en un objeto.
		
		// https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
		
		// De momento, devuelve la misma cadena de entrada.
		
		return filename;
	}
	
	public void playSound(Object snd) {
		
		// FIXME: De momento, el objeto es un string que apunta al
		// nombre del fichero - ver loadSound().
		
		File soundFile = new File((String) snd);
		AudioInputStream audioIn;
		Clip clip = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//clip.start();
		
		// FIXME: Cuando cerrar audioIn y clip ? ? ?
		
		//audioIn.close();
		//clip.close();



		
	}
	
	public int getRow() {
		return ypos;
	}
	
	public int getColumn() {
		return xpos;
	}
	
	public int getWidth() {
		return screen_width;
	}
	
	public int getHeight() {
		return screen_height;
	}
	

	
	
	/**
	 * Actualizar la pantalla.
	 */
	private void show() {

		// Dibujar la pantalla
		
		graphics.drawImage(screen, 0, 0, null);
		
		// Repintar -- NOTA: repaint() es thread-safe
		
		frame.repaint();
	}
	
	public String getMouseClick() {
		int btn = mouseButton;
		mouseButton = MouseEvent.NOBUTTON;
		
		switch(btn) {
			case MouseEvent.BUTTON1 :
				return "left";
			case MouseEvent.BUTTON2 :
				return "middle";
			case MouseEvent.BUTTON3 :
				return "right";
		}
		
		return "";
	}
	
	public int getMouseX() {
		Point p = label.getMousePosition();
		
		if(p != null) {
			return (int) p.getX();
		}
		
		return -1;
	}
	
	public int getMouseY() {
		Point p = label.getMousePosition();
		
		if(p != null) {
			return (int) p.getY();
		}
		
		return -1;
	}
	
	public String getKey() {
		String k = key;
		key = null;
		return k;
	}
	

	/**
	 * Listener para car�cteres introducidos.
	 */
    @Override
    public void keyTyped(KeyEvent e) {

    		char c = e.getKeyChar();
    		
    		if(c != KeyEvent.CHAR_UNDEFINED)
    			key = "" + c;
    		
    		e.consume();

    }


    @Override
    public void keyPressed(KeyEvent e) {
    	// Nada
    }


    @Override
    public void keyReleased(KeyEvent e) {
    	// Nada
    	
    	//key = KeyEvent.getKeyText(e.getKeyCode());

    	switch(e.getKeyCode()) {
    		case KeyEvent.VK_KP_UP :
    		case KeyEvent.VK_UP :
    			key = "up";
    			break;
    		case KeyEvent.VK_KP_DOWN :
    		case KeyEvent.VK_DOWN :
    			key = "down";
    			break;
    		case KeyEvent.VK_KP_LEFT :
    		case KeyEvent.VK_LEFT :
    			key = "left";
    			break;
    		case KeyEvent.VK_KP_RIGHT :
    		case KeyEvent.VK_RIGHT :
    			key = "right";
    			break;
    		case KeyEvent.VK_ENTER :
    			key = "enter";
    			break;
    		case KeyEvent.VK_ESCAPE :
    			key = "escape";
    			break;
    		case KeyEvent.VK_HOME :
    			key = "home";
    			break;
    		case KeyEvent.VK_END :
    			key = "end";
    			break;
    		case KeyEvent.VK_PAGE_UP :
    			key = "page_up";
    			break;
    		case KeyEvent.VK_PAGE_DOWN :
    			key = "page_down";
    			break;
    		
    		default :
    			key = null;
    			break;
    	}
    	
    	if(key != null)
    		e.consume();
    	
    	// consume the event??? FIXME
    			
    }
    
    /**
	 * Helper.
	 * 
	 * Comprueba que una posici�n en la ventana de ejecuci�n, es v�lida. Si no lo
	 * es, indica el error en errorId.
	 * 
	 * @param x  posici�n X
	 * @param y  posici�n Y
	 * @return   true si es v�lida, false en caso contrario
	 */
	public boolean isValidPos(int x, int y) {
		
		// Comprobar que la posici�n es v�lida
		if(x >= 0 && x < screen_width && y >= 0 && y < screen_height)
			return true;
		
		// No es v�lida
		return false;
	}
	
	/**
	 * Helper.
	 * 
	 * Comprueba que la posici�n de un rect�ngulo en la ventana de ejecuci�n, es
	 * v�lida. Si no lo es, indica el error en errorId.
	 * 
	 * @param x       posici�n X
	 * @param y       posici�n Y
	 * @param width   ancho en pixels
	 * @param height  alto en pixels
	 * @return        true si es v�lida, false en caso contrario
	 */
	public boolean isValidRect(int x, int y, int width, int height) {
		
		return isValidPos(x, y) && isValidPos(x + width - 1, y + height - 1);
	}
    
  
}

