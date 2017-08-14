package cn.nju.seg.atg.spfwrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;


/**
 * @author Zhang Yifan
 */
public final class Utils {

  public static void appendWithNewLine(final StringBuilder sb, final String content) {
    assert sb != null && content != null;

    sb.append(content);
    sb.append(Utils.EOL);
  }

  public static final char EOL = '\n';

  public static boolean logToFile(final Path logFile, final boolean isAppend, final Iterable<String> logLines) {
    Preconditions.checkNotNull(logFile);
    Preconditions.checkNotNull(logFile);

    try {
      if (isAppend) {
        Files.write(logFile, logLines, Charset.defaultCharset(),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
      } else {
        Files.write(logFile, logLines, Charset.defaultCharset(),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
      }
      return true;
    } catch (final IOException ignored) {
      return false;
    }
  }

  public static boolean logToFile(final Path logFile, final boolean isAppend, final String... logLines) {
    return Utils.logToFile(logFile, isAppend, Arrays.<String>asList(logLines));
  }

  public static boolean logToFile(final Path logFile, final Iterable<String> logLines) {
    return Utils.logToFile(logFile, true, logLines);
  }

  public static boolean logToFile(final Path logFile, final String... logLines) {
    return Utils.logToFile(logFile, true, logLines);
  }

  public static boolean appendToLogFile(final Path logFile, final String appendLogLine) {
    return Utils.logToFile(logFile, true, ImmutableList.of(appendLogLine));
  }

  @Deprecated
  private Utils() { throw new UnsupportedOperationException(); }
}
