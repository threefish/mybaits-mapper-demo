package com.example.mybaitsmapper.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Component
public class AuditorAwareContextHelper implements ApplicationContextAware {

    /**
     * 应用程序上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 审计员实现类
     *
     * @return
     */
    public static AuditorAware getAuditorAware() {
        Assert.notNull(applicationContext,"applicationContext can not be null");
        final AuditorAware bean = applicationContext.getBean(AuditorAware.class);
        Assert.notNull(bean,"AuditorAware can not be null");
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AuditorAwareContextHelper.applicationContext = applicationContext;
    }


}
