package ru.nsu.sxrose1.expr.eval;

import java.util.HashMap;
import java.util.Map;

/** Represents context for evaluating EvalDAGs. */
public class EvalContext extends HashMap<String, Double> {
    /**
     * @param mapping Map consisting of mappings for variables.
     */
    public EvalContext(Map<String, Double> mapping) {
        super(mapping);
    }
}
