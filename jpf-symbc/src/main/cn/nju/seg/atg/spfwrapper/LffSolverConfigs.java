package cn.nju.seg.atg.spfwrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.numeric.SymbolicConstraintsGeneral;

/**
 * @author Zhang Yifan
 */
public final class LffSolverConfigs {

  // FIXME hard-coded log dir
  private static final Path LOG_HOME = Paths.get("/home/njuseg/experiments/gen");

  //region Configs for logging of `ProblemLff`

  static final boolean IS_CLEAN_LFF_LOG_DIR = false;

  static final Path LFF_LOG_DIR = LffSolverConfigs.LOG_HOME.resolve("LffLogs");

  static {
    try {
      Files.createDirectories(LffSolverConfigs.LFF_LOG_DIR);
    } catch (final IOException ignored) {}

    if (LffSolverConfigs.IS_CLEAN_LFF_LOG_DIR) {
      for (final File logFile : LffSolverConfigs.LFF_LOG_DIR.toFile().listFiles()) {
        final Path logFilePath = logFile.toPath();
        if (Files.isRegularFile(logFilePath)) {
          try {
            Files.delete(logFilePath);
          } catch (final IOException ignored) {}
        }
      }
    }
  }

  //endregion Configs for logging of `ProblemLff`

  //region Configs for logging CW start points

  public static final boolean IS_LOG_CONCOLIC_WALKER_START_POINTS = true;

  public static final Path CONCOLIC_WALKER_START_POINTS_LOG_DIR = LffSolverConfigs.LOG_HOME.resolve("ConcolicWalkerStartPoints");

  static {
    if (LffSolverConfigs.IS_LOG_CONCOLIC_WALKER_START_POINTS) {
      try {
        Files.createDirectories(LffSolverConfigs.CONCOLIC_WALKER_START_POINTS_LOG_DIR);
      } catch (final IOException ignored) {}
    }
  }

  //endregion Configs for logging CW start points

  //region Configs for Lff solver start points

  public static final boolean IS_USE_EXTRA_SOLVER_FOR_START_POINT = true;

  private static String[] DP_FOR_EXTRA_SYMBOLIC_CONSTRAINTS_GENERAL = { "coral" };

  public interface SymbolicConstraintsGeneralFunction<T> {

    T apply(SymbolicConstraintsGeneral sc);
  }

  public static <T> T useExtraSymbolicConstraintsGeneral(final SymbolicConstraintsGeneralFunction<T> func) {
    assert func != null;

    // record current dp
    final String[] currentDp = SymbolicInstructionFactory.dp;

    SymbolicInstructionFactory.dp = LffSolverConfigs.DP_FOR_EXTRA_SYMBOLIC_CONSTRAINTS_GENERAL;

    final SymbolicConstraintsGeneral sc = new SymbolicConstraintsGeneral();
    final T res = func.apply(sc);

    // restore current dp
    SymbolicInstructionFactory.dp = currentDp;

    return res;
  }

  //endregion Configs for Lff solver start points

  @Deprecated
  private LffSolverConfigs() { throw new UnsupportedOperationException(); }
}
