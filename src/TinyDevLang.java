/**
 * TinyDev - Programming language for kids
 *
 * Copyright (C) 2017-2022 Miguel I. García López (FloppySoftware).
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Clase que implementa el lenguaje de programación KidMind.
 * 
 * @author Miguel
 * @version
 * @since
 * 
 * Revisiones:
 * 
 * 25 Mar 2017 : Función length() acepta también una variable de tipo array como argumento.
 *               Devolver error en nº de argumentos, si tras procesar una función, no se
 *               encuentra el paréntesis de cierre.
 * 26 Mar 2017 : Funciones uppercase() y lowercase().
 * 27 Mar 2017 : Función findstring().
 * 28 Mar 2017 : Función repeatstring().
 * 31 Mar 2017 : Función readfile().
 * 01 Abr 2017 : Comando writefile.
 * 06 Abr 2017 : Los identificadores también pueden comenzar por / contener los caracteres '$' y '_'.
 * 03 Jan 2018 : Las funciones empiezan a escribirse como métodos fnXXX fuera de exprValue(). 
 */
public class TinyDevLang {
	
	// --------------
	// Tipos de datos
	// --------------
	
	public enum DataType {
		NUMBER,
		STRING,
		IMAGE,
		SOUND,
		ARRAY,    // Arrays de number, string, image, sound
		BOOLEAN   // Utilizado sólo en expresiones
	}
	
	// ----------------
	// Códigos de error
	// ----------------
	
	public enum Error {
		SUCCESS,
		MISSING_QUOTE,
		VARIABLE_EXISTS,
		VARIABLE_NOT_EXISTS,
		BAD_SYNTAX,
		BAD_DATA_TYPE,
		PLACE_EXISTS,
		PLACE_NOT_EXISTS,
		BAD_ARGUMENT,
		BAD_NUMBER_OF_ARGUMENTS,
		BAD_COLOR,
		TOO_MANY_DO,
		DO_NOT_ACTIVE,
		DO_LOOP_MISMATCH,
		MISSING_LOOP,
		TOO_MANY_CALL,
		CALL_NOT_ACTIVE,
		BAD_SCREEN_POSITION,
		BAD_SCREEN_SIZE,
		CANT_LOAD_IMAGE,
		CANT_SAVE_IMAGE,
		NULL_IMAGE,
		CANT_LOAD_SOUND,
		NULL_SOUND,
		IF_NOT_ACTIVE,
		TOO_MANY_IF,
		MISSING_END_IF,
		BAD_EXPRESSION,
		OUT_OF_BOUNDS,
		BAD_ARRAY_SIZE,
		READING_FILE,
		WRITING_FILE
	}
	
	// ----------------
	// Estilos gráficos
	// ----------------
	
	public enum Style {
		EMPTY,
		FILL,
		SOLID
	}
	
	// --------------------
	// Estilos de la fuente
	// --------------------
	
	public static final int FONT_STYLE_PLAIN = 0;
	public static final int FONT_STYLE_BOLD = 1;
	public static final int FONT_STYLE_ITALIC = 2;
	public static final int FONT_STYLE_TRANSPARENT = 4;

	// -----------------------------------------------------
	// Palabras reservadas del lenguaje, en orden alfabético
	// -----------------------------------------------------
	
	public static final TinyDevKeyword[] keywords = {
		new TinyDevKeyword("bold", TinyDevKeyword.Id.BOLD),
		new TinyDevKeyword("break", TinyDevKeyword.Id.BREAK),
		new TinyDevKeyword("call", TinyDevKeyword.Id.CALL),
		new TinyDevKeyword("center", TinyDevKeyword.Id.CENTER),
		new TinyDevKeyword("clear", TinyDevKeyword.Id.CLEAR),
		new TinyDevKeyword("cursor", TinyDevKeyword.Id.CURSOR),
		new TinyDevKeyword("cursorX", TinyDevKeyword.Id.CURSORX),
		new TinyDevKeyword("cursorY", TinyDevKeyword.Id.CURSORY),
		new TinyDevKeyword("dialog", TinyDevKeyword.Id.DIALOG),
		new TinyDevKeyword("do", TinyDevKeyword.Id.DO),
		new TinyDevKeyword("ellipse", TinyDevKeyword.Id.ELLIPSE),
		new TinyDevKeyword("else", TinyDevKeyword.Id.ELSE),
		new TinyDevKeyword("end", TinyDevKeyword.Id.END),
		new TinyDevKeyword("error", TinyDevKeyword.Id.ERROR),
		new TinyDevKeyword("fill", TinyDevKeyword.Id.FILL),
		new TinyDevKeyword("findstring", TinyDevKeyword.Id.FINDSTRING),
		new TinyDevKeyword("font", TinyDevKeyword.Id.FONT),
		new TinyDevKeyword("if", TinyDevKeyword.Id.IF),
		new TinyDevKeyword("image", TinyDevKeyword.Id.IMAGE),
		new TinyDevKeyword("information", TinyDevKeyword.Id.INFORMATION),
		new TinyDevKeyword("input", TinyDevKeyword.Id.INPUT),
		new TinyDevKeyword("italic", TinyDevKeyword.Id.ITALIC),
		new TinyDevKeyword("jump", TinyDevKeyword.Id.JUMP),
		new TinyDevKeyword("key", TinyDevKeyword.Id.KEY),
		new TinyDevKeyword("left", TinyDevKeyword.Id.LEFT),
		new TinyDevKeyword("length", TinyDevKeyword.Id.LENGTH),
		new TinyDevKeyword("line", TinyDevKeyword.Id.LINE),
		new TinyDevKeyword("loop", TinyDevKeyword.Id.LOOP),
		new TinyDevKeyword("lowercase", TinyDevKeyword.Id.LOWERCASE),
		new TinyDevKeyword("middle", TinyDevKeyword.Id.MIDDLE),
		new TinyDevKeyword("mouseClick", TinyDevKeyword.Id.MOUSECLICK),
		new TinyDevKeyword("mouseX", TinyDevKeyword.Id.MOUSEX),
		new TinyDevKeyword("mouseY", TinyDevKeyword.Id.MOUSEY),
		new TinyDevKeyword("number", TinyDevKeyword.Id.NUMBER),
		new TinyDevKeyword("okCancel", TinyDevKeyword.Id.OKCANCEL),
		new TinyDevKeyword("paper", TinyDevKeyword.Id.PAPER),
		new TinyDevKeyword("pen", TinyDevKeyword.Id.PEN),
		new TinyDevKeyword("plain", TinyDevKeyword.Id.PLAIN),
		new TinyDevKeyword("point", TinyDevKeyword.Id.POINT),
		new TinyDevKeyword("print", TinyDevKeyword.Id.PRINT),
		new TinyDevKeyword("random", TinyDevKeyword.Id.RANDOM),
		new TinyDevKeyword("readfile", TinyDevKeyword.Id.READFILE),
		new TinyDevKeyword("rectangle", TinyDevKeyword.Id.RECTANGLE),
		new TinyDevKeyword("repeatstring", TinyDevKeyword.Id.REPEATSTRING),
		new TinyDevKeyword("reply", TinyDevKeyword.Id.REPLY),
		new TinyDevKeyword("return", TinyDevKeyword.Id.RETURN),
		new TinyDevKeyword("right", TinyDevKeyword.Id.RIGHT),
		new TinyDevKeyword("screen", TinyDevKeyword.Id.SCREEN),
		new TinyDevKeyword("screenHeight", TinyDevKeyword.Id.SCREENHEIGHT),
		new TinyDevKeyword("screenImage", TinyDevKeyword.Id.SCREENIMAGE),
		new TinyDevKeyword("screenWidth", TinyDevKeyword.Id.SCREENWIDTH),
		new TinyDevKeyword("set", TinyDevKeyword.Id.SET),
		new TinyDevKeyword("solid", TinyDevKeyword.Id.SOLID),
		new TinyDevKeyword("sound", TinyDevKeyword.Id.SOUND),
		new TinyDevKeyword("string", TinyDevKeyword.Id.STRING),
		new TinyDevKeyword("textHeight", TinyDevKeyword.Id.TEXTHEIGHT),
		new TinyDevKeyword("textWidth", TinyDevKeyword.Id.TEXTWIDTH),
		new TinyDevKeyword("then", TinyDevKeyword.Id.THEN),
		new TinyDevKeyword("to", TinyDevKeyword.Id.TO),
		new TinyDevKeyword("transparent", TinyDevKeyword.Id.TRANSPARENT),
		new TinyDevKeyword("uppercase", TinyDevKeyword.Id.UPPERCASE),
		new TinyDevKeyword("until", TinyDevKeyword.Id.UNTIL),
		new TinyDevKeyword("variable", TinyDevKeyword.Id.VARIABLE),
		new TinyDevKeyword("wait", TinyDevKeyword.Id.WAIT),
		new TinyDevKeyword("warning", TinyDevKeyword.Id.WARNING),
		new TinyDevKeyword("while", TinyDevKeyword.Id.WHILE),
		new TinyDevKeyword("writefile", TinyDevKeyword.Id.WRITEFILE),
		new TinyDevKeyword("writeimage", TinyDevKeyword.Id.WRITEIMAGE),
		new TinyDevKeyword("yesNo", TinyDevKeyword.Id.YESNO)
	};
	
	// ----------
	// Operadores
	// ----------
	//
	// X = Notar posibles conflictos entre operadores que principian
	//     con el mismo caracter.
	//
	//     Deberán procesarse en el orden correcto al parsear y generar
	//     los tokens.
	
	public static final TinyDevOperator[] operators = {
		new TinyDevOperator("==", TinyDevOperator.Id.EQUAL),            // X
		new TinyDevOperator("!=", TinyDevOperator.Id.NOT_EQUAL),
		new TinyDevOperator("<=", TinyDevOperator.Id.LESS_OR_EQUAL),    // X
		new TinyDevOperator("<",  TinyDevOperator.Id.LESS),             // X
		new TinyDevOperator(">=", TinyDevOperator.Id.GREATER_OR_EQUAL), // X
		new TinyDevOperator(">",  TinyDevOperator.Id.GREATER),          // X
		
		new TinyDevOperator("&&", TinyDevOperator.Id.AND),
		new TinyDevOperator("||", TinyDevOperator.Id.OR),
		
		new TinyDevOperator("'", TinyDevOperator.Id.COMMENT),
		new TinyDevOperator(":", TinyDevOperator.Id.COLON),
		new TinyDevOperator(",", TinyDevOperator.Id.COMMA),
		new TinyDevOperator("(", TinyDevOperator.Id.OPEN_PARENTHESIS),
		new TinyDevOperator(")", TinyDevOperator.Id.CLOSE_PARENTHESIS),
		new TinyDevOperator("[", TinyDevOperator.Id.OPEN_BRACKET),
		new TinyDevOperator("]", TinyDevOperator.Id.CLOSE_BRACKET),
		new TinyDevOperator("+", TinyDevOperator.Id.PLUS),
		new TinyDevOperator("-", TinyDevOperator.Id.MINUS),
		new TinyDevOperator("*", TinyDevOperator.Id.MULTIPLY),
		new TinyDevOperator("/", TinyDevOperator.Id.DIVIDE),
		new TinyDevOperator("%", TinyDevOperator.Id.MODULUS),
		new TinyDevOperator("=", TinyDevOperator.Id.ASSIGN)             // X
	};
	
	// ---------------
	// G L O B A L E S
	// ---------------
	
	TinyDevLocalizator loc = new TinyDevLocalizator("TinyDevLang");  // Localización de textos, etc. (español, inglés...)
	
	TinyDevEditor editor;
	
	private String currentDirectory;
	
	private static final int MAX_ARRAY_SIZE = 100; // Tamaño máximo, para los arrays
	
	private ArrayList<TinyDevVariable> variables;       // Variables definidas
	private ArrayList<TinyDevPlace> places;             // Places definidos
	private ArrayList<TinyDevIdentifier> identifiers;   // Identificadores definidos
	
	private ArrayList<String> source;              // Código fuente recibido
	private ArrayList<TinyDevLine> source_tokens;       // Código fuente tokenizado
	
	private int posNumber;    // Posición (índice 0..?) en el código fuente de la línea actual
	private int lineNumber;   // Nº de línea que se está procesando (0..?)
	private boolean stopped;  // True si el programa ha finalizado, false en caso contrario -- FIXME: ¿volatile?
	private Error errorId;    // Código de error
	
	int exprIndex;                  // Posición en la línea de tokens de la expresión
	Object exprValue;               // Valor (resultado) de la expresión
	DataType exprType;              // Tipo de valor (resultado) de la expresión
	DataType exprSubType;			// Sub-tipo (resultado) si la expresión es un array
	ArrayList<TinyDevToken> exprTokens;  // Línea de tokens de la expresión
	
	private TinyDevScreen io;            // Objeto de entrada / salida desde / hacia el teclado, ventana, etc.
	
	private Thread executor = null; // Thread para la tokenización y ejecución de líneas de código
	
	// DO ... LOOP UNTIL / WHILE
	// DO UNTIL / WHILE ... LOOP
	private static final int MAX_DO_LEVEL = 10;             // Máximo nº de Do activos
	private int doLevel;                                    // Nº de Do activos
	private boolean[] doAlone = new boolean[MAX_DO_LEVEL];  // True si es DO ... LOOP UNTIL | WHILE
	private int[] doLineNumber = new int[MAX_DO_LEVEL];     // Nº de línea del DO
	private int[] doIfBlockLevel = new int[MAX_DO_LEVEL];   // Nº de IF activos por nivel de DO
	
	// Nota: Hay 2 tipos de If, que no se pueden mezclar.
	//
	//       Simple:
	//
	//       IF condición THEN instrucción
	//       {ELSE instrucción}
	//
	//       De bloque:
	//
	//       IF condición THEN
	//           instrucciones
	//       {ELSE
	//           instrucciones}
	//       END IF

    // IF simple
	private boolean ifTrue;  // True, si la condición del If es cierta
	
