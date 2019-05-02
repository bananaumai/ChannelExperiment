package dev.bananaumai.practices.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.random.Random


fun main() = runBlocking<Unit> {
    test()
    showFIFO()
    //
    // concurrentPubSub()
}

fun CoroutineScope.test() = launch {
    println("-------------test start-------------------")

    val channel = Channel<Int>(1000)
    val random = Random.Default

    repeat(100) {
        val n = random.nextInt()
        println("before send $n")
        channel.send(n)
        println("after sent $n")
        delay(10)
        if (it == 99) {
            channel.close()
        }
    }

    var counter = 0
    for (n in channel) {
        if (counter > 0 && counter % 2 == 0) {
            delay(10)
        }
        println("received $n")
        counter += 1
    }

    println("-------------test end-------------------")
}

suspend fun showFIFO() {
    println("-------------showFIFO start-------------------")
    withContext(Dispatchers.Default) {
        val channel = Channel<Int>(1)
        val random = Random.Default

        repeat(100) {
            launch(Dispatchers.Default) {
                val n = random.nextInt()
                println("before send $n")
                channel.send(n)
                println("after sent $n")
            }
            delay(10)
            if (it == 99) {
                channel.close()
            }
        }

        launch(Dispatchers.Default) {
            var counter = 0
            for (n in channel) {
                if (counter > 0 && counter % 2 == 0) {
                    delay(10)
                }
                println("received $n")
                counter += 1
            }
        }
    }
    println("-------------showFIFO end-------------------")
}

suspend fun concurrentPubSub() {
    println("-------------concurrentPubSub start-------------------")
    coroutineScope {
        val channel = Channel<Int>(1)
        val random = Random.Default

        launch(Dispatchers.Default) {
            repeat(100) {
                val n = random.nextInt()
                println("before send $n")
                channel.send(n)
                println("after sent $n")
            }
            channel.close()
        }

        launch(Dispatchers.Default) {
            var counter = 0
            for (n in channel) {
                if (counter > 0 && counter % 2 == 0) {
                    delay(10)
                }
                println("received $n")
                counter += 1
            }
        }

    }
    println("-------------concurrentPubSub end-------------------")
}

val scope1 = CoroutineScope(Job() + Dispatchers.Default)
val scope2 = CoroutineScope(Job() + Dispatchers.Default)

suspend fun problem() {

}
