package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.home.domain.repository.NewsRepository
import javax.inject.Inject

class LoadPreviousNews @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke() = newsRepository.loadPreviousNews()
}