	// IF de bloque
	private static final int MAX_IF_BLOCK_LEVEL = 10;                // Máximo nº de If activos
	private int ifBlockLevel;                                        // Nº de If activos
	private boolean[] ifBlockTrue = new boolean[MAX_IF_BLOCK_LEVEL]; // Resultado de las condiciones de los If activos
	
	// CALL TO / RETURN
	private static final int MAX_CALL_LEVEL = 10;             // Máximo nº de Call activos
	private int[] callLineNumber = new int[MAX_CALL_LEVEL];   // Nº de línea de los Call activos
	private int[] callIfBlockLevel = new int[MAX_CALL_LEVEL]; // Nº de If activos por nivel de Call
	private int[] callDoLevel = new int[MAX_CALL_LEVEL];      // Nº de Do activos por nivel de Call
	private int callLevel;
	
	// RANDOM
	private Random randGen;     // Generador de números aleatorios 
	private int randMax;        // Valor máximo a a generar (0..randMax, ambos inclusives)
	
	// DIALOG
	private String dialogInput; // Valor devuelto
	
	// PEN & PAPER
	private String penName;
	private String paperName;
	
	/**
	 * Constructor.
	 */
	public TinyDevLang(TinyDevEditor ed) {
		
		//
		
		editor = ed;
	}
	
	/**
	 * Ejecutar un programa.
	 * 
	 * @param sourceCode  código fuente
	 * @param screen      objeto de entrada / salida desde / hacia el teclado, pantalla, etc.
	 * 
	 */
	public void execute(ArrayList<String> sourceCode, TinyDevScreen screen, String currentDir) {
		
		// Si hay un programa actualmente en ejecución, detenerlo
		stop();

		// Thread que hará el parseado, tokenizado, y la ejecución del programa en sí
		executor = new Thread() {
			
			@Override
			public void run() {
				
				// Inicialización de variables globales
				variables = new ArrayList<TinyDevVariable>();
				places = new ArrayList<TinyDevPlace>();
				identifiers = new ArrayList<TinyDevIdentifier>();
				source = sourceCode;
				errorId = Error.SUCCESS;
				stopped = false;
				randGen = new Random();
				randMax = 32767;
				penName = "black";
				paperName = "white";
				
				//
				currentDirectory = currentDir;
				
				// Inicialización de la entrada / salida
				io = screen;
				io.reset(TinyDevScreen.DEFAULT_SCREEN_WIDTH, TinyDevScreen.DEFAULT_SCREEN_HEIGHT);
				io.setTitle(loc.getTitle("Parsing"));
								
				// Tokenizar el código fuente
				source_tokens = tokenizeAll(source);
				
				// Paso previo a la ejecución
				if(errorId == Error.SUCCESS)			
					preRun();
				
				// Ejecución del programa
				if(errorId == Error.SUCCESS) {
					
					io.setTitle(loc.getTitle("Running"));
					runProgram();
				}
				
				// Fin de la ejecución
				io.setTitle(loc.getTitle("Stopped"));
				
				// Mostrar mensaje de posibles errores
				if(errorId != Error.SUCCESS)
					printError();
			}
		};
		
		// Comenzar el proceso de parseado, tokenizado, ejecución, etc.		
		executor.start();
	}
	
	/**
	 * Parar la ejecución del programa.
	 */
	public void stop() {
		
		// Enviar la señal
		stopped = true;

		// Si hay un thread creado, invalidarlo
		if(executor != null) {

			// Si el thread sigue vivo, esperar a que finalice
			if(executor.isAlive()) {
				try {
					// Esperar a que finalice
					executor.join();
				} catch (InterruptedException e) {
					// Nada
				}
			}
			
			// Invalidar el objeto
			executor = null;
		}
	}
	
	/**
	 * Devolver la lista de palabras reservadas del lenguaje.
	 * 
	 * @return  lista
	 */
	public String[] getKeywords() {
		
		// Crear array
		String [] kw = new String[keywords.length];
		
		// Rellenar el array
		for(int i = 0; i < kw.length; ++i) {
			
			kw[i] = keywords[i].getName();
		}
		
		// Devolver la lista
		return kw;
	}
	
	/**
	 * Paso previo a la ejecución del programa, después
	 * de haber sido tokenizado el código fuente.
	 * 
	 * Si hay errores, los indica en la variable errorId.
	 */
	private void preRun() {
		
		// Inicializar globales
		lineNumber = 0;
		posNumber = 0;
		
		// Recorrer todas las líneas de tokens
		for(lineNumber = 0; lineNumber < source_tokens.size(); ++lineNumber) {
			
			// Tomar línea actual de tokens
			TinyDevLine line = source_tokens.get(lineNumber);
			
			// Tomar los tokens		
			ArrayList<TinyDevToken> tokens = line.getTokens();
			
			// Si es una línea vacía (sin código), continuar con la siguiente
			if(tokens.isEmpty())
				continue;
			
			// Si es una línea que empieza con una palabra reservada,
			// continuar con la siguiente línea
			if(line.getTokens().get(0).getType() == TinyDevToken.Id.KEYWORD) {
				continue;
			}
			
			// Si la línea contiene 2 tokens, comprobar si se
			// trata de la declaración de un place
			if(tokens.size() == 2) {
				
				// El primer token ha de ser un identificador (el
				// nombre del place), y el segundo token ha
				// de ser el símbolo de 2 puntos ':'
				if(tokens.get(0).getType() == TinyDevToken.Id.IDENTIFIER
						&& tokenIsOtherOf(tokens.get(1), TinyDevOperator.Id.COLON)) {
					
					// Tomar el nombre del identificador
					String name = tokens.get(0).getIdentifier().getName();
					
					// Comprobar si ya existe el place
					TinyDevPlace pl = findPlace(name);
				
					// Ya existe, mostrar mensaje de error y abortar
					if(pl != null) {
						
						errorId = Error.PLACE_EXISTS;
						return;
					}
				
					// No existe, añadirlo y continuar con la siguiente línea
					places.add(new TinyDevPlace(name, lineNumber));
					continue;					
				}
			}
			
			// Tipo de línea no reconocida,
			// mostrar mensaje de error y abortar
			syntaxError();
			return;
		}
	}
	
	/**
	 * Lanza la ejecución del programa, una vez el código fuente ha sido tokenizado,
	 * y tratado con el paso previo a la ejecución.
	 * 
	 * Este metodo finaliza, y la ejecución se realiza mediante un timer.
	 * 
	 * La ejecución finaliza en caso de error, de cierre de la ventana de ejecución,
	 * o después de ejecutar la última línea de código.
	 * 
	 *  Si hay errores, los indica en la variable errorId.
	 */
	private void runProgram() {
		
		// Inicializar globales
		lineNumber = 0;
		posNumber = 0;
		stopped = false;
		ifTrue = false;
		doLevel = 0;
		callLevel = 0;
		ifBlockLevel = 0;
		dialogInput = "";
		
		// Bucle de ejecución
		while(!stopped && lineNumber < source_tokens.size() && !io.isClosed()) {
				
		  		// Tomar línea de tokens a ejecutar
				TinyDevLine line = source_tokens.get(lineNumber);

	  			// Interpretarla, si no está vacía
	  			if(!line.getTokens().isEmpty()) {

	  				// Si el primer token es una palabra reservada,
	  				// ejecutar la línea
	  				if(line.getTokens().get(0).getType() == TinyDevToken.Id.KEYWORD) {
	  					
	  					// Ejecutar la línea
	  					command(line);
	  					
	  					// Si ha habido un error, abortar la ejecución
	  					if(errorId != Error.SUCCESS) {
		    				stopped = true;
		    				break;
	  					}
	  				}
	  				
	  				// En otro caso, será una declaración de un place,
	  				// con lo que no hay que hacer nada
	  			}
	  			
	  			// Siguiente línea
	  			++lineNumber;
		}
		
		// FIXME: Si la ejecución se detiene por haber llegado a
		//        la última línea de código, se podría comprobar
		//        si queda algún DO / CALL / IF activo, y
		//        mostrar un mensaje de error.
	}
	
	/**
	 * Ejecutar una línea de código tokenizado. En caso de errores,
	 * fija la variable errorId.
	 * 
	 * Presupone que el primer token es una palabra reservada.
	 * 
	 * @param line  línea a ejecutar
	 */
	private void command(TinyDevLine line) {
		
		// Tomar los tokens de la línea
		ArrayList<TinyDevToken> tokens = line.getTokens();
		
		// Tomar la 1ª palabra reservada (1er token)
		TinyDevKeyword.Id cmd = line.getTokens().get(0).getKeyword().getCode();
		
		// Inicializar globales
		exprTokens = tokens;
		exprIndex = 1;
	
		// Ejecutar la instrucción correspondiente (la lista
		// está en orden alfabético)
		switch(cmd) {
		
			case BREAK :
				doBreak();
				break;
			case CALL :
				doCall();
				break;
			case CLEAR :
				doClear();
				break;
			case CURSOR :
				doCursor();
				break;
			case DIALOG :
				doDialog();
				break;
			case DO :
				doDo();
				break;
			case ELLIPSE :
				doEllipse();
				break;				
			case ELSE :
				doElse();
				break;
			case END :
				doEnd();
				break;
			case FONT :
				doFont();
				break;
			case IF :
				doIf();
				break;
			case IMAGE :
				doImage();
				break;				
			case JUMP :
				doJump();
				break;
			case LINE :
				doLine();
				break;				
			case LOOP :
				doLoop();
				break;
			case PAPER :
				doPaper();
				break;
			case PEN :
				doPen();
				break;
			case POINT :
				doPoint();
				break;				
			case PRINT :
				doPrint();
				break;
			case RANDOM :
				doRandom();
				break;
			case RECTANGLE :
				doRectangle();
				break;				
			case RETURN :
				doReturn();
				break;
			case SCREEN :
				doScreen();
				break;
			case SET :
				doSet();
				break;
			case SOUND :
				doSound();
				break;
			case VARIABLE :
				doVariable();
				break;
			case WAIT :
				doWait();
				break;
			case WRITEFILE :
				doWriteFile();
				break;
			case WRITEIMAGE :
				doWriteImage();
				break;
				
			// Instrucción no ejecutable (función), o no reconocida
			default :
				
				// Indicar error
				syntaxError();
				break;
		}
	}
	
	/**
	 * Buscar una palabra reservada.
	 * 
	 * @param name  palabra reservada
	 * @return  palabra reservada, o null en caso de no encontrarla
	 */
	private TinyDevKeyword findKeyword(String name) {
		
		// Recorrer la lista de palabras reservadas
		for(TinyDevKeyword kw : keywords) {
			
			// Comparar, ignorando mayúsculas / minúsculas
			if(kw.getName().equalsIgnoreCase(name)) {
				
				// Encontrada
				return kw;
			}
		}
		
		// No ha sido encontrada
		return null;
	}
	
	/**
	 * Buscar un operador de otros, al inicio de una cadena de texto.
	 * 
	 * @param str  cadena de texto
	 * @return  operador, o null si no lo ha encontrdo
	 */
	private TinyDevOperator findOther(String str) {
		
		// Recorrer la lista de otros operadores
		for(TinyDevOperator ot : operators) {
			
			// Comparar con el inicio de la cadena de texto
			if(str.startsWith(ot.getName())) {
				
				// Encontrado
				return ot;
			}
		}
		
		// No ha sido encontrado
		return null;
	}
	
	/**
	 * Buscar variable.
	 * 
	 * @param name  nombre de la variable
	 * @return  variable, o null si no ha sido encontrada
	 */
	private TinyDevVariable findVariable(String name) {
		
		// Recorrer la lista de variables
		for(TinyDevVariable vr : variables) {
			
			// Comparar, ignorando mayúsculas / minúsculas
			if(vr.getName().equalsIgnoreCase(name)) {
				
				// Encontrada
				return vr;
			}
		}
		
		// No ha sido encontrada
		return null;
	}
	
	/**
	 * Buscar un place.
	 * 
	 * @param name  nombre del place
	 * @return  place, o null si no ha sido encontrado
	 */
	private TinyDevPlace findPlace(String name) {
		
		// Recorrer la lista de places
		for(TinyDevPlace pl : places) {
			
			// Comparar, ignorando mayúsculas / minúsculas
			if(pl.getName().equalsIgnoreCase(name)) {
				
				// Encontrado
				return pl;
			}
		}
		
		// No encontrado
		return null;
	}

	/**
	 * Buscar un identificador.
	 * 
	 * @param name  nombre del identificador
	 * @return  identificador, o null si no ha sido encontrado
	 */
	private TinyDevIdentifier findIdentifier(String name) {
		
		// Recorrer la lista de identificadores
		for(TinyDevIdentifier id : identifiers) {
			
			// Comparar, ignorando mayúsculas / minúsculas
			if(id.getName().equalsIgnoreCase(name)) {
				
				// Encontrado
				return id;
			}
		}
		
		// No ha sido encontrado
		return null;
	}
	
	/**
	 * Tokenizar todo el código fuente. En caso de error, se
	 * indicará en la variable errorId.
	 * 
	 * @param source  código fuente
	 * @return  líneas tokenizadas, o null en caso de error
	 */
	public ArrayList<TinyDevLine> tokenizeAll(ArrayList<String> source) {
		
		// Crear un ArrayList de líneas de código tokenizado
		ArrayList<TinyDevLine> lines = new ArrayList<TinyDevLine>();
		
		// Crear un ArrayList de tokens
		ArrayList<TinyDevToken> tokens;
		
		// Inicializar globales
		lineNumber = 0;
		
		// Recorrer todas las líneas de código fuente
		for(String sourceLine : source) {
			
			// Tokenizar la línea
			if((tokens = tokenize(sourceLine)) == null) {
				
				// Hubo un error, abortar
				return null;
			}

			// Añadir la línea tokenizada al ArrayList
			lines.add(new TinyDevLine(lineNumber++, tokens));
		}

		// Devolver las líneas tokenizadas
		return lines;
	}
	
