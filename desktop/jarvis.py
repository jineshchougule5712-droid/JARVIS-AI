import os, webbrowser, requests
try:
 import speech_recognition as sr
 import pyttsx3
except ImportError:
 sr=pyttsx3=None
BACKEND=os.getenv("JARVIS_BACKEND","http://127.0.0.1:8080/api/command")
def speak(x):
 print("JARVIS:",x)
 if pyttsx3:
  e=pyttsx3.init(); e.say(x); e.runAndWait()
def listen():
 if not sr: return input("You: ")
 r=sr.Recognizer()
 with sr.Microphone() as m: print("Listening..."); a=r.listen(m,phrase_time_limit=10)
 try:return r.recognize_google(a)
 except:return ""
def local(c):
 x=c.lower()
 sites={"open youtube":"https://youtube.com","open google":"https://google.com","open linkedin":"https://linkedin.com","open github":"https://github.com"}
 for k,u in sites.items():
  if k in x:webbrowser.open(u);return "Opening "+k[5:]+"."
 if x.startswith("search "):
  webbrowser.open("https://www.google.com/search?q="+requests.utils.quote(c[7:]));return "Searching."
def main():
 speak("JARVIS desktop online.")
 while True:
  c=listen().strip()
  if not c:continue
  if c.lower() in ("exit","quit","stop jarvis"):speak("Goodbye.");break
  r=local(c)
  if r:speak(r);continue
  try:speak(requests.post(BACKEND,json={"text":c},timeout=8).json().get("reply","Done."))
  except:speak("Backend is offline.")
if __name__=="__main__":main()
