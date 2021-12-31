package br.com.poc.job

import br.com.poc.service.GenerateCsvFile
import io.micronaut.scheduling.annotation.Scheduled
import javax.inject.Singleton

@Singleton
class GenerateCsvJob(private val service: GenerateCsvFile) {

    @Scheduled(cron = "\${job.cron.csv.value}")
    fun generateCsvFile(){
        service.findProcessByStatus();
    }
}