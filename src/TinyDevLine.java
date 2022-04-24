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

import java.util.ArrayList;

/**
 * Clase que implementa una l�nea de c�digo, compuesta por tokens.
 * 
 * @author Miguel
 */
public class TinyDevLine {
	
	private int number; 				// N� de l�nea
	private ArrayList<TinyDevToken> tokens;	// Tokens
	
	/**
	 * Constructor.
	 * 
	 * @param nb  n� de l�nea
	 * @param tk  tokens
	 */
	public TinyDevLine(int nb, ArrayList<TinyDevToken> tk) {
		
		number = nb;
		tokens = tk;
	}
	
	/**
	 * Devolver n�mero de l�nea.
	 * 
	 * @return  n� de l�nea
	 */
	public int getNumber() {
		
		return number;
	}
	
	/**
	 * Devolver tokens.
	 * 
	 * @return  tokens
	 */
	public ArrayList<TinyDevToken> getTokens() {
		
		return tokens;
	}
}

