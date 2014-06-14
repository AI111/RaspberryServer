/*
 * untitled.java
 * 
 * Copyright 2014  <pi@raspberrypi>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */


public class Sonar {
	static
    {
       //System.loadLibrary("megalib");
       System.load("/home/pi/projects/test1/libsonar.so.1.0"); 
    }
    public static native void setup();
	public static native int getDistance();
	public static void main (String args[]) {
		setup();
		System.out.println("Java "+getDistance());
		System.out.println("Java "+getDistance());
	}
}

