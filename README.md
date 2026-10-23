# VYRON Android Assistant — project v1.1

This project contains a native Android app with a polished dark UI, tap-to-talk using the installed Android speech recognizer, local command routing, optional Groq AI responses, optional ElevenLabs speech output with Android TTS fallback, a floating overlay orb, an accessibility service for Back/scroll, a notification listener, and a GitHub Actions APK build workflow.

## Important: what it does and does not do
- **Does:** answer general questions when a valid Groq API key is configured; speak replies; launch Spotify or Spotify web search; search YouTube/Google; tell the time; open the dialer; open a WhatsApp message link; request Back/scroll actions through Accessibility; read the most recently captured notification after Notification Access is enabled.
- **Does not yet:** listen continuously for “Hey Vyron”; replace the Android default assistant; guarantee 10–15 languages on all devices; automatically choose/play a first Spotify result; automatically identify contacts or place calls; send messages without review; intelligently tap arbitrary controls on every app. Those require additional implementation and device-specific testing.
- This code is a best-effort project and must be tested on your phone. A successful CI build means it compiled, not that every feature works on every Android device.

## Build from phone using GitHub
1. Create or use a repository, e.g. `Vyron`.
2. Upload all files in this project to the repository root, including hidden `.github/workflows/build-apk.yml`.
3. Open **Actions** → **Build VYRON APK**. Run the workflow, or push to `main`.
4. Open the successful run → download the `VYRON-debug-apk` artifact ZIP.
5. Extract it and install `app-debug.apk`.

## Setup
1. Open VYRON and tap **AI & Voice Settings**. Enter your own Groq API key for AI responses. Optionally add ElevenLabs API key and Voice ID for speech.
2. Tap **Floating Orb** and grant overlay permission if desired.
3. Enable **VYRON Screen Interaction** in Android Accessibility settings only if you want Back/scroll commands.
4. Enable **VYRON Notification Reader** in Notification Access settings only if you want notification reading.
5. Tap **Speak to VYRON** and speak a request.

## Supported command examples
- `open Spotify`, `Spotify lo-fi` (launches Spotify; it does not auto-select/play a track)
- `open YouTube`, `YouTube MrBeast`
- `search Google for weather tomorrow`
- `what time is it`
- `go back`, `scroll down`, `scroll up`
- `read notifications`
- `call Alex` (opens dialer; you choose the contact and confirm)
- `message hello` / `send WhatsApp hello` (opens WhatsApp web link; review before sending)

## Keys and privacy
API keys are stored in private app preferences but are not encrypted by this starter. Do not commit keys to GitHub or share them. Never give accessibility or notification access to apps you do not trust. API provider limits and charges may apply.
