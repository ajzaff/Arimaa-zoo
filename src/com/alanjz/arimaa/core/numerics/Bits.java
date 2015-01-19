package com.alanjz.arimaa.core.numerics;

public final class Bits {
    private static final int[] bitScanForwardIndex = new int[]{
        0, 1, 48, 2, 57, 49, 28, 3,
        61, 58, 50, 42, 38, 29, 17, 4,
        62, 55, 59, 36, 53, 51, 43, 22,
        45, 39, 33, 30, 24, 18, 12, 5,
        63, 47, 56, 27, 60, 41, 37, 16,
        54, 35, 52, 21, 44, 32, 23, 11,
        46, 26, 40, 15, 34, 20, 31, 10,
        25, 14, 19, 9, 13, 8, 7, 6
    };
    private static final long deBruijn64 = 0x03f79d71b4cb0a89l;
    /**
     * @author Martin Läuter (1997)
     * bitScanForward
     * Charles E. Leiserson
     * Harald Prokop
     * Keith H. Randall
     * "Using de Bruijn Sequences to Index a 1 in a Computer Word"
     * @param bb bitboard to scan
     * @return index (0..63) of least significant one bit
     */
    public static int bitScanForward(long bb) {
        if(bb == 0) return -1;
        return bitScanForwardIndex[(int) (((bb & -bb) * deBruijn64) >>> 58)];
    }
    private static int[] populationCount = null;
    public static int populationCount(long bb) {
        if(populationCount == null) {
            populationCount = new int[256];
            populationCount[0] = 0;
            for (int i = 1; i < 256; i++) {
                populationCount[i] = populationCount[i / 2] + (i & 1);
            }
        }
        return populationCount[ (int)(bb & 0xff)] +
                populationCount[(int)((bb >>>  8) & 0xff)] +
                populationCount[(int)((bb >>> 16) & 0xff)] +
                populationCount[(int)((bb >>> 24) & 0xff)] +
                populationCount[(int)((bb >>> 32) & 0xff)] +
                populationCount[(int)((bb >>> 40) & 0xff)] +
                populationCount[(int)((bb >>> 48) & 0xff)] +
                populationCount[(int)( bb >>> 56)];
    }
    public static final long[] fileMask = new long[]{
        72340172838076673l,
        144680345676153346l,
        289360691352306692l,
        578721382704613384l,
        1157442765409226768l,
        2314885530818453536l,
        4629771061636907072l,
        -9187201950435737472l
    };
    public static final long[] rankMask = new long[] {
        255l,
        65280l,
        16711680l,
        4278190080l,
        1095216660480l,
        280375465082880l,
        71776119061217280l,
        -72057594037927936l
    };
}
