package ru.nsu.sxrose1.expr_parse;

import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.Number;
import ru.nsu.sxrose1.expr.Sub;

import java.util.*;
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

  private static final Map<Character, OpType> ops =
      Map.of(
          '+', OpType.SUM,
          '-', OpType.SUB,
          '*', OpType.MUL,
          '/', OpType.DIV);

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

  private static Optional<Queue<Token>> tokenize(String exprStr) {

    Queue<Token> queue = new ArrayDeque<Token>();

    String buf = exprStr;

    while (!buf.isEmpty()) {
      if (Character.isWhitespace(buf.charAt(0))) {
        buf = buf.stripLeading();
        continue;
      }

      if (Character.isDigit(buf.charAt(0))) {
        String num = tokenizeNumber(buf);
        buf = buf.substring(num.length());
        queue.add(new Token(TokenType.NUM, Double.parseDouble(num), null));
        continue;
      }

      if (buf.charAt(0) == '(' || buf.charAt(0) == ')') {
        buf = buf.substring(1);
        queue.add(
            new Token(
                buf.charAt(0) == '(' ? TokenType.BRACKET_OPEN : TokenType.BRACKET_CLOSE,
                0.0,
                null));
      }

      queue.add(new Token(TokenType.OP, 0.0, ops.get(buf.charAt(0))));

      buf = buf.substring(1);
    }

    return Optional.of(queue);
  }

  private static Number parseNumber(Queue<Token> stream) {}

  private static Optional<Expression> prattParseLHS(Queue<Token> tokens) {
    assert (!tokens.isEmpty());

    var negOpt =
        Optional.of(tokens.peek())
            .flatMap(
                (t) -> {
                  if (t.tokenType == TokenType.OP && t.opValue == OpType.SUB) {
                    tokens.poll();
                    return Objects.isNull(tokens.peek()) ? Optional.empty() : Optional.of(true);
                  }

                  return Optional.of(false);
                });

    if (negOpt.isEmpty()) return Optional.empty();
    boolean negate = negOpt.get();

    Token tk = tokens.peek();
    assert (Objects.nonNull(tk));

    return switch (tk.tokenType) {
      case TokenType.BRACKET_OPEN ->
          Optional.of(tokens.poll())
              .flatMap((_n) -> parseExprPrattBP(tokens, 0))
              .map((e) -> negate ? new Sub(new Number(0.0), e) : e)
              .filter(
                  (_e) -> {
                    var nxt = tokens.poll();
                    return Objects.nonNull(nxt) && nxt.tokenType == TokenType.BRACKET_CLOSE;
                  });
      case TokenType.NUM -> Optional.of(new Number(tk.numValue * (negate ? -1.0 : 1.0)));
      default ->
          throw new AssertionError(
              String.format("parseExprPrattBP got unexpected token at the start: %s", tk));
    };
  }

  private static Optional<Expression> parseExprPrattBP(Queue<Token> tokens, int minBP) {
    assert (!tokens.isEmpty());
    var lhsOpt = prattParseLHS(tokens);
  }

  private static Expression parseExpr(Stream<Token> stream) {}

  public static Expression parse(String exprStr) {}
}
