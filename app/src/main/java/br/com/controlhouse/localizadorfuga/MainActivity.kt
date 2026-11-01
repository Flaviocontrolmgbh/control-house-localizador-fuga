package br.com.controlhouse.localizadorfuga

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ln
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val before = findViewById<EditText>(R.id.before)
        val after = findViewById<EditText>(R.id.after)
        val point = findViewById<EditText>(R.id.point)
        val result = findViewById<TextView>(R.id.result)

        findViewById<Button>(R.id.analyze).setOnClickListener {
            val a = before.text.toString().replace(",", ".").toDoubleOrNull()
            val d = after.text.toString().replace(",", ".").toDoubleOrNull()
            if (a == null || d == null || a <= 0 || d <= 0) {
                result.text = "Informe medições válidas em MΩ."
                return@setOnClickListener
            }
            val ratio = d / a
            val improvement = (ratio - 1.0) * 100.0
            val confidence = (35.0 + 20.0 * ln(max(1.0, ratio))).coerceIn(35.0, 95.0)
            val verdict = when {
                ratio >= 10 -> "FORTE INDÍCIO de fuga no trecho isolado"
                ratio >= 3 -> "INDÍCIO RELEVANTE de fuga no trecho isolado"
                ratio >= 1.5 -> "POSSÍVEL contribuição do trecho para a fuga"
                else -> "Sem alteração suficiente para atribuir a fuga a este trecho"
            }
            result.text = "Resultado\n$verdict\nTrecho: ${point.text}\nAntes: %.3f MΩ\nDepois: %.3f MΩ\nMelhora: %.0f%%\nConfiança indicativa: %.0f%%\n\nConfirme repetindo o ensaio e seccionando os trechos adjacentes.".format(a, d, improvement, confidence)
        }
    }
}
