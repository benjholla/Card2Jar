package card2jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {

	/**
	 * Helper method for recursively deleting a file
	 * Stolen from http://stackoverflow.com/a/779529/475329
	 * @param f
	 * @throws IOException
	 */
	public static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				delete(c);
			}
		}
		if (!f.delete()) {
			throw new FileNotFoundException("Failed to delete file: " + f);
		}
	}
	
	/**
	 * Creates a temp directory
	 * @return
	 * @throws IOException
	 */
	public static File createTempDirectory() throws IOException {
		final File temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
		if (!temp.delete()) {
			throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		}
		if (!temp.mkdir()) {
			throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
		}
		return temp;
	}
	
	/**
	 * Helper method for zipping a directory
	 * @param directoryToJar
	 * @param outputZipFile
	 * @return
	 */
	public static boolean jarDirectory(File directoryToJar, File outputZipFile){
		ZipHelper zip = new ZipHelper();
        try {
        	zip.zipDir(directoryToJar, outputZipFile);
        } catch(IOException e) {
            return false;
        }
        return true;
	}

	/**
	 * Stolen and modified from: http://stackoverflow.com/a/15969656/475329
	 */
	private static class ZipHelper  {
	    public void zipDir(File directory, File outputFile) throws IOException {
	        FileOutputStream fw = new FileOutputStream(outputFile);
	        ZipOutputStream zip = new ZipOutputStream(fw);
	        for(File f : directory.listFiles()){
        		if(f.isDirectory()){
        			addFolderToZip("", f, zip);
        		} else {
        			addFileToZip("", f, zip, false);
        		}
        	}
	        
	        zip.close();
	        fw.close();
	    }

	    private void addFolderToZip(String path, File srcFolder, ZipOutputStream zip) throws IOException {
	        if (srcFolder.list().length == 0) {
	            addFileToZip(path , srcFolder, zip, true);
	        }
	        else {
	            for (String fileName : srcFolder.list()) {
	                if (path.equals("")) {
	                    addFileToZip(srcFolder.getName(), new File(srcFolder + "/" + fileName), zip, false);
	                } 
	                else {
	                     addFileToZip(path + "/" + srcFolder.getName(), new File(srcFolder.getAbsolutePath() + "/" + fileName), zip, false);
	                }
	            }
	        }
	    }

	    private void addFileToZip(String path, File srcFile, ZipOutputStream zip, boolean flag) throws IOException {
	        if (flag) {
	            zip.putNextEntry(new ZipEntry(path + "/" +srcFile.getName() + "/"));
	        }
	        else {
	            if (srcFile.isDirectory()) {
	                addFolderToZip(path, srcFile, zip);
	            }
	            else {
	                byte[] buf = new byte[1024];
	                int len;
	                FileInputStream in = new FileInputStream(srcFile);
	                zip.putNextEntry(new ZipEntry(path + "/" + srcFile.getName()));
	                while ((len = in.read(buf)) > 0) {
	                    zip.write(buf, 0, len);
	                }
	                in.close();
	            }
	        }
	    }
	}
	
}