	/**
	 * Tokenizar una línea de código fuente. Si hay errores,
	 * se indicarán en la variable errorId.
	 * 
	 * @param line  línea de código fuente
	 * @return  tokens, o null en caso de error
	 */
	public ArrayList<TinyDevToken> tokenize(String line) {
		
		// Crear un ArrayList de tokens
		ArrayList<TinyDevToken> result = new ArrayList<TinyDevToken>();
		
		// Tomar longitud de la línea de código fuente
		int length = line.length();
		
		// Inicializar la posición (índice) en la línea
		posNumber = 0;
		
		// Recorrer los caracteres de la línea
		while(posNumber < length) {
			
			// Ignorar espacios
			while(Character.isWhitespace(line.charAt(posNumber))) {
				
				// Descartar el espacio, y finalizar si se ha
				// llegado al final de la línea
				if(++posNumber == length)
					break;
			}
				
			// Finalizar si se ha llegado al final de la línea			
			if(posNumber == length)
				break;
			
			// Comprobar si es un número positivo, sin signo
			if(Character.isDigit(line.charAt(posNumber))) {
				
				// Tomar el número
				String number = getNumber(line, posNumber);
				
				// Incrementar la posición
				posNumber += number.length();
				
				// Añadir el token				
				result.add(new TinyDevToken(TinyDevToken.Id.NUMBER, stringToInt(number)));
				
				// Continuar
				continue;
			}
			
			// Comprobar si es un identificador
			if(Character.isLetter(line.charAt(posNumber)) || line.charAt(posNumber) == '$' || line.charAt(posNumber) == '_') {
				
				// Tomar el identificador
				String word = getIdentifier(line, posNumber);
				
				// Incrementar la posición
				posNumber += word.length();
				
				// Comprobar si es una palabra reservada
				TinyDevKeyword kw = findKeyword(word);
				
				if(kw != null) {
					
					// Sí: añadir el token
					result.add(new TinyDevToken(TinyDevToken.Id.KEYWORD, kw));
				}
				else {
					
					// No: identificador desconocido, puede ser una variable
					//     o un place, pero en este momento no se puede
					//     determinar.
					//
					//     Crear y añadir el identificador, si no existe ya.
					
					// Buscar el identificador
					TinyDevIdentifier id = findIdentifier(word);
					
					// Comprobar si existe
					if(id == null) {
						
						// No existe: crearlo, y añadirlo
						id = new TinyDevIdentifier(word);
						identifiers.add(id);
					}
					
					// Añadir el token
					result.add(new TinyDevToken(TinyDevToken.Id.IDENTIFIER, id));
				}
				
				// Continuar
				continue;
			}
			
			// Comprobar si es una cadena de texto entrecomillada
			if(line.charAt(posNumber) == '"') {
				
				// Tomar la cadena de texto
				String str = getString(line, posNumber);
				
				// Si no hubo errores, proceder
				if(str != null) {
					
					// Actualizar la posición
					posNumber += str.length() + 2;
					
					// Añadir el token
					result.add(new TinyDevToken(TinyDevToken.Id.STRING, str));
					
					// Continuar
					continue;
				}
				
				// Cadena de texto mal formada, señalar el error y abortar
				errorId = Error.MISSING_QUOTE;
				posNumber = line.length();
				
				return null;
			}
			
			// Crear una subcadena, para búsqueda de operadores			
			String prefix = line.substring(posNumber);
			
			// NOTA: Las comprobaciones, se han de hacer en el orden
			//       correcto (ver las anotaciones en las definiciones de los
			//       códigos de los distintos operadores).
			
			// Comprobar si es un operador	
			TinyDevOperator ot = findOther(prefix);
			
			// Encontrado
			if(ot != null) {
				
				// Si es un comentario, se ignora, junto con el resto de la línea
				if(ot.getType() == TinyDevOperator.Id.COMMENT)
					break;
				
				// La coma es opcional, y sirve para separar argumentos,
				// además de los espacios

				// Añadir el token, si no es la coma
				// if(ot.getType() != KmOperator.Id.COMMA)
				result.add(new TinyDevToken(TinyDevToken.Id.OPERATOR, ot));
				
				// Actualizar la posición
				posNumber += ot.getName().length();
				
				// Continuar
				continue;
			}
			
			// Tipo de token desconocido, señalar el error y abortar
			errorId = Error.BAD_ARGUMENT;
			
			return null;
		}
		
		// Devolver resultado de la tokenización
		return result;
	}
	
	/**
	 * Tomar un identificador. Asume que el 1er carácter es válido.
	 * 
	 * @param line   línea de código fuente
	 * @param index  posición índice en la línea
	 * @return  identificador
	 */
	private String getIdentifier(String line, int index) {
		
		String id = new String();     // Identificador
		int length = line.length();   // Longitud de la línea
		char ch = line.charAt(index); // 1er carácter
		
		// Recorrer la línea, mientras los caracteres sean
		// válidos
		do {
			
			// Añadir caracter al identificador
			id = id + ch;
			
			// Terminar, si se llegó al final de la línea
			if(++index == length)
				break;
			
			// Tomar siguiente caracter
			ch = line.charAt(index);
			
		} while(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '$');
		
		// Devolver identificador
		return id;
	}
	
	/**
	 * Tomar un número. Asume que el 1er carácter es válido.
	 * 
	 * @param line   línea de código fuente
	 * @param index  posición en la línea
	 * @return  número
	 */
	private String getNumber(String line, int index) {
		
		String number = new String();  // Número
		int length = line.length();    // Longitud de la línea
		
		// Recorrer la línea, mientras los caracteres sean
		// válidos
		do {
			
			// Añadir caracter al número
			number += line.charAt(index);
			
			// Terminar, si se llegó al final de la línea
			if(++index == length)
				break;

		} while(Character.isDigit(line.charAt(index)));
		
		// Devolver número
		return number;
	}
	
	/**
	 * Tomar una cadena de texto entrecomillada. Asume que el 1er carácter es
	 * la doble comilla.
	 * 
	 * @param line   línea de código fuente
	 * @param index  posición en la línea
	 * @return  cadena de texto sin las comillas
	 */
	private String getString(String line, int index) {
		
		String str = new String();   // Cadena de texto
		int length = line.length();  // Longitud de la línea
		
		// Saltar las comillas de comienzo
		++index;
		
		// Recorrer la línea
		while(index < length) {
			
			// Tomar caracter
			char ch = line.charAt(index++);
			
			// Si es un caracter de comillas, finalizar
			if(ch == '"') {
				
				return str;
			}
			
			// Añadir el caracter a la cadena de texto
			str = str + ch;
		}

		// No se encontraron las comillas del final,
		// devolver error
		return null;
	}
	
	// -------------------------
	// I N S T R U C C I O N E S
	// -------------------------
	
	// NOTA: Todos los métodos que corresponden a instrucciones,
	//       indican en la variable errorId los posibles errores.
	
	/**
	 * Instrucción - Imprimir expresión textual:
	 * 
	 * PRINT {LEFT | CENTER | RIGHT} expresión_textual
	 */
	private void doPrint() {
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Comprobar si el 1er argumento es la alineación
		TinyDevToken tk = exprTokens.get(1);
		
		// Alineación
		int align = 0;

		if(tokenIsCommandOf(tk, TinyDevKeyword.Id.LEFT)) {
			align = 0;
			++exprIndex;
		} else if(tokenIsCommandOf(tk, TinyDevKeyword.Id.CENTER)) {
			align = 1;
			++exprIndex;
		} else if (tokenIsCommandOf(tk, TinyDevKeyword.Id.RIGHT)) {
			align = 2;
			++exprIndex;
		}
		
		// Necesita texto
		if(exprString())
			return;
		
		// Tomar mensaje
		String text = (String) exprValue;
		

		// Necesita final
		if(needEnd())
			return;
		
		// Calcular posición X del mensaje, en base a la alineación
		if(align > 0) {
			
			// Posición X
			int x = 0;
			
			if(align == 1)
				x = (io.getWidth() - io.getTextWidth(text)) / 2;  // CENTER
			else if(align == 2)
				x = io.getWidth() - io.getTextWidth(text);    // RIGHT
		
			// Si la posición no es válida, retornar
			//if(!isValidPos(x, 0))
			//	return;
			
			// Ir a la posición
			if(io.cursorTo(x, io.getRow())) {
				
				errorId = Error.BAD_SCREEN_POSITION;
				return;
			}
		}

		// Imprimir el mensaje
		io.print(text);
	}
	
	/**
	 * Instrucción - Cambiar la fuente, su tamaño y los atributos:
	 * 
	 * FONT nombre | tamaño | PLAIN | BOLD | ITALIC | TRANSPARENT {...}
	 */
	private void doFont() {
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Estilo por defecto: ninguno
		int style = FONT_STYLE_PLAIN;
		
		// Flag para indicar que se ha seleccionado el atributo PLAIN
		boolean plain = false;
		
		// Recorrer los argumentos
		do {
			// Tomar token
			TinyDevToken tok = exprTokens.get(exprIndex);
			
			// Eliminar posible coma
			if(tokenIsOtherOf(tok, TinyDevOperator.Id.COMMA)) {
				++exprIndex;
				continue;
			}
			
			// Si es una palabra reservada, ver
			// si corresponde a los atributos
			if(tok.getType() == TinyDevToken.Id.KEYWORD) {
				
				// Cambiar los atributos
				switch(tok.getKeyword().getCode()) {
				
					case PLAIN :
						plain = true;
						break;
					case BOLD :
						style |= FONT_STYLE_BOLD;
						break;
					case ITALIC :
						style |= FONT_STYLE_ITALIC;
						break;
					case TRANSPARENT :
						style |= FONT_STYLE_TRANSPARENT;
						break;
					
					// No corresponde a un atributo,
					// finalizar
					default :
						errorId = Error.BAD_ARGUMENT;
						return;
				}
				
				// Incrementar la posición
				++exprIndex;
				
			} else {
				
				// Retornar, si no es una expresión
				if(expr())
					return;
				
				// Comprobar si es una expresión textual (nombre de la fuente),
				// o numérica (tamaño de la fuente), y actuar en consecuencia
				if(exprType == DataType.STRING) {
					
					io.setFontFace((String) exprValue);    // Cambiar nombre
					
				} else if(exprType == DataType.NUMBER) {
					
					io.setFontSize((int) exprValue);       // Cambiar tamaño
					
					// FIXME : Comprobar si el tamaño es válido
					
				} else {
					
					// Argumento erróneno, finalizar
					errorId = Error.BAD_ARGUMENT;
					
					return;
				}
			}
		} while(tokensLeft());
		
		// Si se han indicado atributos, fijarlos
		if(style != FONT_STYLE_PLAIN || plain)
			io.setFontStyle(style);
	}
	
	/**
	 * Instrucción - Borrar la pantalla, y situar el cursor en 0,0:
	 * 
	 * CLEAR
	 */
	private void doClear() {
		
		// No tiene argumentos		
		if(!needEnd())
			io.clear(); // Borrar la pantalla
	}
	
	/**
	 * Instrucción - Posicionar el cursor:
	 * 
	 * CURSOR TO x y
	 */
	private void doCursor() {
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Comprobar que el siguiente argumento es TO
		if(!tokenIsCommandOf(exprTokens.get(1), TinyDevKeyword.Id.TO)) {
			syntaxError();
			return;
		}
		
		// Saltar TO
		++exprIndex;
		
		// Tomar valor numérico
		if(exprNumber())
			return;
		
		// Tomar el valor de X
		int x = (int) exprValue;
		
		// Coma
		if(eatComma())
			return;
		
		// Tomar valor numérico
		if(exprNumber())
			return;
		
		// Tomar el valor de Y
		int y = (int) exprValue;
		
		// No ha de haber más argumentos
		if(needEnd())
			return;
		
		// Situar el cursor en X,Y
		if(io.cursorTo(x, y)) {
			errorId = Error.BAD_SCREEN_POSITION;
		}
	}
	
	/**
	 * Instrucción - Cambiar el color de pluma:
	 * 
	 * PEN color
	 */
	private void doPen() {
		
		// Tomar valor de color como cadena de texto
		if(exprString())
			return;
		
		penName = (String) exprValue;
		
		// Necesita final
		if(needEnd())
			return;
		
		// Cambiar color
		if(io.setPen(penName))
			errorId = Error.BAD_COLOR;
	}
	
	/**
	 * Instrucción - Cambiar el color de papel:
	 * 
	 * PAPER color
	 */
	private void doPaper() {
		
		// Tomar color como cadena de texto
		if(exprString())
			return;
		
		paperName = (String) exprValue;
		
		// Necesita final
		if(needEnd())
			return;
		
		// Cambiar el color
		if(io.setPaper(paperName))
			errorId = Error.BAD_COLOR;
	}
	
	/**
	 * Instrucción - Cuadro de diálogo:
	 * 
	 * DIALOG INPUT | INFORMATION | WARNING | ERROR | YESNO | OKCANCEL expresión_textual
	 */
	private void doDialog() {
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Saltar el tipo de diálogo
		++exprIndex;
		
		// Necesita texto
		if(exprString())
			return;
		
		// Tomar mensaje
		String msg = (String) exprValue;
		
		// Necesita final		
		if(needEnd())
			return;
		
		// Tomar el token del tipo de mensaje
		TinyDevToken tk = exprTokens.get(1);
		
		// Llamar al cuadro de diálogo correspondiente
		if(tokenIsCommandOf(tk, TinyDevKeyword.Id.INPUT))
			dialogInput = io.readLine(msg, loc.getString("Input"));
		else if(tokenIsCommandOf(tk, TinyDevKeyword.Id.INFORMATION))
			io.dialogInformation(msg, loc.getString("Information"));
		else if(tokenIsCommandOf(tk, TinyDevKeyword.Id.WARNING))
			io.dialogWarning(msg, loc.getString("Warning"));
		else if(tokenIsCommandOf(tk, TinyDevKeyword.Id.ERROR))
			io.dialogError(msg, loc.getString("Error"));
		else if(tokenIsCommandOf(tk, TinyDevKeyword.Id.YESNO))
			dialogInput = io.dialogYesNo(msg, loc.getString("Confirm"));
		else if(tokenIsCommandOf(tk, TinyDevKeyword.Id.OKCANCEL))
			dialogInput = io.dialogOkCancel(msg, loc.getString("Confirm"));
		else {
			
			// Error, argumento erróneo
			errorId = Error.BAD_ARGUMENT;
		}
	}

