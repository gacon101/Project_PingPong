/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingpong_v1;

import java.util.Random;

/**
 *
 * @author Nguyen Duong Phu Trong CE160324
 */
public class Randomizer {
    
    private static Random rand = new Random();
    public static int Random(int min, int max){
        return rand.nextInt((max - min) + 1) + min;
    }
}
