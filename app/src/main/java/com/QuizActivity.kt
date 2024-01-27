package com

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityQuizBinding
import com.example.quizzy.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener{

      companion object{
          var questionModelList : List<QuestionModel> = listOf()
          var timer : String =""
      }

    lateinit var binding : ActivityQuizBinding

    var currentQuestionIndex = 0;
    var selectedAnswer = ""
    var score = 0 ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
        loadQuestion()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis = timer.toInt() *60*1000L
        object : CountDownTimer(totalTimeInMillis,1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished/1000
                val minutes = seconds/60
                val remainingSecconds = seconds  % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d",minutes,remainingSecconds)
            }

            override fun onFinish() {

            }

        }.start()
    }

    private fun finishQuiz(){
        val totalQuestions = questionModelList.size
        val percentage = (score.toFloat() / totalQuestions.toFloat() * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if(percentage>60){
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.CYAN)
            }else{
            scoreTitle.text = "Oops! You have failed"
            scoreTitle.setTextColor(Color.RED)
        }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                finish()

            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }

    private fun loadQuestion(){
        selectedAnswer=""

        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }
        binding.apply{

            questionIndicatorTextview.text = "Question ${currentQuestionIndex+1}/${questionModelList.size}"
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat()/ questionModelList.size.toFloat()*100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view : View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.pink))
            btn1.setBackgroundColor(getColor(R.color.pink))
            btn2.setBackgroundColor(getColor(R.color.pink))
            btn3.setBackgroundColor(getColor(R.color.pink))
        }
        val clickedBtn = view as Button
        if(clickedBtn.id== R.id.next_btn) {
            if(selectedAnswer== questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Score of quiz ",score.toString())
            }
            currentQuestionIndex++
            loadQuestion()
    }else{
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.lilac))

        }

    }

}