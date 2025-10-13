package ru.nsu.sxrose1;

import ru.nsu.sxrose1.expr.parse.ParseUtils;

public class Main {
  public static void main(String[] args) {
    ParseUtils.parse(System.in.toString())
        .ifPresentOrElse(System.out::println, () -> System.out.println("Parse error."));
  }
}
