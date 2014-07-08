import java.io.File;

import card2jar.Card2Jar;


public class Main {

	public static void main(String[] args) throws Exception {
		File cap = new File("C:\\Users\\Ben\\Desktop\\Card2Jar\\Examples\\HelloWorld_2.2.2\\HelloWorld.cap");
		File exp = new File("C:\\Users\\Ben\\Desktop\\Card2Jar\\Examples\\HelloWorld_2.2.2\\HelloWorld.exp");
		File jar = new File("C:\\Users\\Ben\\Desktop\\Card2Jar\\Examples\\HelloWorld_2.2.2\\HelloWorld.jar");
		Card2Jar.convertCardToJar(cap, exp, jar);
	}

}
