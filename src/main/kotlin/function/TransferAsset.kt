package me.lucifer.function

import jp.co.soramitsu.iroha.java.IrohaAPI
import jp.co.soramitsu.iroha.java.Transaction
import jp.co.soramitsu.iroha.java.Utils
import me.lucifer.common.Constant.CREATOR_ACCOUNT_ID
import me.lucifer.common.Constant.CREATOR_ACCOUNT_KEYPAIR
import me.lucifer.function.CheckHash.getTransactionsStatus
import me.lucifer.model.IrohaCommand
import me.lucifer.model.IrohaTransfer
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.Base64

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

    fun getTransferPayloadHash(transfer: IrohaTransfer) : Pair<Transaction, ByteArray> {
        log.info("Creating transaction for transfer[${transfer.senderAccountId} -> ${transfer.receiverAccountId}] ...")
        val transferCommand = Transaction
            .builder(transfer.senderAccountId)
            .transferAsset(
                transfer.senderAccountId,
                transfer.receiverAccountId,
                transfer.asset,
                transfer.description,
                transfer.amount
            ).build()
        log.info("Transaction for transfer[${transfer.senderAccountId} -> ${transfer.receiverAccountId}] is created successfully!")
        return Pair(transferCommand, transferCommand.payloadForSign)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun transferAssetWithSignature(
        transaction: Transaction,
        rawSignature: ByteArray,
        publicKey: String,
        irohaPeer: IrohaAPI
    ) {
        val transferCommand = transaction.sign(rawSignature, publicKey).build()
        val transferCommandBytes = Utils.hash(transferCommand)
        irohaPeer.transaction(transferCommand)
        log.info("Transaction[${transferCommandBytes.toHexString()}] is sent to PEER[${irohaPeer.uri}] successfully!")

        getTransactionsStatus(
            listOfNotNull(
                transferCommandBytes?.let {
                    IrohaCommand(
                        action = "Transfer Asset",
                        objectValue = "",
                        txHashBytes = transferCommandBytes
                    )
                }
            ),
            irohaPeer
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun topUpAsset(amount: BigDecimal, asset: String, irohaPeer: IrohaAPI) {
        log.info("Top up asset: $asset with amount: $amount")
        val transferCommand = Transaction
            .builder(CREATOR_ACCOUNT_ID)
            .addAssetQuantity(asset, amount)
            .build().sign(CREATOR_ACCOUNT_KEYPAIR).build()
        val transferCommandBytes = Utils.hash(transferCommand)
        irohaPeer.transaction(transferCommand)
        log.info("Top up asset transaction[${transferCommandBytes.toHexString()}] is sent to PEER[${irohaPeer.uri}] successfully!")

        getTransactionsStatus(
            listOfNotNull(
                transferCommandBytes?.let {
                    IrohaCommand(
                        action = "Top Up Asset",
                        objectValue = "Asset: $asset, Amount: $amount",
                        txHashBytes = transferCommandBytes
                    )
                }
            ),
            irohaPeer
        )
    }
}