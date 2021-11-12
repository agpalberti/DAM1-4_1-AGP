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
        if (posicionAlumno > -1 && checkEvaluacion(evaluacion)>-1 && nota in 0.0..10.0) {
            evaluaciones[checkEvaluacion(evaluacion)][posicionAlumno] = nota
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

    //fun listaNotas(evaluacion: String): List<Pair<Float, Float>> {}

    //Devuelve un Int con el número de aprobados. Si la evaluación se introduce de forma incorrecta devuelve un -1.
    fun numeroAprobados(evaluacion: String): Int {
        if (checkEvaluacion(evaluacion)>=0) {
            return evaluaciones[checkEvaluacion(evaluacion)].count { it >= 5 }
        } else return -1
    }

    //Devuelve un Float con la nota más baja de una evaluación. Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMasBaja(evaluacion: String): Float {
        if (checkEvaluacion(evaluacion)>=0) return evaluaciones[checkEvaluacion(evaluacion)].minOrNull() ?: (-2.0F)
        else return -1.0F
    }

    //Devuelve un Float con la nota más alta de una evaluación. Si se introduce incorrectamente la evaluación, devuelve un -1.0. Si la evaluación está vacía, devuelve un -2.0.
    fun notaMasAlta(evaluacion: String): Float {
        if(checkEvaluacion(evaluacion)>=0) return evaluaciones[checkEvaluacion(evaluacion)].maxOrNull()?:(-2.0F)
        else return -1.0F
    }

    //fun notaMedia(evaluacion: String): Float {}

    //fun hayAlumnosConDiez(evaluacion: String): Boolean {}

    //fun hayAlumnosAprobados(evaluacion: String): Boolean {}

    //fun primeraNotaNoAprobada(evaluacion: String): Float {}

    //fun listaNotasOrdenadas(evaluacion: String): List<Pair<Float, Float>> {}

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
    private fun checkEvaluacion(evaluacion: String): Int {
        return when(evaluacion.uppercase()){
            "1" -> 0
            "PRIMERA" -> 0
            "2" -> 1
            "SEGUNDA" -> 1
            "3" -> 2
            "TERCERA" -> 2
            "4" -> 3
            "FINAL" -> 3
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
}