package ru.nsu.sxrose1.expr_parse;

import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.Number;

import java.util.Map;
import java.util.stream.Stream;

public class ParseUtils {
  private record IntPair(int left, int right) {}

  private enum TokenType {
    NUM,
    OP,
    BRACKET_OPEN,
    BRACKET_CLOSE
  }

  private enum OpType {
    SUM,
    SUB,
    MUL,
    DIV
  }

  private record Token(TokenType tokenType, double numValue, OpType opValue) {}

  private static final Map<String, OpType> ops =
      Map.of(
          "+", OpType.SUM,
          "-", OpType.SUB,
          "*", OpType.MUL,
          "/", OpType.DIV);

  private static final Map<OpType, IntPair> bindingPowers =
      Map.of(
          OpType.SUM,
          new IntPair(1, 2),
          OpType.SUB,
          new IntPair(1, 2),
          OpType.MUL,
          new IntPair(3, 4),
          OpType.DIV,
          new IntPair(3, 4));

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
        builder.add(new Token(TokenType.NUM, Double.parseDouble(num), null));
        continue;
      }

      if (buf.charAt(0) == '(' || buf.charAt(0) == ')') {
        buf = buf.substring(1);
        builder.add(
            new Token(
                buf.charAt(0) == '(' ? TokenType.BRACKET_OPEN : TokenType.BRACKET_CLOSE,
                0.0,
                null));
      }

      //      builder.add(new Token(Tok buf.substring(0, 0), TokenType.OP));
      buf = buf.substring(1);
    }

    return builder.build();
  }

  private static Number parseNumber(Stream<Token> stream) {}

  private static Expression parseExprBP(Stream<Token> stream, int minBP) {}

  private static Expression parseExpr(Stream<Token> stream) {}

  public static Expression parse(String exprStr) {}
}
