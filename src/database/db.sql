
CREATE TABLE Clients (
                         id SERIAL PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         address VARCHAR(255),
                         telephone VARCHAR(20),
                         estProfessional BOOLEAN NOT NULL
);

CREATE TYPE EtatProject AS ENUM ('en cours', 'Terminer', 'Annuler');

CREATE TABLE Projects (
                          id SERIAL PRIMARY KEY,
                          NomProject VARCHAR(255) NOT NULL,
                          margeBeneficiaire DOUBLE PRECISION,
                          couTotal DOUBLE PRECISION,
                          etatProject EtatProject,
                          client_id INT,
                          FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE
);

CREATE TABLE Composants (
                            id SERIAL PRIMARY KEY,
                            nom VARCHAR(255) NOT NULL,
                            typeComposant VARCHAR(255),
                            tauxTva DOUBLE PRECISION
);

CREATE TABLE Materials (
                           id SERIAL PRIMARY KEY,
                           componsant_id INT,
                           coutUnitaire DOUBLE PRECISION,
                           quantite DOUBLE PRECISION,
                           coutTransport DOUBLE PRECISION,
                           coefficientQualite DOUBLE PRECISION,
                           FOREIGN KEY (componsant_id) REFERENCES Composants(id) ON DELETE CASCADE
);

CREATE TABLE Labor (
                       id SERIAL PRIMARY KEY,
                       componsant_id INT,
                       tauxHoraire DOUBLE PRECISION,
                       heuresTravail DOUBLE PRECISION,
                       productuvuteOuvrier DOUBLE PRECISION,
                       FOREIGN KEY (componsant_id) REFERENCES Composants(id) ON DELETE CASCADE
);

CREATE TABLE Devis (
                        id SERIAL PRIMARY KEY,
                        montantEstime DOUBLE PRECISION,
                        dateEmission DATE,
                        accepte BOOLEAN,
                        project_id INT,
                        FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE
);

