package com.xsoftwareabdullah.jarvis;
import android.content.Context; 
import android.speech.tts.TextToSpeech; 
import java.util.Locale;
public class JarvisVoice {
    private TextToSpeech tts;
    public JarvisVoice(Context c) {
        tts = new TextToSpeech(c, s -> {
            if (s == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.UK); tts.setPitch(0.85f); tts.setSpeechRate(0.88f);
            }
        });
    }
    public void speak(String t) { tts.speak(t, TextToSpeech.QUEUE_FLUSH, null, null); }
    public void shutdown() { if (tts != null) tts.shutdown(); }
}
