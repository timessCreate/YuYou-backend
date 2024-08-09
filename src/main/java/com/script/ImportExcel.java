package com.script;/**
 * @author timess
 */

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * 用于对文本信息的提取
 * @className: script
 * @Version: 1.0
 * @description:
 *
 */

public class ImportExcel {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\timess\\Desktop\\test.xlsx";
        readByListener(fileName);
        synchronousRead(fileName);
    }
    
    /**
     * 使用监听器去读取excel文件
     * @param fileName
     */
    public static void readByListener(String fileName){
        EasyExcel.read(fileName, UserTableInfo.class,new TableListener()).sheet().doRead();
    }

    /**
     * 同步读
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<UserTableInfo> totalDataList =
                EasyExcel.read(fileName).head(UserTableInfo.class).sheet().doReadSync();
        for (UserTableInfo userTableInfo : totalDataList) {
            System.out.println(userTableInfo);
        }
    }
}
