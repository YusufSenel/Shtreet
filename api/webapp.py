from flask import Flask, jsonify
import mysql.connector

conn = mysql.connector.connect(
    host='mariadb',
    user='alecorre',
    password='shtreet',
    db='alecorre'
)

app = Flask(__name__)

@app.route('/voie', methods=['GET'])
def get_voies():
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM Voie')
    voies = cursor.fetchall()
    cursor.close()

    tuples = []
    for row in voies:
        tuple_dict = {
            "idVoie": row[0],
            "NomVoieMaj": row[1],
            "NomVoieSimple": row[2],
            "NomVoie": row[3],
            "TypeVoie": row[4],
            "idArrondissement": row[5],
            "Historique": row[6],
            "VoieDescription": row[7],
        }
        tuples.append(tuple_dict)
    
    return jsonify(tuples)

@app.route('/arrondissement', methods=['GET'])
def get_arrondissmeent():
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM Arrondissement')
    arrondissement = cursor.fetchall()
    cursor.close()
    return jsonify(arrondissement)

@app.route('/voie/<string:NomVoieSimple>')
def get_voie(NomVoieSimple):
    cursor = conn.cursor()
    query = "SELECT * FROM Voie WHERE %s LIKE CONCAT('%', NomVoieSimple, '%')"
    cursor.execute(query, (NomVoieSimple,))
    voie = cursor.fetchone()
    cursor.close()

    if voie is None :
        return jsonify({'error': 'Voie inexistante'}),404
    else:
        tuple = {
                "idVoie": voie[0],
                "NomVoieMaj": voie[1],
                "NomVoieSimple": voie[2],
                "NomVoie": voie[3],
                "TypeVoie": voie[4],
                "idArrondissement": voie[5],
                "Historique": voie[6],
                "VoieDescription": voie[7],
            }
        return jsonify(tuple)

if __name__ == '__main__':
    app.run(debug=True)
