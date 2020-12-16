package com.imooc.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import java.io.IOException;

/**
 * <h1>在过滤器中存储客户端发起请求的时间戳</h1>
 * <h2>zuul由filter责任链构成，zuul具有丰富的filter</h2>
 * Created by Qinyi.
 */
@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {

    @Override
    public String filterType() {

        // 过滤器的类型 鉴权、限流可以考虑在此实现
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 过滤的顺序 数值越小优先级越高
        return 0;
    }

    @Override
    public boolean shouldFilter() {

        // 是否启用当前的过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        // 用于在过滤器之间传递消息。它的数据保存在每个请求的 ThreadLocal 中。它用于存储请求路由到哪里、错误、HttpServletRequest、
        // HttpServletResponse 都存储在 RequestContext中。RequestContext 扩展了 ConcurrentHashMap, 所以,
        // 任何数据都可以存储在上下文中。
        RequestContext ctx = RequestContext.getCurrentContext();

        try {
            ServletInputStream inp = ctx.getRequest().getInputStream();
            log.info("request info: {}", IOUtils.toString(inp));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // 存储客户端发起请求的时间戳
        ctx.set("startTime", System.currentTimeMillis());

        return null;
    }
}
