package carga.interpretter.command;

import java.io.*;
import java.util.*;

import org.slf4j.*;

import carga.*;
import carga.interpretter.*;
import carga.string.*;

public abstract class Command implements Serializable {

	private static final long serialVersionUID = 1L;
	protected List<Command> commands;
	protected int level;
	protected String[] arg;
	org.slf4j.Logger l = LoggerFactory.getLogger(getClass());
	protected Scoop scoope;
	protected Context context;
	protected String originalCodeLine;

	public Command() {
		commands = new ArrayList<Command>();
	}

	public Queue<String> parse(Queue<String> commands)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String command = commands.peek();
		int tmplevel = checkIdent(command);
		if (command == null) {
			return commands;
		}

		if (tmplevel <= this.level) {
			return commands;
		}

		String[] args = command.trim().split(Constants.SPACE);
		String className = args[0].trim();

		className = String.format("carga.interpretter.command.%s",
				StringUtil.capitalize(className));

		Command ex = (Command) Class.forName(className).newInstance();

		ex.setScoope(this.scoope);
		ex.setContext(this.context);
		ex.setLevel(tmplevel);
		ex.setOriginalCodeLine(command);
		ex.setArg(subRange(args, 1, args.length - 1));

		this.commands.add(ex);

		commands.poll();
		ex.parse(commands);
		int l = checkIdent(commands.peek());

		while (l > this.level && l > 0) {
			this.parse(commands);
			l = checkIdent(commands.peek());
		}

		return commands;
	}

	private String[] subRange(String[] commands, int start, int end) {
		String[] result = new String[end - start + 1];
		for (int i = start; i <= end; result[i - start] = commands[i++])
			;

		return result;
	}

	protected Scoop checkScoope(Scoop scoop) {
		return scoop == null ? this.scoope : scoop;
	}

	protected String[] parseVariablesInArgs() {
		for (int i = 0; i < arg.length; i++) {
			if (arg[i].indexOf('$') > -1) {
				Object value = scoope.get(scoope.fieldName(arg[i]));
				if (value != null) {
					arg[i] = value.toString();
				}
			}
		}

		return this.arg;
	}

	public abstract Scoop execute(Scoop scoop, String... args) throws Exception;

	private int checkIdent(String text) {
		if (text == null || text.length() == 0) {
			return 0;
		}
		int level = -1;
		while (text.charAt(++level) == ' ' && level < text.length()) {

		}
		int result = Math.round(level / 2);
		return result;
	}

	protected void executeCommands() throws Exception {
		for (Command command : this.commands) {
			try {
				if (!(command instanceof Procedure)
						&& !(command instanceof Function)) {
					command.execute(null, Constants.EMPTY_STRING);
					scoope.put(Constants.ERROR, Constants.EMPTY_STRING);
				}
			} catch (Exception ex) {
				l.error(StringUtil.concat("Erro ao executar o comando: [",
						command.getOriginalCodeLine(), "]"), ex);
				scoope.put(Constants.ERROR, ex.getMessage());
			}
		}
	}

	private String nchar(char v, int times) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < times; i++) {
			builder.append(v);
		}

		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(nchar(' ', (this.level + 1) * 2))
				.append(this.getClass().getSimpleName()).append('\n');

		for (Command c : this.commands) {
			builder.append(c.toString());
		}

		return builder.toString();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String[] getArg() {
		return arg;
	}

	public void setArg(String[] arg) {
		this.arg = arg;
		this.parseVariablesInArgs();
	}

	public Scoop getScoope() {
		return scoope;
	}

	public void setScoope(Scoop scoope) {
		this.scoope = scoope;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	protected void checkArgs(String... args) {
		if (arg == null || arg.length == 0
				|| arg[0].trim().equals(Constants.EMPTY_STRING)) {
			this.arg = args;
		}
	}

	public String getOriginalCodeLine() {
		return originalCodeLine;
	}

	public void setOriginalCodeLine(String originalCodeLine) {
		this.originalCodeLine = originalCodeLine;
	}
}
