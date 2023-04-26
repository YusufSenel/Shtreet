import openpyxl
import mysql.connector

# Connexion Base De données
connection = mysql.connector.connect(
    host="mariadb",
    user="alecorre",
    password="shtreet",
    database="alecorre"
)

cursor = connection.cursor()

# Fichier Excel
xlsx = openpyxl.load_workbook('Rues_Paris.xlsx')
sheet = xlsx.active


# Parcours du fichier excel
query = ("INSERT INTO Voie (NomVoieMaj, NomVoieSimple, TypeVoie, NomVoie, idArrondissement, Historique, VoieDescription) values (%s, %s, %s ,%s, %s, %s, %s)")

for row in sheet.iter_rows(min_row=1):
    if(row[4].value is None or row[0].value is None):
        continue
    # Données
    data = (row[0].value, row[1].value, row[2].value, row[3].value, int(row[4].value), row[5].value, row[6].value)
        
    print(f"taille :{len(str(row[5].value))} ")

    cursor.execute(query, data)
    print(f"{row[0].value} :{row[0]} Inséré.")
        
        
        
connection.commit()

