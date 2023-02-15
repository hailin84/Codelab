package org.alive.springlab;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringlabApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringlabApplication.class, args);

        initFlowRules();
    }

    /**
     * 初始化Sentinel限流规则
     */
    private static void initFlowRules(){
        //1.创建存放限流规则的集合
        List<FlowRule> rules = new ArrayList<>();
        //2.创建限流规则
        FlowRule rule = new FlowRule();
        //定义资源，表示sentinel会对这个资源生效
        rule.setResource("helloSentinel");
        //定义限流规则类型
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //定义QPS每秒能通过的请求个数
        rule.setCount(2);
        //3.将限流规则放入集合中
        rules.add(rule);
        //4.加载限流规则
        FlowRuleManager.loadRules(rules);
    }
}