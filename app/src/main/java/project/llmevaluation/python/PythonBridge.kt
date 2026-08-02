package project.llmevaluation.python

import android.content.Context
import com.chaquo.python.PyException
import com.chaquo.python.Python
import project.llmevaluation.models.EvaluationResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PythonBridge(private val context: Context) {

    fun evaluate(prompt: String, responseDataJson: String): List<EvaluationResult> {
        return try {
            val py = Python.getInstance()
            val pyModule = py.getModule("evaluation")
            val resultJson = pyModule.callAttr("evaluate_responses", prompt, responseDataJson).toString()
            
            val type = object : TypeToken<List<EvaluationResult>>() {}.type
            Gson().fromJson(resultJson, type)
        } catch (e: PyException) {
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getSamplePrompts(): List<String> {
        return try {
            val py = Python.getInstance()
            val pyModule = py.getModule("evaluation")
            val resultJson = pyModule.callAttr("get_sample_prompts").toString()
            
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(resultJson, type)
        } catch (e: PyException) {
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
