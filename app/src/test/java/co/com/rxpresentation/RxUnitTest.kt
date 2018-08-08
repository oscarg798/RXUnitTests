package co.com.rxpresentation

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Test

class RxUnitTest {


    @Test
    fun shouldEmitANumber() {
        Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onComplete()
        }.subscribe({
            println("Number $it")
            Assert.assertEquals(1, it)
        }, {
            println("Error $it")
        }, {
            println("On complete called")
        })
    }

    @Test
    fun shouldEmitThreeNumbers() {

        Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onNext(3)
            emitter.onComplete()
            emitter.onNext(4)

        }.subscribe({
            println("Number $it")
        }, {
            println("Error $it")
        }, {
            println("On complete called")
            Assert.assertTrue(true)
        })
    }

    @Test
    fun shouldEmitAnError() {
        Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onError(NullPointerException())
            emitter.onComplete()
            emitter.onNext(2)

        }.subscribe({
            println("Number $it")
        }, {
            println("Error $it")
            Assert.assertTrue(it is NullPointerException)
        }, {
            println("On complete called")
        })
    }

    @Test
    fun shouldEmitItems() {
        val testObserver = TestObserver<Int>()
        Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onComplete()

        }.doOnNext(::println).subscribe(testObserver)

        testObserver.assertValue(1)
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun shouldEmitItemsFromListTimesTwo() {
        val testObserver = TestObserver<Int>()
        Observable.fromIterable(arrayListOf(1, 2, 3, 4))
                .map { it * 2 }
                .doOnNext(::println)
                .subscribe(testObserver)

        testObserver.assertValues(2, 4, 6, 8).assertNoErrors().assertComplete()
    }

    @Test
    fun shouldEmitAListOfStringsFromOneString() {
        val testObserver = TestObserver<List<String>>()
        Observable.just("Hello UPB", "We are Condor Labs")
                .flatMap {
                    Observable.just(it.split(" "))
                }
                .doOnNext(::println)
                .subscribe(testObserver)

        testObserver.assertNoErrors().assertComplete()
    }

    @Test
    fun shouldEmitOddNumbers() {
        val testObserver = TestObserver<Int>()
        Observable.fromIterable(arrayListOf(1, 2, 3, 4, 5, 6, 7))
                .filter { it % 2 != 0 }
                .doOnNext(::println)
                .subscribe(testObserver)

        testObserver.assertValues(1, 3, 5, 7).assertNoErrors().assertComplete()
    }

    @Test
    fun shouldMultiplyObservables() {
        val testObserver = TestObserver<Int>()
        val observable1 = Observable.fromIterable(arrayListOf(1, 2, 3))
        val observable2 = Observable.fromIterable(arrayListOf(5, 6, 7))

        Observable.zip(observable1, observable2, BiFunction<Int, Int, Int> { n1, n2 ->
            n1 * n2
        }).doOnNext(::println)
                .subscribe(testObserver)

        testObserver.assertValues(5, 12, 21).assertNoErrors().assertComplete()
    }

    @Test
    fun shouldConcat() {
        val testObserver = TestObserver<Int>()
        val observable1 = Observable.fromIterable(arrayListOf(1, 2, 3))
        val observable2 = Observable.fromIterable(arrayListOf(5, 6, 7))

        Observable.concat(observable1, observable2)
                .doOnNext(::println)
                .subscribe(testObserver)

        testObserver.assertValues(1, 2, 3, 5, 6, 7).assertNoErrors().assertComplete()

    }

    @Test
    fun shouldFailWithAlphaNumericString() {
        Assert.assertFalse("O1o1".isNumeric())
    }

    @Test
    fun shouldNotFailWithNumericString() {
        Assert.assertTrue("123231".isNumeric())
    }

    fun String.isNumeric(): Boolean {
        return !contains("\\D".toRegex())

    }

    @Test
    fun shouldReturnLastIndexOfAThreeElementIntList() {
        Assert.assertEquals(2, arrayListOf(1, 2, 3).lastIndex)
    }

    @Test
    fun shouldReturnLastIndexOfAThreeElementStringList() {
        Assert.assertEquals(2, arrayListOf("A", "B", "C").lastIndex)
    }


    private val <T> List<T>.lastIndex
        get() = size - 1

    @Test
    fun shouldGetLastLetterFromHello() {
        Assert.assertEquals("o", "o".lastLetter)
    }

    @Test
    fun shouldNotGetLastLetterFromAnEmptyString() {
        val myString: String? = ""
        Assert.assertNull(myString.lastLetter)
    }


    private val String?.lastLetter
        get() = if (this?.isNotEmpty() == true) get(length - 1).toString() else null


    interface RoundShape {
        var radius: Long

        fun getArea(): Long = radius * radius
    }

    class Circle(override var radius: Long) : RoundShape {

        override fun getArea(): Long {
            return super.getArea() * 3.1416.toLong()
        }
    }

    class Ellipse(override var radius: Long,
                  val height: Long) : RoundShape
    {
        override fun getArea(): Long {
            return radius * height * 3.1416.toLong()
        }
    }


}
