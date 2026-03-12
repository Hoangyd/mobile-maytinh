package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var isDarkMode by remember { mutableStateOf(false) }

            MaterialTheme {
                CalculatorScreen(
                    isDarkMode = isDarkMode,
                    onToggleTheme = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}

@Composable
fun CalculatorScreen(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {

    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    val buttons = listOf(
        listOf("C","del","%","/"),
        listOf("7","8","9","x"),
        listOf("4","5","6","-"),
        listOf("1","2","3","+"),
        listOf("00","0",".","=")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if(isDarkMode) Color.Black else Color(0xFFEDEDED))
            .padding(16.dp)
    ) {

        // NÚT DARK MODE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(onClick = onToggleTheme) {

                Text(
                    text = if(isDarkMode) "☀️" else "🌙",
                    fontSize = 28.sp
                )
            }
        }

        // PHẦN HIỂN THỊ
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = expression,
                fontSize = 24.sp,
                color = if(isDarkMode) Color.White else Color.Black
            )

            Text(
                text = result,
                fontSize = 48.sp,
                color = if(isDarkMode) Color.White else Color.Black
            )
        }

        // BÀN PHÍM
        Column {

            buttons.forEach { row ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    row.forEach { text ->

                        CalculatorButton(
                            text = text,
                            isDarkMode = isDarkMode,
                            onClick = {

                                when(text){

                                    "C" ->{
                                        expression = ""
                                        result = "0"
                                    }

                                    "del" ->{
                                        expression =
                                            if(expression.isNotEmpty())
                                                expression.dropLast(1)
                                            else ""
                                    }

                                    "=" ->{
                                        result = calculate(expression)
                                    }

                                    else ->{
                                        expression += text
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text:String,
    isDarkMode:Boolean,
    onClick:()->Unit
){

    val color = when(text){

        "C" -> Color(0xFFD6A4A4)

        "/","x","-","+" -> Color(0xFFE0A43C)

        "=" -> Color(0xFF4CAF50)

        else -> if(isDarkMode) Color(0xFF2B2F3A) else Color(0xFFCED4DA)
    }

    val textColor =
        if(isDarkMode) Color.White else Color.Black

    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .padding(4.dp)
            .size(80.dp)
    ) {

        Text(
            text = text,
            fontSize = 22.sp,
            color = textColor,
            maxLines = 1
        )
    }
}

fun calculate(expr: String): String {

    return try {

        val expression = expr
            .replace("x", "*")   // vì nút giao diện dùng x

        val result = ExpressionBuilder(expression)
            .build()
            .evaluate()

        // nếu là số nguyên thì bỏ .0
        if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }

    } catch (e: Exception) {
        "Error"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCalculator() {

    CalculatorScreen(
        isDarkMode = false,
        onToggleTheme = {}
    )
}