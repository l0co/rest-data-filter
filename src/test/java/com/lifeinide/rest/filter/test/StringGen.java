package com.lifeinide.rest.filter.test;

/**
 * @author Lukasz Frankowski
 */
public class StringGen {

	char leftChar = 'a';
	char rightChar = 'a'-1;
	String currentPrefix = "";

	public String nextStr() {
		rightChar++;
		if (rightChar == 'z' + 1) {
			leftChar++;
			rightChar = 'a';
		}

		return String.valueOf(new char[] {leftChar, rightChar});
	};
	

}
