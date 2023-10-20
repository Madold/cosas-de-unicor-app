package com.markusw.cosasdeunicorapp.home.presentation.docs

@Suppress("SpellCheckingInspection")
sealed class DocumentSection(
    val label: String,
    val documents: List<DocumentReference>
) {

    object ConsejoAcademico: DocumentSection(
        label = "Consejo Académico",
        documents = listOf(
            DocumentReference(
                name = "Formato de cancelación de curso extemporáneo",
                documentName = "cancelacion_curso_extemporaneo.docx"
            ),
            DocumentReference(
                name = "Formato de cancelación de semestre extemporaneo",
                documentName = "formato_cancelacion_semestre_extemporaneo.docx"
            ),
            DocumentReference(
                name = "Formato de crédito extra",
                documentName = "formato_credito_extra.docx"
            )
        )
    )

    object Admisiones: DocumentSection(
        label = "Registro y admisiones",
        documents = listOf(
            DocumentReference(
                name = "Puntajes de referencia",
                documentName = "puntajes_referencia_2022.pdf"
            ),
            DocumentReference(
                name = "Simulador de ingreso a la universidad",
                documentName = "simulador_promedio_ponderado_programa.xlsx"
            ),
            DocumentReference(
                name = "Instructivo de casos especiales",
                documentName = "intructivo_ingreso_casos_especiales.docx"
            ),
            DocumentReference(
                name = "Instructivo de legalización de matrícula",
                documentName = "pasos_legalizacion_matricula.docx"
            ),
            DocumentReference(
                name = "Solicitud de doble programa",
                documentName = "solicitud_doble_programa.docx"
            ),
            DocumentReference(
                name = "Requisitos de doble programa",
                documentName = "requisitos_doble_programa.docx"
            ),
            DocumentReference(
                name = "Instructivo de compra de PIN en línea",
                documentName = "intructivo_compra_pin_en_linea.docx"
            ),
            DocumentReference(
                name = "Instructivo de cancelaciones",
                documentName = "intructivo_de_cancelaciones_unicordoba.docx"
            ),
            DocumentReference(
                name = "Calendario Acádemico 2023-2",
                documentName = "calendario_academico_2023_2.pdf"
            )
        )
    )

}
