package com.example.uniqey

import android.content.ClipData
import android.os.Bundle
import android.content.ClipboardManager
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sentenceinput = findViewById<EditText>(R.id.textInputSentance)
        val outputText = findViewById<EditText>(R.id.outputText)
        val pastbtn = findViewById<Button>(R.id.pastBTN)
        val copybtn = findViewById<Button>(R.id.copyBTN)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val textt = findViewById<TextView>(R.id.textView4)
        val CM = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        var clipData: ClipData
        val Converter = uniqueyConverter()
        var sentence: String? = ""
        var outputSen: String? = ""

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.encode) {
                textt.setText("ENCODE operation")
                sentenceinput.setHint("Fill your Sentence here")
                outputText.setHint("Here you can get your uniqey")
                sentence = sentenceinput.text.toString()
                outputSen = Converter.encodeConverter(sentence!!)
                outputText.setText(outputSen)
            } else if (checkedId == R.id.decode) {
                textt.setText("Decode operation")
                sentenceinput.setHint("Fill your uniqey text here")
                outputText.setHint("Here you can get your Sentence")
                sentence = sentenceinput.text.toString()
                outputSen = Converter.decodeConverter(sentence!!)
                outputText.setText(outputSen)
            }
        }

        pastbtn.setOnClickListener {
            radioGroup.clearCheck()
            val pData = CM.primaryClip

            // Retrieving the items
            val item = pData!!.getItemAt(0)

            // item is converted to string and stored in a variable
            val txtPaste = item.text.toString()

            // Textview is set as txtPaste string
//            pasteTxt!!.text = txtPaste
            sentenceinput.setText(txtPaste)
            Toast.makeText(applicationContext,"Pasted Successfuly",Toast.LENGTH_SHORT).show()
        }

        copybtn.setOnClickListener{
            radioGroup.clearCheck()
            val textCopy = outputSen
            clipData = ClipData.newPlainText("text",textCopy)
            CM.setPrimaryClip(clipData)
            Toast.makeText(applicationContext,"Copy Successfuly",Toast.LENGTH_SHORT).show()
        }
    }
}
class uniqueyConverter{
    var size = 0
    var start:kotlin.Int = 0
    var end:kotlin.Int = 0
    var charNum:kotlin.Int = 0
    var firstChar = 0
    var afterFirst:kotlin.Int = 0
    var specialDigit:kotlin.Int = 0
    var uniq: String? = ""
    var real:String? = ""
    var ifA: Boolean = false
    val defaultSD = 31

