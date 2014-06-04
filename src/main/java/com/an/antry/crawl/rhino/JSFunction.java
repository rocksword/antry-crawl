package com.an.antry.crawl.rhino;

import org.mozilla.javascript.Function;

public class JSFunction // extends ScriptableObject
{
    private String name;
    private Function handle;

    public JSFunction(String name) {
        this.name = name;
    }

    public void setHandler(Function func) {
        this.handle = func;
    }

    public Function getHandler() {
        return this.handle;
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
