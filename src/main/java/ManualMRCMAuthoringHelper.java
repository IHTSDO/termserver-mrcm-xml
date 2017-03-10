import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManualMRCMAuthoringHelper {

	public static void main(String[]args) throws IOException {
		autofilFields();
//		generateUUIDs();
	}

	private static void autofilFields() throws IOException {
		String author = "kke@snomed.org";
		String effectiveTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());

		File workFile = new File("international/.work.xml");
		if (!workFile.exists()) {
			throw new RuntimeException("Scratch file does not exist " + workFile.getAbsolutePath());
		}

		File tempFile = new File(workFile.getAbsolutePath() + ".tmp");

		try (BufferedReader reader = new BufferedReader(new FileReader(workFile))) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
				String line;
				Pattern uuidPattern = Pattern.compile("uuid=\"[^\"]*");
				while ((line = reader.readLine()) != null) {
					Matcher matcher = uuidPattern.matcher(line);
					while (matcher.find()) {
						String match = matcher.group();
						line = line.replace(match, "uuid=\"" + UUID.randomUUID());
					}
					line = line.replaceAll("author=\"[^\"]*", "author=\"" + author);
					line = line.replaceAll("effectiveTime=\"[^\"]*", "effectiveTime=\"" + effectiveTime);
					writer.write(line);
					writer.newLine();
				}
			}
		}

		Files.move(tempFile.toPath(), workFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	private static void generateUUIDs() {
		for (int i = 0; i < 100; i++) {
			System.out.println(UUID.randomUUID());
		}
	}
}
