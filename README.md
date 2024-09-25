# bati_cuisine

# Bati-Cuisine: Application d'Estimation des Coûts de Construction des Cuisines

## Description

**Bati-Cuisine** is a Java application designed for construction and renovation professionals to estimate the costs of kitchen projects. The application calculates the total cost of work by considering both materials used and labor costs, with the latter charged by the hour. Key features include client management, customized quote generation, and a comprehensive overview of financial and logistical aspects of renovation projects.

## Features

### 1. Project Management
- **Client Association**: Link a client to each project to track information for billing and quotes.
- **Component Management**: Add and manage components including materials and labor.
- **Quote Association**: Associate a quote with the project to estimate costs before starting work.
- **Project Details**:
    - `nomProjet` (String): Name of the construction or renovation project.
    - `margeBeneficiaire` (double): Profit margin applied to the total cost.
    - `coutTotal` (double): Total cost calculated for the project.
    - `etatProjet` (Enum): Status of the project (In Progress, Completed, Canceled).

### 2. Component Management
- **Materials**: Manage material costs with detailed attributes.
    - `nom` (String): Name of the component.
    - `coutUnitaire` (double): Unit cost of the component.
    - `quantite` (double): Quantity used.
    - `typeComposant` (String): Type of component (Material or Labor).
    - `tauxTVA` (double): VAT rate applicable to the component.
    - `coutTransport` (double): Transportation cost of the material.
    - `coefficientQualite` (double): Quality coefficient of the material.
- **Labor**: Calculate labor costs based on hourly rates, hours worked, and worker productivity.
    - `nom` (String): Name of the component.
    - `typeComposant` (String): Type of component (Material or Labor).
    - `tauxTVA` (double): VAT rate applicable to the component.
    - `tauxHoraire` (double): Hourly rate of labor.
    - `heuresTravail` (double): Hours worked.
    - `productiviteOuvrier` (double): Productivity factor of the workers.

### 3. Client Management
- **Client Records**: Register basic client information and differentiate between professional and private clients.
- **Discounts**: Apply specific discounts based on client type (e.g., regular or professional clients).
- **Client Details**:
    - `nom` (String): Client's name.
    - `adresse` (String): Client's address.
    - `telephone` (String): Client's phone number.
    - `estProfessionnel` (boolean): Indicates if the client is a professional.

### 4. Quote Creation
- **Quote Generation**: Create quotes before starting work, including cost estimates for materials, labor, equipment, and taxes.
- **Quote Details**:
    - `montantEstime` (double): Estimated amount of the project based on the quotes.
    - `dateEmission` (Date): Quote issuance date.
    - `dateValidite` (Date): Quote validity date.
    - `accepte` (boolean): Indicates if the quote has been accepted by the client.

### 5. Cost Calculation
- **Cost Integration**: Include component costs (materials and labor) in the total project cost calculation.
- **Profit Margin**: Apply a profit margin to determine the final project cost.
- **Taxes and Discounts**: Account for applicable VAT and client-specific discounts.
- **Cost Adjustments**: Adjust costs based on material quality or labor productivity.

### 6. Detailed Results and Display
- **Project Summary**: Display complete project details including client information, components, and total cost.
- **Client and Quote Information**: Show detailed information for clients and quotes.
- **Cost Breakdown**: Provide a detailed summary of the total cost, including labor, materials, taxes, and profit margin.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/khalid-oukha/bati-cuisine.git


### dataBase script

CREATE TABLE clients
(
id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
address VARCHAR(255) NOT NULL,
phone VARCHAR(20)  NOT NULL,
isProfessional BOOLEAN NOT NULL
);

CREATE TYPE project_status AS ENUM ('IN_PROGRESS', 'COMPLETED', 'CANCELLED');

CREATE TABLE projects
(
id SERIAL PRIMARY KEY,
name VARCHAR(255)     NOT NULL,
profitMargin DOUBLE PRECISION NOT NULL,
totalCost DOUBLE PRECISION NOT NULL,
projectStatus project_status NOT NULL,
client_id INTEGER REFERENCES clients (id) ON DELETE SET NULL
);

CREATE TABLE component
(
id SERIAL PRIMARY KEY,
name VARCHAR(255),
componentType VARCHAR(255),
vatRate DOUBLE PRECISION,
project_id INTEGER REFERENCES projects (id) ON DELETE cascade ON UPDATE CASCADE
);

CREATE TABLE labor
(
id INTEGER PRIMARY KEY REFERENCES component (id),
hourlyRate DOUBLE PRECISION,
workingHours DOUBLE PRECISION,
workerProductivity DOUBLE PRECISION
);

CREATE TABLE material
(
id INTEGER PRIMARY KEY REFERENCES component (id),
unitCost DOUBLE PRECISION,
quantity DOUBLE PRECISION,
transportCost DOUBLE PRECISION,
qualityCoefficient DOUBLE PRECISION
);

CREATE TABLE quotes
(
id SERIAL PRIMARY KEY,
estimatedAmount DOUBLE PRECISION NOT NULL,
issueDate DATE NOT NULL,
validityDate DATE NOT NULL,
accepted BOOLEAN NOT NULL,
project_id INTEGER REFERENCES projects (id) ON DELETE CASCADE
);

INSERT INTO clients (name, address, phone, isprofessional)
VALUES ('Acme Corp', '123 Business Rd, Business City', '123-456-7890', TRUE),
('John Doe', '456 Residential St, Hometown', '987-654-3210', FALSE),
('Jane Smith', '789 Industrial Ave, Metropolis', '555-123-4567', TRUE);

INSERT INTO projects (name, profitmargin, totalcost, projectstatus, client_id)
VALUES ('Project Alpha', 15.5, 10000.00, 'IN_PROGRESS', 1),
('Project Beta', 10.0, 5000.00, 'COMPLETED', 2);

INSERT INTO components (name, "componentType", vatrate, project_id)
VALUES ('Component X', 'Material', 0.20, 1),
('Component Y', 'Labor', 0.15, 1),
('Component Z', 'Material', 0.10, 2);

INSERT INTO labors (id, hourlyrate, workinghours, workerproductivity)
VALUES (129, 25.00, 40.0, 0.90),
(131, 20.00, 45.0, 0.95);

INSERT INTO materials (id, unitcost, quantity, transportcost, qualitycoefficient)
VALUES (130, 50.00, 100, 200.00, 1);

INSERT INTO quotes (estimatedamount, issuedate, validitydate, accepted, project_id)
VALUES (12000.00, '2024-09-01', '2024-12-01', TRUE, 1),
(5500.00, '2024-09-10', '2024-11-10', FALSE, 2),
(16000.00, '2024-09-15', '2024-12-15', TRUE, 3);