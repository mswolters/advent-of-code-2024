import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.log10

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> {
    val file = File("src/main/resources/", "$name.txt")
    if (!file.exists()) throw IllegalArgumentException("$file missing")
    return file.readLines().apply { if (isEmpty()) throw IllegalArgumentException("$file is empty") }
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun Iterable<String>.asInts(): List<Int> = map { it.toInt() }
fun Sequence<String>.asInts(): Sequence<Int> = map { it.toInt() }

fun Iterable<String>.asLongs(): List<Long> = map { it.toLong() }
fun Sequence<String>.asLongs(): Sequence<Long> = map { it.toLong() }

fun <T> Iterable<T>.asPair(): Pair<T, T> {
    require(this.count() == 2)
    return Pair(this.first(), this.last())
}

/**
 * Like Iterable<T>.chunked, but by columns. Every list will contain every nth element of the starting element.
 */
fun <T> Iterable<T>.byNth(n: Int): List<List<T>> {
    val result = List(n) { mutableListOf<T>() }
    forEachIndexed { i: Int, t: T ->
        result[i.mod(n)].add(t)
    }
    return result
}

fun Int.pow(exponent: Int): Int = toBigInteger().pow(exponent).toInt()
fun Long.pow(exponent: Int): Long = toBigInteger().pow(exponent).toLong()

fun <A, B> Iterable<A>.cartesianProduct(other: Iterable<B>): Sequence<Pair<A, B>> = sequence {
    forEach { a ->
        other.forEach { b ->
            yield(a to b)
        }
    }
}

fun <T> List<T>.repeat(times: Int): List<T> {
    val result = mutableListOf<T>()
    repeat(times) {
        result.addAll(this)
    }
    return result
}

fun Int.readBit(index: Int): Boolean {
    return (this.shr(index) and 1) == 1
}

fun Long.readBit(index: Int): Boolean {
    return (this.shr(index) and 1L) == 1L
}

fun Int.base10Length(): Int {
    if (this == 0) return 1
    if (this < 0) return 1 + (this * -1).base10Length()
    return (log10(toDouble()) + 1).toInt()
}

fun Long.base10Length(): Int {
    if (this == 0L) return 1
    if (this < 0L) return 1 + (this * -1).base10Length()
    return (log10(toDouble()) + 1).toInt()
}
