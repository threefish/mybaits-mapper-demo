package com.example.mybaitsmapper.core;

import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 * date: 2021/6/6
 */
public interface AuditorAware<T> {

    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    Optional<T> getCurrentAuditor();

}
