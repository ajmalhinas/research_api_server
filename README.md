## REST API Server
### Prerequisites 
Tomcat > 8.5
Jersey 1.8

## Image upload for OCR
``` bash
curl -F "image=@/home/rifthy/Downloads/agile.png" http://localhost:8080/api_server/api/upload

return TESSERACT SCANNED TEXT
```

A. Rifthy Ahmed
<rifthyahmed@gmail.com>
