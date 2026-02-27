# URL Shortener

This a simple URL Shortener front and backend service storing requests in a mongodb database.
You will be able to provide a long url to shorten. You can use a custom alias as well. 
You can delete your requests.

## To use the project
Clone the project from here.
Make sure Docker is Installed
open a terminal and run docker compose up --build -d

This will build and launch 3 containers. 
1. MongoDB 8 community and create a volume local to the project for storing data. This will set up a replica set locally as the code uses transactions which require this.
2. A frontend accessible at localhost:3000
3. A backend API written in kotlin using ktor running at localhost:8080

### Frontend Usage

I have tried to make the colours work on light and dark.

   <img width="866" height="680" alt="Screenshot 2026-02-27 at 10 20 00" src="https://github.com/user-attachments/assets/16e78b9d-c2a9-4348-b478-fb7007fd400a" />

   Simply enter a long url in the text box, you can provide an alias between 3 and 20 characters and click the Shorten URL button. You will then see a shortened URL appear below.

   <img width="866" height="778" alt="Screenshot 2026-02-27 at 10 22 21" src="https://github.com/user-attachments/assets/eea21b76-4501-44c0-bce4-005085e475f6" />

   By clicking The List URLS Button you can manage your requests.

   <img width="869" height="610" alt="Screenshot 2026-02-08 at 21 09 00" src="https://github.com/user-attachments/assets/248a1b35-b49b-4b41-9879-4c98f0ca3280" />

   You can Delete a requested URL by using the Delete action in the table. By clicking on the table you will open the original URL in a new tab.

### Backend API

I used HTTPie to test the response. 

You can use the endpoints as listed in the openapi.yaml file.

The backend is built using kotlin and ktor for a quick and lightwieight server. 
Full validation and API Error Handling has been added. Certain database actions have been wrapped in a transaction for safety and consistentency of data.
There are a number of Tests as well to cover the functionality.

<img width="1129" height="699" alt="Screenshot 2026-02-27 at 10 32 20" src="https://github.com/user-attachments/assets/f313f66d-8c96-426a-8666-6aa39f08506a" />


<img width="795" height="454" alt="Screenshot 2026-02-27 at 10 33 48" src="https://github.com/user-attachments/assets/3641a871-1c64-486f-8b5d-b672eb0521ea" />




