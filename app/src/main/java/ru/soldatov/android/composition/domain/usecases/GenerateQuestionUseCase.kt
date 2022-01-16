package ru.soldatov.android.composition.domain.usecases

import ru.soldatov.android.composition.domain.entity.Question
import ru.soldatov.android.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    companion object {

        private const val COUNT_OF_OPTIONS = 6

    }

}