import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class SafeFileReader {
    private static final Logger LOGGER = Logger.getLogger(SafeFileReader.class.getName());
    private static final long MAX_BYTES = 64 * 1024; 
    private SafeFileReader() {}

    public static Optional<String> readUtf8TextSafely(Path baseDir, String requestedFileName) {
        if (baseDir == null || requestedFileName == null || requestedFileName.isBlank()) {
            return Optional.empty();
        }

        try {
            Path base = baseDir.toAbsolutePath().normalize();
            Path candidate = base.resolve(requestedFileName).normalize();

            if (!candidate.startsWith(base)) {
                LOGGER.warning("Denied file read: path escapes base directory.");
                return Optional.empty();
            }

            if (!Files.exists(candidate) ||
                !Files.isRegularFile(candidate, LinkOption.NOFOLLOW_LINKS)) {
                return Optional.empty();
            }

            long size = Files.size(candidate);
            if (size > MAX_BYTES) {
                LOGGER.warning("Denied file read: file too large.");
                return Optional.empty();
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br =
                    Files.newBufferedReader(candidate, StandardCharsets.UTF_8)) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append(System.lineSeparator());
                }
            }

            return Optional.of(sb.toString());

        } catch (InvalidPathException e) {
            LOGGER.log(Level.WARNING, "Invalid path.", e);
            return Optional.empty();
        } catch (SecurityException e) {
            LOGGER.log(Level.WARNING, "Security manager blocked access.", e);
            return Optional.empty();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "I/O error while reading file.", e);
            return Optional.empty();
        }
    }
}