package com.script;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 将用户导入到数据库
 * @className: ImportUser
 * @Version: 1.0
 * @description:
 */

public class ImportUser {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\timess\\Desktop\\test.xlsx";
        List<UserTableInfo> userInfoList =
                EasyExcel.read(fileName).head(UserTableInfo.class).sheet().doReadSync();
        System.out.println("总数" + userInfoList.size());
        Map<String, List<UserTableInfo>> listMap =
                userInfoList.stream().
                        filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername()))
                        .collect(Collectors.groupingBy(UserTableInfo::getUsername));
        System.out.println("不重复的昵称数 = " + listMap.keySet().size());
    }
}
