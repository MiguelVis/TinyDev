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
 * Clase que implementa otros tipos de tokens.
 * 
 * @author Miguel
 *
 */
public class TinyDevOperator {
	
	// ---------------------------
	// Otros operadores y símbolos
	// ---------------------------
	
	public enum Id {
		EQUAL,             // ==
		NOT_EQUAL,         // !=
		LESS_OR_EQUAL,     // >=
		LESS,              // <
		GREATER_OR_EQUAL,  // >=
		GREATER,           // >
		AND,               // &&
		OR,                // ||
		COMMENT,           // '
		COLON,             // :
		COMMA,             // ,
		OPEN_PARENTHESIS,  // (
		CLOSE_PARENTHESIS, // )
		OPEN_BRACKET,      // [
		CLOSE_BRACKET,     // ]
		PLUS,              // +
		MINUS,             // -
		MULTIPLY,          // *
		DIVIDE,            // /
		MODULUS,           // %
		ASSIGN,            // =
	}
	
	private String name;         // Nombre
	private Id type;   // Tipo
	
	/**
	 * Constructor.
	 * 
	 * @param nm  nombre
	 * @param tp  tipo
	 */
	public TinyDevOperator(String nm, Id tp) {
		
		name = nm;
		type = tp;
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
	 * Devolver tipo.
	 * 
	 * @return  tipo
	 */
	public Id getType() {
		
		return type;
	}
}

