package com.markusw.cosasdeunicorapp.home.domain.repository

import android.net.Uri
import com.markusw.cosasdeunicorapp.core.utils.Result

/**
 * Created by Markus Water on 02/10/2023.
 *
 * Interface to download documents from a remote storage service.
 */
interface RemoteStorage {
    /**
     * Downloads a document from a remote storage service.
     * @param fileName The name of the file to download.
     * @return A [Result] with the result of the operation that contains a [String] Uri pointing to the fileName.
     * @see Result
     * @see Result.Success
     */
    suspend fun downloadDocument(fileName: String): Result<Unit>

    /*
    * Uploads an image to a remote storage service and returns the url of the image.
    * @param image The image to upload.Uri pointing
    * @return A [Result] with the result of the operation that contains a [String] pointing to the image url.
    * */
    suspend fun uploadImage(image: Uri): Result<String>

}