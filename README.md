# VYRON — Android Assistant Starter

This is a real native Android starter project, intended to be developed and tested from a phone using a GitHub cloud build workflow if local Android SDK setup is difficult.

## What is implemented now

- Native Android Java app with a futuristic VYRON interface.
- Speech input through Android's installed speech-recognition activity.
- Spoken responses using Android's built-in TextToSpeech.
- Basic command routing:
  - Open Spotify if installed (or open Spotify web fallback).
  - Search YouTube in a browser.
  - Search Google/the web.
  - Open the phone dialer for a call request.
  - Tell the current time.
  - Stop current TTS speech.
- Optional floating orb overlay, after the user grants overlay permission.
- Accessibility service declaration and a starter service that can inspect accessible text when extended.
- GitHub Actions workflow to build a debug APK in the cloud.

## Important: this is a starter, not the finished VYRON promised in the master prompt

Not yet implemented:
- AI/LLM backend and task planner.
- ElevenLabs integration.
- Always-on wake word.
- Default digital assistant role integration.
- Reliable app-specific Spotify search, first-result selection and playback verification.
- Automatic contact matching/calling.
- WhatsApp sending/replying.
- Notification reading.
- A complete accessibility automation command engine.

The app deliberately does not pretend these features work. They require additional development, testing, permissions, and in some cases external API access.

## Build the APK from your phone using GitHub

1. Create a GitHub repository named `VYRON`.
2. Upload the entire contents of this folder to the repository (the `.github` folder must be included).
3. Open the repository's **Actions** tab.
4. Select **Build VYRON APK**.
5. Tap **Run workflow** if it has not started automatically.
6. Wait for the workflow to finish.
7. Open the completed workflow run and download the `vyron-debug-apk` artifact.
8. Extract the artifact ZIP; it contains `app-debug.apk`.
9. Install the APK. Android may ask you to allow installation from the browser/files app you used.

If GitHub rejects a file upload from mobile, use GitHub's web editor or a mobile Git client to upload the files. Keep the `.github/workflows/build-apk.yml` path exact.

## Requirements

- Android 8.0 (API 26) or later.
- Internet connection for web search commands.
- A speech-recognition provider installed/enabled for voice input.

## Privacy notes

- Microphone permission is requested when you tap the speech button.
- The app does not implement an always-listening microphone.
- The app does not read or upload screen content continuously.
- Accessibility access is optional and must be enabled manually in Android Settings.
- Overlay permission is requested only when you enable the floating orb.
- Calling opens the dialer; it does not silently place a call.
- Never add API keys to this repository. For ElevenLabs or an LLM, use a secure backend or carefully review the security trade-offs before client-side integration.

## Troubleshooting

### Build fails
Open the failed GitHub Actions run and copy the first actual error message. Share it for debugging.

### Speech button says recognition unavailable
Enable Google speech services or another Android speech-recognition provider in your device settings.

### Floating orb doesn't appear
Open VYRON, tap **FLOATING ORB**, grant display-over-other-apps access, return to VYRON, then tap the button again. Long-press the orb to dismiss it.

### Spotify opens but doesn't play a song
That is expected in this starter: song-specific search, first-result selection, and playback verification are not implemented yet.
