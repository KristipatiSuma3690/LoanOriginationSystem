Designed and build a Loan Origination System (LOS) that safely and concurrently processes multiple loan applications, simulates system approval delays, supports agent-manager hierarchies, and sends mocknotifications to relevant parties. The focus is on clean architecture, database design, thread-safety, and deployment readiness.

Steps to run:

Pre-requisites:
1. Mysql
2. Java 17
3. Maven
4. Tomcat
5. Eclipse/ Intellij / SpringToolSuite4
6. Postman

1. Make sure the Database is connected, I used Mysql with Tables names loan, agent
2. Checkout the code from github
3. Maven install ->  Maven is a open source build tool mainly used for java projects to automate compile, running test cases, packaging application
     pom.xml -> project object model used to manage project dependencies and soon
4. Run the LoanServiceApp.java (which is main class that runs the spring boot application
5. From Postman, Test the LOS backend system: (Make sure application is running with out any errors)

   Loan 1. POST: http://localhost:8080/api/v1/loans

   Sample Request:
   Json Object:
   
{
  "customerName": "Sheith Keith",
  "customerPhone": "9898912345",
  "loanAmount": 50000,
  "loanType": "PERSONAL"
}

Status: 200OK is successful else will through custom Exception


        2. GET all loans: http://localhost:8080/api/v1/loans/
        3. GET by Id: http://localhost:8080/api/v1/loans/1
        4. Status Count: http://localhost:8080/api/v1/loans/status-count
        5. Top Customers: http://localhost:8080/api/v1/loans/top

  Agent: 

        1. POST : http://localhost:8080/api/v1/agents/

        Sample Request: 

        manager: 
{
    "agentName": "Rocky Agent",
}

Employees under that managers:

{
    "agentName": "Rocky Agent",
  "manager": {
    "id": 1
  }
}


        2. GET all agents: http://localhost:8080/api/v1/agents/
        3. GET by Id: http://localhost:8080/api/v1/agents/1
        4. PUT -> after assigning the agent for the loan, Agent decisions will be taken APPROVE / REJECT
            http://localhost:8080/api/v1/agents/{agentId}/loans/{loanId}/decision

            Sample Request:
                 { "decision": "APPROVE" } or { "decision": "REJECT" }


                 
         
   
   
Overview: 
1. Created a POJO class: 
    loan -> loan applications
    agent -> List of agents

2. I used JPA Respository to make database interaction from application
3. Used Multithreading to Implement a background job using a thread pool to simulate system approval. and assign the agents to the loan application to verify and make a decisions on loan approval
4. Implementated a Notification system to alert the agents



