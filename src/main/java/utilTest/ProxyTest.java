package utilTest;
import com.google.common.collect.Lists;

import java.lang.reflect.*;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: hu.xiaohe
 * @Date: 2019/10/15 16:27
 * @Description:
 */
public class ProxyTest  {

    public interface  HelloService{
        void sayHello();
    }

    public interface  Interceptor{
        Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException;
    }
    static class Invocation{
        private Object target;
        private Method method;
        private Object[] args;
        public Invocation(Object target,Method method,Object[] args){
            this.target = target;
            this.method = method;
            this.args = args;
        }

        public Object process() throws InvocationTargetException, IllegalAccessException {
            Object rs = method.invoke(target, args);
            return rs;
        }
    }

    static class LogInceptor implements Interceptor {

        @Override
        public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
            System.out.println("前置通知。。。");
            Object process = invocation.process();
            return process;
        }
    }

    static class TranstionInceptor implements Interceptor {

        @Override
        public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
            Object process = invocation.process();
            System.out.println("后置通知。。。");
            return process;
        }
    }

    static class HelloServiceImpl implements HelloService {

        @Override
        public void sayHello() {
            System.out.println("hello world!!");
        }
    }

    static class MyInvocationHander implements InvocationHandler {
        private Interceptor interceptor ;

        private Object target;
        public MyInvocationHander(Object target,Interceptor Interceptor){
            this.target = target;
            this.interceptor = interceptor;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Invocation invocation = new Invocation(target, method, args);
            return invocation.process();
        }
    }

    public static Object swap(Object target,List<Interceptor> interceptors){
       // Object instance = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new MyInvocationHander(target,interceptors));
        return null;
    }

    public static void main(String[] args) {
        ArrayList<Interceptor> interceptors = Lists.newArrayList(new LogInceptor(), new TranstionInceptor());
        HelloService proxy = (HelloService) swap(new HelloServiceImpl(),interceptors);
        proxy.sayHello();
    }
}
