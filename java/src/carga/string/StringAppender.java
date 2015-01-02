/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package carga.string;

import java.io.*;

/**
 * 
 * @author Edgard Leal
 */
public class StringAppender {

	private final String self;

	public StringAppender(String value) {
		this.self = value;
	}

	public String to(String string) {
		return StringUtil.concat(self, string);
	}

	public void to(File file) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(file, true)))) {
			out.println(self);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
