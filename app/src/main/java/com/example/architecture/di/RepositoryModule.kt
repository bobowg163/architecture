package com.example.architecture.di

import android.content.Context
import androidx.room.Room
import com.example.architecture.data.DefaultTaskRepository
import com.example.architecture.data.TaskRepository
import com.example.architecture.data.source.local.TaskDao
import com.example.architecture.data.source.local.ToDoDatabase
import com.example.architecture.data.source.network.NetworkDataSource
import com.example.architecture.data.source.network.TaskNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.di
 * @作者 : bobo
 * @日期和时间: 2024/11/17 20:04
 **/

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: DefaultTaskRepository): TaskRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: TaskNetworkDataSource): NetworkDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ToDoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "Tasks.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: ToDoDatabase): TaskDao = database.taskDao()
}