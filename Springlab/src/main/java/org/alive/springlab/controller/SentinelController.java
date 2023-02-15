package org.alive.springlab.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 示范使用Sentinel做流控
 * </p>
 *
 * @author hailin84
 * @since 2023/2/15
 */
@RestController
@RequestMapping("/sentinel")
public class SentinelController {
    @RequestMapping("/flow")
    public String flowControl() {
        // 1.5.0 版本开始可以利用 try-with-resources 特性
        // 资源名可使用任意有业务语义的字符串，比如方法名、接口名或其它可唯一标识的字符串。
        try (Entry entry = SphU.entry("helloSentinel")) {
            // 被保护的业务逻辑
            // do something here...
            return "OK";
        } catch (BlockException ex) {
            // 资源访问阻止，被限流或被降级
            // 在此处进行相应的处理操作
            return "BLOCKED";
        }
    }

    /**
     * 使用注解方式实现流控
     *
     * @return 响应
     */
    @RequestMapping("/flow2")
    @SentinelResource(value = "helloSentinel", blockHandler = "blockHandlerForFlowControl")
    public String flowControl2() {
        return "OK";
    }

    public String blockHandlerForFlowControl(BlockException e) {
        // e.printStackTrace();
        return "BLOCKED";
    }
}
