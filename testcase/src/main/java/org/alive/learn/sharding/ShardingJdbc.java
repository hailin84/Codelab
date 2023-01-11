package org.alive.learn.sharding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.jdbc.core.datasource.ShardingDataSource;

/**
 * 原文{@link http://blog.csdn.net/clypm/article/details/54378502}
 */
public class ShardingJdbc {

	public static void main(String[] args) throws Exception {
		// 数据源
		Map<String, DataSource> dataSourceMap = new HashMap<>(2);
		dataSourceMap.put("sharding_0", createDataSource("sharding_0"));
		dataSourceMap.put("sharding_1", createDataSource("sharding_1"));

		DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
		// 分表分库的表，第一个参数是逻辑表名，第二个是实际表名，第三个是实际库
		// TableRule orderTableRule = new TableRule("t_order",
		// Arrays.asList("t_order_0", "t_order_1"), dataSourceRule);
		// TableRule orderItemTableRule = new TableRule("t_order_item",
		// Arrays.asList("t_order_item_0", "t_order_item_1"),
		// dataSourceRule);

		TableRule orderTableRule = TableRule.builder("t_order").actualTables(Arrays.asList("t_order_0", "t_order_1"))
				.dataSourceRule(dataSourceRule).build();
		TableRule orderItemTableRule = TableRule.builder("t_order_item")
				.actualTables(Arrays.asList("t_order_item_0", "t_order_item_1")).dataSourceRule(dataSourceRule).build();

		/**
		 * DatabaseShardingStrategy 分库策略 参数一：根据哪个字段分库 参数二：分库路由函数
		 * TableShardingStrategy 分表策略 参数一：根据哪个字段分表 参数二：分表路由函数
		 * 
		 */
		// ShardingRule shardingRule = new ShardingRule(dataSourceRule,
		// Arrays.asList(orderTableRule, orderItemTableRule),
		// Arrays.asList(new BindingTableRule(Arrays.asList(orderTableRule,
		// orderItemTableRule))),
		// new DatabaseShardingStrategy("user_id", new
		// ModuloDatabaseShardingAlgorithm()),
		// new TableShardingStrategy("order_id", new
		// ModuloTableShardingAlgorithm()));
		ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule)
				.tableRules(Arrays.asList(orderTableRule, orderItemTableRule))
				.bindingTableRules(
						Arrays.asList(new BindingTableRule(Arrays.asList(orderTableRule, orderItemTableRule))))
				.databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new DemoDatabaseShardingAlgorithm()))
				.tableShardingStrategy(new TableShardingStrategy("order_id", new DemoTableShardingAlgorithm())).build();

		ShardingDataSource dataSource = new ShardingDataSource(shardingRule);
		String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";
		
		// JDK1.7+提供的try with resource语法
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, 10);
			pstmt.setInt(2, 1001);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					System.out.println(rs.getInt(1));
					System.out.println(rs.getInt(2));
					System.out.println(rs.getInt(3));
				}
			}
		}
		
		dataSource.close();
	}

	private static DataSource createDataSource(String dataSourceName) {
		BasicDataSource result = new BasicDataSource();
		result.setDriverClassName("com.mysql.jdbc.Driver");
		result.setUrl("jdbc:mysql://localhost:3306/" + dataSourceName);
		result.setUsername("root");
		result.setPassword("");
		return result;
	}
}
