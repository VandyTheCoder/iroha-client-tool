package me.lucifer

import me.lucifer.common.Constant.CREATOR_ACCOUNT_ID
import me.lucifer.common.Constant.CREATOR_ACCOUNT_KEYPAIR
import me.lucifer.common.Constant.DOMAIN_ID
import me.lucifer.common.Constant.USD_ASSET
import me.lucifer.function.InitPeer
import me.lucifer.function.TransferAsset
import me.lucifer.model.IrohaTransfer
import java.math.BigDecimal
import javax.xml.bind.DatatypeConverter

class IrohaClientTool {
    companion object {

        private val trxHashes = listOf(
            "830d8480181ef43b09da6bf826b0c44a92421c67db115a06d10371d1647609f7"
        ).map { DatatypeConverter.parseHexBinary(it) }

        @JvmStatic
        fun main(args: Array<String>) {
            InitPeer.start()

            // Check Existing Hash
            // CheckHash.getTransactionsStatus(trxHashes)

            // Transfer Asset
//            val irohaTransfer = IrohaTransfer(
//                senderAccountId = CREATOR_ACCOUNT_ID,
//                senderKeyPair = CREATOR_ACCOUNT_KEYPAIR,
//                receiverAccountId = "violet@$DOMAIN_ID",
//                amount = BigDecimal(100),
//                asset = USD_ASSET,
//                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis ."
//            )
//            TransferAsset.createTransaction(irohaTransfer, InitPeer.getPeer())
        }
    }
}