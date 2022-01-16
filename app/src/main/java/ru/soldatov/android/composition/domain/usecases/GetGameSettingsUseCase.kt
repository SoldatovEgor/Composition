package ru.soldatov.android.composition.domain.usecases

import ru.soldatov.android.composition.domain.entity.GameSettings
import ru.soldatov.android.composition.domain.entity.Level
import ru.soldatov.android.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }

}