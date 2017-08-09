package cn.nju.seg.atg.spfwrapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Zhang Yifan
 */
public abstract class AbstractProblemLff extends IProblemLffParser {

  //region Instance fields

  protected final StringBuilder expressionBuilder = new StringBuilder();

  protected final StringBuilder valNameBuilder = new StringBuilder();

  protected final StringBuilder valTypeBuilder = new StringBuilder();

  private int nextValIndex = 0;

  protected final HashMap<String, Integer> valNameToIndexMap = Maps.newHashMap();

  protected final ArrayList<String> logLines = Lists.newArrayList();

  //endregion Instance fields

  @Override
  public void post(final Object constraint) {
    if (this.expressionBuilder.length() != 0) {
      this.expressionBuilder.append(" && ");
    }
    this.expressionBuilder.append('(');
    this.expressionBuilder.append(IProblemLffParser.castToExpInstance(constraint));
    this.expressionBuilder.append(')');
  }

  @Override
  public void postLogicalOR(final Object[] constraints) {
    for (final Object constraint : constraints) {
      assert constraint instanceof String;
    }

    final StringBuilder orResultBuilder = new StringBuilder("(");
    Joiner.on("||").appendTo(orResultBuilder, constraints);
    orResultBuilder.append(")");
    this.post(orResultBuilder.toString());
  }

  protected void logToFile(final String fileName) {
    final Path logFile = AbstractProblemLff.logDir.resolve(fileName + ".txt");
    try {
      Files.write(logFile, this.logLines, Charset.defaultCharset(),
                  StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    } catch (final IOException ignored) {}
  }

  //region Mange variables

  @Override
  public Object makeIntVar(final String name, final int min, final int max) {
    return this.makeVar(name, "int");
  }

  @Override public Object makeRealVar(final String name, final double min, final double max) {
    return this.makeVar(name, "double");
  }

  private String makeVar(final String name, final String type) {
    if (this.valNameBuilder.length() != 0) {
      this.valNameBuilder.append(',');
      this.valTypeBuilder.append(',');
    }
    this.valNameBuilder.append(name);
    this.valTypeBuilder.append(type);

    // record the val's index
    this.valNameToIndexMap.put(name, Integer.valueOf(this.nextValIndex));
    ++this.nextValIndex;

    return name;
  }

  protected abstract double getValValueByName(final String valName);

  @Override
  public double getRealValue(final Object dpVar) {
    assert dpVar != null && dpVar instanceof String;

    final double dpVarValue = this.getValValueByName((String) dpVar);
    return dpVarValue;
  }

  @Override
  public int getIntValue(final Object dpVar) {
    assert dpVar != null && dpVar instanceof String;

    final double dpVarValue = this.getValValueByName((String) dpVar);
    return (int) dpVarValue;
  }

  @Override
  public double getRealValueInf(final Object dpvar) {
    return -1; // refer to `ProblemCoral`
  }

  @Override
  public double getRealValueSup(final Object dpVar) {
    return -1;
  }

  //endregion Mange variables

  //region Static fields

  // FIXME hard-coded log dir
  private static final Path logDir = Paths.get("/home/njuseg/experiments/gen/LffLogs");

  private static final boolean IS_CLEAN_LOG_DIR = false;

  static {
    try {
      Files.createDirectories(logDir);
    } catch (final IOException ignored) {}

    if (AbstractProblemLff.IS_CLEAN_LOG_DIR) {
      for (final File logFile : logDir.toFile().listFiles()) {
        final Path logFilePath = logFile.toPath();
        if (Files.isRegularFile(logFilePath)) {
          try {
            Files.delete(logFilePath);
          } catch (final IOException ignored) {}
        }
      }
    }
  }

  //endregion Static fields
}
