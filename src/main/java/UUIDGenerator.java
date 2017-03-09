import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UUIDGenerator {

	public static void main(String[]args) throws IOException {
		autofilFields();
//		generateUUIDs();
	}

	private static void autofilFields() throws IOException {
		String author = "kke@snomed.org";
		String effectiveTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());

		File mrcmFile = new File("international/mrcm.xmi");
		if (!mrcmFile.exists()) {
			throw new RuntimeException("MRCM file does not exist " + mrcmFile.getAbsolutePath());
		}

		File tempFile = new File(mrcmFile.getAbsolutePath() + ".tmp");

		try (BufferedReader reader = new BufferedReader(new FileReader(mrcmFile))) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					while (line.contains("uuid=\"\"")) {
						line = line.replace("uuid=\"\"", "uuid=\"" + UUID.randomUUID() + "\"");
					}
					line = line.replace("author=\"\"", "author=\"" + author + "\"");
					line = line.replace("effectiveTime=\"\"", "effectiveTime=\"" + effectiveTime + "\"");
					writer.write(line);
					writer.newLine();
				}
			}
		}

		Files.move(tempFile.toPath(), mrcmFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	private static void generateUUIDs() {
		for (int i = 0; i < 100; i++) {
			System.out.println(UUID.randomUUID());
		}
	}
}
