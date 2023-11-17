package org.alive.idealab.generate;

import java.io.File;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.*;

public class SqlGenerator {
    public static void main(String[] args) throws Exception {
        Map<String, String> ret = loadData();

        Set<String> set = loadPayData();

        String sql1 = "update dst_paycenter.pay_order_record set user_id = ''{0}'' where dst_biz_no = ''{1}'';";
        String sql2 = "update dst_paycenter.pay_prepay_record set user_id = ''{0}'' where trade_no = ''{1}'';";


//        for (String dstBizNo : set) {
//            String userId = ret.get(dstBizNo);
//            System.out.println(MessageFormat.format(sql1, userId, dstBizNo));
//            System.out.println(MessageFormat.format(sql2, userId, dstBizNo));
//        }

        Map<String, String> depositMap = loadDeposit();
        for (String dstBizNo : set) {
            String deposit = depositMap.get(dstBizNo);
            if (deposit == null) {
                continue;
            }
            depositMap.remove(dstBizNo);
            String userId = ret.get(dstBizNo);
            System.out.println(dstBizNo + " " + userId + " " + deposit);
        }

        System.out.println(depositMap);
    }



    public static Map<String, String> loadData() throws Exception {
        String file = "D:\\工作内容\\线上问题处理\\20230915 交易中心用户ID上送77777777777777777导致的数据错乱问题\\支付单对应的实际UserID.txt";
        Map<String, String> ret = new HashMap<>();
        List<String> lines = Files.readAllLines(new File(file).toPath());
        lines.forEach(
                line -> {
                    String parts[] = line.split(" ");
                    ret.put(parts[0], parts[1]);
                }
        );
        return ret;
    }

    public static Set<String> loadPayData() throws Exception {
        String file = "D:\\工作内容\\线上问题处理\\20230915 交易中心用户ID上送77777777777777777导致的数据错乱问题\\支付中心有效的支付单号.txt";
        List<String> lines = Files.readAllLines(new File(file).toPath());
        return new HashSet<>(lines);
    }

    public static Map<String, String> loadDeposit() throws Exception {
        String file = "D:\\工作内容\\线上问题处理\\20230915 交易中心用户ID上送77777777777777777导致的数据错乱问题\\支付单对应的押金.txt";
        Map<String, String> ret = new HashMap<>();
        List<String> lines = Files.readAllLines(new File(file).toPath());
        lines.forEach(
                line -> {
                    String parts[] = line.split(" ");
                    ret.put(parts[0], parts[1] + " " + parts[2]);
                }
        );
        return ret;
    }
}
