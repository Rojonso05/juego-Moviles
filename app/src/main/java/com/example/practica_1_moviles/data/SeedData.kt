package com.example.practica_1_moviles.data

// ---------------------------------------------------------------
//  PREGUNTAS DEL JUEGO
//  Formato:  q("temática", "pregunta", "correcta", "mala1", "mala2", "mala3")
//  Escribe SIEMPRE la respuesta correcta primero: el orden de las
//  4 respuestas se mezcla solo.
//  Temática nueva = escribir un nombre nuevo en el primer campo.
// ---------------------------------------------------------------

object Categories {
    const val GENERAL = "Cultura general"
    const val MONSTRUOS = "Monstruos"
    const val VIDEOJUEGOS = "Videojuegos"
}

private fun q(
    category: String,
    text: String,
    correct: String,
    wrong1: String,
    wrong2: String,
    wrong3: String
): QuestionEntity {
    val answers = listOf(correct, wrong1, wrong2, wrong3).shuffled()
    return QuestionEntity(
        category = category,
        text = text,
        answer1 = answers[0],
        answer2 = answers[1],
        answer3 = answers[2],
        answer4 = answers[3],
        correctIndex = answers.indexOf(correct)
    )
}

object SeedData {

    val questions: List<QuestionEntity> = listOf(

        // ----- Cultura general -----
        q(Categories.GENERAL, "¿Cuál es la capital de Australia?",
            "Canberra", "Sídney", "Melbourne", "Perth"),
        q(Categories.GENERAL, "¿Cuántos planetas tiene el Sistema Solar?",
            "8", "7", "9", "10"),
        q(Categories.GENERAL, "¿Quién pintó \"Las Meninas\"?",
            "Velázquez", "Goya", "Picasso", "Dalí"),
        q(Categories.GENERAL, "¿Cuál es el océano más grande del mundo?",
            "Pacífico", "Atlántico", "Índico", "Ártico"),
        q(Categories.GENERAL, "¿En qué año llegó el ser humano a la Luna?",
            "1969", "1959", "1972", "1965"),
        q(Categories.GENERAL, "¿Cuál es el río más largo de la península ibérica?",
            "Tajo", "Ebro", "Duero", "Guadalquivir"),
        q(Categories.GENERAL, "¿Qué elemento químico tiene el símbolo \"Au\"?",
            "Oro", "Plata", "Aluminio", "Argón"),
        q(Categories.GENERAL, "¿En qué año comenzó la Revolución Francesa?",
            "1789", "1776", "1799", "1815"),
        q(Categories.GENERAL, "¿Quién escribió \"Don Quijote de la Mancha\"?",
            "Miguel de Cervantes", "Lope de Vega", "Francisco de Quevedo", "Calderón de la Barca"),
        q(Categories.GENERAL, "¿Cuántos huesos tiene el cuerpo de un adulto?",
            "206", "186", "226", "256"),
        q(Categories.GENERAL, "¿Cuál es el país más grande del mundo por superficie?",
            "Rusia", "Canadá", "China", "Estados Unidos"),
        q(Categories.GENERAL, "¿Qué gas absorben las plantas para hacer la fotosíntesis?",
            "Dióxido de carbono", "Oxígeno", "Nitrógeno", "Hidrógeno"),
        q(Categories.GENERAL, "¿Cuál es el animal terrestre más rápido?",
            "Guepardo", "León", "Antílope", "Caballo"),
        q(Categories.GENERAL, "¿Quién formuló la teoría de la relatividad?",
            "Albert Einstein", "Isaac Newton", "Galileo Galilei", "Stephen Hawking"),
        q(Categories.GENERAL, "¿Cuál es la montaña más alta del mundo sobre el nivel del mar?",
            "Everest", "K2", "Kilimanjaro", "Mont Blanc"),

        // ----- Monstruos -----
        q(Categories.MONSTRUOS, "¿Qué escritor creó a Drácula?",
            "Bram Stoker", "Mary Shelley", "Edgar Allan Poe", "H. P. Lovecraft"),
        q(Categories.MONSTRUOS, "Según el folclore, ¿con qué material se mata a un hombre lobo?",
            "Plata", "Oro", "Hierro", "Plomo"),
        q(Categories.MONSTRUOS, "¿Quién creó a la criatura en la novela de Mary Shelley?",
            "Victor Frankenstein", "Igor", "Van Helsing", "Henry Jekyll"),
        q(Categories.MONSTRUOS, "¿Qué monstruo mitológico convertía en piedra a quien la miraba?",
            "Medusa", "Hidra", "Cerbero", "Esfinge"),
        q(Categories.MONSTRUOS, "¿Qué suelen buscar los zombis en la cultura popular?",
            "Cerebros", "Oro", "Agua", "Huesos"),
        q(Categories.MONSTRUOS, "¿Cómo se llama el perro de tres cabezas que guarda la entrada del Hades?",
            "Cerbero", "Fenrir", "Anubis", "Argos"),
        q(Categories.MONSTRUOS, "¿Qué criatura, mitad hombre y mitad toro, habitaba el laberinto de Creta?",
            "Minotauro", "Centauro", "Sátiro", "Cíclope"),
        q(Categories.MONSTRUOS, "¿Cómo se llama el gigantesco monstruo marino del folclore escandinavo que hunde barcos?",
            "Kraken", "Leviatán", "Caribdis", "Nessie"),
        q(Categories.MONSTRUOS, "¿Qué cíclope se enfrenta a Ulises en La Odisea?",
            "Polifemo", "Caribdis", "Argos", "Talos"),
        q(Categories.MONSTRUOS, "¿Dónde dice la leyenda que vive el monstruo \"Nessie\"?",
            "En un lago de Escocia", "En un fiordo de Noruega", "En el Amazonas", "En el mar Muerto"),
        q(Categories.MONSTRUOS, "En la tradición judía, ¿de qué material se fabrica un Golem?",
            "Barro", "Hierro", "Madera", "Hielo"),
        q(Categories.MONSTRUOS, "¿Qué ave mitológica renace de sus propias cenizas?",
            "Fénix", "Grifo", "Arpía", "Roc"),
        q(Categories.MONSTRUOS, "¿Qué héroe griego derrotó a la Hidra de Lerna?",
            "Hércules", "Perseo", "Teseo", "Aquiles"),
        q(Categories.MONSTRUOS, "¿Qué criatura mitológica tiene cuerpo de león y cabeza y alas de águila?",
            "Grifo", "Quimera", "Hipogrifo", "Mantícora"),
        q(Categories.MONSTRUOS, "¿Qué criatura del folclore latinoamericano ataca al ganado y le chupa la sangre?",
            "Chupacabras", "Cadejo", "Nahual", "La Llorona"),

        // ----- Videojuegos -----
        q(Categories.VIDEOJUEGOS, "¿Cómo se llama el protagonista de Super Mario Bros.?",
            "Mario", "Luigi", "Wario", "Yoshi"),
        q(Categories.VIDEOJUEGOS, "¿Quién es el protagonista de The Legend of Zelda?",
            "Link", "Zelda", "Ganon", "Epona"),
        q(Categories.VIDEOJUEGOS, "¿Qué compañía creó a Sonic?",
            "SEGA", "Nintendo", "Sony", "Atari"),
        q(Categories.VIDEOJUEGOS, "¿De qué tipo es Pikachu?",
            "Eléctrico", "Fuego", "Agua", "Planta"),
        q(Categories.VIDEOJUEGOS, "¿En qué videojuego aparecen los Creepers?",
            "Minecraft", "Terraria", "Fortnite", "Roblox"),
        q(Categories.VIDEOJUEGOS, "¿Cómo se llama la princesa a la que Mario suele rescatar?",
            "Peach", "Zelda", "Daisy", "Rosalina"),
        q(Categories.VIDEOJUEGOS, "¿Qué empresa desarrolló Fortnite?",
            "Epic Games", "Valve", "Riot Games", "Activision"),
        q(Categories.VIDEOJUEGOS, "¿En qué año salieron a la venta los primeros juegos de Pokémon en Japón?",
            "1996", "1993", "1999", "2001"),
        q(Categories.VIDEOJUEGOS, "¿Qué clásico arcade consiste en comer puntos mientras huyes de fantasmas?",
            "Pac-Man", "Space Invaders", "Pong", "Asteroids"),
        q(Categories.VIDEOJUEGOS, "¿Quién es el protagonista de la saga Halo?",
            "Jefe Maestro", "Marcus Fenix", "Gordon Freeman", "Samus Aran"),
        q(Categories.VIDEOJUEGOS, "¿Qué juego de bloques que caen creó Alekséi Pázhitnov?",
            "Tetris", "Columns", "Puyo Puyo", "Bejeweled"),
        q(Categories.VIDEOJUEGOS, "¿En qué videojuego aparece la ciudad ficticia de Los Santos?",
            "Grand Theft Auto V", "Saints Row", "Watch Dogs", "Mafia"),
        q(Categories.VIDEOJUEGOS, "¿Cómo se llama la protagonista de la saga Metroid?",
            "Samus Aran", "Lara Croft", "Aloy", "Ellie"),
        q(Categories.VIDEOJUEGOS, "¿Qué consola lanzó Sony en 1994?",
            "PlayStation", "Dreamcast", "Nintendo 64", "Saturn"),
        q(Categories.VIDEOJUEGOS, "¿Qué gorila de Nintendo lanza barriles en su juego arcade original?",
            "Donkey Kong", "Diddy Kong", "Bowser", "Wario"),

    )
}

// Sustituye las preguntas de la base de datos por las de este archivo.
// Se ejecuta una vez cada vez que arranca la app. No toca las puntuaciones.
suspend fun seedQuestions(dao: QuestionDao) {
    dao.deleteAll()
    dao.insertAll(SeedData.questions)
}
