
CREATE TABLE Region (
    NomRegion varchar(100) NOT NULL,
    RegionDescription varchar(500),
    PRIMARY KEY (NomRegion)
);

CREATE TABLE Departement (
    NomDepartement varchar(100) NOT NULL,
    DepartementDescription varchar(500),
    NumeroDepartement int NOT NULL,
    NomRegion varchar(100) NOT NULL,
    PRIMARY KEY (NomDepartement),
    FOREIGN KEY (NomRegion) REFERENCES Region(NomRegion)
);

/*
CodeInsee : Code INSEE de la commune 
NomCommuneMaj : Nom de la commune en majuscule sans accents
NomSimple : Nom de la commune en minuscule sans les accents et tirets remplacés par des espaces
NomReel : Nom de la commune correct avec les accents
CodePostal : Code postal de la commune (si plusieurs, séparés par des tirets)
*/
CREATE TABLE Commune (
    CodeInsee int NOT NULL,
    NomCommuneMaj varchar(100) NOT NULL,
    NomCommuneSimple varchar(100) NOT NULL,
    NomCommuneReel varchar(100) NOT NULL,
    CodePostal varchar(500) NOT NULL,
    DescriptionCommune varchar(500),
    NomDepartement varchar(30) NOT NULL,
    PRIMARY KEY (CodeInsee),
    FOREIGN KEY (NomDepartement) REFERENCES Departement(NomDepartement)
);

/*
NomArrondissementMaj : Nom de l'arrondissement en majuscule sans accents
NomArrondissementSimple : Nom de l'arrondissement en minuscule sans les accents et tirets remplacés par des espaces
NomArrondissementReel : Nom de l'arrondissement correct avec les accents
*/
CREATE TABLE Arrondissement (
    idArrondissement int NOT NULL AUTO_INCREMENT,
    NomArrondissementMaj varchar(100) NOT NULL,
    NomArrondissementSimple varchar(100) NOT NULL,
    NomArrondissementReel varchar(100) NOT NULL,
    ArrondissmentDescription varchar(500),
    CommuneCodeInsee int NOT NULL,
    PRIMARY KEY (idArrondissement),
    FOREIGN KEY (CommuneCodeInsee) REFERENCES Commune(CodeInsee)
);

/*
NomVoieMaj : Nom de la voie en majuscule sans accents
NomVoieSimple : Nom de la voie en minuscule avec les accents
NomVoie : Nom de la voie sans type de rue (Rue, Avenue, Impasse...)
*/
CREATE TABLE Voie (
    idVoie int NOT NULL AUTO_INCREMENT,
    NomVoieMaj varchar(100) NOT NULL,
    NomVoieSimple varchar(100) NOT NULL,
    NomVoie varchar(100) NOT NULL,
    TypeVoie varchar(15) NOT NULL,
    idArrondissement int NOT NULL,
    Historique varchar(500),
    VoieDescription varchar(500),
    PRIMARY KEY (idVoie),
    FOREIGN KEY (idArrondissement) REFERENCES Arrondissement(idArrondissement)
);