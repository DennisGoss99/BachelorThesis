import cga.framework.foldChunkedParallel
import cga.framework.foreachParallelIndexed
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.system.measureTimeMillis

class ParallelListExtensionTest {

    @Test
    fun parallelForEachTest(){
        runBlocking {
                val numbers = mutableListOf<Int>()

                repeat(1000000) { numbers.add(it) }

                val numbersParallel = numbers.toMutableList()

                println(
                    measureTimeMillis {
                        numbers.forEachIndexed() { index, i ->
                            numbers[index] = index + 5
                        }
                    }

                )
                println(
                    measureTimeMillis {
                        numbersParallel.foreachParallelIndexed(33) { i, index->
                            numbersParallel[index] = index + 5
                        }
                    }
                )
            assertEquals(numbers, numbersParallel)
        }

    }

    fun isPrime(n: Int): Boolean {
        // Corner case
        if (n <= 1) return false

        // Check from 2 to n-1
        for (i in 2 until n) if (n % i == 0) return false
        return true
    }

    @Test
    fun parallelFoldTest(){
        runBlocking {

            val numbers = mutableListOf<Int>()

            repeat(30000){ numbers.add(it) }

            var primeNumbersCount = listOf<Int>()
            var primeNumbersCountParallel = listOf<Int>()

            println(
                measureTimeMillis {
                    primeNumbersCount = numbers.fold(mutableListOf()){
                            acc, num ->
                        if (isPrime(num))
                            acc.add(num)
                        acc
                    }
                }
            )
            println(
                measureTimeMillis {
                    primeNumbersCountParallel = numbers.foldChunkedParallel(40, { mutableListOf<Int>() }){
                            acc, num ->
                        if (isPrime(num))
                            acc.add(num)
                        acc
                    }.fold(listOf()){ acc, item -> acc + item }
                }
            )

            assertEquals(primeNumbersCount, primeNumbersCountParallel)
        }
    }

    @Test
    fun parallelFoldTest2(){
        runBlocking {

            val chars = mutableListOf<Char>()
            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            repeat(5000){ chars.add(charPool[Random.nextInt(0, charPool.size)]) }

            var string = ""
            var stringParallel = ""

            println(
                measureTimeMillis {
                    string = chars.fold("") { acc, c -> acc + c}
                }
            )

            println(
                measureTimeMillis {
                    stringParallel = chars.foldChunkedParallel(40, { "" }){
                            acc, c -> acc + c
                    }.fold("") { acc, c -> acc + c}
                }
            )
            assertEquals(string, stringParallel)
        }
    }












}