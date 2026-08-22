# JARVIS 2.0 — Personal AI Assistant
Multi-device foundation for Android + Windows.

## Included
- Spring Boot AI gateway with command planning
- Persistent PostgreSQL memory/reminders/jobs
- OpenAI-compatible LLM adapter via environment variables
- Windows desktop voice client and safe local actions
- Android voice client
- Playwright browser worker
- Job scoring and application-plan APIs
- Confirmation gate for risky actions

## Start backend
1. Install Java 21 and Maven.
2. Start PostgreSQL with `docker compose up -d`.
3. Set `LLM_API_KEY` if you want model responses.
4. Run `cd backend && mvn spring-boot:run`.

## Start Windows
`cd desktop`
`python -m venv .venv`
`.venv\Scripts\activate`
`pip install -r requirements.txt`
`python jarvis.py`

## Start browser worker
`cd browser-agent`
`npm install`
`npm start`

## Android
Open `android` in Android Studio and Run. For a physical phone, change BACKEND_URL in MainActivity.kt to your laptop LAN IP.

Never bypass CAPTCHA, OTP, authentication, paywalls, or website security. Keep final job submission and other consequential actions behind confirmation.
