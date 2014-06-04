package com.an.antry.crawl.rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MathEvalMain {
    public static void main(String[] args) {
        Context context = Context.enter();
        try {
            Scriptable scope = context.initStandardObjects();
            String str = "9*(1+2)";
            Object result = context.evaluateString(scope, str, null, 1, null);
            double res = Context.toNumber(result);
            System.out.println(res);
        } finally {
            Context.exit();
        }
    }
}
