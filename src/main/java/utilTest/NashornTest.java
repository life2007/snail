package utilTest;

import com.google.common.collect.Lists;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.junit.Before;
import org.junit.Test;

import javax.script.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: hu.xiaohe
 * @Date: 2018/11/22 15:41
 * @Description:
 */
public class NashornTest {
    NashornScriptEngine engine = null;

    @Before
    public void before(){
        ScriptEngineManager sem = new ScriptEngineManager();
         engine = (NashornScriptEngine) sem.getEngineByName("nashorn");
    }

    @Test
    public void test1() throws ScriptException, NoSuchMethodException, InterruptedException {
        Compilable compilableEngine = this.engine;
        for(int i=0;i<20;i++){
            Random random = new Random();
            new Thread(()->{
                try {
                    String functionScript = " function add(op1,op2){return op1+op2};   ";
                    CompiledScript fScript = compilableEngine.compile(functionScript);
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(10));
                    Object rs = fScript.eval();
                    System.out.println("thead--"+rs);
                } catch (InterruptedException | ScriptException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        for(int i=0;i<20;i++){
            Random random = new Random();
            new Thread(()->{
                try {
                    String functionScript1 = " function add(op1,op2){return op1-op2};   ";
                    CompiledScript fScript1 = compilableEngine.compile(functionScript1);
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(10));
                    System.out.println(fScript1.eval());
                } catch (ScriptException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.MILLISECONDS.sleep(2000);
    }
}