	/**
	 * Instrucción - Crear una variable o array:
	 * 
	 * VARIABLE tipo nombre {= valor}
	 * VARIABLE tipo nombre[tamaño]
	 */
	private void doVariable() {
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Tomar tipo de dato
		TinyDevToken token = exprTokens.get(1);
		DataType dataType;
		
		if(tokenIsCommandOf(token, TinyDevKeyword.Id.NUMBER))
			dataType = DataType.NUMBER;
		else if(tokenIsCommandOf(token, TinyDevKeyword.Id.STRING))
			dataType = DataType.STRING;
		else if(tokenIsCommandOf(token, TinyDevKeyword.Id.IMAGE))
			dataType = DataType.IMAGE;
		else if(tokenIsCommandOf(token, TinyDevKeyword.Id.SOUND))
			dataType = DataType.SOUND;
		else {
			
			// Error, tipo erróneo
			errorId = Error.BAD_DATA_TYPE;
			return;
		}
		
		// Saltar tipo
		++exprIndex;
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Tomar identificador
		if(exprTokens.get(2).getType() != TinyDevToken.Id.IDENTIFIER) {	
			// Error, no es un identificador
			syntaxError();
			return;
		}
		
		// Tomar el nombre del identificador
		String name = exprTokens.get(2).getIdentifier().getName();
		
		// Ver si la variable ya existe
		if(findVariable(name) != null) {
			
			// Error, ya existe
			errorId = Error.VARIABLE_EXISTS;
			return;
		}
		
		// Saltar nombre de variable
		++exprIndex;
		
		// Por defecto, no es un array
		boolean isArray = false;
		int arraySize = -1;
		
		// Comprobar si es un array
		if(arrayIndex()) {
			// Es un array
			isArray = true;
			
			// Tomar tamaño
			arraySize = (int) exprValue;
			
			// Comprobar si el tamaño es válido (0 indica array vacío)
			if(arraySize < 0 || arraySize > MAX_ARRAY_SIZE) {
				
				// Error, tamaño no válido
				errorId = Error.BAD_ARRAY_SIZE;
				return;
			}

			/*
			// Necesita final, puesto que los
			// arrays no pueden ser inicializados
			if(needEnd())
				return;
			
			// Añadir array
			variables.add(new TinyDevVariable(name, DataType.ARRAY, dataType, size));
			
			// Retornar
			return;
			*/
		}
			
		// Retornar si arrayIndex() detectó un error
		if(errorId != Error.SUCCESS)
			return;
		
		// Indicar si se va a inicializar con un valor. Por defecto: no.
		boolean hasValue = false;
		
		// Si quedan tokens, será la inicialización
		if(tokensLeft()) {
			
			// Ha de ser =
			if(!tokenIsOtherOf(exprTokens.get(exprIndex), TinyDevOperator.Id.ASSIGN)) {		
				// No, error
				syntaxError();
				return;
			}
			
			// Saltar =
			++exprIndex;
			
			// Ha de quedar la expresión con el valor
			if(!tokensLeft()) {
				
				// No hay valor, error
				syntaxError();
				return;
			}
			
			// Indicar que hay inicialización
			hasValue = true;
		}
		
		// Variable a crear
		TinyDevVariable var = null;
		
		// Crear la variable, según el tipo de dato, e inicializar
		// al valor por defecto, o al indicado
		switch((isArray ? DataType.ARRAY : dataType)) {
			case NUMBER :
				exprValue = (Object) 0;
				if(hasValue) {
					if(exprNumber())
						return;
				}
				var = new TinyDevVariable(name, DataType.NUMBER, (int) exprValue);
				break;
			case STRING :
				exprValue = (Object) "";
				if(hasValue) {
					if(exprString())
						return;
				}
				var = new TinyDevVariable(name, DataType.STRING, (String) exprValue);
				break;
			case IMAGE :
				exprValue = (Object) null;
				if(hasValue) {
					if(exprImage())
						return;
				}
				var = new TinyDevVariable(name, DataType.IMAGE, exprValue);
				break;
			case SOUND :
				exprValue = (Object) null;
				if(hasValue) {
					if(exprSound())
						return;
				}
				var = new TinyDevVariable(name, DataType.SOUND, exprValue);
				break;
			case ARRAY :
				var = new TinyDevVariable(name, DataType.ARRAY, dataType, arraySize);
				if(hasValue) {
					if(expr())
						return;
					if(exprType != DataType.ARRAY || exprSubType != var.getSubType()) {
						errorId = Error.BAD_DATA_TYPE;
						return;
					}
					Object src[] = (Object []) exprValue;
					Object dst[] = new Object[src.length];
					
					for(int i = 0; i < src.length; ++i) {
						dst[i] = src[i];
					}

					var.setObjectValue(dst);
					var.setSize(dst.length);
				}
				break;
			default :
				
				// Tipo de dato desconocido, error --- no debería ocurrir
				errorId = Error.BAD_DATA_TYPE;
				return;
		}
		
		// Necesita final
		if(needEnd())
			return;
		
		// Añadir la variable
		variables.add(var);
//System.out.println(var.getName() + " " + var.getType() + " " + var.getSize() + " of " + var.getSubType());		
	}
	
	/**
	 * Instrucción - Asignar un valor a una variable o elemento de un array:
	 * 
	 * SET variable = valor
	 * SET array[índice] = valor
	 */
	private void doSet() {
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Comprobar que el 1er argumento es un identificador
		if(exprTokens.get(1).getType() != TinyDevToken.Id.IDENTIFIER) {
			
			// No, error
			syntaxError();
			return;
		}
		
		// Buscar la variable o array
		TinyDevVariable var = findVariable(exprTokens.get(1).getIdentifier().getName());
		
		// Abortar, si la variable o array no existe
		if(var == null) {
			
			errorId = Error.VARIABLE_NOT_EXISTS;
			return;
		}
		
		// Saltar el identificador
		++exprIndex;
		
		boolean isArray = false;  // Por defecto, no es un array
		int index = 0;            // Indice para el array
		
		// Ver si es un array
		if(arrayIndex()) {
			
			// Si la variable no es un array, error
			if(var.getType() != DataType.ARRAY) {
				
				errorId = Error.BAD_DATA_TYPE;
				return;
			}
			
			// Tomar índice
			index = (int) exprValue;
			
			// Comprobar que el índice es correcto
			if(index < 0 || index >= var.getSize()) {
				
				errorId = Error.OUT_OF_BOUNDS;
				return;
			}
			
			// Ok, es un array
			isArray = true;
		}
		
		// Comprobar si arrayIndex() ha señalado un error
		if(errorId != Error.SUCCESS)
			return;

		// Necesita argumento
		if(needArg())
			return;
		
		// Ha de ser =
		if(!tokenIsOtherOf(exprTokens.get(exprIndex), TinyDevOperator.Id.ASSIGN)) {
			
			syntaxError();
			return;
		}
		
		// Saltar =
		++exprIndex;
		
		// Tomar tipo del dato
		DataType dt = isArray ? var.getSubType() : var.getType();
		
		// Tomar valor, dependiendo del tipo de dato
		switch(dt) {
			case NUMBER :
				if(exprNumber())
					return;
				break;
			case STRING :
				if(exprString())
					return;
				break;
			case IMAGE :
				if(exprImage())
					return;
				break;
			case SOUND :
				if(exprSound())
					return;
				break;
			case ARRAY :
				if(expr())
					return;
				if(exprType != DataType.ARRAY || exprSubType != var.getSubType()) {
					errorId = Error.BAD_DATA_TYPE;
					return;
				}
				break;
			default :
				// Tipo de dato desconocido
				errorId = Error.BAD_DATA_TYPE;
				return;
		}
		
		// Necesita final
		if(needEnd())
			return;
		
		// Si es un array, asignarle el valor y terminar
		if(isArray) {
			
			Object array[] = (Object[]) var.getObjectValue();
			array[index] = exprValue;
			
			return;
		}
		
		// Asignar valor
		if(dt == DataType.ARRAY) {
			// Es un array
			Object src[] = (Object []) exprValue;
			Object dst[] = new Object[src.length];
			
			for(int i = 0; i < src.length; ++i) {
				dst[i] = src[i];
			}

			var.setObjectValue(dst);
			var.setSize(dst.length);
		}
		else {
			// Es una variable, asignarle el valor
			var.setObjectValue(exprValue);
		}
	}

	/**
	 * Instrucción - Ejecución condicional simple:
	 * 
	 * IF condición THEN instrucción
	 * {ELSE IF condición THEN instrucción}
	 * {ELSE instrucción}
	 * 
	 * Ejecución condicional de bloque:
	 * 
	 * IF condición THEN
	 *     instrucciones
	 * {ELSE IF condición THEN
	 *     instrucciones}
	 * {ELSE
	 *     instrucciones}
	 * END IF
	 * 
	 * No se pueden mezclar tipos simples y de bloque.
	 */
	private void doIf() {

		// Tomar valor de la condición
		if(exprBoolean())
			return;
		
		// Necesita argumento
		if(needArg())
			return;
		
		// Ha de ser THEN
		if(!tokenIsCommandOf(exprTokens.get(exprIndex), TinyDevKeyword.Id.THEN)) {
			
			syntaxError();
			return;
		}
		
		// Saltar THEN
		++exprIndex;
		
		// Tomar el resultado de la condición
		boolean test = (boolean) exprValue;
		
		// Si quedan argumentos, es un IF simple
		if(tokensLeft()) {
			
			// Ejecutar la instrucción si la condición es cierta
			if(test) {
				
				// Crear una lista de tokens
				ArrayList<TinyDevToken> tks = new ArrayList<TinyDevToken>();
				
				// Tomar los tokens de la instrucción
				for(int i = exprIndex; i < exprTokens.size(); ++i)
					tks.add(exprTokens.get(i));
				
				// Ejecutar la instrucción		
				command(new TinyDevLine(lineNumber, tks));
			}

			// Indicar el resultado de la condición
			ifTrue = test;
			
			// Finalizar
			return;
		}
		
		// Es un IF de bloque

		// Comprobar si hay IF de bloque disponibles
		if(ifBlockLevel == MAX_IF_BLOCK_LEVEL) {
			
			// No, error
			errorId = Error.TOO_MANY_IF;
			return;
		}
		
		// Añadir nivel de IF
		ifBlockTrue[ifBlockLevel++] = test;

		// Si la condición es falsa, saltar las
		// instrucciones dentro del bloque IF
		if(!test) {
			exitIf(true);			
		}
	}
	
	/**
	 * Instrucción - Ejecución condicional simple:
	 * 
	 * IF condición THEN instrucción
	 * {ELSE IF condición THEN instrucción}
	 * {ELSE instrucción}
	 * 
	 * Ejecución condicional de bloque:
	 * 
	 * IF condición THEN
	 *     instrucciones
	 * {ELSE IF condición THEN
	 *     instrucciones}
	 * {ELSE
	 *     instrucciones}
	 * END IF
	 * 
	 * No se pueden mezclar tipos simples y de bloque.
	 */
	private void doElse() {

		// Resultado de la condición del IF anterior
		boolean wasTrue = false;
		
		// Comprobar si hay IF de bloque activos 
		if(ifBlockLevel > 0) {
			
			// Resultado de la condición del IF de bloque anterior
			wasTrue = ifBlockTrue[ifBlockLevel - 1];
			
			// Si la condición de IF es cierta, saltar
			// las instrucciones del bloque ELSE
			if(wasTrue) {
					exitIf(false);
					return;
			}
			
			// Si es un ELSE sin argumentos, retornar
			if(!tokensLeft()) {
				return;
			}
			
			// Es un ELSE con argumentos
			
			// Ha de ser un ELSE IF
			
			if(!tokenIsCommandOf(exprTokens.get(exprIndex), TinyDevKeyword.Id.IF)) {
				syntaxError();
				return;
			}
			
			// Es un ELSE IF
			
			// Si dicho ELSE se va a ejecutar, decrementar el nivel actual de IF,
			// para que al ejecutarlo, el IF lo incremente, y se quede en
			// el nivel actual (sólo tenemos un END IF).
			if(!wasTrue) {
				--ifBlockLevel;
			}
			
		} else {
			
			if(!tokensLeft()) {
				
				errorId = Error.IF_NOT_ACTIVE;
				return;
			}
			wasTrue = ifTrue;
		}

		// Comprobar si es ELSE de bloque
		/***************************
		if(!tokensLeft()) {
			
			// Si no hay IFs activos, error
			if(ifBlockLevel == 0) {
				
				errorId = Error.IF_NOT_ACTIVE;
				return;
			}
			
			// Si la condición de IF es cierta, saltar
			// las instrucciones del bloque ELSE
			if(ifBlockTrue[ifBlockLevel - 1]) {
				exitIf(false);
			}
			
			// Finalizar
			return;
		}
		*************************/
		// Es un ELSE simple: ELSE instrucciones

		// Ejecutar las instrucciones, si la condición
		// del IF simple es falsa
		if(!wasTrue) {
			
			// Crear array de Tokens
			ArrayList<TinyDevToken> tks = new ArrayList<TinyDevToken>();
			
			// Tomar los Tokens de la instrucción ELSE
			for(int i = 1; i < exprTokens.size(); ++i) {
				
				// Añadir Token
				tks.add(exprTokens.get(i));
			}
			
			// Crear línea de instrucciones y ejecutarla
			command(new TinyDevLine(lineNumber, tks));
		}
	}
	
