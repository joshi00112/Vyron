package com.vyron.assistant;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingOrbService extends Service {
    private WindowManager windowManager;
    private View orb;

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Grant display-over-other-apps permission first.", Toast.LENGTH_LONG).show();
            stopSelf();
            return START_NOT_STICKY;
        }
        if (orb != null) return START_STICKY;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        TextView bubble = new TextView(this);
        bubble.setText("V");
        bubble.setTextColor(Color.WHITE);
        bubble.setTextSize(23);
        bubble.setTypeface(Typeface.DEFAULT_BOLD);
        bubble.setGravity(Gravity.CENTER);
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(Color.rgb(10, 43, 75));
        bg.setStroke(3, Color.rgb(65, 220, 255));
        bubble.setBackground(bg);
        int type = android.os.Build.VERSION.SDK_INT >= 26 ?
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            62, 62, type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 20; params.y = 180;
        bubble.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });
        bubble.setOnLongClickListener(v -> { stopSelf(); return true; });
        try {
            windowManager.addView(bubble, params);
            orb = bubble;
        } catch (Exception e) {
            Toast.makeText(this, "Couldn't show floating orb: " + e.getMessage(), Toast.LENGTH_LONG).show();
            stopSelf();
        }
        return START_STICKY;
    }

    @Override public void onDestroy() {
        if (windowManager != null && orb != null) {
            try { windowManager.removeView(orb); } catch (Exception ignored) {}
        }
        orb = null;
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}