    fun reset() {
        afterFirst = 0
        firstChar = afterFirst
        charNum = firstChar
        end = charNum
        start = end
        size = start
        ifA = false
    }
    fun encodeConverter(input: String): String? {
        var sentence:String = "_"
        var addSen:Boolean = true
        var specialChar: String = ""
        reset()
        size = input.length
        specialDigit = defaultSD
        uniq = "_"

        for (i in 0 until size) {
            charNum = input[i].toInt()
            if (charNum >= 97 && charNum <= 122) {
                charNum -= 32
            } else if (!(charNum >= 65 && charNum <= 90)) {
                charNum = 95
                specialChar += input[i]
            }
            uniq += charNum.toChar()
        }

        uniq += "_"
        Log.d("massage","input     : |$input|")
        Log.d("massage","uniq      : |$uniq|")
        Log.d("massage","SpecialChar : |$specialChar|")
        for (i in 1..size) {
            if (uniq!![i-1].toInt() == 95 && uniq!![i].toInt() != 95) {
                start = i
                end = start
                addSen = false
                //Log.d("massage","start to end : |$start| to |$end|")
            }
            if (uniq!![i+1].toInt() == 95 && uniq!![i].toInt() != 95) {
                var temp = ""
                end = i
//                Log.d("massage","start  : |$start| to |$end|")
                firstChar = uniq!![start].toInt()//H = 72
                if (firstChar == 65) {
                    firstChar += 2
                    ifA = true
                }
                firstChar -= 39 // H = 33
//                Log.d("massage","firstChar  : |$firstChar|")//done
                for (si in start + 1..end) {
                    Log.d("massage","si  : |$si|")
                    afterFirst = uniq!![si].toInt() //A = 65
                    afterFirst -= 39 //A = 26
                    afterFirst -= firstChar //A = -7
//                    Log.d("massage","afterFirst  : |$afterFirst|")
                    afterFirst += if (afterFirst < 0) {
                        123 //
                    } else {
                        65
                    }
//                    Log.d("massage","2 afterFirst  : |$afterFirst|")
                    temp += afterFirst.toChar()
//                    Log.d("massage","num2  : |$num2|")
//                    Log.d("massage","sentence  : |$sentence|")
                }
                if (ifA) {
                    firstChar -= 2
                    ifA = false
                }
                firstChar -= specialDigit
                firstChar += if (firstChar < 0) {
                    123 //
                } else {
                    65
                }
                // cout<<firstChar<<end;
                sentence += firstChar.toChar() + temp
                addSen = true
                continue
            }
            if(addSen){
                sentence += uniq!![i]
            }
        }
        sentence += '_'
        Log.d("massage","sentence  : |$sentence|")
        // cout<<"\nUniq : "<<uniq<<endl;
        var uni:String = ""
        var i = 1
        var ei = 0
        var templ = 0
        while (i <= size) {
            templ = sentence[i].toInt()
            Log.d("massage","templ  : |$templ|")
            if (sentence[i].toInt() == 95) {
                Log.d("massage","Need to chnage  : |$templ|")
                templ = specialChar[ei].toInt()
                ei++
            }
            uni += templ.toChar()
            i++
        }
        return uni
    }
    fun decodeConverter(input: String): String? {
        var sentence:String = "_"
        var addSen:Boolean = true
        var specialChar: String = ""
        reset()
        size = input.length
        specialDigit = defaultSD
        real = "_"
        specialDigit = defaultSD
        for (i in 0 until size) {
            charNum = input[i].toInt()
            if (!( (charNum >= 65 && charNum <= 90) || (charNum >= 97 && charNum <= 122) )) {
                charNum = 95
                specialChar += input[i]
            }
            real += charNum.toChar()
        }
        real += "_"
//        Log.d("massage","input     : |$input|")
//        Log.d("massage","real      : |$real|")
//        Log.d("massage","SpecialChar : |$specialChar|")
        for (i in 1..size) {
            if (real!![i - 1].toInt() == 95 && real!![i].toInt() != 95) {
                start = i
                end = start
                addSen = false
            }
            if (real!![i + 1].toInt() == 95 && real!![i].toInt() != 95) {
                var temp:String = ""
                end = i
                firstChar = real!![start].toInt() //G = 71
                firstChar -= if (firstChar >= 97 && firstChar <= 122) {
                    123 //
                } else {
                    65
                }
                firstChar += specialDigit //33
                if (firstChar == 26) {
                    firstChar += 2
                    ifA = true
                }
                for (si in start + 1..end) {
                    afterFirst = real!![si].toInt() //116
                    afterFirst -= if (afterFirst >= 97 && afterFirst <= 122) {
                        123
                    } else {
                        65
                    }
                    afterFirst += firstChar
                    afterFirst += 71
                    temp += afterFirst.toChar()
                }
                if (ifA) {
                    firstChar -= 2
                    ifA = false
                }
                // firstChar += 39;
                firstChar += 71
                sentence += firstChar.toChar() + temp
                addSen = true
                continue
            }
            if (addSen) {
                sentence += real!![i]
            }
        }
        sentence += '_'
        Log.d("massage","sentence  : |$sentence|")
        var uni:String = ""
        var i = 1
        var ei = 0
        var templ = 0
        while (i <= size) {
            templ = sentence[i].toInt()
            Log.d("massage","templ  : |$templ|")
            if (sentence[i].toInt() == 95) {
                Log.d("massage","Need to chnage  : |$templ|")
                templ = specialChar[ei].toInt()
                ei++
            }
            uni += templ.toChar()
            i++
        }
        return uni
    }
}