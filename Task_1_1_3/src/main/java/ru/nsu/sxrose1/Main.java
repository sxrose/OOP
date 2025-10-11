package ru.nsu.sxrose1;

import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.eval.EvalUtils;
import ru.nsu.sxrose1.expr.parse.ParseUtils;

import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) {

    Expression e1 = ParseUtils.parse("2").get();

    System.out.println(EvalUtils.eval(e1, Map.of()).get());

    Expression e2 = ParseUtils.parse("-xxx + 226 * 1337 / 0").get();

    System.out.println(e2);
  }
}
