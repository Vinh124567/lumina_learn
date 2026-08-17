package com.example.luminalearn.presentation.main

sealed class AppDestination (val route:String){
    data object Login: AppDestination("login")
    data object Main: AppDestination("main")
    data object Lesson: AppDestination("lesson")
    data object SparkAI: AppDestination("spark_ai")
    data object Reward: AppDestination("reward")
}