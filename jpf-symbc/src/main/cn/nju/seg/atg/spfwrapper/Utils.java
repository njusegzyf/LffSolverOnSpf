package cn.nju.seg.atg.spfwrapper;

import com.google.common.base.Preconditions;

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

  private Utils() {throw new UnsupportedOperationException();}
}
