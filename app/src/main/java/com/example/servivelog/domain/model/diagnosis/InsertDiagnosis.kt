package com.example.servivelog.domain.model.diagnosis

data class InsertDiagnosis(
    var nombrelab: String,
    var ServiceTag: String,
    var descripcion: String,
    var ruta1: String,
    var ruta2: String,
    var ruta3: String,
    var ruta4: String,
    var fecha: String
)
