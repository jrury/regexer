package com.quailstreetsoftware.regexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 * Goofy little utility for taking a big text file, another text file specifying 
 * reg ex patterns to run, and specifying an output file. Each regex in the file is run
 * against each line in the input file, replacing the given reg ex with an empty string.
 * If the result is an empty string, nothing is written to the output.
 * Example usage: java -jar regexer.jar input regexes output
 * @author Jonathan
 *
 */
public class RegexMain {

	public static void main(String[] args) {

		Boolean valid = Boolean.TRUE;
		String fileToProcess = null, regexesToRun = null, outputFile = null;
		if (args.length > 2) {
			fileToProcess = args[0];
			regexesToRun = args[1];
			outputFile = args[2];
		} else {
			valid = Boolean.FALSE;
		}

		if (valid) {
			try {
				List<Object> regexes = loadLines(regexesToRun);
				processFile(fileToProcess, regexes, outputFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// BLOW UP
		}
	}

	private static List<Object> loadLines(final String pathSpecification)
			throws IOException {
		Stream<String> lines = Files.lines(Paths.get(pathSpecification));
		List<Object> list = Arrays.asList(lines.toArray());
		lines.close();
		return list;
	}

	private static void processFile(final String input, List<Object> regexes,
			final String output) throws IOException {

		File outputFile = new File(output);
		FileUtils.writeStringToFile(outputFile, "");
		LineIterator it = FileUtils.lineIterator(new File(input), "UTF-8");
		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				for (Object o : regexes) {
					if (o instanceof String) {
						line = line.replaceAll((String) o, "");
					}
					if (line.equals("")) {
						break;
					}
				}
				if (!line.equals("")) {
					FileUtils.writeStringToFile(outputFile, line + "\n", true);
				}
			}
		} finally {
			LineIterator.closeQuietly(it);
		}

	}
}
