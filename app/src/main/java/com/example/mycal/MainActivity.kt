package com.example.mycal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mycal.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import kotlin.text.replace

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private var ans: Double = 0.0
    private val PI : String = "π"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        /*try {
            val scriptEngineManager = ScriptEngineManager()
            val scriptEngine = scriptEngineManager.getEngineByName("js")
            val s = "30.0^3"

            ans = scriptEngine.eval(s) as Double

            Toast.makeText(this,"$ans",Toast.LENGTH_LONG).show()

        }catch (e:ScriptException){
            showToast("Script Exception")
        }
        catch ( e : Exception){
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
        }*/

        /*try {
            val txt = "82389235"
            val numbers = txt.map { it.toString().toInt() }
            val n = "*".toInt()
//            val i = '+'.code
            Toast.makeText(this,"$n",Toast.LENGTH_SHORT).show()

        }catch (e : NumberFormatException){
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }*/


        setUpButtons();
    }

    private fun setUpButtons() {

        // Numeric values
        b.zero.setOnClickListener {
            checkForCharAndAddToStr("0")
        }

        b.one.setOnClickListener {
            checkForCharAndAddToStr("1")
        }

        b.two.setOnClickListener {
            checkForCharAndAddToStr("2")
        }

        b.three.setOnClickListener {
            checkForCharAndAddToStr("3")
        }

        b.four.setOnClickListener {
            checkForCharAndAddToStr("4")
        }

        b.five.setOnClickListener {
            checkForCharAndAddToStr("5")
        }

        b.six.setOnClickListener {
            checkForCharAndAddToStr("6")
        }

        b.seven.setOnClickListener {
            checkForCharAndAddToStr("7")
        }

        b.eight.setOnClickListener {
            checkForCharAndAddToStr("8")
        }

        b.nine.setOnClickListener {
            checkForCharAndAddToStr("9")
        }

        b.point.setOnClickListener {
            checkForCharAndAddToStr(".")
        }


        // Arithmetic operators
        b.add.setOnClickListener {
            checkForCharAndAddToStr("+")
        }

        b.sub.setOnClickListener {
            checkForCharAndAddToStr("-")
        }

        b.multiply.setOnClickListener {
            checkForCharAndAddToStr("*")
        }

        b.divide.setOnClickListener {
            checkForCharAndAddToStr("/")
        }

        b.exponent.setOnClickListener {
            checkForCharAndAddToStr("^")
        }

        b.remainder.setOnClickListener {
            checkForCharAndAddToStr("%")
        }



        // Brackets
        b.openBrac.setOnClickListener {
            checkForCharAndAddToStr("(")
        }

        b.closeBrac.setOnClickListener {
            checkForCharAndAddToStr(")")
        }


        // PI and EulerNumber
        b.pi.setOnClickListener {
            checkForCharAndAddToStr(PI)
        }

        b.eulerNo.setOnClickListener {
            checkForCharAndAddToStr("e")
        }


        // clear operators
        b.backBtn.setOnClickListener {
            val str = b.expressionView.text
            val exp = str.dropLast(1)
            b.expressionView.text = exp
        }

        b.reset.setOnClickListener {
            b.expressionView.text = ""
        }

        b.equalTo.setOnClickListener {

            if (b.expressionView.text.isEmpty()) return@setOnClickListener

            val scriptEngineManager = ScriptEngineManager()
            val scriptEngine = scriptEngineManager.getEngineByName("js")

            try {
                var exp = b.expressionView.text
                exp = "$exp".replace(PI,"3.14159")
                exp = "$exp".replace("e","2.71828")

                ans = scriptEngine.eval(exp) as Double

                val df = DecimalFormat("#.###")
                df.roundingMode = RoundingMode.FLOOR
                ans = df.format(ans).toDouble()

//                ans  = Keval.eval("$exp")
                b.expressionView.text = "$ans"

            }catch (e:ScriptException){
                showToast("Invalid Expression")
            }catch ( e : Exception){
//                Toast.makeText(this,,Toast.LENGTH_LONG).show()
                showToast("${e.localizedMessage}")
            }
        }

    }

    private fun checkForCharAndAddToStr(c : String){

        val exp = b.expressionView.text

        if (exp.equals("")) {
            when(c){
                "+","*","/","^","%",")" -> return
            }
            b.expressionView.text = c       // Simply add the character to the Expression_TextView
            return
        }

        if (exp.length == 25) {
            showToast("Maximum limit reached: 25 characters")
            return
        }

        val lastChar = "${exp[exp.length - 1]}"

        when(c){
            "1","2","3","4","5","6","7","8","9","0" -> {
                if (lastChar == PI || lastChar == "e" || lastChar == ")"){
                    b.expressionView.text = "$exp*$c"
                }
                else{
                    b.expressionView.text = "$exp$c"
                }
            }

            "." -> {
                checkForDot(c,lastChar,exp)
            }

            "+","-","*","/","^","%" -> {
                when(lastChar){
                    "." -> b.expressionView.text = "$exp"+ "0" +"$c"
                    "1","2","3","4","5","6","7","8","9","0","e",PI -> b.expressionView.text = "$exp$c"
                    "(" -> {
                        if (c == "+" || c == "-") b.expressionView.text = "$exp$c"
                    }
                    ")" -> b.expressionView.text = "$exp$c"

                    "+","-","*","/","^","%" -> if (exp.length != 1) b.expressionView.text = "${exp.dropLast(1)}$c"
                }
            }

            PI,"e" -> {
                when(lastChar){
                    "." -> b.expressionView.text = "$exp"+ "0*" +"$c"
                    "1","2","3","4","5","6","7","8","9","0","e",PI -> b.expressionView.text = "$exp*$c"
                    ")" -> b.expressionView.text = "$exp*$c"
                    else -> b.expressionView.text = "$exp$c"
                }
            }

            "(" -> {
                when(lastChar){
                    "+","-","*","/","^","%" -> b.expressionView.text = "$exp$c"
                    "1","2","3","4","5","6","7","8","9","0","e",PI ->  b.expressionView.text = "$exp*$c"
                    "." -> b.expressionView.text = "$exp"+ "0*" +"$c"
                    "(" -> b.expressionView.text = "$exp$c"
                    ")" -> b.expressionView.text = "$exp*$c"
                }
            }

            ")" -> {
                when(lastChar){
                    "1","2","3","4","5","6","7","8","9","0","e",PI ->  b.expressionView.text = "$exp$c"
                    "." -> b.expressionView.text = "$exp"+ "0" +"$c"
                    ")" -> b.expressionView.text = "$exp$c"
                }
            }

        }

    }

    private fun checkForDot(c: String, lastChar: String, exp: CharSequence?) {
        /**
         * Here:
         *      c           -> (.)
         *      lastChar    -> last character of the expression string
         *      exp         -> the complete expression
         */


        when(lastChar){
            "1","2","3","4","5","6","7","8","9","0","." -> {
                if (canApplyDot(exp)){
                    b.expressionView.text = "$exp$c"
                }
            }

            "+","-","*","/","^","%" -> {
                b.expressionView.text = "$exp"+"0"+"$c"
            }

            PI,"e" -> {
                b.expressionView.text = "$exp*0$c"
            }

        }
    }

    private fun canApplyDot(exp: CharSequence?): Boolean {

        val str = exp.toString()

        val exp1 =  StringBuilder(str).reverse().toString()

        // 34+3.34
        if (exp != null) {
            for (e in exp1){
                when("$e"){
                    "+","-","*","/","^","%" -> return true
                    "." -> return false
                }
            }
            return true
        }
        else return true
    }

    private fun showToast(msg : String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

}