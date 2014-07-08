package card2jar;

import java.io.File;
import java.util.ArrayList;

public class Card2Jar {
	
	/**
	 * Converts a Java Card Classic (2.2.2 or lower) CAP file to a JAR of standard bytecode Class files
	 * Caveats: Requires the Export EXP file, does not support Java Card connected apis or 3.x Java Card applets
	 * Uses the normalizer library in the JCDK3.0.4_ClassicEdition SDK distribution
	 * @param capFile
	 * @param expFile
	 * @param outputJarFile
	 * @throws Exception
	 */
	public static void convertCardToJar(File capFile, File expFile, File outputJarFile) throws Exception {
		// create a folder named the same as the cap file (without the file extension) inside a temporary directory
		File tempDirectory = Utils.createTempDirectory();
		File normalizerOutputDirectory = new File(tempDirectory.getAbsolutePath() 
				+ File.separatorChar + capFile.getName().replace(".cap", ""));
		// run the normalizer to get the byte code
		int normalizerStatus = normalize(capFile, expFile, normalizerOutputDirectory);
		if(normalizerStatus == 0){
			// jar the resulting byte code up
			jar(normalizerOutputDirectory, outputJarFile);
			Utils.delete(tempDirectory);
		} else {
			// conversion failed, do some cleanup
			Utils.delete(tempDirectory);
			throw new Exception("Normalizer failed: exit status " + normalizerStatus);
		}
	}
	
	// helper method for jar'ing a directory
	private static void jar(File normalizedCardDirectory, File outputJarFile){
		outputJarFile.getParentFile().mkdirs();
		Utils.jarDirectory(normalizedCardDirectory, outputJarFile);
	}
	
	// helper method for interacting directly with the normalizer code instead of using normalizer.bat
	private static int normalize(File capFile, File expFile, File outputDirectory) throws Exception {
		// setup args and subcommand
		ArrayList<String> args = new ArrayList<String>();
		args.add("normalize");
		
		// set the input cap file
		args.add("--in");
		args.add(capFile.getAbsolutePath());
		
		// set the input exp file
		args.add("--exportfile");
		args.add(expFile.getAbsolutePath());
		
		// set up api export files path
		args.add("--exportpath");
		args.add(new File("resources" + File.separatorChar + "api_export_files").getAbsolutePath());
		
		// set up byte code output directory
		args.add("--keepall");
		args.add(outputDirectory.getAbsolutePath());

		// run the normalizer
		return com.sun.javacard.normalizer.Main.execute(args.toArray(new String[args.size()]), true);
	}

}
