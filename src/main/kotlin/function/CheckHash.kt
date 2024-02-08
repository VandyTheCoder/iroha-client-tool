package me.lucifer.function

import iroha.protocol.Endpoint
import jp.co.soramitsu.iroha.java.IrohaAPI
import me.lucifer.common.Constant.IROHA_PEERS
import me.lucifer.common.Constant.VALID_STATUS_TO_WAIT
import me.lucifer.model.IrohaCommand
import me.lucifer.model.IrohaStatus
import org.slf4j.LoggerFactory

object CheckHash {

    private val log = LoggerFactory.getLogger(this::class.java)

    @OptIn(ExperimentalStdlibApi::class)
    fun getTransactionsStatus(irohaCommands : List<IrohaCommand>, peer: IrohaAPI) : Map<String, IrohaStatus> {
        val result = mutableListOf<IrohaStatus>()
        for (irohaCommand in irohaCommands) {
            var checkStatus = true
            while (checkStatus) {
                try {
                    val status = peer.txStatusSync(irohaCommand.txHashBytes)
                    log.info("Transaction Status[${status.txStatus.name}] for Command[${irohaCommand.action} - ${irohaCommand.objectValue}]")
                    when {
                        status.txStatus == Endpoint.TxStatus.COMMITTED -> {
                            result.add(IrohaStatus(irohaCommand.txHashBytes.toHexString(), status.txStatus.name))
                            checkStatus = false
                        }
                        VALID_STATUS_TO_WAIT.contains(status.txStatus) -> Thread.sleep(1000)
                        else -> {
                            result.add(IrohaStatus(irohaCommand.txHashBytes.toHexString(), status.txStatus.name))
                            checkStatus = false
                        }
                    }
                } catch (e: Exception) {
                    log.info("Error while checking status for Command[${irohaCommand.action} - ${irohaCommand.objectValue}]", e)
                    result.add(IrohaStatus(irohaCommand.txHashBytes.toHexString(), "FAILED_DURING_CHECK_STATUS"))
                    checkStatus = false
                }
            }
        }
        return result.associateBy { it.txHash }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getTransactionsStatus(trxHashes: List<ByteArray>) {
        for (trxHash in trxHashes) {
            log.info("Getting transaction with Hash[${trxHash.toHexString()}] status ...")
            IROHA_PEERS.forEach {
                try {
                    val status = it.txStatusSync(trxHash)
                    log.info("Transaction Status[${status.txStatus.name}] for Hash[${trxHash.toHexString()}]")
                } catch (e: Exception) {
                    log.info("IROHA::Error while checking status for Hash[${trxHash.toHexString()}]", e)
                }
            }
        }
    }
}