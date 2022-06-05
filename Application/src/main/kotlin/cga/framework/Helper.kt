package cga.framework

import kotlin.system.measureTimeMillis

inline fun printTimeMillis(message : String = "", block: () -> Unit){
    print("$message ${measureTimeMillis(block)} ms")
}

inline fun printTimeNano(message : String = "", block: () -> Unit){
    print("$message ${measureTimeMillis(block)} ns")
}

inline fun printlnTimeMillis(message : String = "", block: () -> Unit){
    println("$message ${measureTimeMillis(block)} ms")
}

inline fun printlnTimeNano(message : String = "", block: () -> Unit){
    println("$message ${measureTimeMillis(block)} ns")
}