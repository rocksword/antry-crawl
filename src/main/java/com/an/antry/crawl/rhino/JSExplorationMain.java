package com.an.antry.crawl.rhino;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

//纯java 的javascript引擎：rhino
//http://www.iteye.com/topic/87423
public class JSExplorationMain {
    private Context cx = Context.enter();
    private Scriptable scope = cx.initStandardObjects();

    public JSExplorationMain() {
    }

    public Object runJavaScript(String filename) {
        String jsContent = this.getJsContent(filename);
        Object result = cx.evaluateString(scope, jsContent, filename, 1, null);
        return result;
    }

    private String getJsContent(String filename) {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(filename));
            StringBuffer sb = new StringBuffer();
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Scriptable getScope() {
        return scope;
    }

    public static void main(String[] args) {
        URL res = JSExplorationMain.class.getResource("/jsmap.js");
        System.out.println("filename: " + res.getFile());

        JSExplorationMain jsExploration = new JSExplorationMain();
        Object result = jsExploration.runJavaScript(res.getFile());
        Scriptable scope = jsExploration.getScope();

        Scriptable obj = (Scriptable) scope.get("obj", scope);
        System.out.println("obj.a == " + obj.get("a", obj));
        Scriptable b = (Scriptable) obj.get("b", obj);
        System.out.println("b[0] == " + b.get(0, b));
        Boolean flag = (Boolean) scope.get("flag", scope);
        System.out.println(flag);

        Scriptable myobj = (Scriptable) scope.get("obj", scope);
        Boolean myflag = (Boolean) scope.get("flag", scope);
        System.out.println(myflag);

        Scriptable jsFunction = (Scriptable) scope.get("jsFunction", scope);
        Function fc = (Function) jsFunction.get("handler", jsFunction);
        Object isPrime = fc.call(Context.getCurrentContext(), jsFunction, fc, new Object[] { "this is my test" });
    }
}
