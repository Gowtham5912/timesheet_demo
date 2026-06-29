step 1: Open Linux Terminal and DO sudo systemctl start jenkins
step 2: sudo systemctl status jenkins
step 3: Open another terminal and do ngrok http 8080 ( This is to run ngrok)
step 4: Copy the forwarding url provided by ngrok and paste that url in browser 
step 5: Open your project in VSCode and do any changes and push to GitHub
step 6: See the pipeline there will be auto build been executed.
step 7: After succefull build open browser and type http://localhost:8081

