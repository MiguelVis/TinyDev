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

public class TinyDevVariable {
	
	private String name = null;
	private TinyDevLang.DataType type = null;
	private Object value = null;
	
	private TinyDevLang.DataType subtype = null;
	private int size = 0;
	
	public TinyDevVariable(String nm, TinyDevLang.DataType tp, String val) {
		
		name = nm;
		type = tp;
		value = val;
	}
	
	public TinyDevVariable(String nm, TinyDevLang.DataType tp, int val) {
		
		name = nm;
		type = tp;
		value = val;
	}
	
	public TinyDevVariable(String nm, TinyDevLang.DataType tp, Object val) {
		
		name = nm;
		type = tp;
		value = val;
	}
	
	public TinyDevVariable(String nm, TinyDevLang.DataType tp, TinyDevLang.DataType subtp, int sz) {
		
		name = nm;
		type = tp;
		subtype = subtp;
		size = sz;
		value = new Object[sz];
		
		Object init = null;
		
		if(subtype == TinyDevLang.DataType.NUMBER) {
			init = 0;
		}
		else if(subtype == TinyDevLang.DataType.STRING) {
			init = "";
		}
		
		Object arr[] = (Object []) value;
		
		for(int i = 0; i < size; ++i) {
			arr[i] = init;
		}
	}

	public TinyDevLang.DataType getType() {
		return type;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getStringValue() {
		
		return (String) value;
	}
	
	public int getNumberValue() {
		
		return (int) value;
	}
	
	public void setStringValue(String str) {
		
		value = str;
	}
	
	public void setNumberValue(int num) {
		
		value = num;
	}
	
	public Object getObjectValue() {
		return value;
	}
	
	public void setObjectValue(Object obj) {
		value = obj;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int sz) {
		size = sz;
	}
	
	public TinyDevLang.DataType getSubType() {
		return subtype;
	}

}
