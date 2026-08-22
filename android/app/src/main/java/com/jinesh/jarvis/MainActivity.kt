package com.jinesh.jarvis
import android.app.Activity;import android.os.Bundle;import android.content.Intent;import android.speech.RecognizerIntent;import android.speech.tts.TextToSpeech;import android.widget.*;import java.net.*;import java.util.*;import kotlin.concurrent.thread
class MainActivity:Activity(),TextToSpeech.OnInitListener{
 private lateinit var tts:TextToSpeech;private lateinit var status:TextView;private lateinit var log:TextView
 private val REQ=501;private val BACKEND_URL="http://10.0.2.2:8080/api/command"
 override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(R.layout.activity_main);status=findViewById(R.id.status);log=findViewById(R.id.log);tts=TextToSpeech(this,this);findViewById<Button>(R.id.speak).setOnClickListener{listen()};requestPermissions(arrayOf("android.permission.RECORD_AUDIO"),10);speak("JARVIS online.")}
 private fun listen(){val i=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);status.text="Listening...";startActivityForResult(i,REQ)}
 override fun onActivityResult(r:Int,c:Int,d:Intent?){super.onActivityResult(r,c,d);if(r==REQ&&c==RESULT_OK){val t=d?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()?:return;log.append("\nYOU: "+t);send(t)}}
 private fun send(t:String){thread{try{val q=URL(BACKEND_URL).openConnection() as HttpURLConnection;q.requestMethod="POST";q.setRequestProperty("Content-Type","application/json");q.doOutput=true;q.outputStream.use{it.write(("{\"text\":"+org.json.JSONObject.quote(t)+"}").toByteArray())};val o=q.inputStream.bufferedReader().readText();val r=org.json.JSONObject(o).optString("reply","Done.");runOnUiThread{ speak(r)}}catch(e:Exception){runOnUiThread{speak("Backend is offline.")}}}}
 private fun speak(s:String){status.text=s;log.append("\nJARVIS: "+s);tts.speak(s,TextToSpeech.QUEUE_FLUSH,null,"jarvis")}
 override fun onInit(x:Int){if(x==TextToSpeech.SUCCESS)tts.language=Locale.US};override fun onDestroy(){tts.stop();tts.shutdown();super.onDestroy()}
}