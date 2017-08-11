package cn.nju.seg.atg.spfwrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.LinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.LogicalORLinearIntegerConstraints;
import gov.nasa.jpf.symbc.numeric.MixedConstraint;
import gov.nasa.jpf.symbc.numeric.NonLinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.RealConstraint;

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

  public static boolean logToFile(final Path logFile, final Iterable<String> logLines, final boolean isAppend) {
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

  public static boolean logToFile(final Path logFile, final Iterable<String> logLines) {
    return Utils.logToFile(logFile, logLines, true);
  }

  public static boolean appendToLogFile(final Path logFile, final String appendLogLine) {
    return Utils.logToFile(logFile, ImmutableList.of(appendLogLine), true);
  }

  private Utils() {throw new UnsupportedOperationException();}
}
