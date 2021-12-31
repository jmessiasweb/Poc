package br.com.poc.service

import br.com.poc.constants.BaseConstants
import br.com.poc.entity.Process
import br.com.poc.repository.TransactionRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Singleton


@Singleton
open class GenerateCsvFileImpl(
    private val repository: TransactionRepository
) : GenerateCsvFile {

    private var path1 = "<path>/file1.csv"
    private var path2 = "<path>/file2.csv"

    override fun findProcessByStatus() {
        LOG.info("Start CSV generate file")

        val processData = repository.findByStatus(BaseConstants.PROCESSED).toMutableList()

        // Geracao do arquivo para file 1
        val generateFileCSV1 = generateFile1CSV(processData)

        // Gera arquivo csv para a file 2
        val generateFileCSV2 = generateFile2CSV(processData)

        // Validar se o arquivo foi enviado para o path com sucesso
        if (generateFileCSV1 && generateFileCSV2) {
            // Atualiza todos os dados processados para concluido
            updateProcessToConcluded(processData)
        }

        LOG.info("Generated CSV file successfully")
    }

    private fun generateFile1CSV(processList: List<Process>): Boolean {

        try {
            val writer = Files.newBufferedWriter(Paths.get(path1))
            val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT)

            for (process in processList) {
                csvPrinter.printRecord(
                    process.id,
                    process.cpf,
                    process.email,
                    process.phone,
                    process.points.toString()
                )
            }
            csvPrinter.flush()
            csvPrinter.close()

        } catch (ex: IOException) {
            LOG.error(BaseConstants.ERROR_GENERATE_CSV_FILE)
            return false
        }
        LOG.info("Send CSV file to path 1")

        return true
    }

    private fun generateFile2CSV(processList: List<Process>): Boolean {

        try {
            val writer = Files.newBufferedWriter(Paths.get(path2))
            val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT)

            for (process in processList) {
                csvPrinter.printRecord(
                    process.id,
                    process.cpf,
                    process.email,
                    process.name,
                    process.transactionAmount.toString(),
                    process.dataOfBird,
                    process.phone,
                    process.points.toString()
                )
            }
            csvPrinter.flush()
            csvPrinter.close()

        } catch (ex: IOException) {
            LOG.error(BaseConstants.ERROR_GENERATE_CSV_FILE)
            return false
        }
        LOG.info("Send CSV file to path 2")

        return true
    }

    private fun updateProcessToConcluded(processList: List<Process>) {

        processList.stream().forEach { process ->
            process.status = BaseConstants.CONCLUDED
            repository.update(process)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GenerateCsvFileImpl::class.java)
    }
}