	/**
	 * Helper para IF / ELSE: Salir de un bloque IF.
	 * 
	 * @param isIf  true si es if quien lo llama, false para else
	 */
	private void exitIf(boolean isIf) {
		
		TinyDevLine line;                       // Línea de instrucciones		
		int lines = source_tokens.size();  // Nº de líneas del código
		int level = ifBlockLevel;          // Nivel de if

		// Recorrer las líneas de código tokenizado, a partir de
		// la siguiente a la actual
		for(int i = lineNumber + 1; i < lines; ++i) {
			
			// Tomar línea tokenizada
			line = source_tokens.get(i);
			
			// Tratarla si no está vacía
			if(!line.getTokens().isEmpty()) {
				
				// Tomar el 1er token
				TinyDevToken tok = line.getTokens().get(0);
				
				// Si el token es un comando, ver si es
				// uno de: IF, ELSE, END IF.
				if(tok.getType() == TinyDevToken.Id.KEYWORD) {
					
					// Tomar comando
					TinyDevKeyword.Id type = tok.getKeyword().getCode();
					
					// Comprobar si es END IF
					if(type == TinyDevKeyword.Id.END) {
						if(line.getTokens().size() > 1) {
							if(line.getTokens().get(1).getType() == TinyDevToken.Id.KEYWORD) {
								if(line.getTokens().get(1).getKeyword().getCode() == TinyDevKeyword.Id.IF) {
									
									// Retornar, si es un END IF del mismo nivel
									if(level == ifBlockLevel) {
										lineNumber = i;
										--ifBlockLevel;
										return;
									}
									
									// Es un END IF de otro nivel, decrementar y continuar buscando
									--level;
								}
							}
						}
					// Comprobar si es IF --- FIXME: Comprobar si es un IF simple
					} else if(type == TinyDevKeyword.Id.IF) {
						
						// Incrementar nivel y continuar buscando
						++level;
					// Comprobar si es ELSE y el método lo llama un IF  --- FIXME: Comprobar si es un ELSE simple
					} else if(isIf && type == TinyDevKeyword.Id.ELSE) {
						
						// Retornar, si es un ELSE del mismo nivel
						if(level == ifBlockLevel) {
							lineNumber = i - 1;
							return;
						}
					}
				}
			}
		}
		
		// Error, se ha llegado al final del código
		errorId = Error.MISSING_END_IF;
	}
	
	/**
	 * Instrucción - Ejecutar la instrucción DO de un bucle,
	 * que puede ser de 2 tipos:
	 * 
	 * DO
	 *    ...
	 * LOOP WHILE | UNTIL condición
	 * 
	 * 
	 * DO WHILE | UNTIL condición
	 *    ...
	 * LOOP
	 */
	private void doDo() {
		
		// Por defecto, es DO no es nuevo
		boolean flagNew = false;
		
		// Comprobar si es un DO nuevo
		if(doLevel == 0 || (doLevel > 0 && lineNumber != doLineNumber[doLevel - 1])) {

			// Comprobar si se pueden crear más
			if(doLevel == MAX_DO_LEVEL) {
				
				// No hay niveles disponibles, error
				errorId = Error.TOO_MANY_DO;
				return;
			}
			
			// Crear nuevo nivel de DO
			doLineNumber[doLevel] = lineNumber;
			doIfBlockLevel[doLevel] = ifBlockLevel;
			flagNew = true;
		}
		
		// Comprobar si es DO
		if(!tokensLeft()) {
			
			// Sí, indicarlo
			if(flagNew) {
				doAlone[doLevel++] = true;
			}

			// Finalizar
			return;
		}
		
		// Ha de ser uno de estos 2:
		//
	    // DO WHILE expresión
	    // DO UNTIL expresión
		
		// Comprobar el resultado de la expresión
		boolean test = testDo();
		
		// Continuar si no hubo errores
		if(errorId == Error.SUCCESS) {
			
			// Si es un DO nuevo, indicar el resultado
			// de la condición
			if(flagNew) {
				doAlone[doLevel++] = false;
			}
			
			// Si la condición es falsa, salir del bucle
			if(!test)
				exitDo();
		}
	}
	
	/**
	 * Instrucción - Ejecutar la instrucción LOOP de un bucle,
	 * que puede ser de 2 tipos:
	 * 
	 * DO
	 *    ...
	 * LOOP WHILE | UNTIL condición
	 * 
	 * 
	 * DO WHILE | UNTIL condición
	 *    ...
	 * LOOP
	 */
	private void doLoop() {
		
		// Si no hay DO activo, abortar
		if(doLevel == 0) {
			errorId = Error.DO_NOT_ACTIVE;
			return;
		}
		
		// Comprobar si es LOOP sin condición
		if(!tokensLeft()) {
			
			// Si el DO lleva la condición, saltar a la línea
			// del DO, de lo contrario, error
			if(doAlone[doLevel - 1])
				errorId = Error.DO_LOOP_MISMATCH;
			else
				lineNumber = doLineNumber[doLevel - 1] - 1;
			
			// Finalizar
			return;			
		}
		
		// Puede ser 1 de estas 2 formas:
		//
	    // LOOP WHILE expresión
	    // LOOP UNTIL expresión
		
		// Si es un LOOP sin expresión, es un error
		if(!doAlone[doLevel - 1]) {
			errorId = Error.DO_LOOP_MISMATCH;
			return;
		}
		
		// Comprobar el resultado de la expresión
		if(testDo())
			lineNumber = doLineNumber[doLevel - 1] - 1;  // Verdadera: Saltar a la línea del DO
		else {
			// Falsa: Salir del bucle
			--lineNumber; // exitDo() busca a partir de la línea actual, así que debemos decrementarla para que encuentre este LOOP
			exitDo();
			//--doLevel;
			//ifBlockLevel = doIfBlockLevel[doLevel];
		}
	}
	
	/**
	 * Instrucción - Salir de un bucle:
	 * 
	 * BREAK
	 */
	private void doBreak() {
		
		// Necesita final, pues no tiene argumentos
		if(needEnd())
			return;
		
		// Comprobar que hay algún bucle activo
		if(doLevel == 0) {
			errorId = Error.DO_NOT_ACTIVE;
			return;
		}
		
		// Salir del bucle
		exitDo();
	}
	
	/**
	 * Helper para DO / LOOP: Salir de un bucle.
	 */
	private void exitDo() {
		
		TinyDevLine line;		               // Línea de tokens
		int lines = source_tokens.size();  // Nº de líneas del código
		int level = doLevel;               // Nivel del bucle
		
		// Recorrer todas las líneas, desde la posición siguiente a la actual
		for(int i = lineNumber + 1; i < lines; ++i) {
			
			// Tomar línea
			line = source_tokens.get(i);
			
			// Proceder si no está vacía
			if(!line.getTokens().isEmpty()) {
				
				// Tomar 1er token
				TinyDevToken tok = line.getTokens().get(0);
				
				// Proceder si es una palabra reservada, y comprobar si es DO o LOOP
				if(tok.getType() == TinyDevToken.Id.KEYWORD) {
					
					// Tomar tipo de palabra reservada
					TinyDevKeyword.Id type = tok.getKeyword().getCode();
					
					// Comprobar si es LOOP
					if(type == TinyDevKeyword.Id.LOOP) {
						
						// Si es el mismo nivel, saltar a la línea
						// posterior al bucle y finalizar
						if(level == doLevel) {
							
							lineNumber = i;
							--doLevel;
							ifBlockLevel = doIfBlockLevel[doLevel];
							return;
						}
						
						// No es el mismo nivel, decrementar y continuar
						--level;
						
					// Comprobar si es DO
					} else if(type == TinyDevKeyword.Id.DO) {
						
						// Incrementar y continuar
						++level;
					}
				}
			}
		}
		
		// Se ha llegado al final, error
		errorId = Error.MISSING_LOOP;
	}
	
	/**
	 * Helper para bucles: Comprobar si la condición es cierta o falsa,
	 * y devolver un valor que indica si se ha de continuar la ejecución del bucle o no,
	 * dependiendo del resultado de la condición, y de si es un bucle WHILE o UNTIL.
	 * 
	 * @return  true para continuar en el bucle, false en caso contrario
	 */
	private boolean testDo() {
		
		// Puede ser una de estas formas:
		//
		// DO WHILE expresión
		// DO UNTIL expresión
		// LOOP WHILE expresión
		// LOOP UNTIL expresión
		
		// Necesitar argumento (WHILE o UNTIL)
		if(needArg())
			return false;
		
		// Flag para discriminar entre WHILE o UNTIL
		boolean isWhile;
		
		// Comprobar si es WHILE o UNTIL
		if(tokenIsCommandOf(exprTokens.get(1), TinyDevKeyword.Id.WHILE))
			isWhile = true;
		else if(tokenIsCommandOf(exprTokens.get(1), TinyDevKeyword.Id.UNTIL))
			isWhile = false;
		else {
			// Ninguno de los 2, error
			syntaxError();
			return false;
		}
		
		// Saltar WHILE o UNTIL
		++exprIndex;
		
		// Comprobar la expresión, y retornar si hubo error
		if(exprBoolean())
			return false;
		
		// Tomar el resultado de la expresión
		boolean test = (boolean) exprValue;
		
		// Necesita final, no hay más argumentos
		if(needEnd())
			return false;
		
		// Devolver TRUE si la condición es cierta y es un WHILE,
		// o si la condición es falsa y es un UNTIL,
		// para continuar en el bucle
		if((test && isWhile) || (!test && !isWhile))
			return true;
		
		// Hay que salir del bucle
		return false;
	}
	
	/**
	 * Instrucción - Saltar a un place:
	 * 
	 * JUMP place
	 */
	private void doJump() {
		
		// Necesita argumentos: place
		if(needArg())
			return;
		
		// Comprobar que hay un identificador
		if(exprTokens.get(exprIndex).getType() != TinyDevToken.Id.IDENTIFIER) {
			syntaxError();
			return;
		}
		
		// Buscar el place
		TinyDevPlace pl = findPlace(exprTokens.get(exprIndex).getIdentifier().getName());
		
		// Si no existe, error
		if(pl == null) {
			errorId = Error.PLACE_NOT_EXISTS;
			return;
		}
		
		// Saltar el place
		++exprIndex;
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Saltar a la línea del place
		lineNumber = pl.getLine() - 1;
	}
	
	/**
	 * Instrucción - Llamar a un place (se vuelve con RETURN):
	 * 
	 * CALL place
	 */
	private void doCall() {
		
		// Si no es posible crear más niveles de CALL, error		
		if(callLevel == MAX_CALL_LEVEL) {
			errorId = Error.TOO_MANY_CALL;
			return;
		}
		
		// Tomar nota del nº de línea actual
		callLineNumber[callLevel] = lineNumber;       // Esto ha de hacerse antes de llamar a doJump()
		
		// Tomar niveles actuales de IF y DO (necesario para RETURN)
		callIfBlockLevel[callLevel] = ifBlockLevel;
		callDoLevel[callLevel] = doLevel;
		
		// Añadir un nivel de CALL
		++callLevel;
		
		// Llamar a JUMP, que se encargará del resto
		doJump();
	}
	
	/**
	 * Instrucción - Retornar desde una llamada CALL TO:
	 * 
	 * RETURN
	 */
	private void doReturn() {
		
		// No tiene argumentos
		if(needEnd())
			return;
		
		// Si no hay niveles de CALL, error
		if(callLevel == 0) {
			errorId = Error.CALL_NOT_ACTIVE;
			return;
		}
		
		// Decrementar el nivel de CALLs
		--callLevel;
		
		// Recuperar niveles de IF y DO
		ifBlockLevel = callIfBlockLevel[callLevel];
		doLevel = callDoLevel[callLevel];
		
		// Saltar a la línea del CALL (el executor la incrementará)
		lineNumber = callLineNumber[callLevel];
	}
	
	/**
	 * Instrucción - Finaliza el programa, o un bloque IF:
	 * 
	 * END
	 * END IF
	 */
	private void doEnd() {
		
		// Si no tiene argumentos, finalizar el programa		
		if(!tokensLeft()) {
			stopped = true;
		}
		else if(tokenIsCommandOf(exprTokens.get(1), TinyDevKeyword.Id.IF)) {
			
			// Es un IF, decrementar el nivel de IFs
			if(ifBlockLevel > 0) {
				
				--ifBlockLevel;
			} else {
				
				// Error, no hay IFs activos
				errorId = Error.IF_NOT_ACTIVE;
			}
			
			// Saltar el IF
			++exprIndex;
		}
		
		// No ha de haber más argumentos
		needEnd();
	}
	
	/**
	 * Instrucción - Dibujar una elipse (o círculo):
	 * 
	 * ELLIPSE {FILL | SOLID} x, y, ancho, alto
	 */
	private void doEllipse() {
		
		// Estilo por defecto (dibujar sólo borde)
		Style style = Style.EMPTY;
				
		if(tokenIsCommandOf(exprTokens.get(exprIndex), TinyDevKeyword.Id.FILL)) {
			style = Style.FILL;
			++exprIndex;
		} else if(tokenIsCommandOf(exprTokens.get(exprIndex), TinyDevKeyword.Id.SOLID)) {
			style = Style.SOLID;
			++exprIndex;
		}
				
		if(exprNumber())             // Calcular X
			return;
		
		int x = (int) exprValue;     // Tomar X
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())              // Calcular Y
			return;

