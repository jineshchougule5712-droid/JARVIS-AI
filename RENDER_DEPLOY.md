# Render deployment

1. Push this folder to GitHub.
2. In Render, choose New > Blueprint and select the repository containing render.yaml.
3. Render creates `jarvis-ai` and `jarvis-db`.
4. In the `jarvis-ai` service Environment page, add `LLM_API_KEY` and save/deploy.
5. Open the service URL shown by Render.
6. Test:
   POST /api/command with JSON {"text":"find Java jobs"}
   POST /api/ai/chat with JSON {"message":"Hello JARVIS"}
7. Use the resulting HTTPS service URL in Android `MainActivity.kt`.

The Docker image listens on Render's PORT through `server.port=${PORT:8080}`.
Keep API keys in Render environment variables, not GitHub.
