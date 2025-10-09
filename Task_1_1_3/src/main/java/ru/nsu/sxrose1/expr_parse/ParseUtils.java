package ru.nsu.sxrose1.expr_parse;

import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.Number;

import java.util.stream.Stream;

public class ParseUtils {

  enum TokenType {
    NUM,
    OP,
    BRACKET_OPEN,
    BRACKET_CLOSE
  }

  record Token(String value, TokenType tokenType) {}

  private static String tokenizeNumber(String buf) {
    int i = 0;
    while (Character.isDigit(buf.charAt(i))) i++;

    if (buf.charAt(i) == '.') {
      do i++;
      while (Character.isDigit(buf.charAt(i)));
    }

    return buf.substring(0, i - 1);
  }

  private static Stream<Token> tokenize(String exprStr) {
    Stream.Builder<Token> builder = Stream.builder();

    String buf = exprStr;

    while (!buf.isEmpty()) {
      if (Character.isWhitespace(buf.charAt(0))) {
        buf = buf.stripLeading();
        continue;
      }

      if (Character.isDigit(buf.charAt(0))) {
        String num = tokenizeNumber(buf);
        buf = buf.substring(num.length());
        builder.add(new Token(num, TokenType.NUM));
        continue;
      }

      if (buf.charAt(0) == '(' || buf.charAt(0) == ')') {
        buf = buf.substring(1);
        builder.add(
            new Token(
                null, buf.charAt(0) == '(' ? TokenType.BRACKET_OPEN : TokenType.BRACKET_CLOSE));
      }

      builder.add(new Token(buf.substring(0, 0), TokenType.OP));
      buf = buf.substring(1);
    }

    return builder.build();
  }

  private static Number parseNumber(Stream<Token> stream) {}

  public static Expression parse(String exprStr) {}
}
