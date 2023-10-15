package com.markusw.cosasdeunicorapp.home.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import com.markusw.cosasdeunicorapp.BuildConfig
import com.markusw.cosasdeunicorapp.core.ext.toast
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.home.domain.repository.RemoteStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

/**
 * Created by Markus Water on 02/10/2023.
 * Implementation of [RemoteStorage] that uses Firebase Storage.
 * @see RemoteStorage
 * @see FirebaseStorage
 * @see Resource
 * @see Resource.Success
 * @see Resource.Error
 * @see UiText
 * @see UiText.DynamicString
 */
class FirebaseStorageService(
    private val context: Context,
    storage: FirebaseStorage
) : RemoteStorage {

    private val storageRef = storage.reference

    override suspend fun downloadDocument(fileName: String): Resource<String> {
        return try {
            if (!isExternalStorageWritable()) {
                Resource.Error<Unit>(UiText.DynamicString("No se pudo acceder al almacenamiento externo"))
            }

            val downloadsDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val localFile = File(downloadsDirectory, fileName)

            if (!localFile.exists()) {
                saveDocumentLocally(fileName, localFile)
            }

            val documentUri = FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.provider",
                localFile
            )
            val mimeType = getMimeTypeFromFileExtension(getFileExtension(fileName))
            openFile(documentUri, context, mimeType)

            Resource.Success(documentUri.toString())
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Error(UiText.DynamicString("Error al descargar el documento"))
        }

    }

    /**
     * Saves a document from Firebase Storage to the local storage.
     * @param documentName Name of the document to save.
     * @param localFile File to save the document to.
     */
    private suspend fun saveDocumentLocally(documentName: String, localFile: File) {
        val fileData = storageRef
            .child("formatos/$documentName")
            .getBytes(1024 * 1024)
            .await()

        val outputStream = localFile.outputStream()
        withContext(Dispatchers.IO) {
            outputStream.write(fileData)
            outputStream.close()
        }
    }

    /**
     * Checks if the external storage is writable.
     * @return True if the external storage is writable, false otherwise.
     */
    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /**
     * Gets the file extension of a file.
     * @param fileName Name of the file.
     * @return The file extension.
     */
    private fun getFileExtension(fileName: String): String {
        val lastDotIndex = fileName.lastIndexOf(".")
        return if (lastDotIndex == -1 || lastDotIndex == fileName.length - 1) {
            // If there's no dot in the file name or it's the last character, there's no extension.
            ""
        } else {
            // Return the substring after the last dot (which is the file extension).
            fileName.substring(lastDotIndex + 1)
        }
    }

    /**
     * Gets the mime type of a file from its extension.
     * @param fileExtension Extension of the file.
     * @return The mime type of the file.
     */
    private fun getMimeTypeFromFileExtension(fileExtension: String): String {
        return when (fileExtension) {
            "pdf" -> "application/pdf"
            "docx" -> "application/msword"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            else -> "application/*"
        }
    }

    /**
     * Opens a file with the default app for the file type.
     * @param fileUri Uri of the file to open.
     * @param context Context to start the activity.
     */
    private suspend fun openFile(fileUri: Uri, context: Context, mimeType: String) {
        Timber.d("Opening file with mime: $mimeType")

        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, mimeType)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            withContext(Dispatchers.Main) {
                context.toast("No se encontró una aplicación para abrir el archivo")
            }
        }
    }

}