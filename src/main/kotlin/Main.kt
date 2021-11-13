import java.lang.NumberFormatException

class Modulo(maxAlumnos: Int) {
    var maxAlumnos = maxAlumnos
        set(value) {
            if (value > 0) field = value
            else throw NumberFormatException("No puede ser 0 o menor a 0.")
        }
    var alumnos: Array<Alumno?> = arrayOfNulls(this.maxAlumnos)
    var evaluaciones: Array<Array<Float>> = Array(4) { (Array(this.maxAlumnos) { -1.0F }) }

    companion object {
        const val EV_PRIMERA = 0
        const val EV_SEGUNDA = 1
        const val EV_TERCERA = 2
        const val EV_FINAL = 3
    }

    //Apartado 1. Permite establecer la nota de cierto alumno y evaluación.
    // Si se introduce correctamente los datos devuelve un true, si no, false. También devuelve false si el alumno no existe.
    fun establecerNota(idAlumno: String, evaluacion: String, nota: Float): Boolean {
        val posicionAlumno = alumnos.indexOfFirst { it?.id == idAlumno }
        val eval = traducirEvaluacion(evaluacion)
        if (posicionAlumno > -1 && eval >= EV_PRIMERA && nota in 0.0..10.0) {
            evaluaciones[eval][posicionAlumno] = nota
            return true
        } else return false
    }

    //Apartado 2. Establece la nota de la EV_FINAL para todos los alumnos mediante el promedio de las 3 primeras evaluaciones
    fun calculaEvaluacionFinal() {
        for (i in 0 until this.maxAlumnos) {
            evaluaciones[EV_FINAL][i] =
                (evaluaciones[EV_PRIMERA][i] + evaluaciones[EV_SEGUNDA][i] + evaluaciones[EV_TERCERA][i]) / 3
        }
    }

    //Apartado 3. Devuelve una lista de Pair con el formato de listOf(Pair(Alumno,Nota)).
    // Si se introduce incorrectamente la evaluación, devuelve una lista vacía.
    fun listaNotas(evaluacion: String = "FINAL"): List<Pair<Alumno, Float>> {
        val eval = traducirEvaluacion(evaluacion)
        val listaNotas: MutableList<Pair<Alumno, Float>> = mutableListOf()
        if (eval >= EV_PRIMERA) {
            for (i in 0 until this.maxAlumnos) {
                if (alumnos[i] != null) listaNotas.add(Pair(alumnos[i]!!, evaluaciones[eval][i]))
            }
        }
        return listaNotas
    }

    //Apartado 4. Devuelve un Int con el número de aprobados. Si la evaluación se introduce de forma incorrecta devuelve un -1.
    fun numeroAprobados(evaluacion: String = "FINAL"): Int {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) {
            return evaluaciones[eval].count { it >= 5 }
        } else return -1
    }

    //Apartado 5. Devuelve un Float con la nota más baja de una evaluación.
    // Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMasBaja(evaluacion: String = "FINAL"): Float {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return (evaluaciones[eval].filter { it >= 0 }
            .minOrNull() ?: (-2.0F))
        else return -1.0F
    }

    //Apartado 6. Devuelve un Float con la nota más alta de una evaluación.
    // Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMasAlta(evaluacion: String = "FINAL"): Float {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval].filter { it >= 0 }.maxOrNull() ?: (-2.0F)
        else return -1.0F
    }

    //Apartado 7. Devuelve un Float con la nota media de una evaluación.
    // Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMedia(evaluacion: String = "FINAL"): Float {
        val eval = traducirEvaluacion(evaluacion)
        val notaMedia = evaluaciones[eval].filter { it >= 0 }.average().toFloat()
        if (eval >= EV_PRIMERA) {
            return if (notaMedia.isNaN()) -2.0F else notaMedia
        } else return -1.0F
    }

    //Apartado 8. Devuelve un Boolean si hay alumnos con diez o no.
    // Si no se introduce correctamente la evaluación, devuelve false también.
    fun hayAlumnosConDiez(evaluacion: String = "FINAL"): Boolean {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval].any { it == 10.0F }
        else return false
    }

    //Apartado 9. Devuelve un Boolean si hay alumnos aprobados o no.
    // Si no se introduce correctamente la evaluación, devuelve false también.
    fun hayAlumnosAprobados(evaluacion: String = "FINAL"): Boolean {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval].any { it >= 5.0F }
        else return false
    }

    //Apartado 10. Devuelve un Float con la primera nota aprobada en el array empezando desde la posición 0.
    // Si no se introduce correctamente la evaluación, devuelve -1.0.
    fun primeraNotaNoAprobada(evaluacion: String = "FINAL"): Float {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval][evaluaciones[eval].filter { it >= 0.0F }
            .indexOfFirst { it < 5.0F }]
        else return -1.0F
    }

    //Apartado 11
    fun listaNotasOrdenadas(evaluacion: String = "FINAL"): List<Pair<Alumno, Float>> {
        val eval = traducirEvaluacion(evaluacion)
        val listaNotas = mutableListOf<Pair<Alumno, Float>>()
        if (eval >= EV_PRIMERA) {
            for (i in 0 until this.maxAlumnos) {
                if (alumnos[i] != null) listaNotas.add(Pair(alumnos[i]!!, evaluaciones[eval][i]))
            }
        }
        return listaNotas.sortedBy { it.second }
    }

    //Apartado 12. Permite matricular a un alumno.
    // Si se cumple correctamente devuelve true. En caso de que no haya espacio para matricular a más alumnos, devuelve false.
    //Si se introduce un alumno ya matriculado, devuelve false.
    fun matricularAlumno(alumno: Alumno): Boolean {
        if (alumnos.none { it?.id == alumno.id }) {
            val posicionAlumnoVacio = alumnos.indexOfFirst { it == null }
            if (posicionAlumnoVacio > -1) {
                alumnos[posicionAlumnoVacio] = alumno
                for (i in EV_PRIMERA..EV_FINAL) {
                    evaluaciones[i][posicionAlumnoVacio] = 0.0F
                }
                return true
            } else return false
        } else return false
    }

    //Apartado 13. Permite dar de baja a un alumno según su ID. Si se cumple correctamente, devuelve true.
    // En caso de no existir dicho alumno, devuelve false.
    fun bajaAlumno(idAlumno: String): Boolean {
        val posicionAlumno = alumnos.indexOfFirst { it?.id == idAlumno }
        if (posicionAlumno > -1) {
            alumnos[posicionAlumno] = null
            for (i in EV_PRIMERA..EV_FINAL) {
                evaluaciones[i][posicionAlumno] = -1.0F
            }
            return true
        } else return false
    }

    //Devuelve un Int traduciendo la evaluación introducida a la posición del array, sea por letra o por número.
    // Si se introduce algo incorrecto, devuelve -1.
    private fun traducirEvaluacion(evaluacion: String): Int {
        return when (evaluacion.uppercase()) {
            "1" -> EV_PRIMERA
            "PRIMERA" -> EV_PRIMERA
            "2" -> EV_SEGUNDA
            "SEGUNDA" -> EV_SEGUNDA
            "3" -> EV_TERCERA
            "TERCERA" -> EV_TERCERA
            "4" -> EV_FINAL
            "FINAL" -> EV_FINAL
            else -> -1
        }
    }
}

