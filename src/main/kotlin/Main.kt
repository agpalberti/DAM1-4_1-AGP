import java.lang.NumberFormatException

class Modulo(maxAlumnos: Int) {
    var maxAlumnos = maxAlumnos
        set(value) {
            if (value > 0) field = value
            else throw NumberFormatException("No puede ser 0 o menor a 0.")
        }
    var alumnos: Array<Alumno?> = arrayOfNulls<Alumno>(this.maxAlumnos)
    var evaluaciones: Array<Array<Float>> = Array(4) { (Array<Float>(this.maxAlumnos) { -1.0F }) }

    companion object{
        const val EV_PRIMERA = 0
        const val EV_SEGUNDA = 1
        const val EV_TERCERA = 2
        const val EV_FINAL = 3
    }

    fun matricularAlumno(alumno: Alumno): Boolean {
        val position = alumnos.indexOfFirst { it == null }
        if (position > -1) {
            alumnos[position] = alumno
            for (i in EV_PRIMERA..EV_FINAL) {
                evaluaciones[i][position] = 0.0F
            }
            return true
        } else return false
    }

    fun bajaAlumno(idAlumno: String): Boolean {
        val position = alumnos.indexOfFirst { it?.id == idAlumno }
        if (position > -1) {
            alumnos[position] = null
            for (i in EV_PRIMERA..EV_FINAL) {
                evaluaciones[i][position] = -1.0F
            }
            return true
        } else return false
    }

    fun establecerNota(idAlumno: String, evaluacion: String, nota: Float): Boolean {
        val position = alumnos.indexOfFirst { it?.id == idAlumno }
        if (position > -1) {
            try {
                evaluaciones[evaluacion.toInt()][position] = nota
                return true
            }  catch (_:Exception) { return false }
        } else return false
    }

    fun calculaEvaluacionFinal(){

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
    println(a)
}