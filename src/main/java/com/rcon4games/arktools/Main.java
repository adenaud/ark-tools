package com.rcon4games.arktools;

import com.rcon4games.arktools.tools.ModUpdateChecker;

import java.io.IOException;

public class Main {
	public static void main(String[] args){
		ModUpdateChecker modUpdateChecker = new ModUpdateChecker();
		try {
			modUpdateChecker.check();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
