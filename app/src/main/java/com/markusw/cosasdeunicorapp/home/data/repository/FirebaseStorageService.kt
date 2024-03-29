package com.markusw.cosasdeunicorapp.home.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.markusw.cosasdeunicorapp.BuildConfig
import com.markusw.cosasdeunicorapp.core.ext.toast
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.DOCX
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.PDF
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.XLSX
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
 * @see Result
 * @see Result.Success
 * @see Result.Error
 * @see UiText
 * @see UiText.DynamicString
 */
class FirebaseStorageService(
    private val context: Context,
    storage: FirebaseStorage
) : RemoteStorage {

    private val storageRef = storage.reference

    companion object {
        private const val PDF_MIME_TYPE = "application/pdf"
        private const val DOCX_MIME_TYPE = "application/msword"
        private const val XLSX_MIME_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        private const val GENERIC_MIME_TYPE = "application/*"
    }

    override suspend fun downloadDocument(fileName: String): Result<Unit> {

        Timber.d("Downloading document $fileName")

        return try {
            if (!isExternalStorageWritable()) {
                Result.Error<Unit>(UiText.DynamicString("No se pudo acceder al almacenamiento externo"))
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
            val mimeType =
                getMimeTypeFromFileExtension(TextUtils.getFileExtensionFromName(fileName))
            openFile(documentUri, context, mimeType)

            Result.Success(Unit)
        } catch (e: StorageException) {

            when (e.errorCode) {
                StorageException.ERROR_OBJECT_NOT_FOUND -> Result.Error(UiText.DynamicString("El documento no existe"))
                else -> Result.Error(UiText.DynamicString("Error al descargar el documento"))
            }

        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(UiText.DynamicString("Error desconocido"))
        }

    }

    override suspend fun uploadImage(image: Uri): Result<String> {
        return try {
            val imageUrl = storageRef
                .child("images/${image.lastPathSegment}")
                .putFile(image)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()

            return Result.Success(imageUrl)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(UiText.DynamicString(e.toString()))
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
     * Gets the mime type of a file from its extension.
     * @param fileExtension Extension of the file.
     * @return The mime type of the file.
     */
    private fun getMimeTypeFromFileExtension(fileExtension: String): String {
        return when (fileExtension) {
            PDF -> PDF_MIME_TYPE
            DOCX -> DOCX_MIME_TYPE
            XLSX -> XLSX_MIME_TYPE
            else -> GENERIC_MIME_TYPE
        }
    }

    /**
     * Opens a file with the default app for the file type.
     * @param fileUri Uri of the file to open.
     * @param context Context to start the activity.
     */
    private suspend fun openFile(fileUri: Uri, context: Context, mimeType: String) {
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