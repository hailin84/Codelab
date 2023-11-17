package org.alive.idealab.tools.json;

import lombok.Data;

import java.util.Objects;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/23 14:43
 */
@Data
public class FilterKvs
{
    private int id;

    private int kvId;

    private String kvKey;

    private String kvValue;

    private String enable;

    private String kvType;

    private long createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterKvs filterKvs = (FilterKvs) o;
        return kvValue.equals(filterKvs.kvValue) && kvKey.equals(filterKvs.kvKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kvValue);
    }
}
