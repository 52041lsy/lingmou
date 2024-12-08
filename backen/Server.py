from http.server import HTTPServer, BaseHTTPRequestHandler
import json
import socket
import cgi
from cgi import parse_header, parse_multipart
import urllib.request
import io,shutil
import re

class Resquest(BaseHTTPRequestHandler):
    def handler(self):
        print("data:", self.rfile.readline().decode())
        self.wfile.write(self.rfile.readline())

    def do_GET(self):        # 处理get请求
        print(self.requestline)
        if self.path != '/hello':
            self.send_error(404, "Page not Found!")
            return

        data = {
            'result_code': True
                        }
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        self.wfile.write(json.dumps(data).encode())

    def do_POST(self):         # 处理post请求
        length = int(self.headers['content-length'])
        req_data = self.rfile.read(length)    # 读取所有http请求报文
        print(self.headers)        # 请求头信息
        print(self.command)      # 请求方式
        # 使用re解析出http请求中的图片,图片为字节类型
        # 图片数据需要去除httpserver加进去的form-data的边界线和文件的描述信息
        # 本例使用re去除boundary和文件描述的key:value  
        pattern1 = re.compile(b"-+\w*\s{2}(.*?\s{2}){2}\s{2}")
        pattern2 = re.compile(b"\s{2}-+.+\s{2}")
        res1 = re.match(pattern1, req_data)
        res2 = re.search(pattern2, req_data)
        file_data = req_data[res1.end():res2.start()]  

        # 将解析出的文件字节, 保存到本地
        with open("xx.jpg", "wb") as w:
            w.write(file_data)       

        data = {
                "status":True
                           }
        # 返回响应报文
        self.send_response(200)   # 响应行
        self.send_header('Content-type', 'application/json') # 响应头
        self.end_headers()  # 空行
        self.wfile.write(json.dumps(data).encode('utf-8'))

if __name__ == '__main__':
    host = ('192.168.2.241', 9002)
    server = HTTPServer(host, Resquest)
    print("Starting server, listen at: %s:%s" % host)
    server.serve_forever()