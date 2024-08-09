package com.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author timess
 */
class AlgorithmUtilsTest {

    @Test
    void minDistance() {
        List<String> list1 = Arrays.asList("java", "大二", "男");
        List<String> list2 = Arrays.asList("java", "大二", "女");
        List<String> list3 = Arrays.asList("python", "大二", "男");
        System.out.println(AlgorithmUtils.minDistance(list1,list2));
        System.out.println(AlgorithmUtils.minDistance(list3,list2));

    }
}