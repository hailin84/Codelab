package org.alive.idealab.tools.json;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/23 14:38
 */
@Data
public class Route {
    private int id;

    private String routeId;

    private String routeName;

    private String routeUri;

    private int executeOrder;

    private int enable;

    private long createTime;

    private List<Predicats> predicats;

    private List<Filters> filters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return routeUri.equals(route.routeUri) && getPredictString().equals(route.getPredictString())
                && getFilterString().equals(route.getFilterString());
    }

    public String getPredictString() {
        Map<String, String> map = new TreeMap<>();
        for (Predicats ele : predicats) {
            map.put(ele.getPredicatConstantName(), ele.getKvString());
        }
        return map.toString();
    }

    public String getFilterString() {
        Map<String, String> map = new TreeMap<>();
        for (Filters ele : filters) {
            map.put(ele.getFilterConstantName(), ele.getKvString());
        }
        return map.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeUri, predicats, filters);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(routeUri).append("#").append(getPredictString()).append("#").append(getFilterString());
        return builder.toString();
    }
}
