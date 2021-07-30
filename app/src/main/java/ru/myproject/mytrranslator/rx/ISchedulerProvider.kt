package ru.myproject.mytrranslator.rx

import io.reactivex.Scheduler

// Для тестирования
interface ISchedulerProvider {

    fun ui(): Scheduler

    fun io(): Scheduler
}