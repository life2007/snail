package utilTest;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.joda.time.Seconds;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.*;

public class JavaExecScriptMTDemo {

    public static void main(String[] args) throws Exception {

        ScriptEngineManager sem = new ScriptEngineManager();
        NashornScriptEngine engine = (NashornScriptEngine) sem.getEngineByName("nashorn");
        Compilable Compilable = engine;
        String script1 = "function() {  function transform(){" +
                " return 2; }  transform();}" ;
        CompiledScript compiledScript = Compilable.compile(script1);
        Callable<Object> addition = new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    TimeUnit.MILLISECONDS.sleep(10 );
                    Object rs = compiledScript.eval();
                    return rs;
//                    ScriptObjectMirror mirror = (ScriptObjectMirror) engine.invokeFunction("ElementSchedule.transform", Arrays.asList(1, 2, 3));
//                    return mirror.values();
                } catch (ScriptException  | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool();
        ArrayList<Future<Object>> results = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            results.add(executor.submit(addition));
        }
        engine.eval(script1);
        for (Future<Object> result : results) {
            Object jsResult = result.get();
            System.out.println(jsResult);
        }

        executor.awaitTermination(1, TimeUnit.SECONDS);


//        System.out.println("Overall: " + miscalculations + " wrong values for 1 + 1.");
    }
}
