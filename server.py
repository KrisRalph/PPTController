from http.server import HTTPServer, BaseHTTPRequestHandler 
import argparse
import pyautogui

NUM_SLIDES = 15
PORT = 8080

class RemoteRequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        path = self.path
        if path == "/forwards":
            pyautogui.press("space")
        elif path == "/backwards":
            pyautogui.press("left")
        elif path == "/start":
            # cheap and cheerful and so, so awful
            s = str(NUM_SLIDES)
            bs = s.encode()
            self.wfile.write(bs)

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="server.py PORT NUM_SLIDES")
    parser.add_argument('port', type=int,
            help='Choose the port to serve on',
            default=PORT)

    parser.add_argument('slides', type=int,
            help='Pop the number of slides in your presentation',
            default=NUM_SLIDES)

    args = parser.parse_args()
    PORT = args.port
    NUM_SLIDES = args.slides

    address = ('', PORT)
    handler = RemoteRequestHandler
    server = HTTPServer(address, handler)
    server.serve_forever()

