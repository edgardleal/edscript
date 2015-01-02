/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package carga.system.shell;

import carga.string.*;

/**
 * 
 * @author Edgard Leal
 */
public class BashScripCreator {

	private static final String header = "#!/bin/bash";
	private static final String footer = "exit 0";
	private static final int ident = 2;
	private final StringBuilder builder = new StringBuilder();

	public BashScripCreator append(final String command) {
		if (command == null || command.trim().isEmpty()) {
			builder.append('\n');
			return this;
		}
		appendIdent().append(command).append('\n');
		return this;
	}

	private StringBuilder appendIdent() {
		for (int i = 0; i < ident; i++) {
			builder.append(Constants.SPACE);
		}
		return builder;
	}

	private StringBuilder appendQuoted(final String value) {
		return builder.append('"').append(value).append('"');
	}

	public BashScripCreator addVariable(final String name, final String value) {
		appendIdent().append("declare ").append(name).append('=');
		appendQuoted(value).append('\n');
		return this;
	}

	public String getString() {
		return String.format("%s\n%s\n%s", header, builder.toString(), footer);
	}

	public BashScripCreator clear() {
		builder.delete(0, builder.length());
		builder.append(header).append('\n');
		return this;
	}
}
