package org.alive.idealab.tools.json;

import lombok.Data;

import java.util.Objects;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/23 14:36
 */
@Data
public class PredicatKvs
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
        PredicatKvs that = (PredicatKvs) o;
        return kvValue.equals(that.kvValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kvValue);
    }
}


