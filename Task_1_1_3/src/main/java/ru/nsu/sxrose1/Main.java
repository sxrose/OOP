package ru.nsu.sxrose1;

import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.eval.EvalUtils;
import ru.nsu.sxrose1.expr.parse.ParseUtils;

import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    ParseUtils.parse(System.in.toString())
        .ifPresentOrElse(System.out::println, () -> System.out.println("Parse error."));
  }
}
