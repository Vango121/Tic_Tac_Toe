package com.vango.tictactoe

import com.vango.tictactoe.ui.singleGame.SingleGameViewModel
import org.junit.Assert
import org.junit.Test

class AiUnitTest {
    @Test
    fun boardTesting(){

        Assert.assertEquals(2,GFG.findBestMove(arrayOf(
            charArrayOf('x', 'o', 'x'),
            charArrayOf('o', 'o', 'x'),
            charArrayOf('_', '_', '_')
        )).row)

    }
    @Test
    fun level2Test(){
        val singleGameViewModel = SingleGameViewModel()
        val list = listOf("0","1")
        val list1 = listOf("0","6")
        val list2 = listOf("0","4","5")
//        Assert.assertEquals(2,singleGameViewModel.level2Move(list as MutableList<String>))
//        Assert.assertEquals(3,singleGameViewModel.level2Move(list1 as MutableList<String>))
//        Assert.assertEquals(8,singleGameViewModel.level2Move(list2 as MutableList<String>))
    }
    @Test
    fun level3Test(){
        val singleGameViewModel = SingleGameViewModel()
        val list = listOf("0","4","5","7")
        val list1 = listOf("2","3","8")
        val list2 = listOf("0","4","5")
        //Assert.assertEquals(1,singleGameViewModel.level3Move()

    }
}