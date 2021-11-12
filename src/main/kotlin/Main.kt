import java.lang.NumberFormatException

class Modulo(maxAlumnos: Int) {
    var maxAlumnos = maxAlumnos
        set(value) {
            if (value > 0) field = value
            else throw NumberFormatException("No puede ser 0 o menor a 0.")
        }
    var alumnos: Array<Alumno?> = arrayOfNulls<Alumno>(this.maxAlumnos)
    var evaluaciones: Array<Array<Float>> = Array(4) { (Array<Float>(this.maxAlumnos) { -1.0F }) }

    companion object {
        const val EV_PRIMERA = 0
        const val EV_SEGUNDA = 1
        const val EV_TERCERA = 2
        const val EV_FINAL = 3
    }

    //Permite establecer la nota de cierto alumno y evaluación. Si se introduce correctamente los datos devuelve un true, si no, false. También devuelve false si el alumno no existe.
    fun establecerNota(idAlumno: String, evaluacion: String, nota: Float): Boolean {
        val posicionAlumno = alumnos.indexOfFirst { it?.id == idAlumno }
        val eval = traducirEvaluacion(evaluacion)
        if (posicionAlumno > -1 && eval >= EV_PRIMERA && nota in 0.0..10.0) {
            evaluaciones[eval][posicionAlumno] = nota
            return true
        } else return false
    }

    //Establece la nota de la EV_FINAL para todos los alumnos mediante el promedio de las 3 primeras evaluaciones
    fun calculaEvaluacionFinal() {
        for (i in 0 until this.maxAlumnos) {
            evaluaciones[EV_FINAL][i] =
                (evaluaciones[EV_PRIMERA][i] + evaluaciones[EV_SEGUNDA][i] + evaluaciones[EV_TERCERA][i]) / 3
        }
    }

    //Devuelve una lista de Pair con el formato de listOf(Pair(Alumno,Nota)). Si se introduce incorrectamente la evaluación, devuelve una lista vacía.
    fun listaNotas(evaluacion: String = "FINAL"): List<Pair<Alumno, Float>> {
        val eval = traducirEvaluacion(evaluacion)
        var listaNotas: MutableList<Pair<Alumno, Float>> = mutableListOf()
        if (eval >= EV_PRIMERA) {
            for (i in 0..maxAlumnos) {
                if (alumnos[i] != null) listaNotas.add(Pair(alumnos[i]!!, evaluaciones[eval][i]))
            }
        }
        return listaNotas
    }

    //Devuelve un Int con el número de aprobados. Si la evaluación se introduce de forma incorrecta devuelve un -1.
    fun numeroAprobados(evaluacion: String): Int {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) {
            return evaluaciones[eval].count { it >= 5 }
        } else return -1
    }

    //Devuelve un Float con la nota más baja de una evaluación. Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMasBaja(evaluacion: String): Float {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return (evaluaciones[eval].filter { it >= 0 }
            .minOrNull() ?: (-2.0F))
        else return -1.0F
    }

    //Devuelve un Float con la nota más alta de una evaluación. Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMasAlta(evaluacion: String): Float {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval].filter { it >= 0 }
            .maxOrNull() ?: (-2.0F)
        else return -1.0F
    }

    //Devuelve un Float con la nota media de una evaluación. Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMedia(evaluacion: String): Float {
        val eval = traducirEvaluacion(evaluacion)
        val notaMedia = evaluaciones[eval].filter { it >= 0 }.average().toFloat()
        if (eval >= EV_PRIMERA) {
            return if (!notaMedia.isNaN()) notaMedia else -2.0F
        } else return -1.0F
    }

    //Devuelve un Boolean si hay alumnos con diez o no. Si no se introduce correctamente la evaluación, devuelve false también.
    fun hayAlumnosConDiez(evaluacion: String): Boolean {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval].any { it == 10.0F }
        else return false
    }

    //Devuelve un Boolean si hay alumnos aprobados o no. Si no se introduce correctamente la evaluación, devuelve false también.
    fun hayAlumnosAprobados(evaluacion: String): Boolean {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval].any { it >= 5.0F }
        else return false
    }

    //Devuelve un Float con la primera nota aprobada en el array empezando desde la posición 0. Si no se introduce correctamente la evaluación, devuelve -1.0.
    fun primeraNotaNoAprobada(evaluacion: String): Float {
        val eval = traducirEvaluacion(evaluacion)
        if (eval >= EV_PRIMERA) return evaluaciones[eval][evaluaciones[eval].filter { it >= 0.0F }
            .indexOfFirst { it < 5.0F }]
        else return -1.0F
    }

    fun listaNotasOrdenadas(evaluacion: String): List<Pair<Float, Float>> {
        val eval = traducirEvaluacion(evaluacion)

    }

    fun matricularAlumno(alumno: Alumno): Boolean {
        val posicionAlumnoVacio = alumnos.indexOfFirst { it == null }
        if (posicionAlumnoVacio > -1) {
            alumnos[posicionAlumnoVacio] = alumno
            for (i in EV_PRIMERA..EV_FINAL) {
                evaluaciones[i][posicionAlumnoVacio] = 0.0F
            }
            return true
        } else return false
    }

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

    //Devuelve un Int traduciendo la evaluación introducida a la posición del array, sea por letra o por número. Si se introduce algo incorrecto, devuelve -1.
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

fun main() {
    var a: Alumno = Alumno("agonpar518", "Alejandro", "González", "Parra")
    var b: Modulo = Modulo(15)
    println(b.notaMedia("2"))
}