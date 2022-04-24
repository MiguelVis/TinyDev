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

/**
 * Clase que implementa las palabras reservadas del lenguaje.
 * 
 * @author Miguel
 *
 */
public class TinyDevKeyword {
	
	// ----------------------------------------
	// Palabras reservadas, en orden alfabético
	// ----------------------------------------
	
	public enum Id {
		BOLD,
		BREAK,
		CALL,
		CENTER,
		CLEAR,
		CURSOR,
		CURSORX,
		CURSORY,
		DIALOG,
		DO,
		ELLIPSE,
		ELSE,
		END,
		ERROR,
		FILL,
		FONT,
		IF,
		IMAGE,
		INFORMATION,
		INPUT,
		ITALIC,
		JUMP,
		KEY,
		LEFT,
		LENGTH,
		LINE,
		LOOP,
		LOWERCASE,
		MIDDLE,
		MOUSEX,
		MOUSEY,
		MOUSECLICK,
		NUMBER,
		OKCANCEL,
		PAPER,
		PEN,
		PLAIN,
		POINT,
		PRINT,
		RANDOM,
		READFILE,
		RECTANGLE,
		REPEATSTRING,
		REPLY,
		RETURN,
		RIGHT,
		SCREEN,
		SCREENHEIGHT,
		SCREENIMAGE,
		SCREENWIDTH,
		SET,
		SOLID,
		SOUND,
		STRING,
		FINDSTRING,
		TEXTHEIGHT,
		TEXTWIDTH,
		THEN,
		TO,
		TRANSPARENT,
		UPPERCASE,
		UNTIL,
		VARIABLE,
		WAIT,
		WARNING,
		WHILE,
		WRITEFILE,
		WRITEIMAGE,
		YESNO
	}
	
	private String name;  // Nombre
	private Id code;      // Código
	
	/**
	 * Constructor.
	 * 
	 * @param nm  nombre
	 * @param cd  código
	 */
	public TinyDevKeyword(String nm, Id cd) {
		
		name = nm;
		code = cd;
	}
	
	/**
	 * Devolver nombre.
	 * 
	 * @return  nombre
	 */
	public String getName() {
		
		return name;
	}
	
	/**
	 * Devolver código.
	 * 
	 * @return  código
	 */
	public Id getCode() {
		
		return code;
	}
}
