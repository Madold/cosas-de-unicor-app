package com.markusw.cosasdeunicorapp.home.presentation

import com.markusw.cosasdeunicorapp.core.domain.model.LocalSettings
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.News
import com.markusw.cosasdeunicorapp.home.presentation.docs.DocumentReference

data class HomeState(
    val globalChatList: List<Message> = emptyList(),
    val message: String = "",
    val currentUser: User = User(),
    val isFetchingPreviousGlobalMessages: Boolean = false,
    val repliedMessage: Message? = null,
    val isDownloadingDocument: Boolean = false,
    val isFetchingPreviousNews: Boolean = false,
    val newsList: List<News> = emptyList(),
    val usersCount: Int = 0,
    val localSettings: LocalSettings = LocalSettings(),
    val isClosingSession: Boolean = false,
    val isLoading: Boolean = false,
    val documentsList: List<DocumentReference> = listOf(
        DocumentReference(
            name = "Formato de cancelación de curso extemporáneo",
            documentName = "cancelacion_curso_extemporaneo.docx",
            category = "Consejo Académico"
        ),
        DocumentReference(
            name = "Formato de cancelación de semestre extemporaneo",
            documentName = "formato_cancelacion_semestre_extemporaneo.docx",
            category = "Consejo Académico"
        ),
        DocumentReference(
            name = "Formato de crédito extra",
            documentName = "formato_credito_extra.docx",
            category = "Consejo Académico"
        ),
        DocumentReference(
            name = "Puntajes de referencia",
            documentName = "puntajes_referencia_2022.pdf",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Simulador de ingreso a la universidad",
            documentName = "simulador_promedio_ponderado_programa.xlsx",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Instructivo de casos especiales",
            documentName = "intructivo_ingreso_casos_especiales.docx",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Instructivo de legalización de matrícula",
            documentName = "pasos_legalizacion_matricula.docx",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Solicitud de doble programa",
            documentName = "solicitud_doble_programa.docx",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Requisitos de doble programa",
            documentName = "requisitos_doble_programa.docx",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Instructivo de compra de PIN en línea",
            documentName = "intructivo_compra_pin_en_linea.docx",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Instructivo de cancelaciones",
            documentName = "intructivo_de_cancelaciones_unicordoba.docx",
            category = "Registro y admisiones"
        ),
        DocumentReference(
            name = "Calendario Acádemico 2023-2",
            documentName = "calendario_academico_2023_2.pdf",
            category = "Registro y admisiones"
        )
    ),
    val documentCategories: List<String> = listOf(
        "Consejo Académico",
        "Registro y admisiones"
    ),
    val documentName: String = "",
    val isDocumentSearchBarActive: Boolean = false,
    val searchedDocumentsList: List<DocumentReference> = emptyList()
)
