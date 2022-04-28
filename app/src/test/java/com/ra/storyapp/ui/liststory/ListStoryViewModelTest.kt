package com.ra.storyapp.ui.liststory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.nhaarman.mockitokotlin2.verify
import com.ra.storyapp.adapter.ListStoryAdapter
import com.ra.storyapp.data.source.local.database.entity.StoryEntity
import com.ra.storyapp.domain.usecase.StoryAppUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import storyapp.DataDummy
import storyapp.MainCoroutineRule
import storyapp.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var useCase: StoryAppUseCase

    private lateinit var viewModel: ListStoryViewModel

    @Before
    fun setup() {
        viewModel = ListStoryViewModel(useCase)
    }

    @Test
    fun `should return empty data`() = runTest {
        val data = PagedTestDataSources.snapshot( ArrayList())
        val stories = MutableLiveData<PagingData<StoryEntity>>()
        stories.value = data
        `when`(useCase.getAllStories()).thenReturn(stories)

        val actualListStory = viewModel.getAllStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher,
        )
        differ.submitData(actualListStory)
        verify(useCase).getAllStories()
        assertEquals(0, differ.snapshot().size)
    }

    @Test
    fun `getAllStory(), should not empty`() = runTest {
        val dummyStories = DataDummy.listStoryEntities()
        val data = PagedTestDataSources.snapshot(dummyStories)
        val stories = MutableLiveData<PagingData<StoryEntity>>()
        stories.value = data
        `when`(useCase.getAllStories()).thenReturn(stories)

        val actualListStory = viewModel.getAllStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher,
        )
        differ.submitData(actualListStory)
        verify(useCase).getAllStories()

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `signOut, should be success`() = runTest {
        `when`(useCase.signOut()).thenReturn(Unit)
        useCase.signOut()
        verify(useCase).signOut()
    }
}

class PagedTestDataSources private constructor(private val items: List<StoryEntity>) :
    PagingSource<Int, LiveData<List<StoryEntity>>>() {

    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0 , 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}