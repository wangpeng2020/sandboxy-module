package modules.demo;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;


@MetaInfServices(Module.class)
@Information(id = "spring-demo-fix")
public class SpringDemoModule implements Module {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command("printCall")
    public void printCall() {

        new EventWatchBuilder(moduleEventWatcher)
                .onClass("com.wangpeng.spring.demo.todo.service.impl.TodoServiceImpl")
                .onBehavior("addTodo")
                .onWatch(new AdviceListener() {

                    /**
                     * 拦截{com.wangpeng.spring.demo.todo.service.impl.TodoServiceImpl.andTodo}方法，当这个方法调用之前将会被
                     * before()所拦截, 返回前将会被afterReturning()所拦截
                     */
                    @Override
                    protected void before(Advice advice) {
                        System.out.println(String.format("call addTodo %s", "print by sandy-box module"));
                    }

                    @Override
                    protected void afterReturning(Advice advice) throws Throwable {
                        System.out.println(String.format("after return %s", advice.getReturnObj().toString()));
                    }
                });
    }
}
