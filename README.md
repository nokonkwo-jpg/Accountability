# Accountability 💰

Accountability is your financial dashboard built with Spring Boot, Thymeleaf, JavaScript, and PostgreSQL, leveraging the Plaid API for connecting and displaying personal financial data. Add accounts and view historical transaction data that is saved to a PosgreSQL DB —all with a sleek, user-friendly interface. 🏦✨


## Features

- Connects to your bank accounts via the Plaid API.
- Displays a unified view of financial data (checking & savings only).
- I'm working to export data from PostgreSQL DB to CSV files.
## Technologies Used

- **Spring Boot**: Backend framework for the application.
- **Thymeleaf**: Templating engine to render dynamic content on the frontend.
- **JavaScript**: For front-end interactivity.
- **PostgreSQL**: Database to store user and financial data.
- **Plaid API**: Used to access financial data and integrate accounts.

## Setup

### Prerequisites

- JDK 11 or higher
- PostgreSQL database
- Maven
- Plaid API credentials (sign up at [Plaid](https://plaid.com/) to get your API keys)

### Steps to Run Locally

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/plaid-integration-app.git
   cd plaid-integration-app
    ```

2. Set up PostgreSQL:

- Create a PostgreSQL database.
- Update your application.properties file with the correct database credentials.

3. Configure Plaid API

- Sign up for a Plaid developer account and obtain your client_id and secret
- Add your Plaid API credentials to the application.properties file.

4. Build & Run the application:

    ```bash
   mvn clean install
   mvn spring-boot:run
   ```

6. Open your browser and go to http://localhost:8080 to access the app.

## Contributing

Please feel free to contribute as this is an ongoing project. If there are feature
you'd like to see in this app follow the instructions below:

1. Fork the repo.
2. Create a new branch 
    ```git
    git checkout -b feature-name
   ```
3. Commit your change
    ```git
   git commit -am 'Message you're committing'
   ```
4. Push to the branch
    ```git
   git push origin feature-name
   ```
5. Create a new pull request
