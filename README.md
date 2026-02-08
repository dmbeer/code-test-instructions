# URL Shortener

This a simple url shortener front and backend service storing requests in a mongodb datbase.
You will be able to provide a long url to shorten. You can use a custom alias as well. 
You can manage the urls generated as well.

## To use the project
Clone the project from here.
Make sure Docker is Installed
open a terminal and run docker compose up --build -d

This will build and launch 3 containers. 
1. MongoDB 8 comunity and create a volume local to the project for storing data.
2. A frontend accessible at localhost:3000
3. A backend API written in kotlin using ktor running at localhost:8080

### Frontend Uage

I have tried to make the colurs work on light and dark.

   <img width="869" height="617" alt="Screenshot 2026-02-08 at 21 02 53" src="https://github.com/user-attachments/assets/fcbb8ce9-594c-4195-b450-9c874ee7bc53" />

   Simply enter a long url in the text box and click the Shorten URL button. You will then see a shortened URL appear below.

   <img width="869" height="610" alt="Screenshot 2026-02-08 at 21 07 25" src="https://github.com/user-attachments/assets/66358e6b-3cab-4a82-bbcb-8d48c9c17da8" />

   By clicking The List URLS Button you can manage your requests.

   <img width="869" height="610" alt="Screenshot 2026-02-08 at 21 09 00" src="https://github.com/user-attachments/assets/248a1b35-b49b-4b41-9879-4c98f0ca3280" />

   You can Delete a requested URL by using the Delete action in the table.

### Backend API

I used HTTPie to test the response. 

You can use the endpoints as listed in the openapi.yaml file.

The backend is built using kotlin and ktor for a quick and lightwieight server. 
Full validation and API Error Handling has been built.
There are a number of Tests as well to cover the functionality.

<img width="1164" height="734" alt="Screenshot 2026-02-08 at 21 15 46" src="https://github.com/user-attachments/assets/996ed629-d792-4a47-add8-ed0983ff9b5d" />


<img width="1164" height="734" alt="Screenshot 2026-02-08 at 21 15 09" src="https://github.com/user-attachments/assets/1268ca49-adf4-48c6-8837-c2884db7e754" />




## Deliverables

- Working software.
- Decoupled web frontend (using a modern framework like React, Next.js, Vue.js, Angular, or Flask with templates).
- RESTful API matching the OpenAPI spec.
- Tests.
- A git commit history that shows your thought process.
- Dockerfile.
- README with:
  - How to build and run locally.
  - Example usage (frontend and API).
  - Any notes or assumptions.
