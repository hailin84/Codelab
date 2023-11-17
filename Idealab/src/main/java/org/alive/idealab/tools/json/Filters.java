package org.alive.idealab.tools.json;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.*;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/23 14:44
 */
@Data
public class Filters
{
    private int id;

    private int routeId;

    private String filterName;

    private String filterConstantName;

    private List<FilterKvs> filterKvs;

    private int executeOrder;

    private int enable;

    private long createTime;

    private int kvType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filters filters = (Filters) o;
        return filterConstantName.equals(filters.filterConstantName) && getKvString().equals(filters.getKvString());
    }

    public String getKvString() {
        Map<String, String> kvMap = new TreeMap<>();
        for (FilterKvs ele : filterKvs) {
            kvMap.put(ele.getKvKey(), ele.getKvValue());
        }
        return JSON.toJSONString(kvMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterConstantName, filterKvs);
    }
}