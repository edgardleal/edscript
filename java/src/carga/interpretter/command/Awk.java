/**
 * 
 */
package carga.interpretter.command;

import java.io.*;

import carga.interpretter.*;
import carga.string.*;
import carga.system.*;

/**
 * Ler as linhas de um arquivo e cria um loop, para cada linha sao criadas
 * variaveis numeradas para cada coluna encontrada na linha
 * 
 * @author edgardleal
 *
 */
public class Awk extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see carga.interpretter.command.Command#execute(carga.interpretter.Scoop,
	 * java.lang.String[])
	 */
	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		// TODO: implementar leitura da variavel quando o arquivo nao existir
		BufferedReader br = new BufferedReader(new FileReader(
				FileUtil.getFullPath(null, arg[0])));
		String pattern = arg.length > 1 ? arg[1]
				: Constants.REGEX_BLANK_CHARACTERS;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(pattern);
				for (int i = 0; i < parts.length; i++) {
					scoope.put(String.valueOf(i), parts[i]);
				}
				this.executeCommands();
			}
		} finally {
			br.close();
		}
		return scoop;
	}

}
