package me.lucifer

import me.lucifer.common.Constant.CREATOR_ACCOUNT_ID
import me.lucifer.common.Constant.CREATOR_ACCOUNT_KEYPAIR
import me.lucifer.common.Constant.DOMAIN_ID
import me.lucifer.common.Constant.USD_ASSET
import me.lucifer.function.CreateAccount
import me.lucifer.function.InitPeer
import me.lucifer.function.TransferAsset
import me.lucifer.model.IrohaCreateAccount
import me.lucifer.model.IrohaTransfer
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.Base64

class IrohaClientTool {

    companion object {

        private val log = LoggerFactory.getLogger(this::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            InitPeer.start()

            // Create New Account
            val irohaCreateAccount = IrohaCreateAccount(
                accountName = "testing",
                publicKey = "7b566d852f830940cb2b78bcb87d8c632e93a9dbe5f55b7d5f22cef113758d9d"
            )
            CreateAccount.createAccount(irohaCreateAccount, InitPeer.getPeer())

            // Top Up Asset to Admin
            TransferAsset.topUpAsset(BigDecimal(1000000), USD_ASSET, InitPeer.getPeer())

            // Transfer Asset to New Account
            val irohaTransfer = IrohaTransfer(
                senderAccountId = CREATOR_ACCOUNT_ID,
                senderKeyPair = CREATOR_ACCOUNT_KEYPAIR,
                receiverAccountId = "${irohaCreateAccount.accountName}@$DOMAIN_ID",
                amount = BigDecimal(100),
                asset = USD_ASSET,
                description = "testing"
            )
            TransferAsset.createTransaction(irohaTransfer, InitPeer.getPeer())

            // Transfer Asset Back to Admin
            val irohaTransferBack = IrohaTransfer(
                senderAccountId = "${irohaCreateAccount.accountName}@$DOMAIN_ID",
                senderKeyPair = CREATOR_ACCOUNT_KEYPAIR,
                receiverAccountId = CREATOR_ACCOUNT_ID,
                amount = BigDecimal(100),
                asset = USD_ASSET,
                description = "testing"
            )
            val payload = TransferAsset.getTransferPayloadHash(irohaTransferBack)
            val decodedPayload = Base64.getEncoder().encodeToString(payload.first.hash())
            log.info("Payload Hash for Signature: $decodedPayload")

            // TODO: Get Signature from Vault
            var signature: String = ""
            var publicKey: String = ""
            var rawSignature = Base64.getDecoder().decode(signature)
            TransferAsset.transferAssetWithSignature(
                transaction = payload.first,
                rawSignature = rawSignature,
                publicKey = publicKey,
                irohaPeer = InitPeer.getPeer()
            )
        }
    }
}