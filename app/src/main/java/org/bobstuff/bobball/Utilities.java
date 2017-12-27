/*
  Copyright (c) 2012 Richard Martin. All rights reserved.
  Licensed under the terms of the BSD License, see LICENSE.txt
*/

package org.bobstuff.bobball;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class Utilities extends Activity {

	/**
	 * Copies a source array to a destination array.
	 *
	 * @param source Two dimensional source array
	 * @param destination Two dimensional destination array
	 */
	public static void arrayCopy(int[][] source,int[][] destination) {
		for (int a=0; a < source.length; ++a) {
			System.arraycopy(source[a], 0, destination[a], 0, source[a].length);
		}
	}

	/**
	 * Creates a dropdown adapter with numbers starting by 1.
	 *
	 * @param context App Context
	 * @param count The Last number in the dropdown menu
	 * @return ArrayAdapter
	 */
	public static ArrayAdapter createDropdown (Context context, int count) {
		List<String> dropdownItems = new ArrayList<>();

		for (int i = 1; i <= count; i++) { dropdownItems.add("" + i); }

		ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item, dropdownItems);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return adapter;
	}

	/**
	 * Creates a dropdown adapter from a string array in the strings.xml file.
	 *
	 * @param context Application Context
	 * @param stringArray ID of the string array (R.array.x)
	 * @return ArrayAdapter
	 */
	public static ArrayAdapter createDropdownFromStrings (Context context, int stringArray){
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, stringArray, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	/**
	 * Hides the title bar.
	 *
	 * @param activity The activity for which the title bar shall be hidden
	 */
	public static void hideTitleBar(Activity activity) {

		Window window = activity.getWindow();

		window.requestFeature(Window.FEATURE_NO_TITLE);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
