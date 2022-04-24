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
 * Clase que implementa los tokens.
 * 
 * @author Miguel
 *
 */
public class TinyDevToken {
	
	// ---------------
	// Tipos de tokens
	// ---------------
	
	public enum Id {
		KEYWORD,
		NUMBER,
		STRING,
		IDENTIFIER,
		OPERATOR
	}
		
	private Id type;  // Tipo
	private Object object = null;   // Token


	/**
	 * Constructor.
	 * 
	 * @param tp  tipo
	 * @param ob  token
	 */
	public TinyDevToken(Id tp, Object ob) {
		
		type = tp;
		object = ob;
	}
	
	
	public Id getType() {
		
		return type;
	}
	
	public Object getObject() {
		
		return object;
	}
	
	public TinyDevIdentifier getIdentifier() {
		
		return (TinyDevIdentifier) object;
	}
	
	public TinyDevKeyword getKeyword() {
		return (TinyDevKeyword) object;
	}
	
	public TinyDevOperator getOperator() {
		return (TinyDevOperator) object;
	}
	
	public String getString() {
		return (String) object;
	}
	
	public int getNumber() {
		return (int) object;
	}
}
