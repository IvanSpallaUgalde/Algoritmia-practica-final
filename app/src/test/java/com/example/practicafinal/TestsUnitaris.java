package com.example.practicafinal;

import static com.example.practicafinal.data.WordDictionary.esParaulaSolucio;

import org.junit.Test;

import static org.junit.Assert.*;
import com.example.practicafinal.data.WordDictionary;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class TestsUnitaris {

    @Test
    public void ParaulaSolucioIsCorrect() {
        assertTrue(esParaulaSolucio("puresa", "apres"));

        assertFalse(esParaulaSolucio("puresa", "apresa"));

        assertTrue(esParaulaSolucio("copta", "coa"));

        assertTrue(esParaulaSolucio("saca", "casa"));

        assertTrue(esParaulaSolucio("feiner", "ene"));

        assertTrue(esParaulaSolucio("nado", "dona"));

        assertFalse(esParaulaSolucio("nimfa", "fama"));
    }
}