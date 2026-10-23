package com.vyron.assistant;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView status, transcript, response;
    private TextToSpeech tts;
    private static final int REQ_SPEECH = 100;
    private static final int REQ_AUDIO = 101;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(5, 13, 30));
        getWindow().setNavigationBarColor(Color.rgb(5, 13, 30));
        tts = new TextToSpeech(this, result -> {
            if (result == TextToSpeech.SUCCESS) tts.setLanguage(Locale.getDefault());
        });
        buildUi();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(22), dp(22), dp(18));
        root.setBackgroundColor(Color.rgb(5, 13, 30));

        TextView title = new TextView(this);
        title.setText("V Y R O N");
        title.setTextColor(Color.rgb(95, 225, 255));
        title.setTextSize(27);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title, matchWrap());

        TextView subtitle = new TextView(this);
        subtitle.setText("YOUR VOICE-FIRST ANDROID COMPANION");
        subtitle.setTextColor(Color.rgb(166, 190, 215));
        subtitle.setTextSize(10);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(5), 0, dp(20));
        root.addView(subtitle, matchWrap());

        TextView orb = new TextView(this);
        orb.setText("V");
        orb.setTextColor(Color.WHITE);
        orb.setTextSize(48);
        orb.setTypeface(Typeface.DEFAULT_BOLD);
        orb.setGravity(Gravity.CENTER);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(Color.rgb(10, 43, 75));
        circle.setStroke(dp(3), Color.rgb(65, 220, 255));
        orb.setBackground(circle);
        LinearLayout.LayoutParams orbLp = new LinearLayout.LayoutParams(dp(150), dp(150));
        orbLp.gravity = Gravity.CENTER;
        orbLp.bottomMargin = dp(16);
        root.addView(orb, orbLp);

        status = label("READY • TAP THE MIC TO SPEAK", Color.rgb(95, 225, 255), 13);
        status.setGravity(Gravity.CENTER);
        root.addView(status, matchWrap());

        transcript = label("You: —", Color.WHITE, 14);
        transcript.setPadding(dp(12), dp(12), dp(12), dp(12));
        transcript.setBackground(panel());
        LinearLayout.LayoutParams boxLp = matchWrap();
        boxLp.topMargin = dp(18);
        root.addView(transcript, boxLp);

        response = label("VYRON: Hi! I'm ready. Try “open Spotify” or “search YouTube for MrBeast”.", Color.rgb(220, 235, 250), 14);
        response.setPadding(dp(12), dp(12), dp(12), dp(12));
        response.setBackground(panel());
        LinearLayout.LayoutParams rlp = matchWrap();
        rlp.topMargin = dp(10);
        root.addView(response, rlp);

        Button mic = button("🎙  SPEAK TO VYRON");
        mic.setOnClickListener(v -> startListening());
        addButton(root, mic);

        Button overlay = button("FLOATING ORB");
        overlay.setOnClickListener(v -> enableOverlay());
        addButton(root, overlay);

        Button accessibility = button("SCREEN INTERACTION SETUP");
        accessibility.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        addButton(root, accessibility);

        Button help = button("TRY: OPEN SPOTIFY");
        help.setOnClickListener(v -> handleCommand("open Spotify"));
        addButton(root, help);

        TextView foot = label("Starter build • Actions are limited to supported Android features. Sensitive actions may require confirmation.", Color.rgb(140, 160, 185), 11);
        foot.setGravity(Gravity.CENTER);
        foot.setPadding(0, dp(15), 0, 0);
        root.addView(foot, matchWrap());

        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        setContentView(scroll);
    }

    private void startListening() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQ_AUDIO);
            return;
        }
        if (!android.speech.SpeechRecognizer.isRecognitionAvailable(this)) {
            setResultText("Speech recognition isn't available on this device. Install/enable a speech recognition service.");
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to VYRON");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        try {
            startActivityForResult(intent, REQ_SPEECH);
            status.setText("LISTENING REQUEST OPENED");
        } catch (ActivityNotFoundException e) {
            setResultText("No compatible speech-recognition activity was found.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SPEECH && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String command = results.get(0);
                transcript.setText("You: " + command);
                handleCommand(command);
            }
        }
    }

    private void handleCommand(String raw) {
        String command = raw == null ? "" : raw.trim();
        String lower = command.toLowerCase(Locale.ROOT);
        status.setText("PROCESSING");
        if (lower.startsWith("open spotify") || lower.contains("open spotify")) {
            launchPackageOrUrl("com.spotify.music", "https://open.spotify.com/search/" + Uri.encode(""));
            setResultText("I tried to open Spotify. Tell me a song title in a follow-up build for search-and-play automation.");
        } else if (lower.contains("search youtube for") || lower.contains("youtube") || lower.contains("search for")) {
            String query = command;
            query = query.replaceAll("(?i)search youtube for", "").replaceAll("(?i)open youtube and search for", "").replaceAll("(?i)search for", "").trim();
            if (query.isEmpty()) query = "YouTube";
            openUrl("https://www.youtube.com/results?search_query=" + Uri.encode(query));
            setResultText("Opened YouTube search for: " + query + ". Automatic result selection is not included in this starter build.");
        } else if (lower.startsWith("search web for") || lower.startsWith("google")) {
            String query = command.replaceAll("(?i)search web for", "").replaceAll("(?i)google", "").trim();
            openUrl("https://www.google.com/search?q=" + Uri.encode(query));
            setResultText("Searching the web for: " + query);
        } else if (lower.startsWith("call ")) {
            String who = command.substring(5).trim();
            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
            try {
                startActivity(dial);
                setResultText("Opened the dialer. Choose the contact/number and confirm the call there. Automatic contact matching is not implemented yet.");
            } catch (Exception e) { setResultText("Couldn't open the phone dialer."); }
        } else if (lower.contains("time")) {
            String time = new java.text.SimpleDateFormat("h:mm a", Locale.getDefault()).format(new java.util.Date());
            setResultText("The current time is " + time + ".");
        } else if (lower.contains("stop talking") || lower.equals("stop")) {
            if (tts != null) tts.stop();
            setResultText("Okay, stopped speaking.");
        } else {
            setResultText("I heard: “" + command + "”. This starter build supports a few local actions. An AI backend and full automation engine still need to be connected.");
        }
    }

    private void launchPackageOrUrl(String packageName, String fallbackUrl) {
        try {
            Intent launch = getPackageManager().getLaunchIntentForPackage(packageName);
            if (launch != null) startActivity(launch);
            else openUrl(fallbackUrl);
        } catch (Exception e) { openUrl(fallbackUrl); }
    }

    private void openUrl(String url) {
        try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); }
        catch (Exception e) { setResultText("Couldn't open the requested link."); }
    }

    private void enableOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
            setResultText("Allow VYRON to display over other apps, then return and tap FLOATING ORB again.");
        } else {
            startService(new Intent(this, FloatingOrbService.class));
            setResultText("Floating orb started. Tap the orb to return to VYRON.");
        }
    }

    private void setResultText(String text) {
        response.setText("VYRON: " + text);
        status.setText("READY");
        if (tts != null) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "vyron_reply");
    }

    private TextView label(String text, int color, int size) {
        TextView v = new TextView(this);
        v.setText(text); v.setTextColor(color); v.setTextSize(size);
        return v;
    }
    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text); b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setBackground(panel());
        return b;
    }
    private void addButton(LinearLayout root, Button b) {
        LinearLayout.LayoutParams lp = matchWrap();
        lp.topMargin = dp(8);
        root.addView(b, lp);
    }
    private GradientDrawable panel() {
        GradientDrawable d = new GradientDrawable();
        d.setColor(Color.rgb(13, 29, 52));
        d.setCornerRadius(dp(16));
        d.setStroke(dp(1), Color.rgb(39, 94, 130));
        return d;
    }
    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    private int dp(int value) { return (int)(value * getResources().getDisplayMetrics().density); }

    @Override protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }
}
