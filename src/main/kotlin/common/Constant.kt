package me.lucifer.common

import iroha.protocol.Endpoint
import jp.co.soramitsu.iroha.java.IrohaAPI
import jp.co.soramitsu.iroha.java.Utils


object Constant {
    const val DOMAIN_ID = "lucifer"
    const val USD_ASSET = "usd#$DOMAIN_ID"
    const val KHR_ASSET = "khr#$DOMAIN_ID"

    const val CREATOR_ACCOUNT_ID = "admin@$DOMAIN_ID"
    val CREATOR_ACCOUNT_KEYPAIR = Utils.parseHexKeypair(
        "1eaad1d668c9374c32c174c7dd7c9a0475a465173f44ecaa97e6e4739aebf446",
        "c41acce4682e3f727c22c64cb982fd4aa32a6a886227effd3f0cfc2e97d90521"
    )
    private val IROHA_NODES = listOf("localhost:50051")
    val IROHA_PEERS = IROHA_NODES.map {
        val (host, port) = it.split(":")
        IrohaAPI(host, port.toInt())
    }
    val VALID_STATUS_TO_WAIT = listOf(
        Endpoint.TxStatus.STATEFUL_VALIDATION_SUCCESS,
        Endpoint.TxStatus.STATELESS_VALIDATION_SUCCESS,
        Endpoint.TxStatus.MST_PENDING,
        Endpoint.TxStatus.NOT_RECEIVED,
        Endpoint.TxStatus.ENOUGH_SIGNATURES_COLLECTED
    )
}