package br.com.poc.service

import br.com.poc.constants.BaseConstants
import br.com.poc.entity.Accumulation
import br.com.poc.entity.Process
import br.com.poc.repository.AccumulationRepository
import br.com.poc.repository.TransactionRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.Reader
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class CSVReaderFileImpl(
    private val transactionRepository: TransactionRepository,
    private val accumulationRepository: AccumulationRepository
) : CsvReaderFile {

    private val processDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date());
    private val divisionFactor = BigDecimal(5)

    override fun readCsv(path: String) {
        LOG.info(BaseConstants.START_READING_CSV_FILE)

        try {
            val reader: Reader = Files.newBufferedReader(Paths.get(path))
            val csvParser = CSVParser(reader, CSVFormat.DEFAULT)
            val processList = ArrayList<Process>()

            for (csvRecord in csvParser) {
                csvParser.stream()
                    .map { data ->
                        Process(
                            null,
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            BigDecimal(data[5]),
                            pointCalculator(BigDecimal(data[5]), data[0]),
                            processDate,
                            BaseConstants.PROCESSED
                        )
                    }
                    .forEach { process ->
                        processList.add(process)
                    }
            }

            validatorAccumulationToProcess(processList)

        } catch (ex: IOException) {
            LOG.error(BaseConstants.ERROR_READ_CSV_FILE)
            throw IOException(ex.message)
        }

        LOG.info(BaseConstants.EXECUTE_SUCCESSFULLY_JOB)
    }

    private fun validatorAccumulationToProcess(processList: MutableList<Process>) {
        val accumulation = accumulationRepository.findAccumulationByProcessDateTime(processDate).toMutableList()

        processList.forEach { process ->
            accumulation.forEach { accumulationData ->

                // Se existir cpf do cliente com valor acumulado, vai somar os valores
                if (process.cpf.compareTo(accumulationData.cpf) == 0) {
                    process.transactionAmount = process.transactionAmount + accumulationData.transactionAmount

                    updateValueAccumulation(process, accumulationData)

                }
            }

            saveProcess(process)
        }
    }


    private fun saveProcess(process: Process) {
        // Se o ponto for igual a zero, o valor da transacao esta abaixo dos 5 reais, nao tera pontos
        // e sera incluido no acumulo
        if (process.points != BigDecimal.ZERO) {
            transactionRepository.save(process)
        }
    }


    private fun updateValueAccumulation(process: Process, accumulationData: Accumulation) {
        val remainder = process.transactionAmount.remainder(divisionFactor)

        // Se a divisao pelo fator 5 for diferente de zero, sera atualizado o valor do acumulo para o cpf
        if (remainder.compareTo(BigDecimal.ZERO) != 0) {
            process.points = process.transactionAmount.divide(divisionFactor, 0, RoundingMode.DOWN)

            val restAccumulation =
                process.transactionAmount.subtract(process.points.multiply(divisionFactor))
                    .setScale(2, RoundingMode.HALF_EVEN)

            accumulationRepository.update(
                Accumulation(
                    accumulationData.id,
                    accumulationData.cpf,
                    restAccumulation,
                    processDate,
                )
            )
        }
    }


    /**
     * Method points calculator
     *
     * @param value Transaction value
     * @return Return point calculator by transaction
     */

    private fun pointCalculator(value: BigDecimal, cpf: String): BigDecimal {

        val remainder = value.remainder(divisionFactor)
        val points: BigDecimal

        return if (remainder.compareTo(BigDecimal.ZERO) == 0) {
            points = value.div(divisionFactor)

            points

        } else {
            points = value.divide(divisionFactor, 0, RoundingMode.DOWN)

            val restAccumulation = value.subtract(points.multiply(divisionFactor))
                .setScale(2, RoundingMode.HALF_EVEN)

            accumulationRepository.save(
                Accumulation(
                    null,
                    cpf,
                    restAccumulation,
                    processDate
                )
            )
            points
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CSVReaderFileImpl::class.java)
    }
}