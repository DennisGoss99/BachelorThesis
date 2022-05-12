package cga.framework

import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
suspend fun <T> List<T>.foreachParallel(jobCount : Int, predicate : ((T)->Unit)) {
    this.foreachParallelIndexed(jobCount) { t: T, _: Int -> predicate(t) }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun <T> List<T>.foreachParallelIndexed(jobCount : Int, predicate : ((T, index: Int)->Unit)) {
    val items = this
    val jobs = mutableListOf<Job>()

    val chunkSize = items.size / jobCount
    val remains = items.size - (chunkSize * jobCount)

    for(jobIndex in 0 until jobCount){
        jobs.add(GlobalScope.launch(){
            for(index in jobIndex * chunkSize until (jobIndex + 1) * chunkSize + if(jobIndex != jobCount-1) 0 else remains){
                predicate(items[index], index)
            }
        })
    }
    jobs.joinAll()
}

