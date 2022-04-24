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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class TinyDevProperties {
	
	private Properties props = null;
	private String name = null;

	public TinyDevProperties(String name) {
		this.props = new Properties();
		this.name  = name + ".properties";
	}
	
	public void load()
	{
		try {
			FileInputStream in = new FileInputStream(this.name);
			this.props.load(in);
			in.close();
		} catch(Exception e) {
			e.printStackTrace(); // FIXME controlar mejor las excepciones
		}
	}
	
	public void save()
	{
		try {
			FileOutputStream out = new FileOutputStream(this.name);
			this.props.store(out, "TinyDev Properties");
			out.close();
		} catch(Exception e) {
			e.printStackTrace(); // FIXME controlar mejor las excepciones
		}
	}
	
	public String get(String key, String defaultValue)
	{
		return this.props.getProperty(key, defaultValue);
	}
	
	public void set(String key, String value)
	{
		this.props.setProperty(key, value);
	}
}
