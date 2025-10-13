package ru.nsu.sxrose1.expr.eval;

import java.util.HashMap;
import java.util.Map;

public class EvalContext extends HashMap<String, Double> {
  public EvalContext(Map<String, Double> mapping) {
    super(mapping);
  }
}
