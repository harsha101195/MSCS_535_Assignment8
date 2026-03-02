import java.nio.file.Path;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Path baseDir = Path.of("data");
        String requested = (args.length > 0) ? args[0] : "example.txt";

        Optional<String> content =
                SafeFileReader.readUtf8TextSafely(baseDir, requested);

        if (content.isPresent()) {
            System.out.println("READ OK:");
            System.out.println(content.get());
        } else {
            System.out.println("READ DENIED OR FAILED SAFELY.");
        }
    }
}