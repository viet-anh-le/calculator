package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), OnClickListener {
    lateinit var textView : TextView
    lateinit var smallView : TextView
    var currentInput : String = ""
    var lastOperator : Char? = null
    var firstOperand : Double? = null
    var checkNewExp : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.tvDisplay)
        smallView = findViewById(R.id.small_view)

        val btnIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnCE, R.id.btnC, R.id.btnBS, R.id.btnDot, R.id.btnPlusMinus, R.id.btnEqual
        )

        for (id in btnIds){
            findViewById<Button>(id).setOnClickListener(this);
        }
    }

    override fun onClick(v: View?) {
        v as Button
        when(v.id){
            R.id.btnCE -> handleCE()
            R.id.btnC -> handleClear()
            R.id.btnBS -> handleBS()
            R.id.btnPlusMinus -> toggleSign()
            R.id.btnEqual -> calculate()
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide -> handleOperator(v.text[0])
            else -> handleNumber(v.text.toString())
        }
    }

    fun handleNumber(number : String){
        if (checkNewExp){
            handleClear()
            checkNewExp = false;
        }
        if (currentInput.length < 10){
            currentInput += number;
            updateDisplay();
        }
    }

    fun updateDisplay(){
        textView.text = if (currentInput.isEmpty()) "0" else currentInput
    }

    fun handleOperator(operator: Char){
        if (currentInput.length != 0){
            checkNewExp = false
            firstOperand = currentInput.toDouble()
            lastOperator = operator
            currentInput = ""
            if (firstOperand!!.compareTo(firstOperand!!.toLong()) == 0){
                smallView.text = String.format("${firstOperand!!.toLong()} ${operator}")
            }
            else smallView.text = String.format("${firstOperand} ${operator}")
        }
    }

    fun handleCE(){
        currentInput =  ""
        textView.text = "0"
        if (lastOperator == null) smallView.text = ""
    }

    fun handleClear(){
        currentInput = ""
        firstOperand = null
        lastOperator = null
        textView.text = "0"
        smallView.text = ""
    }

    fun handleBS(){
        if (currentInput.isNotEmpty()){
            currentInput = currentInput.dropLast(1)
            updateDisplay()
        }
    }

    fun toggleSign(){
        checkNewExp = false
        currentInput = if (currentInput.length != 0 && currentInput[0] == '-') currentInput.substring(1) else "-${currentInput}"
        updateDisplay()
    }

    fun calculate(){
        if (firstOperand != null && currentInput.length != 0){
            val secondOperand = currentInput.toDouble();
            smallView.text = String.format("${smallView.text} ${textView.text} =")
            val result = when (lastOperator){
                '+' -> firstOperand!! + secondOperand
                '-' -> firstOperand!! - secondOperand
                'x' -> firstOperand!! * secondOperand
                '/' -> firstOperand!! / secondOperand
                else -> return
            }

            if (result.compareTo(result.toLong()) == 0){
                textView.text = String.format(result.toLong().toString())
                currentInput = result.toLong().toString()
            }
            else{
                textView.text = String.format(result.toString())
                currentInput = result.toString()
            }
            firstOperand = null
            lastOperator = null
            checkNewExp = true
        }
    }
}