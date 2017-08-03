package com.example.shokiterashita.learningmaterial.views.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.timer


import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.lib.manager.LessonMaterialManager

import android.util.Log
import com.example.shokiterashita.learningmaterial.views.lib.manager.TOEICFlash600Word


@Suppress("UNUSED_EXPRESSION")
class LearningMaterialTestFragment : Fragment() {

    lateinit var testTitleTextView: TextView
    lateinit var currentTestNumberTextView: TextView
    lateinit var overallTestNumberTextView: TextView
    lateinit var testWordTextView: TextView

    lateinit var choiceAButton: Button
    lateinit var choiceBButton: Button
    lateinit var choiceCButton: Button
    lateinit var initTestContent : TOEICFlash600Word
    lateinit var wordJP: CharSequence
    var countTime = 0
    val limitTime = 500
    val handler = Handler()

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LessonMaterialManager.setup(context)

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_learning_material_test, container, false)
        initTestContent = LessonMaterialManager.fetchTestList(2) //DEMO: テスト選択画面にて、テスト２が選択されました。

        wordJP = initTestContent.wordjp.toString()

        testTitleTextView = view.findViewById(R.id.material_test_title)!!
        currentTestNumberTextView = view.findViewById(R.id.currentTestNumber)
        overallTestNumberTextView = view.findViewById(R.id.overallTestNumber)
        testWordTextView = view.findViewById(R.id.testWord)

        choiceAButton = view.findViewById(R.id.choice_a)
        choiceBButton = view.findViewById(R.id.choice_b)
        choiceCButton = view.findViewById(R.id.choice_c)

        testWordTextView.text = initTestContent.worden
        choiceAButton.text = initTestContent.wordjp
        choiceBButton.text = initTestContent.option_1
        choiceCButton.text = initTestContent.option_2

        main("うんこ")


        choiceAButton.setOnClickListener {
            checkAnswer(choiceAButton.text)
        }
        choiceBButton.setOnClickListener {
            checkAnswer(choiceBButton.text)
        }
        choiceCButton.setOnClickListener {
            checkAnswer(choiceCButton.text)
        }

        fun timer(
                name: String? = "countDown",
                daemon: Boolean = false,
                initialDelay: Long = 0.toLong(),
                period: Long,
                action: Unit
        ) {
            timer("countDown", false, 0, 5000, showNextTest(LessonMaterialManager.nextQuestion()))

        }



        return view
    }



    override fun onDetach() {
        super.onDetach()
    }

    fun checkAnswer(answerText: CharSequence){//ボタンタップ時に、呼ばれる。->時間切の場合、呼ばれない。
        if (answerText == wordJP){
            correct()
        } else {
            inCorrect()
        }
    }

    fun correct(){
        Log.d("答えは","正解です")
        //瞬間回答・通常回答のロジック
        showNextTest(LessonMaterialManager.nextQuestion())

    }
    fun inCorrect(){
        Log.d("答えは","不正解です")
        //不正解のロジック
        showNextTest(LessonMaterialManager.nextQuestion())

    }

    fun main(args: String) {
        timer(initialDelay = 0, period = 5000) {
            showNextTest(LessonMaterialManager.nextQuestion())
        }
    }

    fun showNextTest(testContent:TOEICFlash600Word){
        testWordTextView.text = testContent.worden
        choiceAButton.text = testContent.wordjp
        choiceBButton.text = testContent.option_1
        choiceCButton.text = testContent.option_2
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}