data class Alumno(val id: String, val nombre: String, val ap1: String, val ap2: String) {

    init {
        require(id.isNotEmpty()) { "No puede estar vacío." }
        require(nombre.isNotEmpty() && !nombre.any { it.isDigit() }) { "No puede estar vacío o incluir números." }
        require(ap1.isNotEmpty() && !ap1.any { it.isDigit() }) { "No puede estar vacío o incluir números." }
        if (ap2.isNotEmpty()) require(!ap2.any { it.isDigit() }) { "No puede incluir números." }
    }

}

//Devuelve un alumno con un id, nombre y apellidos aleatorios.
fun alumnoRandom(): Alumno {
    fun stringRandom(): String {
        var string = ""
        val consonantes = listOf<Char>(
            'q', 'w', 'r', 't', 'y', 'p', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'
        )
        val vocales = listOf<Char>('a', 'e', 'i', 'o', 'u')

        string += consonantes.random().uppercase()
        repeat(3) {
            string += vocales.random()
            string += consonantes.random()
        }
        string += vocales.random()
        return string
    }

    val alumno: Alumno
    var id = ""
    val nombre = stringRandom()
    val ap1 = stringRandom()
    val ap2 = stringRandom()

    repeat(8) { id += (0..9).random() }
    repeat(3) { id += ('A'..'Z').random() }

    alumno = Alumno(id, nombre, ap1, ap2)
    return alumno
}

fun main() {
    //Apartado 1. Asigno 10 alumnos y sus respectivas notas aleatorias.
    val programacion = Modulo(15)
    repeat(10) { programacion.matricularAlumno(alumnoRandom()) }

    for (i in 0 until programacion.maxAlumnos) {
        if (programacion.alumnos[i] != null) {
            for (eval in Modulo.EV_PRIMERA..Modulo.EV_FINAL) {
                programacion.alumnos[i].let {
                    it?.let { it1 ->
                        programacion.establecerNota(
                            it1.id,
                            eval.toString(),
                            (0..10).random().toFloat()
                        )
                    }
                }
            }
        }
    }

    //Apartado 2. Calculo la nota final de los módulos.
    programacion.calculaEvaluacionFinal()

    //Apartado 3. Obtengo una lista de los alumnos con sus notas.
    println("LISTA DE ALUMNOS Y SUS NOTAS:")
    println(programacion.listaNotas())

    //Apartado 4. Obtengo cuantos alumnos han aprobado.
    println("ALUMNOS APROBADOS: ${programacion.numeroAprobados()}")

    //Apartado 5. Obtengo cuál es la nota más baja.
    println("NOTA MÁS BAJA: ${programacion.notaMasBaja()}")

    //Apartado 6. Obtengo la nota más alta.
    println("NOTA MÁS ALTA: ${programacion.notaMasAlta()}")

    //Apartado 7. Obtengo la nota media.
    println("NOTA MEDIA: ${programacion.notaMedia()}")

    //Apartado 8. Miro si hay algún diez.
    if (programacion.hayAlumnosConDiez()) println("Hay alumnos con diez.") else println("No hay alumnos con diez.")

    //Apartado 9. Miro si hay alumnos aprobados.
    if (programacion.hayAlumnosAprobados()) println("Hay alumnos aprobados.") else println("No ha aprobado nadie.")

    //Apartado 10. Calculo la primera nota suspensa.
    println("LA PRIMERA NOTA SUSPENSA ES: ${programacion.primeraNotaNoAprobada()}")

    //Apartado 11. Obtengo una lista de los alumnos y notas ordenadas de menor a mayor.
    println("LISTA DE ALUMNOS Y SUS NOTAS ORDENADAS:")
    println(programacion.listaNotasOrdenadas())

    //Apartado 12. Matriculo a un alumno adicional.
    val alumno = Alumno("12345678QWE", "Alejandro", "González", "Parra")
    if (programacion.matricularAlumno(alumno)) println("El alumno ha sido matriculado correctamente.")
    else println("No se ha podido matricular al alumno")

    //Apartado 13. Elimino al alumno.
    if (programacion.bajaAlumno("12345678QWE")) println("El alumno ha sido eliminado correctamente.")
    else println("No se ha encontrado al alumno.")

}