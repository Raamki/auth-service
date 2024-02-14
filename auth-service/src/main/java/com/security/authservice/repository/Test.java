package com.security.authservice.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        int k=2;
        int n=4;
        List<Integer> Qp = Arrays.asList(4,2);
        List<Integer> S = Arrays.asList(1,2,1,1);
        List<Integer> P = Arrays.asList(2,2,3,5);
        testing(k, n, Qp, S, P);
    }

    private static void testing(int k, int n, List<Integer> qp, List<Integer> s, List<Integer> p) {
        List<Integer> test = new ArrayList<>();
        for (int i=1; i<= qp.size(); i++) {
            test = new ArrayList<>();
            for (int j=0; j< s.size(); j++) {
                if (s.get(j) == i) {

                }
            }

        }
    }
}
