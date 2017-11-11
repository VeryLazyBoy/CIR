package com.cir.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import com.cir.models.Alias;

public class ArrayUtility {
    public static String[] convertAliasListToStringArray(ArrayList<Alias> aliasList) {
        return aliasList.stream().map(Alias::getShortName).toArray(String[]::new);
    }

    public static String[] getYearArray(String startYear, String endYear) {
        int[] years = IntStream.range(Integer.valueOf(startYear), Integer.valueOf(endYear) + 1).toArray();
        String[] yearArray = Arrays.stream(years).boxed().map(String::valueOf).toArray(String[]::new);
        return yearArray;
    }
}
