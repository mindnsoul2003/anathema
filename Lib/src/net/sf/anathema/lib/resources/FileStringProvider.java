package net.sf.anathema.lib.resources;

import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

public class FileStringProvider implements IStringResourceHandler {

  private final Properties properties = new Properties();

  public FileStringProvider(String fileBase, Locale locale) throws IOException {
    Path propertyFile = getPropertyFile(fileBase, locale);
    if (Files.exists(propertyFile)) {
      InputStream stream = Files.newInputStream(propertyFile);
      try {
        properties.load(stream);
      } finally {
        Closeables.closeQuietly(stream);
      }
    }
  }

  private Path getPropertyFile(String fileBase, Locale locale) {
    String localeSuffix = locale.toString();
    String preferredBundleName = fileBase;
    if (localeSuffix.length() > 0) {
      preferredBundleName += "_" + localeSuffix;
    }
    preferredBundleName += ".properties";
    Path preferredFile = Paths.get(preferredBundleName);
    if (Files.exists(preferredFile)) {
      return preferredFile;
    }
    return Paths.get(fileBase + ".properties");
  }

  @Override
  public String getString(String key, Object... arguments) {
    if (arguments.length == 0) {
      return properties.getProperty(key);
    }
    String formatPattern = getString(key);
    return MessageFormat.format(formatPattern, arguments);
  }

  @Override
  public boolean supportsKey(String key) {
    return properties.containsKey(key);
  }
}