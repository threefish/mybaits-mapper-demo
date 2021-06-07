package com.example.mybaitsmapper.core;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Component
public class CurrentAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("xxxx");
    }
}
