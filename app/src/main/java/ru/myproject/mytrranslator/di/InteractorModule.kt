package ru.myproject.mytrranslator.di

import dagger.Module
import dagger.Provides
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.model.repository.Repository
import ru.myproject.mytrranslator.view.main.MainInteractor
import javax.inject.Named

@Module
class InteractorModule {

    @Provides
    internal fun provideInteractor(
        @Named(NAME_REMOTE) repositoryRemote: Repository<List<DataModel>>,
        @Named(NAME_LOCAL) repositoryLocal: Repository<List<DataModel>>
    ) = MainInteractor(repositoryRemote, repositoryLocal)
}