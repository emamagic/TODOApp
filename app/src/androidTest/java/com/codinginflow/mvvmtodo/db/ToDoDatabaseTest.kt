package com.codinginflow.mvvmtodo.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codinginflow.mvvmtodo.TestCoroutineRule
import com.codinginflow.mvvmtodo.db.entity.Task
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToDoDatabaseTest {

    private lateinit var db: ToDoDatabase
    private lateinit var dao: ToDoDao

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext() ,ToDoDatabase::class.java)
                .setTransactionExecutor(testDispatcher.asExecutor())
                .setQueryExecutor(testDispatcher.asExecutor())
                .build()
        dao = db.getDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertTask() = runBlocking {
        val task = Task("ali" ,important = false ,completed = false)
        val result = dao.insert(task)
        assertThat(result).isEqualTo(1)
    }

//    @Test
//    fun deleteTask() = testScope.runBlockingTest {
//        val task = Task("ali" ,important = false ,completed = false)
//        dao.insert(task)
//        dao.delete(task)
//        val result = dao.getTasks().toList()
//        assertThat(result).doesNotContain(task)
//    }


    @Test
    fun updateTask() = runBlockingTest {
        val task = Task("ali" ,important = false ,completed = false)
        val task2 = Task("reza" ,important = false ,completed = false)
        dao.insert(task)
        dao.update(task2)

    }

}