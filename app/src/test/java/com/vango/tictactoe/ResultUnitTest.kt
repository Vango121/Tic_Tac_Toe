package com.vango.tictactoe

import com.vango.tictactoe.ui.singleGame.SingleGameViewModel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ResultUnitTest {
    val singleGameViewModel: SingleGameViewModel = SingleGameViewModel()
    @Test
    fun test_isWon() {
        val list = mutableListOf<String>("0","1","2")
        val list1 = mutableListOf<String>("3","4","5")
        val list3 = mutableListOf<String>("6","7","8")
        val list4 = mutableListOf<String>("0","2","4")
        val list5 = mutableListOf<String>("1","0","2")
        assertTrue(singleGameViewModel.checkIfWon(list))
        assertTrue(singleGameViewModel.checkIfWon(list1))
        assertTrue(singleGameViewModel.checkIfWon(list3))
        assertFalse(singleGameViewModel.checkIfWon(list4))
        assertTrue(singleGameViewModel.checkIfWon(list5))
    }
}