package ru.soldatov.android.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.soldatov.android.composition.R
import ru.soldatov.android.composition.data.GameRepositoryImpl
import ru.soldatov.android.composition.domain.entity.GameResult
import ru.soldatov.android.composition.domain.entity.GameSettings
import ru.soldatov.android.composition.domain.entity.Level
import ru.soldatov.android.composition.domain.entity.Question
import ru.soldatov.android.composition.domain.usecases.GenerateQuestionUseCase
import ru.soldatov.android.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings

    private val repository = GameRepositoryImpl
    private var timer: CountDownTimer? = null

    private val getGameSettingUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private var countRightOfAnswers = 0
    private var countOfQuestions = 0

    private var _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private var _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var _percentOfRightAnswer = MutableLiveData<Int>()
    val percentOfRightAnswer: LiveData<Int>
        get() = _percentOfRightAnswer

    private var _progressAnswer = MutableLiveData<String>()
    val progressAnswer: LiveData<String>
        get() = _progressAnswer

    private var _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private var _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private var _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private var _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    fun startGame(level: Level) {
        getGameSetting(level)
        startTimer()
        generateQuestion()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRight()
        _percentOfRightAnswer.value = percent
        _progressAnswer.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfQuestions,
            gameSettings.minCountOfRightAnswers
        )
        _enoughCount.value = countRightOfAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswer
    }

    private fun calculatePercentOfRight(): Int {
        return ((countRightOfAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightsAnswer = question.value?.rightAnswer
        if (number == rightsAnswer) {
            countRightOfAnswers++
        }
        countOfQuestions++
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswer = countRightOfAnswers,
            countOfQuestions = countOfQuestions,
            gameSettings = gameSettings
        )
    }

    private fun getGameSetting(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswer
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTimer(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    private fun formatTimer(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECOND_IN_MINUTES
        val leftSecond = seconds - (minutes * SECOND_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSecond)
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECOND_IN_MINUTES = 60
    }

}