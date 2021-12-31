package br.com.poc.job

import br.com.poc.service.CsvReaderFile
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton

@Singleton
class MainJob(private  val service: CsvReaderFile) {
    private  var path = "<path>/fileMain.csv"

    @Scheduled(cron = "\${main.job.cron.value}")
    fun processCsvFile(){
        service.readCsv(path);
    }
}