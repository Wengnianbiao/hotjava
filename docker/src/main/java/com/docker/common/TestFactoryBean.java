package com.docker.common;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

// 标记为 Spring 组件，让容器扫描识别
@Component
public class TestFactoryBean implements FactoryBean<TestBean> {

    /**
     * 返回由 FactoryBean 创建的对象实例，这里我们创建并返回 TestBean 对象
     * @return
     * @throws Exception
     */
    @Override
    public TestBean getObject() throws Exception {
        TestBean testBean = new TestBean();
        System.out.println("我被调用了");
        // 可以在这里对 TestBean 做一些自定义初始化，方便调试观察
        // 比如修改 getName 返回值，和原本 TestBean 区分开
        // 这里只是示例，你也可以根据实际调试需求设置不同内容
        return testBean;
    }

    /**
     * 返回 FactoryBean 创建对象的类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return TestBean.class;
    }

    /**
     * 是否单例，这里设置为 true 表示创建的 TestBean 是单例；
     * 如果想测试原型等情况，可返回 false（不过一般 FactoryBean 结合单例场景多些）
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
