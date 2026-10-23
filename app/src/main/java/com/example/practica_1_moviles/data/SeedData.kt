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
            "Minecraft", "Terraria", "Fortnite", "Roblox")
    )
}

// Mete las preguntas iniciales solo si la base de datos está vacía
suspend fun seedIfEmpty(dao: QuestionDao) {
    if (dao.count() == 0) {
        dao.insertAll(SeedData.questions)
    }
}