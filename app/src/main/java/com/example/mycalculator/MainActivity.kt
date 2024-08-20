package com.example.mycalculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var expression = ""
    private var tvTopAns : TextView? = null
    private var tvBottomAns : TextView? = null
    private var lastCalculation = "0"
    private var isEqualClicked = false


    private fun evaluate(str: String): Double {

        data class Data(val rest: List<Char>, val value: Double)

        return object : Any() {

            fun parse(chars: List<Char>): Double {
                return getExpression(chars.filter { it != ' ' })
                    .also { if (it.rest.isNotEmpty()) throw RuntimeException("Unexpected character: ${it.rest.first()}") }
                    .value
            }

            private fun getExpression(chars: List<Char>): Data {
                var (rest, carry) = getTerm(chars)
                while (true) {
                    when {
                        rest.firstOrNull() == '+' -> rest = getTerm(rest.drop(1)).also { carry += it.value }.rest
                        rest.firstOrNull() == '-' -> rest = getTerm(rest.drop(1)).also { carry -= it.value }.rest
                        else                      -> return Data(rest, carry)
                    }
                }
            }

            private fun getTerm(chars: List<Char>): Data {
                var (rest, carry) = getFactor(chars)
                while (true) {
                    when {
                        rest.firstOrNull() == '*' -> rest = getTerm(rest.drop(1)).also { carry *= it.value }.rest
                        rest.firstOrNull() == '/' -> rest = getTerm(rest.drop(1)).also { carry /= it.value }.rest
                        else                      -> return Data(rest, carry)
                    }
                }
            }

            private fun getFactor(chars: List<Char>): Data {
                return when (val char = chars.firstOrNull()) {
                    '+'              -> getFactor(chars.drop(1)).let { Data(it.rest, +it.value) }
                    '-'              -> getFactor(chars.drop(1)).let { Data(it.rest, -it.value) }
                    '('              -> getParenthesizedExpression(chars.drop(1))
                    in '0'..'9', '.' -> getNumber(chars) // valid first characters of a number
                    else             -> throw RuntimeException("Unexpected character: $char")
                }
            }

            private fun getParenthesizedExpression(chars: List<Char>): Data {
                return getExpression(chars)
                    .also { if (it.rest.firstOrNull() != ')') throw RuntimeException("Missing closing parenthesis") }
                    .let { Data(it.rest.drop(1), it.value) }
            }

            private fun getNumber(chars: List<Char>): Data {
                val s = chars.takeWhile { it.isDigit() || it == '.' }.joinToString("")
                return Data(chars.drop(s.length), s.toDouble())
            }

        }.parse(str.toList())

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tvOne = findViewById<Button>(R.id.tvOne)
        val tvTwo = findViewById<Button>(R.id.tvTwo)
        val tvThree = findViewById<Button>(R.id.tvThree)
        val tvFour = findViewById<Button>(R.id.tvFour)
        val tvFive = findViewById<Button>(R.id.tvFive)
        val tvSix = findViewById<Button>(R.id.tvSix)
        val tvSeven = findViewById<Button>(R.id.tvSeven)
        val tvEight = findViewById<Button>(R.id.tvEight)
        val tvNine = findViewById<Button>(R.id.tvNine)
        val tvZero = findViewById<Button>(R.id.tvZero)

        val tvDecimal = findViewById<Button>(R.id.tvDecimal)
        val tvEqualTo = findViewById<Button>(R.id.tvEqualTo)
        val tvAdd = findViewById<Button>(R.id.tvAdd)
        val tvSubtract = findViewById<Button>(R.id.tvSubtract)
        val tvMultiple = findViewById<Button>(R.id.tvMultiple)
        val tvDivide = findViewById<Button>(R.id.tvDivide)
        val tvPercentage = findViewById<Button>(R.id.tvPercentage)
        val tvClear = findViewById<Button>(R.id.tvClear)
        val tvDelete = findViewById<Button>(R.id.tvDelete)

        tvTopAns = findViewById<TextView>(R.id.tvTopAns)
        tvBottomAns = findViewById<TextView>(R.id.tvBottomAns)

        tvOne.setOnClickListener {

            expression += "1"
            displayTopAns()
            displayBottomAns()

        }

        tvTwo.setOnClickListener {

            expression += "2"
            displayTopAns()
            displayBottomAns()


        }

        tvThree.setOnClickListener {

            expression += "3"
            displayTopAns()
            displayBottomAns()

        }

        tvFour.setOnClickListener {

            expression += "4"
            displayTopAns()
            displayBottomAns()

        }

        tvFive.setOnClickListener {

            expression += "5"
            displayTopAns()
            displayBottomAns()

        }

        tvSix.setOnClickListener {

            expression += "6"
            displayTopAns()
            displayBottomAns()

        }

        tvSeven.setOnClickListener {

            expression += "7"
            displayTopAns()
            displayBottomAns()

        }

        tvEight.setOnClickListener {

            expression += "8"
            displayTopAns()
            displayBottomAns()

        }

        tvNine.setOnClickListener {

            expression += "9"
            displayTopAns()
            displayBottomAns()

        }

        tvZero.setOnClickListener {

            expression += "0"
            displayTopAns()
            displayBottomAns()

        }

        tvDecimal.setOnClickListener {

            expression += "."
            displayTopAns()
        }

        tvClear.setOnClickListener {

            expression = ""
            displayTopAns()
            displayBottomAns()

        }

        tvDelete.setOnClickListener {

            if(expression.length > 1){
                expression = expression.substring(0,expression.length - 1)
                if(expression[expression.length - 1].isDigit()){
                    displayBottomAns()
                }
            }
            else if(expression.length == 1){
                expression = ""
                tvBottomAns?.text = ""
            }
            displayTopAns()


        }

        tvPercentage.setOnClickListener {

            if(isOperator(expression.elementAt(expression.length - 1))){
                expression = expression.substring(0,expression.length-1)
            }

            expression += "."
            displayTopAns()
        }

        tvDivide.setOnClickListener {

            if(isEqualClicked){
                expression = lastCalculation
                tvTopAns?.textSize = 45f
                tvBottomAns?.textSize = 25f
            }

            if(isOperator(expression.elementAt(expression.length - 1))){
                expression = expression.substring(0,expression.length-1)
            }

            expression += "/"
            displayTopAns()

            isEqualClicked = false
//            Toast.makeText(this,lastCalculation,Toast.LENGTH_LONG).show()


        }

        tvMultiple.setOnClickListener {

            if(isOperator(expression.elementAt(expression.length - 1))){
                expression = expression.substring(0,expression.length-1)
            }

            if(isEqualClicked){
                expression = lastCalculation
                tvTopAns?.textSize = 45f
                tvBottomAns?.textSize = 25f
            }

            expression += "*"
            displayTopAns()


            isEqualClicked = false
//            Toast.makeText(this,lastCalculation,Toast.LENGTH_LONG).show()

        }

        tvSubtract.setOnClickListener {

            if(isOperator(expression.elementAt(expression.length - 1))){
                expression = expression.substring(0,expression.length-1)
            }

            if(isEqualClicked){
                expression = lastCalculation
                tvTopAns?.textSize = 45f
                tvBottomAns?.textSize = 25f
            }

            expression += "-"
            displayTopAns()

            isEqualClicked = false

//            Toast.makeText(this,lastCalculation,Toast.LENGTH_LONG).show()
        }

        tvAdd.setOnClickListener {

            if(isOperator(expression.elementAt(expression.length - 1))){
                expression = expression.substring(0,expression.length-1)
            }

            if(isEqualClicked){
                expression = lastCalculation
                tvTopAns?.textSize = 45f
                tvBottomAns?.textSize = 25f
            }

            expression += "+"
            displayTopAns()


            isEqualClicked = false
//            Toast.makeText(this,lastCalculation,Toast.LENGTH_LONG).show()

        }

        tvEqualTo.setOnClickListener {


            tvTopAns?.textSize = 25.0f
            tvBottomAns?.textSize = 45.0f


            if(expression[expression.length - 1].isDigit()){

                lastCalculation = if(expression.contains(".") || expression.contains("/")){

                    evaluate(expression).toString()
                } else{
                    evaluate(expression).toLong().toString()
                }

            }
            else{

                expression = expression.substring(0,expression.length - 1)
                lastCalculation = evaluate(expression).toString()

            }
            displayBottomAns()

            isEqualClicked = true

        }

    }

    private fun displayTopAns(){
        if (expression.isEmpty()){
            tvTopAns?.text = "0"
        }
        else{
            tvTopAns?.text =  expression
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayBottomAns(){

        if(expression.isEmpty()) {
            tvBottomAns?.text = ""
        }
        else if(expression.contains(".") || expression.contains("/")){
            tvBottomAns?.text = "= " + Math.round(evaluate(expression) * 10.0) / 10.0
        }
        else {
            tvBottomAns?.text = "= " + evaluate(expression).toLong().toString()
        }

    }

    fun isOperator(ope : Char) : Boolean{
        return (ope == '+' || ope == '-' || ope == '*' || ope == '/' || ope == '%')
    }


}