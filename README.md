# Some PPT clicker thing you can use
[Amazon says that these are worth
£12.](https://www.amazon.co.uk/August-Wireless-PowerPoint-Clicker-LP200-Black/dp/B00CXNQR0Y)

What'd you think I am, a person with a job?

So here's a pretty bad alternative.
It uses wifi, and not bluetooth, however.

## Server Usage
Usage: python server.py PORT NUM\_SLIDES

Just sit this down in a terminal somewhere and fullscreen your presentation.

Remember to kill it afterwards, because it's a really bad http server and will
press space whenever somebody GETs /forwards.

## Android App Usage
Enter your IP and port into the app and you're ready to go.

If you set your slides correctly then a progress bar should appear on the app,
allowing you to keep track of how much you've got left to waffle.

Just press the forward button to go forward one slide, and the back button to go
back.

### Dependencies
Uses Volley on Android for the net stuff and pyautogui for the server side.

I used android studio/gradle for the app part. 

