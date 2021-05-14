import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Make40Ver {

	public static void main(String[] args) throws IOException {
		File file = new File("test40ver.csv");
		FileWriter fw = new FileWriter(file);
		for (int i = 0; i < 40; i++) {
			fw.write((int)(Math.random()*25000) + "\n");
		}
		fw.close();
	}

}