		int y = (int) exprValue;      // Tomar Y
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())              // Calcular ancho
			return;
		
		int width = (int) exprValue;  // Tomar ancho
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())              // Calcular alto
			return;

		int height = (int) exprValue; // Tomar alto
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Si las posiciones son válidas, dibujar
		if(io.ellipse(x,  y, width, height, style))
			errorId = Error.BAD_SCREEN_POSITION;
	}
	
	/**
	 * Instrucción - Dibujar un rectángulo (o cuadrado):
	 * 
	 * RECTANGLE {FILL | SOLID} x, y, ancho, alto
	 */
	private void doRectangle() {
		
		// Estilo por defecto (dibujar sólo borde)
		Style style = Style.EMPTY;
				
		if(tokenIsCommandOf(exprTokens.get(exprIndex), TinyDevKeyword.Id.FILL)) {
			style = Style.FILL;
			++exprIndex;
		} else if(tokenIsCommandOf(exprTokens.get(exprIndex), TinyDevKeyword.Id.SOLID)) {
			style = Style.SOLID;
			++exprIndex;
		}
		
		if(exprNumber())               // Calcular X
			return;
		
		int x = (int) exprValue;       // Tomar X
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())               // Calcular Y
			return;
		
		int y = (int) exprValue;       // Tomar Y
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())               // Calcular ancho
			return;
		
		int width = (int) exprValue;   // Tomar ancho
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())               // Calcular alto
			return;
		
		int height = (int) exprValue;  // Tomar alto
		
		// No ha de haber más argumentos
		if(needEnd())
			return;
		
		// Si las posiciones son válidas, dibujar
		if(io.rectangle(x, y, width, height, style))
			errorId = Error.BAD_SCREEN_POSITION;
	}
	
	/**
	 * Instrucción - Dibujar línea:
	 * 
	 * LINE x y x2 y2
	 */
	private void doLine() {
		
		if(exprNumber())               // Calcular X
			return;
		
		int x = (int) exprValue;       // Tomar X
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())               // Calcular Y
			return;
		
		int y = (int) exprValue;       // Tomar Y
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())               // Calcular X2
			return;
		
		int x2 = (int) exprValue;      // Tomar X2
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())               // Calcular Y2
			return;
		
		int y2 = (int) exprValue;      // Tomar Y2
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Si las posiciones son válidas, dibujar
		if(io.line(x, y, x2, y2))
			errorId = Error.BAD_SCREEN_POSITION;
	}
	
	/**
	 * Instrucción - Cambiar las dimensiones de la ventana de ejecución,
	 * y borrar su contenido:
	 * 
	 * SCREEN ancho alto
	 */
	private void doScreen() {
		
		if(exprNumber())              // Calcular ancho
			return;
		
		int width = (int) exprValue;  // Tomar ancho
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())              // Calcular alto
			return;
		
		int height = (int) exprValue; // Tomar alto
		
		// No hay más argumentos
		if(needEnd())
			return;

		// Cambiar las dimensiones de la pantalla
		if(io.reset(width, height))
			errorId = Error.BAD_SCREEN_SIZE;
	}
	
	/**
	 * Instrucción - Dibujar un punto:
	 * 
	 * POINT x y
	 */
	private void doPoint() {
		
		if(exprNumber())         // Calcular X
			return;
		
		int x = (int) exprValue; // Tomar X
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())         // Calcular Y
			return;
		
		int y = (int) exprValue; // Tomar Y
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Si la posición es correcta, dibujar
		if(io.point(x,y))
			errorId = Error.BAD_SCREEN_POSITION;
	}
	
	/**
	 * Instrucción - Dibujar una imagen:
	 * 
	 * IMAGE x y imagen
	 */
	private void doImage() {
		
		if(exprNumber())         // Calcular X
			return;
		
		int x = (int) exprValue; // Tomar X
		
		if(eatComma())				 // Coma
			return;
		
		if(exprNumber())         // Calcular Y
			return;
		
		int y = (int) exprValue; // Tomar Y
		
		if(eatComma())				 // Coma
			return;

		if(exprImage())          // Calcular la imagen
			return;
		
		// Comprobar que la imagen es válida
		if(exprValue == null) {
			errorId = Error.NULL_IMAGE;
			return;
		}
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Dibujar la imagen
		if(	io.drawImage(x, y, exprValue))
			errorId = Error.BAD_SCREEN_POSITION;
	}
	
	/**
	 * Instrucción - Emitir un sonido:
	 * 
	 * SOUND sonido
	 */
	private void doSound() {
		
		// Calcular la expresión
		if(expr())
			return;
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Sonido
		Object snd = null;
		
		// Por ahora, sólo almacena una String con el nombre del fichero (no hay objetos sound,
		// a diferencia de lo que ocurre con image)
		
		// Tomar valor del sonido
		if(exprType == DataType.SOUND) {
			snd = (Object) exprValue;
		} else if(exprType == DataType.STRING) {
			snd = (Object) exprValue;
		} else {
			
			// Error en el tipo de dato
			errorId = Error.BAD_DATA_TYPE;
			return;
		}
		
		// Comprobar que el sonido tenga contenido
		if(snd == null) {
			errorId = Error.NULL_SOUND;
			return;
		}
		
		// Hacerlo sonar
		io.playSound(snd);
	}
	
	/**
	 * Instrucción - Indicar el nº máximo que dará el generador
	 * de números aleatorios:
	 * 
	 * RANDOM máximo
	 */
	private void doRandom() {
		
		// Calcular el máximo
		if(exprNumber())
			return;
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Tomar el valor
		int max = (int) exprValue;
		
		// Comprobar que es un valor correcto
		if(max < 2 || max > 32767) {
			errorId = Error.BAD_ARGUMENT; // FIXME: Cambiar el tipo de error
			return;
		}
		
		// Establecer
		randMax = max;
	}
	
	/**
	 * Instrucción - Pausar la ejecución del programa:
	 * 
	 * WAIT milisegundos
	 */
	private void doWait() {
		
		// Calcular el valor
		if(exprNumber())
			return;
		
		// No hay más argumentos
		if(needEnd())
			return;
		
		// Establecer la pausa
		int waitTime = (int) exprValue; // FIXME: Comprobar el valor de waitTime
		
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // FIXME
		}
	}
	
	/**
	 * Instrucción - Escribir fichero:
	 * 
	 * WRITEFILE filename, textArray
	 */
	private void doWriteFile() {
		
		// Tomar valor del nombre de fichero
		if(exprString())
			return;
		
		String fname = addCurrentDirToFile((String) exprValue);
		
		// Coma
		if(eatComma())
			return;
		
		// Ha de ser un identificador
		if(exprTokens.get(3).getType() != TinyDevToken.Id.IDENTIFIER) {
			syntaxError();
			return;
		}
		
		// Ha de ser una variable
		TinyDevVariable var = findVariable(exprTokens.get(3).getIdentifier().getName());
		
		// Abortar, si la variable no existe
		if(var == null) {
			errorId = Error.VARIABLE_NOT_EXISTS;
			return;
		}
		
		// Ha de ser un array de tipo string
		if(var.getType() != DataType.ARRAY || var.getSubType() != DataType.STRING) {
			errorId = Error.BAD_DATA_TYPE;
			return;
		}

		// Saltar el identificador
		++exprIndex;
		
		// Necesita final
		if(needEnd())
			return;
		
		//
		Object arr[] = (Object[]) var.getObjectValue();
		
		//
		BufferedWriter bfwr = null;
		
		//
		try {
			
			//
			bfwr = new BufferedWriter(new FileWriter(fname));
			
			for(int i = 0; i < arr.length; ++i) {
				bfwr.write((String) arr[i]);
				bfwr.newLine();
			}
			
			bfwr.flush();
			
		} catch (IOException e) {
			errorId = Error.WRITING_FILE;
		}
		
		if(bfwr != null) {
			try {
				bfwr.close();
			} catch (IOException ex) {
				errorId = Error.WRITING_FILE;
			}
		}
	}
	
	/**
	 * Instrucción - Escribir imagen:
	 * 
	 * WRITEIMAGE filename, image
	 */
	private void doWriteImage() {
		
		// Tomar valor del nombre de fichero
		if(exprString())
			return;
		
		String fname = addCurrentDirToFile((String) exprValue);
		
		// Coma
		if(eatComma())
			return;
		
		// Tomar valor de la imagen -- FIXME ¿Qué pasa si... writeImage("p.png", "p.png") ?
		if(exprImage()) {
			return;
		}

		// Comprobar que la imagen es válida
		if(exprValue == null) {
			errorId = Error.NULL_IMAGE;
			return;
		}
		
		Object img = exprValue;
		
		// Necesita final
		if(needEnd())
			return;
		
		//
		if(io.saveImage(fname, img)) {
			errorId = Error.CANT_SAVE_IMAGE;
		}
	}
	
	
	/**
	 * Helper.
	 * 
	 * Prefijar un nombre de fichero con el directorio de trabajo actual,
	 * si lo precisa.
	 * 
	 * @param fname  nombre del fichero
	 * @return       nombre del fichero transformado
	 */
	private String addCurrentDirToFile(String fname)
	{
		File f = new File(fname);
		
		return f.isAbsolute() ? fname : currentDirectory + File.separator + fname;
	}
	
	/**
	 * Helper.
	 * 
	 * Convertir una cadena textual en su valor numérico correspondiente.
	 * 
	 * @param s  cadena textual
	 * @return   valor numérico, o 0 en caso de no tratarse de un número
	 */
	private int stringToInt(String s) {
		
		// Valor resultante
		int val;
		
		// Parsear y calcular
		try {
			// Convertir
			val = Integer.parseInt(s);
		} catch(NumberFormatException ex) {
			// En caso de error, dar el valor 0
			val = 0;
		}

		// Retornar valor
		return val;
	}
	
	/**
	 * Helper.
	 * 
	 * Establece el código de error en BAD_SYNTAX.
	 */
	private void syntaxError() {
		
		errorId = Error.BAD_SYNTAX;
	}
	
	// ------------------
	// GESTIÓN DE ERRORES
	// ------------------
	
	/**
	 * Helper.
	 * 
	 * Devuelve el mensaje de texto, que corresponde a un código de error.
	 * 
	 * @param id  código de error
	 * @return    mensaje de texto correspondiente
	 */
	private String errorMsg(Error id) {
		
		// Devolver mensaje de texto que corresponde
		// al código de error
		switch(id) {
			case MISSING_QUOTE : return loc.getError("MissingQuote");
			case VARIABLE_EXISTS : return loc.getError("VariableExists");
			case VARIABLE_NOT_EXISTS : return loc.getError("VariableNotExists");
			case BAD_SYNTAX : return loc.getError("BadSyntax");
			case BAD_DATA_TYPE : return loc.getError("BadDataType");
			case PLACE_EXISTS : return loc.getError("PlaceExists");
			case PLACE_NOT_EXISTS : return loc.getError("PlaceNotExists");
			case BAD_ARGUMENT : return loc.getError("BadArgument");
			case BAD_NUMBER_OF_ARGUMENTS : return loc.getError("BadNumberOfArguments");
			case BAD_COLOR : return loc.getError("BadColor");
			case TOO_MANY_DO : return loc.getError("TooManyDo");
			case DO_NOT_ACTIVE : return loc.getError("DoNotActive");
			case DO_LOOP_MISMATCH : return loc.getError("DoLoopMismatch");
			case MISSING_LOOP : return loc.getError("MissingLoop");
			case TOO_MANY_CALL : return loc.getError("TooManyCall");
			case CALL_NOT_ACTIVE : return loc.getError("CallNotActive");
			case BAD_SCREEN_POSITION : return loc.getError("BadScreenPosition");
			case BAD_SCREEN_SIZE : return loc.getError("BadScreenSize");
			case CANT_LOAD_IMAGE : return loc.getError("CantLoadImage");
			case CANT_SAVE_IMAGE : return loc.getError("CantSaveImage");
			case NULL_IMAGE : return loc.getError("NullImage");
			case CANT_LOAD_SOUND : return loc.getError("CantLoadSound");
			case NULL_SOUND : return loc.getError("NullSound");
			case IF_NOT_ACTIVE : return loc.getError("IfNotActive");
			case TOO_MANY_IF : return loc.getError("TooManyIf");
			case MISSING_END_IF : return loc.getError("MissingEndIf");
			case BAD_EXPRESSION : return loc.getError("BadExpression");
			case OUT_OF_BOUNDS : return loc.getError("OutOfBounds");
			case BAD_ARRAY_SIZE : return loc.getError("BadArraySize");
			case READING_FILE : return loc.getError("ReadingFile");
			case WRITING_FILE : return loc.getError("WritingFile");
			
			// Código desconocido
			default :
				return loc.getError("Unknown");
		}
	}
	
	/**
	 * Emite un mensaje de error, según el código de error actual
	 * establecido en errorId.
	 */
	private void printError() {
		
		// Titulo de la ventana
		String title = loc.getTitle("Error");
		
		// Mensaje de error
		String text  = loc.getString("ErrorInLineNumber") + " " + (lineNumber + 1) + ": "
				+ errorMsg(errorId) + "." + "\n\n"
				+ source.get(lineNumber).trim();   // Línea de código fuente sin espacios alrededor
		
		// Mostrar mensaje de error
		io.dialogError(text, title);
		
		editor.notifyError(lineNumber, source.get(lineNumber), errorMsg(errorId));

	}
	
	// ---------------------
	// E X P R E S I O N E S
	// ---------------------

	// El cálculo de expresiones, se basa en un parser descendente recursivo, relativamente simple.
	//
	// Me he inspirado en el parser de MESCC (Mike's Enhanced Small C Compiler), mi propia
	// versión de Small C, para el sistema operativo CP/M y la cpu Z80.
	//
	// A partir de él, se han creado una serie de helpers, para cada tipo de expresión.
	
	// FIXME: Indicar operadores, prioridad, etc.

	/**
	 * Helper: Calcula una expresión numérica.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprNumber() {
		
		// Calcular expresión
		if(!expr()) {
			
			// Si el resultado es un número, éxito
			if(exprType == DataType.NUMBER)
				return false;
			
			/************************
			// Si el resultado es una cadena textual, convertir a su
			// valor numérico, y éxito ---- FIXME: Esto debería ser sólo para ASSIGN y VARIABLE
			if(exprType == DataType.STRING) {
				
				exprType = DataType.NUMBER;
				exprValue = stringToInt((String) exprValue);
				return false;
			}
			**********************/
			
			// Tipo resultante incorrecto
			errorId = Error.BAD_DATA_TYPE;
		}
		
		// Indicar error
		return true;
	}
	
	/**
	 * Helper: Calcula una expresión textual.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprString() {
		
		// Calcular expresión
		if(!expr()) {
			
			// Si el resultado es textual, éxito
			if(exprType == DataType.STRING)
				return false;

			/****************************
			// Si el resultado es numérico, convertir a su valor textual
			// y éxito ---- FIXME: Esto debería ser sólo para ASSIGN y VARIABLE
			if(exprType == DataType.NUMBER) {
				
				exprType = DataType.STRING;
				exprValue = "" + (int) exprValue;
				return false;
			}
			***********************/
			
			// Tipo resultante incorrecto
			errorId = Error.BAD_DATA_TYPE;
		}
		
		// Indicar error
		return true;
	}
	
	/**
	 * Helper: Calcula una expresión booleana.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprBoolean() {
		
		// Calcular la expresión
		if(!expr()) {
			
			// Si el resultado es booleano, éxito
			if(exprType == DataType.BOOLEAN)
				return false;

			// Tipo incorrecto
			errorId = Error.BAD_DATA_TYPE;
		}
		
		// Indicar error
		return true;
	}
	
	/**
	 * Helper: Calcula una expresión de imagen.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprImage() {
		
		// Calcular expresión
		if(!expr()) {
			
			// Si el resultado es de tipo imagen, éxito
			if(exprType == DataType.IMAGE)
				return false;
			
			// Si el resultado es de tipo textual, considerar
			// que es un nombre de fichero, y tratar de convertirlo
			// en imagen
			if(exprType == DataType.STRING) {
				
				// Tomar el valor del nombre de fichero, y cargarlo
				Object img = io.loadImage(addCurrentDirToFile((String)exprValue));
				
				// Comprobar error de carga
				if(img == null) {
					errorId = Error.CANT_LOAD_IMAGE;
					return true;
				}
				
				// Éxito
				exprType = DataType.IMAGE;
				exprValue = img;
				return false;
			}
			
			// Tipo incorrecto
			errorId = Error.BAD_DATA_TYPE;
		}
		
		// Indicar error
		return true;
	}
	
	/**
	 * Helper: Calcula una expresión de sonido.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprSound() {
		
		// Calcular expresión
		if(!expr()) {
			
			// Si es de tipo sonido, éxito
			if(exprType == DataType.SOUND)
				return false;
			
			// Si se trata de una cadena, considerar
			// que es un nombre de fichero
			if(exprType == DataType.STRING) {
				
				// Cargar el sonido
				Object snd = io.loadSound(addCurrentDirToFile((String)exprValue));
				
				// Comprobar si se pudo cargar
				if(snd == null) {
					errorId = Error.CANT_LOAD_SOUND;
					return true;
				}
				
				// Éxito
				exprType = DataType.SOUND;
				exprValue = snd;
				return false;
			}
			
			// Tipo incorrecto
			errorId = Error.BAD_DATA_TYPE;
		}
		
		// Indicar error
		return true;
	}
	
	/**
	 * Calcula una expresión de cualquier tipo.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean expr() {
		
		exprType = null;	// Tipo resultante de la expresión
		exprSubType = null;	// Sub-tipo para arrays
		exprValue = null;	// Valor resultante de la expresión
		
		// Calcular la expresión, llamando al 1er nivel
		if(exprLogical()) {
			
			// Si ha devuelto true, es que hubo un error, por lo que
			// se ha que indicar un código de error genérico, de no ser que
			// alguno de los distintos métodos que componen el cálculo de
			// la expresión, haya indicado un código de error
			// concreto.
			if(errorId == Error.SUCCESS)
				errorId = Error.BAD_EXPRESSION;
			
			// Fracaso
			return true;
		}
	
		// Éxito
		return false;
	}
	
	/**
	 * Comparación lógica AND, OR.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprLogical() {
		
		// Llamar al nivel siguiente
		if(exprRelational())
			return true;
		
		// Bucle
		while(true) {
			
			// Tomar siguiente token
			TinyDevToken token = peekToken();
			
			// Salir del bucle si no es un operador
			if(token == null || token.getType() != TinyDevToken.Id.OPERATOR)
				break;
			
			// Tomar tipo de operador
			TinyDevOperator.Id id = token.getOperator().getType();
			
			// Salir del bucle si no es un operador de este nivel
			if(id != TinyDevOperator.Id.AND && id != TinyDevOperator.Id.OR)
				break;
			
			// Saltar el operador
			++exprIndex;

			// El valor de la izquierda ha de ser booleano
			if(exprType != DataType.BOOLEAN)
				return true;
			
			// Tomar valor de la izquierda
			boolean b1 = (boolean) exprValue;
			
			// Llamar al nivel siguiente
			if(exprRelational())
				return true;
			
			// El valor de la derecha, ha de ser booleano			
			if(exprType != DataType.BOOLEAN)
				return true;
			
			// Tomar valor de la derecha
			boolean b2 = (boolean) exprValue;
			
			// Calcular el resultado de la expresión
			switch(id) {
				case AND :
					exprValue = b1 && b2;
					break;
				case OR :
					exprValue = b1 || b2;
					break;
				default :
					// Operador desconocido -- no debería ocurrir
					return true;
			}
		}
		
		// Éxito
		return false;		
	}
	
	/**
	 * Comparación relacional ==, !=, >, >=, <, <=.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprRelational() {
		
		// Llamar al siguiente nivel
		if(exprAdd())
			return true;
		
		// Bucle
		while(true) {
			
			// Tomar siguiente token
			TinyDevToken token = peekToken();
			
			// Salir del bucle si no es un operador
			if(token == null || token.getType() != TinyDevToken.Id.OPERATOR)
				break;
			
			// Tomar tipo de operador
			TinyDevOperator.Id id = token.getOperator().getType();
			
			// Salir del bucle si no es un operador de este nivel
			if(id != TinyDevOperator.Id.EQUAL && id != TinyDevOperator.Id.NOT_EQUAL
					&& id != TinyDevOperator.Id.LESS && id != TinyDevOperator.Id.LESS_OR_EQUAL
					&& id != TinyDevOperator.Id.GREATER && id != TinyDevOperator.Id.GREATER_OR_EQUAL)
				break;
			
			// Saltar el operador
			++exprIndex;
			
			// Guardar tipo y valor actuales
			DataType exType = exprType;
			Object exValue = exprValue;
			
			// Llamar al siguiente nivel
			if(exprAdd())
				return true;
			
			// Solo se permiten comparaciones de 2 valores del
			// mismo tipo, y que sean numéricos o textuales
			if(exType != exprType || (exType != DataType.NUMBER && exType != DataType.STRING))
				return true;
			
			// Resultado de la comparación
			int result;
			
			// Comparar
			if(exType == DataType.NUMBER)
				result = (int) exValue - (int) exprValue;
			else {
				String s1 = (String) exValue;
				String s2 = (String) exprValue;
				result = s1.compareTo(s2);
			}
			
			// El tipo de la expresión será booleano
			exprType = DataType.BOOLEAN;
		
			// Calcular el valor de la expresión
			switch(id) {
				case EQUAL :
					exprValue = result == 0;
					break;
				case NOT_EQUAL :
					exprValue = result != 0;
					break;
				case GREATER:
					exprValue = result > 0;
					break;
				case GREATER_OR_EQUAL :
					exprValue = result >= 0;
					break;
				case LESS:
					exprValue = result < 0;
					break;
				case LESS_OR_EQUAL:
					exprValue = result <= 0;
					break;
				default :
					// Operador desconocido -- no debería ocurrir
					return true;
			}
		}
		
		// Éxito
		return false;
	}
	
	/**
	 * Operación aritmética: +, -.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprAdd() {
		
		// Llamar al nivel siguiente
		if(exprMul())
			return true;
		
		// Bucle
		while(true) {
			
			// Tomar siguiente token
			TinyDevToken token = peekToken();
			
			// Si no es del tipo requerido, salir del bucle
			if(token == null || token.getType() != TinyDevToken.Id.OPERATOR)
				break;
			
			// Tomar tipo de token
			TinyDevOperator.Id id = token.getOperator().getType();
			
			// Si no es + o -, salir del bucle
			if(id != TinyDevOperator.Id.PLUS && id != TinyDevOperator.Id.MINUS)
				break;
			
			// Saltar operador
			++exprIndex;
			
			// Guardar tipo y valor actuales
			DataType exType = exprType;
			Object exValue = exprValue;
			
			// Llamar al siguiente nivel
			if(exprMul())
				return true;
			
			// Sólo se permiten valores numéricos o textuales				
			if((exType != DataType.STRING  && exType != DataType.NUMBER)
					|| (exprType != DataType.STRING && exprType != DataType.NUMBER))
				return true;
			
			// Calcular el valor y tipo de la expresión
			switch(id) {
			
				// Suma: Se pueden sumar 2 números, o concatenar
				//       2 cadenas.
				case PLUS :
					if(exType == DataType.NUMBER && exprType == DataType.NUMBER) {
						// Suma: 5 + 6 == 11
						exprValue = (int) exValue + (int) exprValue;
						break;
					}
					
					if(exType == DataType.STRING && exprType == DataType.STRING) {
						// Concatena: "k" + "q" == "kq"
						exprValue = (String) exValue + (String) exprValue;
						break;
					}
					
					// Error: "k" + 5
					// Error: 5 + "k"
					return true;
					
				// Resta: Sólo se pueden restar 2 números.
				case MINUS :
					if(exType == DataType.NUMBER && exprType == DataType.NUMBER) {
						// Resta: 5 - 6 == -1
						exprValue = (int) exValue - (int) exprValue;
						break;
					}
					
					// Hay algún valor textual, error
					return true;
				default :
					// Operador desconocido -- no debería ocurrir
					return true;
			}
		}
		
		// Éxito
		return false;
	}
	
	/**
	 * Operación aritmética: *, /, %.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprMul() {
		
		// Llamar al siguiente nivel
		if(exprValue())
			return true;
		
		// Bucle
		while(true) {
			
			// Tomar token
			TinyDevToken token = peekToken();
			
			// Si no es del tipo requerido, salir del bucle
			if(token == null || token.getType() != TinyDevToken.Id.OPERATOR)
				break;
			
			// Tomar tipo del operador
			TinyDevOperator.Id id = token.getOperator().getType();
			
			// Si no es uno de los requeridos, salir del bucle
			if(id != TinyDevOperator.Id.MULTIPLY && id != TinyDevOperator.Id.DIVIDE && id != TinyDevOperator.Id.MODULUS)
				break;
			
			// Saltar operador
			++exprIndex;
			
			// El valor de la izquierda ha de ser numérico
			if(exprType != DataType.NUMBER)
				return true;
			
			// Tomar el valor de la izquierda
			int v1 = (int) exprValue;
			
			// Llamar al siguiente nivel
			if(exprValue())
				return true;
			
			// El valor de la derecha ha de ser numérico
			if(exprType != DataType.NUMBER)
				return true;
			
			// Tomar valor de la derecha
			int v2 = (int) exprValue;
			
			switch(id) {
				case MULTIPLY :
					exprValue = v1 * v2;
					break;
				case DIVIDE :
					exprValue = v1 / v2;
					break;
				case MODULUS :
					exprValue = v1 % v2;
					break;
				default :
					// Operador erróneo -- no debería suceder
					return true;
			}
		}
		
		// Éxito
		return false;
	}
	
	/**
	 * Tomar valor primario.
	 * 
	 * En caso de error, lo indica en la variable errorId.
	 * 
	 * @return true en caso de error, false en caso contrario
	 */
	private boolean exprValue() {
		
		// Tomar token
		TinyDevToken token = peekToken();
		
		// Si hay token disponible, ver de qué tipo es
		if(token != null) {
			
			// Abrir paréntesis: expresión anidada
			if(tokenIsOtherOf(token, TinyDevOperator.Id.OPEN_PARENTHESIS)) {
				
				// Saltar paréntesis
				++exprIndex;
				
				// Llamar al nivel más bajo de expresión
				if(exprLogical())
					return true;
				
				// Tomar token
				token = peekToken();
				
				// Si hay token disponible, ha de ser el cierre del paréntesis
				if(token != null) {
					if(tokenIsOtherOf(token, TinyDevOperator.Id.CLOSE_PARENTHESIS)) {
						
							// Saltar paréntesis
							++exprIndex;
							
							// Éxito
							return false;
					}
				}
				
				// Error
				return true;
			}
		
			// Signo '-', ha de ser un valor numérico negativo;
			if(tokenIsOtherOf(token, TinyDevOperator.Id.MINUS)) {

				// Saltar signo
				++exprIndex;

				// Llamar a este mismo nivel
				if(!exprValue()) {
					
					// Ha de ser un número
					if(exprType == DataType.NUMBER) {
						
						// Tomar su valor negativo
						exprValue = -(int)exprValue;
						
						// Éxito
						return false;
					}
				}
				
				// Error
				return true;
			}
			
			// Tomar el tipo del token
			TinyDevToken.Id tokType = token.getType();
			
			// Ver si es un identificador
			if(tokType == TinyDevToken.Id.IDENTIFIER) {
				
				// Saltar identificador
				++exprIndex;
				
				// Ha de ser una variable
				TinyDevVariable var = findVariable(token.getIdentifier().getName());
				
				// Si no existe como variable, error
				if(var == null) {
					
					errorId = Error.VARIABLE_NOT_EXISTS;
					return true;
				}
				
				// Tomar el tipo de la variable
				DataType varType = var.getType();
				
				// Ver si es un array
				if(varType == DataType.ARRAY) {
					// Ver si es un elemento de un array
					if(arrayIndex()) {
							
						// Tomar el índice
						int index = (int) exprValue;
						
						// Comprobar que el índice es correcto
						if(index >= 0 && index < var.getSize()) {
							
							// Tomar el valor del elemento del array
							Object array[] = (Object []) var.getObjectValue();
							exprValue = array[(int) exprValue];
							
							// Tomar el tipo
							exprType = var.getSubType();
							
							// Éxito
							return false;
						}
						
						// Error en el índice del array
						errorId = Error.OUT_OF_BOUNDS;
						
						return true;
					}
					
					if(errorId != Error.SUCCESS) {
						// Fracaso
						return true;
					}
					
					// Sub-tipo del array
					exprSubType = var.getSubType();
				}
				
				// Tomar el tipo y valor de la variable
				exprType = varType;
				exprValue = var.getObjectValue();
				
				// Éxito
				return false;
			}
			
			// Ver si es un nº positivo
			if(tokType == TinyDevToken.Id.NUMBER) {
				
				// Saltar número
				++exprIndex;
				
				// Tomar tipo y valor
				exprType = DataType.NUMBER;
				exprValue = token.getNumber();
	
				// Éxito
				return false;
			}
			
			// Ver si el token es una cadena textual
			if(tokType == TinyDevToken.Id.STRING) {
				
				// Saltar token de cadena
				++exprIndex;
				
				// Tomar tipo y valor
				exprType = DataType.STRING;
				exprValue = token.getString();
				
				// Éxito
				return false;
			}
			
			// Ha de ser una palabra reservada, que devuelve
			// un valor (función)
			if(tokType == TinyDevToken.Id.KEYWORD) {
				
				// Saltar palabra reservada
				++exprIndex;
				
				// Ha de ser un paréntesis abierto
				if(tokenIsOtherOf(peekToken(), TinyDevOperator.Id.OPEN_PARENTHESIS)) {
				
					// Saltar (
					++exprIndex;
					
					// Comprobar cuál es, y generar el tipo y
					// valor correspondiente
					
					// Dentro de una función, los errores se han de indicar
					// únicamente fijando errorId.
					switch(token.getKeyword().getCode()) {
						case RANDOM : // random()
							fnRandom();
							break;
						case MOUSEX : // mouseX()
							fnMouseX();
							break;
						case MOUSEY : // mouseY()
							fnMouseY();
							break;
						case KEY :  // key()
							fnKey();
							break;
						case REPLY :  // reply()
							exprType = DataType.STRING;
							exprValue = dialogInput;
							break;
						case MOUSECLICK :  // mouseClick()
							fnMouseClick();
							break;
						case NUMBER :  // number(string)
							if(!exprString()) {
								exprType = DataType.NUMBER;
								exprValue = stringToInt((String) exprValue);
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case STRING :  // string(number)
							if(!exprNumber()) {
								exprType = DataType.STRING;
								exprValue = "" + (int) exprValue;
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case LENGTH :  // length(variable_de_tipo_array | string)
							// Tomar token
							token = peekToken();
							
							// Si hay token disponible, ver de qué tipo es
							if(token != null && token.getType() == TinyDevToken.Id.IDENTIFIER) {
								// Ha de ser una variable...
								TinyDevVariable var = findVariable(token.getIdentifier().getName());
								
								// ... de tipo array
								if(var != null && var.getType() == DataType.ARRAY) {
									// Saltar identificador
									++exprIndex;
									
									// Devolver la longitud del array
									exprType = DataType.NUMBER;
									exprValue = var.getSize();
									break;
								}
							}
							
							// Ha de ser una expresión de cadena
							if(!exprString()) {
								exprType = DataType.NUMBER;
								exprValue = ((String) exprValue).length();
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case BREAK :  // break()
							exprType = DataType.STRING;
							exprValue = "\n";
							break;
						case MIDDLE : // middle(string, first_pos, how_many)
							if(!exprString()) {
								String s = (String) exprValue;
								if(eatComma())
									break;
								if(!exprNumber()) {
									int firstPos = (int) exprValue;
									if(eatComma())
										break;
									if(!exprNumber()) {
										int howMany = (int) exprValue;
										try {
											exprType = DataType.STRING;
											exprValue = s.substring(firstPos, firstPos + howMany);
											break;
										} catch(IndexOutOfBoundsException ex) {
											errorId = Error.OUT_OF_BOUNDS;
											break;
										}
									}
								}
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case TEXTHEIGHT :  // textHeight(string)
							if(!exprString()) {
								exprType = DataType.NUMBER;
								exprValue = io.getTextHeight(/* (String) exprValue */);
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case TEXTWIDTH :  // textWidth(string)
							if(!exprString()) {
								exprType = DataType.NUMBER;
								exprValue = io.getTextWidth((String) exprValue);
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case PEN : // pen()
							exprType = DataType.STRING;
							exprValue = penName;
							break;
						case PAPER : // paper()
							exprType = DataType.STRING;
							exprValue = paperName;
							break;
						case CURSORX :  // cursorX()
							exprType = DataType.NUMBER;
							exprValue = io.getColumn();
							break;
						case CURSORY :  // cursorY()
							exprType = DataType.NUMBER;
							exprValue = io.getRow();
							break;
						case SCREENHEIGHT :  // screenHeight()
							exprType = DataType.NUMBER;
							exprValue = io.getHeight();
							break;
						case SCREENWIDTH :  // screenWidth()
							exprType = DataType.NUMBER;
							exprValue = io.getWidth();
							break;
						case LOWERCASE :  // lowercase(string)
							if(!exprString()) {
								exprType = DataType.STRING;
								exprValue = ((String) exprValue).toLowerCase(Locale.ROOT);
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case UPPERCASE :  // uppercase(string)
							if(!exprString()) {
								exprType = DataType.STRING;
								exprValue = ((String) exprValue).toUpperCase(Locale.ROOT);
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case FINDSTRING : // findstring(string, substring, {position})
							if(!exprString()) {
								String s = (String) exprValue;
								if(eatComma())
									break;
								if(!exprString()) {
									String ss = (String) exprValue;
									int p = 0;
									if(tokensLeft() && tokenIsOtherOf(peekToken(), TinyDevOperator.Id.COMMA)) {
										++exprIndex;
										if(exprNumber())
											break;
										p = (int) exprValue;
									}
									if(p < 0) {
										errorId = Error.BAD_ARGUMENT;
										break;
									}
									exprType = DataType.NUMBER;
									exprValue = s.indexOf(ss, p);
									break;
								}
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case REPEATSTRING :
							if(!exprString()) {
								String s = (String) exprValue;
								if(eatComma())
									break;
								if(!exprNumber()) {
									int n = (int) exprValue;
									
									if(n < 0) {
										errorId = Error.BAD_ARGUMENT;
										break;
									}
									String r = "";
									
									for(int i = 0; i < n; ++i) {
										r = r.concat(s);
									}
									exprType = DataType.STRING;
									exprValue = r;
									break;
								}
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case READFILE :
							if(!exprString()) {
								String fname = (String) exprValue;

								try {
									List<String> lines = Files.readAllLines(Paths.get(addCurrentDirToFile(fname)), StandardCharsets.UTF_8);
									
									exprType = DataType.ARRAY;
									exprSubType = DataType.STRING;
									exprValue = (Object) lines.toArray();
									
								} catch (IOException e) {
									errorId = Error.READING_FILE;
									
									// FIXME - problemas:
									// - si charset del fichero no es utf8 da error.
									// - ¿permitir paths absolutos o ../ ?
								}
								
								break;
							}
							//errorId = Error.BAD_DATA_TYPE;
							break;
						case SCREENIMAGE :
							fnScreenImage();
							break;
						default :
							// Es una palabra reservada que
							// no es una función, error
							errorId = Error.BAD_SYNTAX;
							break;
					}
					
					// Comprobar si hubo un error devuelto por la función
					if(errorId != Error.SUCCESS)
						return true;
					
					// Ha de ser un paréntesis cerrado
					if(tokenIsCloseParenthesis()) {
				
						// Saltar palabra reservada
						++exprIndex;
						
						// Éxito
						return false;
					}
					
					// Error: falta el paréntesis de cierre
					errorId = Error.BAD_NUMBER_OF_ARGUMENTS;
				}
				
				// Error
			}
			
			// Error
			return true;
		}

		// Faltan argumentos, error
		errorId = Error.BAD_NUMBER_OF_ARGUMENTS;
		return true;
	}
	
	private void fnKey() {
		if(tokenIsCloseParenthesis()) {
			exprType = DataType.STRING;
			String str = io.getKey();
			exprValue = str != null ? str : "";
		}
	}
	
	private void fnRandom() {
		if(tokenIsCloseParenthesis()) {
			exprType = DataType.NUMBER;
			exprValue = randGen.nextInt(randMax + 1);
		}
	}
	
	private void fnMouseX() {
		if(tokenIsCloseParenthesis()) {
			exprType = DataType.NUMBER;
			exprValue = io.getMouseX();
		}
	}
		
	private void fnMouseY() {
		if(tokenIsCloseParenthesis()) {
			exprType = DataType.NUMBER;
			exprValue = io.getMouseY();
		}
	}

	private void fnMouseClick() {
		if(tokenIsCloseParenthesis()) {
			exprType = DataType.STRING;
			exprValue = io.getMouseClick();
		}
	}
	
	// image = screenImage({x, y, width, height})
	private void fnScreenImage() {
		int x, y, width, height;
		
		if(tokenIsCloseParenthesis()) {
			// Pantalla completa
			x = y = width = height = 0;
		}
		else {
			// Pantalla parcial
			if(exprNumber())
				return;
			
			x = (int) exprValue;
			
			if(eatComma())
				return;
			
			if(exprNumber())
				return;
			
			y = (int) exprValue;
			
			if(eatComma())
				return;
			
			if(exprNumber())
				return;
			
			width = (int) exprValue;
			
			if(eatComma())
				return;
			
			if(exprNumber())
				return;
			
			height = (int) exprValue;
		}
		
		if(tokenIsCloseParenthesis()) {
			// Leer imagen de la pantalla
			Object img = io.getImage(x, y, width, height);
			
			if(img != null) {
				exprType = DataType.IMAGE;
				exprValue = img;
			}
			else {
				// Error: posición y/o dimensiones erróneas
				errorId = Error.BAD_SCREEN_SIZE;
			}
		}
	}

	/**
	 * Helper: Devuelve un token sin avanzar la posición en la expresión,
	 * o null si no quedan más.
	 * 
	 * @return  token en la posición actual, o null si no quedan más
	 */
	private TinyDevToken peekToken() {
		
		// Devolver token si todavía quedan
		if(exprIndex < exprTokens.size()) {
			return exprTokens.get(exprIndex);
		}
		
		// No quedan
		return null;
	}
	
	/**
	 * Comprueba si un token de tipo 'otros' es uno determinado.
	 * 
	 * @param token  token
	 * @param type   tipo
	 * @return       true si es el indicado, false en caso contrario
	 */
	private boolean tokenIsOtherOf(TinyDevToken token, TinyDevOperator.Id type) {
		
		// Hacer la comprobación, si token no es null
		if(token != null) {
			// Comprobar si el token coincide con lo indicado
			if(token.getType() == TinyDevToken.Id.OPERATOR && token.getOperator().getType() == type) {
				
				// Sí
				return true;
			}
		}
		
		// No
		return false;
	}
	
	private boolean tokenIsCloseParenthesis() {
		return tokenIsOtherOf(peekToken(), TinyDevOperator.Id.CLOSE_PARENTHESIS);
	}
	
	/**
	 * Comprobar si el token es la palabra reservada indicada.
	 * 
	 * @param token  token
	 * @param id     palabra reservada
	 * @return       true si coinciden, false en caso contrario
	 */
	private boolean tokenIsCommandOf(TinyDevToken token, TinyDevKeyword.Id id) {
		
		// Comprobar si la palabra reservada coincide
		if(token.getType() == TinyDevToken.Id.KEYWORD && token.getKeyword().getCode() == id) {
			
			// Sí
			return true;
		}
		
		// No
		return false;
	}

	/**
	 * Comprobar si quedan tokens por leer.
	 * 
	 * @return  true si quedan, false en caso contrario
	 */
	private boolean tokensLeft() {
		
		// Devolver comprobación
		return exprIndex < exprTokens.size();
	}
	
	/**
	 * Comprobar si quedan tokens por leer. Si no quedan,
	 * establece el código de error.
	 * 
	 * @return  true si no quedan, false en caso contrario
	 */
	private boolean needArg() {
		
		// Comprobar si quedan tokens
		if(!tokensLeft()) {
			
			// No quedan, error
			errorId = Error.BAD_NUMBER_OF_ARGUMENTS;
			return true;
		}
		
		// Sí quedan
		return false;
	}
	
	/**
	 * Comprobar si quedan tokens por leer. Si quedan,
	 * establece el código de error.
	 * 
	 * @return  true si quedan, false en caso contrario
	 */
	private boolean needEnd() {
		
		// Comprobar si quedan tokens
		if(tokensLeft()) {
			
			// Sí quedan, error
			errorId = Error.BAD_NUMBER_OF_ARGUMENTS;
			return true;
		}
		
		// No quedan
		return false;
	}
	
	private boolean eatComma() {
		if(tokenIsOtherOf(peekToken(), TinyDevOperator.Id.COMMA)) {
			++exprIndex;
			return false;
		}
		
		errorId = Error.BAD_NUMBER_OF_ARGUMENTS;
		return true;
	}
	
	/**
	 * Comprobar si es una expresión entre corchetes, que indica
	 * el índice de un array.
	 * 
	 * Si la expresión [num] está bien construida, establece el
	 * valor de la expresión en el número de índice. En caso
	 * contrario, establece el error.
	 * 
	 * Si no es una expresión entre corchetes, no establece
	 * error alguno.
	 * 
	 * @return  true si es una expresión entre corchetes, false en caso contrario o error
	 */
	private boolean arrayIndex() {
		
		// Han de quedar tokens
		if(tokensLeft()) {
			
			// Si es un corchete abierto, ha de ser un índice
			if(tokenIsOtherOf(exprTokens.get(exprIndex), TinyDevOperator.Id.OPEN_BRACKET)) {
				
				// Saltar el corchete
				++exprIndex;
				
				// Ha de ser una expresión numérica
				if(!exprNumber()) {
					
					// Ha de ser un corchete de cierre
					if(tokensLeft()) {
						if(tokenIsOtherOf(exprTokens.get(exprIndex), TinyDevOperator.Id.CLOSE_BRACKET)) {
							
							// Saltar el corchete
							++exprIndex;
							
							// Éxito
							return true;
						}
					}
					
					// Expresión de índice mal construida
					syntaxError();
				}
				
				// Error en la expresión entre corchetes
				return false;
			}
		}
		
		// No es una expresión entre corchetes
		return false;
	}

}
