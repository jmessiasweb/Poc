package br.com.poc.service

import javax.inject.Singleton

@Singleton
interface CsvReaderFile {

    fun readCsv(path: String)
}