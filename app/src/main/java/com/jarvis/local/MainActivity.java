package com.xsoftwareabdullah.jarvis;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.widget.ImageView;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText inputField;
    TextView chatText;
    Button btnSend, btnMic, btnBgListening, btnSettings;
    JarvisVoice jarvisVoice;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    // BLUE Reactor Animation
    ImageView reactor = findViewById(R.id.arcReactor);
    ObjectAnimator anim = ObjectAnimator.ofFloat(reactor, "rotation", 0f, 360f);
    anim.setDuration(4000);
    anim.setRepeatCount(ValueAnimator.INFINITE);
    anim.start();
    
    inputField = findViewById(R.id.inputField);
    chatText = findViewById(R.id.chatText);
    btnSend = findViewById(R.id.btnSend);
    btnMic = findViewById(R.id.btnMic);
    btnBgListening = findViewById(R.id.btnBgListening);
    btnSettings = findViewById(R.id.btnSettings);

    jarvisVoice = new JarvisVoice(this);
}

        btnSend.setOnClickListener(v -> sendToJarvis());

        btnMic.setOnClickListener(v -> startVoiceInput());

        btnBgListening.setOnClickListener(v -> {
            startService(new Intent(this, JarvisService.class));
            chatText.append("\nJARVIS: Background listening armed, Sir.");
            jarvisVoice.speak("Background listening armed");
        });

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        });
    }

    void sendToJarvis() {
        String userText = inputField.getText().toString().trim();
        if (userText.isEmpty()) return;
        
        chatText.append("\n\nYOU: " + userText);
        inputField.setText("");
        chatText.append("\nJARVIS: Thinking...");

        new Thread(() -> {
            try {
                String reply = FreeAiService.askJarvis(userText);
                runOnUiThread(() -> {
                    chatText.setText(chatText.getText().toString().replace("JARVIS: Thinking...", "JARVIS: " + reply));
                    jarvisVoice.speak(reply);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    chatText.setText(chatText.getText().toString().replace("JARVIS: Thinking...", "JARVIS: Sorry Sir, I am offline. " + e.getMessage()));
                });
            }
        }).start();
    }

    void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            inputField.setText(result.get(0));
            sendToJarvis();
        }
    }

    @Override
    protected void onDestroy() {
        jarvisVoice.shutdown();
        super.onDestroy();
    }
                                    }
