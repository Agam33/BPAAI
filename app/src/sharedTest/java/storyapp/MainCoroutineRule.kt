package storyapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()):
    TestWatcher(),
    TestCoroutineScope by TestCoroutineScope(dispatcher) {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}

//@OptIn(ExperimentalCoroutinesApi::class)
//class MainCoroutineRule(
//    val dispatcher: TestDispatcher = StandardTestDispatcher(),
//    val testScope: TestScope = TestScope(dispatcher)
//): TestWatcher() {
//    override fun starting(description: Description?) {
//        super.starting(description)
//        Dispatchers.setMain(dispatcher)
//    }
//
//    override fun finished(description: Description?) {
//        super.finished(description)
//
//        Dispatchers.resetMain()
//    }
//}
//
//@OptIn(ExperimentalCoroutinesApi::class)
//fun MainCoroutineRule.runTesting(
//    block: suspend TestScope.() -> Unit
//) = this.testScope.runTest {
//    block()
//}

