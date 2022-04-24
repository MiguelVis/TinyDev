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

import java.util.ResourceBundle;

import javax.swing.KeyStroke;


public class TinyDevLocalizator {
	
	private ResourceBundle rb = null;
	
	public TinyDevLocalizator(String name) {

		try {
			rb = ResourceBundle.getBundle(name);
		} catch(Exception e) {
			e.printStackTrace(); // FIXME controlar mejor las excepciones
		}
	}
	
	public String getString(String id) {
		String s = null;
		
		try {
			s = rb.getString(id);
		} catch(Exception e) {
			e.printStackTrace(); // FIXME controlar mejor las posibles excepciones
		}
		
		return s;
	}
	
	public String getLabel(String id) {
		return getString(id + ".label");
	}
	
	public String getTitle(String id) {
		return getString(id + ".title");
	}
	
	public String getMsg(String id) {
		return getString(id + ".msg");
	}
	
	public String getTip(String id) {
		return getString(id + ".tip");
	}
	
	public KeyStroke getAccel(String id) {
		return KeyStroke.getKeyStroke(getString(id + ".accel"));
	}
	
	public int getKey(String id) {
		return KeyStroke.getKeyStroke(getString(id + ".key")).getKeyCode();
	}
	
	public String getError(String id) {
		return getString(id + ".err");
	}

}
