package com.company;

import java.util.Random;

public class Calc {
    private static Random rand;
    private static long seed;

    static {
        seed = System.currentTimeMillis();
        rand = new Random(seed);
    }

    public static double sigmoid(double x){
        return (1/(1 + Math.pow(Math.E, (-1 * x))));
    }

    public static double[][] sigmoid(double[][] a){
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                c[i][j] = sigmoid(a[i][j]);
            }
        }

        return c;
    }

    public static double[][] randMatrix(int m, int n){
        double[][] a = new double[m][n];

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                a[i][j] = rand.nextDouble();
            }
        }
        return a;
    }

    public static double[][] biasMatrix(int m, int n, int x){
        double[][] a = new double[m][n];

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                a[i][j] = x;
            }
        }
        return a;
    }

    public static double[][] transpose(double[][] a){
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    public static void printMatrix(double[][] a){
        int m = a.length;
        int n = a[0].length;
        String[] result = new String[m];
        for(int i = 0; i < m; i ++){
            result[i] = "";
            for(int j = 0; j < n; j++){
                result[i] += " " +   a[i][j];
            }
        }

        for(int d = 0; d < m; d++){
            System.out.println(result[d]);
        }
    }


    public static double normalize(int max, int min, int x){
        return (double) (x - min) / (max - min);
    }

    public static double[][] add(double[][] a, double[][] b){
        if(a.length != b.length || a[0].length != b[0].length){
            throw new IllegalArgumentException("Matrices must be the same dimensions");
        }else{
            int m = a.length;
            int n = a[0].length;
            double[][] c = new double[m][n];
            for(int i = 0; i < m; i++){
                for(int j = 0; j < n; j++){
                    c[i][j] = a[i][j] + b[i][j];
                }
            }
            return c;
        }
    }

    public static double[][] dot(double[][] a, double[][] b){
       int m1 = a.length;
       int n1 = a[0].length;
       int m2 = b.length;
       int n2 = b[0].length;

       if(n1 != m2){
           throw new IllegalArgumentException("Matrices must be of dimension n1 = m2");
       }else{
           double[][] c = new double[m1][n2];
           for(int i = 0; i < m1; i++){
               for(int j = 0; j < n2; j++){
                   for(int k = 0; k < n1; k++){
                       c[i][j] += a[i][k] * b[k][j];
                   }
               }
           }
           return c;
       }
    }

    public static int getArrayMaxIndex(double[] a){
        double maxValue = 0;
        int index = 0;

        for(int i = 0; i < a.length; i++){
            if(a[i] > maxValue){
                maxValue = a[i];
                index = i;
            }
        }
        return index;
    }

}
