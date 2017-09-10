package com.f_candy_d.olga.structure;

import com.f_candy_d.dutils.Quantizable;

/**
 * Created by daichi on 17/08/30.
 */

public enum TaskType implements Quantizable {
    NONE {@Override public int quantize() {return 0;}},
    REMINDER {@Override public int quantize() {return 1;}},
    EVENT {@Override public int quantize() {return 2;}}
}
