package cga.framework

import kotlinx.coroutines.*

suspend inline fun Array<Job?>.joinAll() = this.forEach { it?.join() }

@OptIn(DelicateCoroutinesApi::class)
suspend fun <T> List<T>.foreachParallel(jobCount : Int, predicate : ((T)->Unit)) {
    this.foreachParallelIndexed(jobCount) { t: T, _: Int -> predicate(t) }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun <T> List<T>.foreachParallelIndexed(jobCount : Int, predicate : ((T, index: Int)->Unit)) {
    val items = this
    val jobs = Array<Job?>(jobCount){null}

    val chunkSize = items.size / jobCount
    val remains = items.size - (chunkSize * jobCount)

    for(jobIndex in 0 until jobCount){
        jobs[jobIndex] = GlobalScope.launch(){
            for(index in jobIndex * chunkSize until (jobIndex + 1) * chunkSize + if(jobIndex != jobCount-1) 0 else remains){
                predicate(items[index], index)
            }
        }
    }
    jobs.joinAll()
}

@OptIn(DelicateCoroutinesApi::class)
suspend inline fun <T, reified R> List<T>.foldChunkedParallel(jobCount : Int, crossinline initialChunk : (()-> R), crossinline operation : ((acc : R, T)->R)) : Array<R> {
    val items = this
    val jobs = Array<Job?>(jobCount){null}

    val chunkSize = items.size / jobCount
    val remains = items.size - (chunkSize * jobCount)

    val accumulatorResults = Array<R>(jobCount){initialChunk()}

    for(jobIndex in 0 until jobCount){
        jobs[jobIndex] = GlobalScope.launch(){
            var accumulator = initialChunk()

                for(index in jobIndex * chunkSize until (jobIndex + 1) * chunkSize + if(jobIndex != jobCount-1) 0 else remains){
                    accumulator = operation(accumulator, items[index])
                }

            accumulatorResults[jobIndex] = accumulator!!
        }
    }
    jobs.joinAll()

    return accumulatorResults
}



