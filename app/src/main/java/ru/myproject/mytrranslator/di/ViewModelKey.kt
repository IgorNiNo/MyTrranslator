package ru.myproject.mytrranslator.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/*
ViewModelKey помогает сопоставить классы ViewModel,
чтобы ViewModelFactory мог правильно их предоставлять / внедрять.
 */

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)

@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
