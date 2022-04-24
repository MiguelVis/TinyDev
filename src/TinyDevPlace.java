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
 * Clase que implementa los 'places', es decir, posiciones etiquetadas
 * en el código.
 * 
 * @author Miguel
 */
public class TinyDevPlace {
	
	private String name;  // Nombre
	private int line;     // Nº de línea
	
	/**
	 * Constructor.
	 * 
	 * @param nm  nombre
	 * @param ln  nº de línea
	 */
	public TinyDevPlace(String nm, int ln) {
		
		name = nm;
		line = ln;
	}
	
	/**
	 * Devolver el nombre.
	 * 
	 * @return  nombre.
	 */
	public String getName() {
		
		return name;
	}

	/**
	 * Devolver el nº de línea.
	 * 
	 * @return  nº de línea
	 */
	public int getLine() {
		
		return line;
	}
}
