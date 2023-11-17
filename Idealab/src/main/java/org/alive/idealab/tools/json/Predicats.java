package org.alive.idealab.tools.json;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/23 14:41
 */
@Data
public class Predicats
{
    private int id;

    private int routeId;

    private String predicatName;

    private String predicatConstantName;

    private List<PredicatKvs> predicatKvs;

    private int executeOrder;

    private int enable;

    private long createTime;

    private int kvType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Predicats predicats = (Predicats) o;
        return predicatConstantName.equals(predicats.predicatConstantName) && getKvString().equals(predicats.getKvString());
    }

    public String getKvString() {
        Set<String> kvSet = new TreeSet<>();
        for (PredicatKvs ele : predicatKvs) {
            kvSet.add(ele.getKvValue());
        }
        return JSON.toJSONString(kvSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicatConstantName, predicatKvs);
    }
}