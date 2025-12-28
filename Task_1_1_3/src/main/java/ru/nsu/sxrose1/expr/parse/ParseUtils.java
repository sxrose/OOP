package ru.nsu.sxrose1.expr.parse;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import ru.nsu.sxrose1.expr.Add;
import ru.nsu.sxrose1.expr.BinaryExpression;
import ru.nsu.sxrose1.expr.Div;
import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.Mul;
import ru.nsu.sxrose1.expr.Number;
import ru.nsu.sxrose1.expr.Sub;
import ru.nsu.sxrose1.expr.Variable;

/** Util class for parsing expressions from string. */
public class ParseUtils {
    private static final Map<Character, TokenType> brackets =
            Map.of('(', TokenType.BRACKET_OPEN, ')', TokenType.BRACKET_CLOSE);
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

    private static BinaryExpression binaryExpressionFromOp(
            Expression lhs, Expression rhs, OpType op) {
        return switch (op) {
            case OpType.SUM -> new Add(lhs, rhs);
            case OpType.SUB -> new Sub(lhs, rhs);
            case OpType.MUL -> new Mul(lhs, rhs);
            case OpType.DIV -> new Div(lhs, rhs);
        };
    }

    private static String tokenizeNumber(String buf) {
        int i = 0;
        while (i < buf.length() && Character.isDigit(buf.charAt(i))) {
            i++;
        }

        if (i >= buf.length()) {
            return buf.substring(0, i);
        }

        if (buf.charAt(i) == '.') {
            do {
                i++;
            } while (i < buf.length() && Character.isDigit(buf.charAt(i)));
        }

        return buf.substring(0, i);
    }

    private static String tokenizeVariable(String buf) {
        int i = 0;

        do {
            i++;
        } while (i < buf.length()
                && !Character.isWhitespace(buf.charAt(i))
                && !ops.containsKey(buf.charAt(i))
                && !brackets.containsKey(buf.charAt(i)));

        return buf.substring(0, i);
    }

    private static Optional<Queue<Token>> tokenize(String exprStr) {

        Queue<Token> queue = new ArrayDeque<>();

        String buf = exprStr;

        while (!buf.isEmpty()) {
            if (Character.isWhitespace(buf.charAt(0))) {
                buf = buf.stripLeading();
                continue;
            }

            if (Character.isDigit(buf.charAt(0))) {
                String num = tokenizeNumber(buf);
                buf = buf.substring(num.length());
                queue.add(new Token(TokenType.NUM, Double.parseDouble(num), null, null));
                continue;
            }

            if (Character.isAlphabetic(buf.charAt(0))) {
                String variable = tokenizeVariable(buf);
                buf = buf.substring(variable.length());
                queue.add(new Token(TokenType.VAR, 0.0, variable, null));
                continue;
            }

            if (brackets.containsKey(buf.charAt(0))) {
                queue.add(new Token(brackets.get(buf.charAt(0)), 0.0, null, null));

                buf = buf.substring(1);
                continue;
            }

            queue.add(new Token(TokenType.OP, 0.0, null, ops.get(buf.charAt(0))));

            buf = buf.substring(1);
        }

        return Optional.of(queue);
    }

    private static Optional<Expression> prattParseLhs(Queue<Token> tokens) {
        var negOpt =
                Optional.ofNullable(tokens.peek())
                        .map(
                                (t) -> {
                                    if (t.tokenType == TokenType.OP && t.opValue == OpType.SUB) {
                                        tokens.poll();
                                        return true;
                                    }

                                    return false;
                                });

        if (negOpt.isEmpty()) {
            return Optional.empty();
        }
        boolean negate = negOpt.get();

        return Optional.ofNullable(tokens.poll())
                .flatMap(
                        (tk) -> {
                            return switch (tk.tokenType) {
                                case TokenType.BRACKET_OPEN ->
                                        prattParse(tokens, 0)
                                                .map(
                                                        (e) ->
                                                                negate
                                                                        ? new Sub(
                                                                                new Number(0.0), e)
                                                                        : e)
                                                .filter(
                                                        (e) -> {
                                                            var nxt = tokens.poll();
                                                            return Objects.nonNull(nxt)
                                                                    && nxt.tokenType
                                                                            == TokenType
                                                                                    .BRACKET_CLOSE;
                                                        });

                                case TokenType.NUM ->
                                        Optional.of(
                                                new Number(tk.numValue * (negate ? -1.0 : 1.0)));

                                case TokenType.VAR ->
                                        Optional.of(new Variable(tk.varValue))
                                                .map(
                                                        (e) ->
                                                                negate
                                                                        ? new Sub(
                                                                                new Number(0.0), e)
                                                                        : e);

                                default -> Optional.empty();
                            };
                        });
    }

    private static Optional<Expression> prattParse(Queue<Token> tokens, int minbp) {
        if (tokens.isEmpty()) {
            return Optional.empty();
        }

        var lhsOpt = prattParseLhs(tokens);
        if (lhsOpt.isEmpty()) {
            return Optional.empty();
        }

        while (true) {
            Token tk = tokens.peek();

            var bpOpt =
                    Optional.ofNullable(tk)
                            .filter((t) -> t.tokenType == TokenType.OP)
                            .flatMap((t) -> Optional.ofNullable(bindingPowers.get(t.opValue)));

            if (bpOpt.isEmpty()) {
                break;
            }
            IntPair bp = bpOpt.get();

            if (bp.left < minbp) {
                break;
            }

            tokens.poll();

            lhsOpt =
                    lhsOpt.flatMap(
                            (lhs) ->
                                    prattParse(tokens, bp.right)
                                            .map(
                                                    (rhs) ->
                                                            binaryExpressionFromOp(
                                                                    lhs, rhs, tk.opValue)));
        }

        return lhsOpt;
    }

    /**
     * Parses expression from String.
     *
     * @param exprStr String that contains expression to parse.
     * @return parsed expression on successful parsing, none otherwise.
     */
    public static Optional<Expression> parse(String exprStr) {
        assert (ops.size() == OpType.values().length);
        assert (bindingPowers.size() == OpType.values().length);

        return tokenize(exprStr).flatMap((tokens) -> prattParse(tokens, 0));
    }

    private enum TokenType {
        NUM,
        VAR,
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

    private record IntPair(int left, int right) {}

    private record Token(TokenType tokenType, double numValue, String varValue, OpType opValue) {}
}
