package ru.soldatov.android.composition.domain.repository

import ru.soldatov.android.composition.domain.entity.GameSettings
import ru.soldatov.android.composition.domain.entity.Level
import ru.soldatov.android.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings

}
