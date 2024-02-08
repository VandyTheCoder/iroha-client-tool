package me.lucifer.function

import jp.co.soramitsu.iroha.java.IrohaAPI
import jp.co.soramitsu.iroha.java.Transaction
import jp.co.soramitsu.iroha.java.Utils
import me.lucifer.function.CheckHash.getTransactionsStatus
import me.lucifer.model.IrohaCommand
import me.lucifer.model.IrohaTransfer
import org.slf4j.LoggerFactory

object TransferAsset {

    private val log = LoggerFactory.getLogger(this::class.java)

    @OptIn(ExperimentalStdlibApi::class)
    fun createTransaction(transfer: IrohaTransfer, irohaPeer: IrohaAPI) {
        log.info("Creating transaction for transfer[${transfer.senderAccountId} -> ${transfer.receiverAccountId}] ...")
        val transferCommand = Transaction
            .builder(transfer.senderAccountId)
            .transferAsset(
                transfer.senderAccountId,
                transfer.receiverAccountId,
                transfer.asset,
                transfer.description,
                transfer.amount
            ).build().sign(transfer.senderKeyPair).build()

        val transferCommandBytes = Utils.hash(transferCommand)
        irohaPeer.transaction(transferCommand)
        log.info("Transaction[${transferCommandBytes.toHexString()}] for transfer[${transfer.senderAccountId} -> ${transfer.receiverAccountId}] is sent to PEER[${irohaPeer.uri}] successfully!")

        getTransactionsStatus(
            listOfNotNull(
                transferCommandBytes?.let {
                    IrohaCommand(
                        action = "Transfer Asset",
                        objectValue = """${transfer.asset}[${transfer.amount}] - Description Length[${transfer.description.length}]""",
                        txHashBytes = transferCommandBytes
                    )
                }
            ),
            irohaPeer
        )
    }
}