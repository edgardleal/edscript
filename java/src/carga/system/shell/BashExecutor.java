/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package carga.system.shell;

import java.io.*;
import java.util.concurrent.*;

/**
 * 
 * @author edgardleal
 */
public class BashExecutor {

	public static BashExecuteResult executeCommand(String... command)
			throws IOException {
		byte[] bo = new byte[100];
		Process p = Runtime.getRuntime().exec(command);
		p.getInputStream().read(bo);
		try {
			p.waitFor(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String result = new String(bo).trim();
		return new BashExecuteResult(result, p.exitValue());
	}

	public static void main(String args[]) throws IOException {

		System.out.println("done");
	}
}
