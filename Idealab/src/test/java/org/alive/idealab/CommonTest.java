package org.alive.idealab;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommonTest {

    @Test
    public void testArrayInit() {
        int[] data = new int[2];
        Assertions.assertArrayEquals(new int[] {0 , 0}, data);
    }
}
